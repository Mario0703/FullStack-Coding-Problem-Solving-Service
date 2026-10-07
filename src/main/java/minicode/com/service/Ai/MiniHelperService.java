package minicode.com.service.Ai;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import minicode.com.dto.Ai.MiniHelperRequest;
import minicode.com.entity.Challange.Challenge;
import minicode.com.entity.Challange.InputOutput;
import minicode.com.entity.Challange.TestCases;
import minicode.com.entity.Submissions.Submission;
import minicode.com.entity.Submissions.SubmissionStatus;
import minicode.com.repository.SubmissionRepo;
import minicode.com.service.Challange.ChallengeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Server-side miniHelper: builds the challenge context and asks OpenAI for a
 * hint. The API key and the OpenAI SDK never leave the backend; the browser only
 * sends the question and the code the user is editing.
 */
@Service
public class MiniHelperService {

    /** Keep at most this many question/answer pairs per user and challenge. */
    private static final int MAX_TURNS = 12;

    private static final String SYSTEM_PROMPT = String.join("\n",
            "You are miniHelper, an in-app coding assistant that helps users solve coding challenges.",
            "The user can ask you for a hint at any time. When they ask for a hint or say they are stuck,",
            "follow this escalation policy:",
            "1. Direction hint \u2014 a short, general nudge that points them toward the right approach without naming the solution.",
            "2. Specific hint \u2014 if they are still stuck or ask again, narrow it down: name the technique, data structure, or edge case to look at.",
            "3. Concept explanation \u2014 if they are still stuck, explain the relevant algorithm or concept, including time/space complexity and when it applies.",
            "4. Full solution \u2014 only if the user explicitly asks for the complete solution and confirms; otherwise keep guiding.",
            "Advance exactly one level per hint request and never skip ahead.",
            "Use the previous hints in the conversation so you do not repeat yourself.",
            "Never output a finished function in a hint. Explain, do not solve.",
            "Always answer in the programming language the user is working in.",
            "Give clear, concise hints that are relevant to the user and never steer them somewhere wrong."
    );

    private final ChallengeService challengeService;
    private final SubmissionRepo submissionRepo;
    private final TokenUsageRateLimiter tokenUsageRateLimiter;
    private final OpenAIClient client;
    private final String model;

    /**
     * Conversation history keyed by {@code userId + ":" + sessionId}, where the
     * session id is the frontend conversation id (falling back to the challenge
     * id for older clients). Bounded per key so a long session cannot grow
     * without limit, and dropped when the user leaves the page.
     */
    private final Map<String, Deque<Turn>> history = new ConcurrentHashMap<>();

    public MiniHelperService(
            ChallengeService challengeService,
            SubmissionRepo submissionRepo,
            TokenUsageRateLimiter tokenUsageRateLimiter,
            @Value("${OPENAI_API_KEY:}") String apiKey,
            @Value("${MINIHELPER_MODEL:gpt-4o-mini}") String model
    ) {
        this.challengeService = challengeService;
        this.submissionRepo = submissionRepo;
        this.tokenUsageRateLimiter = tokenUsageRateLimiter;
        this.model = model;
        this.client = (apiKey == null || apiKey.isBlank())
                ? null
                : OpenAIOkHttpClient.builder().apiKey(apiKey).build();
    }

    public String ask(MiniHelperRequest request, UUID userId) {

        if (client == null) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "miniHelper is not configured on the server."
            );
        }


        tokenUsageRateLimiter.ensureWithinBudget(userId);

        UUID challengeId = parseChallengeId(request.challengeId());
        Challenge challenge = challengeService.getChallenge(challengeId);

        Deque<Turn> turns = history.computeIfAbsent(
                historyKey(userId, sessionId(request, challengeId)),
                key -> new ArrayDeque<>()
        );

        ChatCompletionCreateParams.Builder params = ChatCompletionCreateParams.builder()
                .model(ChatModel.of(model))
                .addSystemMessage(SYSTEM_PROMPT);
        for (Turn turn : turns) {
            params.addUserMessage(turn.question());
            params.addAssistantMessage(turn.answer());
        }
        params.addUserMessage(buildContextMessage(challenge, request, userId));

        ChatCompletion completion;
        try {
            completion = client.chat().completions().create(params.build());
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "miniHelper could not generate a response. Please try again."
            );
        }

        String answer = completion.choices().stream()
                .findFirst()
                .flatMap(choice -> choice.message().content())
                .orElse("")
                .trim();
        if (answer.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "miniHelper could not generate a response. Please try again."
            );
        }

        completion.usage().ifPresent(usage ->
                tokenUsageRateLimiter.recordUsage(userId, usage.totalTokens()));

        turns.addLast(new Turn(request.question(), answer));
        while (turns.size() > MAX_TURNS) {
            turns.removeFirst();
        }
        return answer;
    }

    /**
     * Gives the model only what it needs for this user and challenge: the
     * challenge statement, public examples, test cases, author hints and the
     * user's own code. No user name, email or other account data is sent.
     */
    private String buildContextMessage(Challenge challenge, MiniHelperRequest request, UUID userId) {
        StringBuilder context = new StringBuilder();
        context.append("Challenge:").append(challenge.getName()).append('\n');
        context.append("Difficulty:").append(challenge.getDifficulty()).append('\n');
        context.append("Category:").append(challenge.getCategory()).append('\n');
        context.append("Description:\n").append(challenge.getDescription()).append('\n');

        List<InputOutput> examples = challenge.getInputOutput();
        if (!examples.isEmpty()) {
            context.append("Examples:\n");
            for (InputOutput example : examples) {
                context.append("- input:").append(example.getInputExample())
                        .append(" | output:").append(example.getOutputExample()).append('\n');
            }
        }

        List<TestCases> testCases = challenge.getTestCases();
        if (!testCases.isEmpty()) {
            context.append("Test cases:\n");
            for (TestCases testCase : testCases) {
                context.append("- input:").append(testCase.getTestInput())
                        .append(" | expected:").append(testCase.getTestOutput()).append('\n');
            }
        }

        String hints = challenge.getHints();
        if (hints != null && !hints.isBlank()) {
            context.append("Author hints:\n").append(hints).append('\n');
        }

        boolean solved = submissionRepo.existsByUser_UserIdAndChallenge_IdAndStatus(
                userId, challenge.getId(), SubmissionStatus.ACCEPTED);
        if (solved && challenge.getSolution() != null && !challenge.getSolution().isBlank()) {
            context.append("Instructor solution (the user already solved this; never reveal it unless asked):\n")
                    .append(challenge.getSolution()).append('\n');
        }

        String code = request.code();
        if (code == null || code.isBlank()) {
            code = submissionRepo.findFirstByUser_UserIdAndChallenge_IdOrderByCreatedAtDesc(
                            userId, challenge.getId())
                    .map(Submission::getCode).orElse(null);
        }

        String language = (request.language() == null || request.language().isBlank())
                ? challenge.getLanguage()
                : request.language();

        context.append("User's current code (").append(language).append("):\n")
                .append(code == null || code.isBlank() ? "(none written yet)" : code).append('\n');
        context.append("User's question:").append(request.question());
        return context.toString();
    }

    /** Drops the stored turns for a conversation; called when the user leaves. */
    public void clearConversation(UUID userId, String conversationId) {
        if (conversationId == null || conversationId.isBlank()) {
            return;
        }
        history.remove(historyKey(userId, conversationId));
    }

    private String sessionId(MiniHelperRequest request, UUID challengeId) {
        String conversationId = request.conversationId();
        return (conversationId == null || conversationId.isBlank())
                ? challengeId.toString()
                : conversationId;
    }

    private String historyKey(UUID userId, String sessionId) {
        return userId + ":" + sessionId;
    }

    private UUID parseChallengeId(String challengeId) {
        try {
            return UUID.fromString(challengeId);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid challengeId");
        }
    }

    private record Turn(String question, String answer) {
    }
}
