package minicode.com.controller.Challenges;

import minicode.com.auth.CurrentUser;
import minicode.com.entity.Submissions.Submission;
import minicode.com.repository.SubmissionRepo;
import minicode.com.repository.UserRepo;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;
import java.util.function.ToIntFunction;

public final class SubmissionHelper {

    private SubmissionHelper() {
    }

    public static List<Integer> passedTestCasesForUser(
            UserRepo userRepo,
            SubmissionRepo submissionRepo,
            Authentication authentication
    ) {
        return testCaseValues(receiptSubmissions(userRepo, submissionRepo, authentication), Submission::getTestCasesPassed);
    }

    public static List<Integer> failedTestCasesForUser(
            UserRepo userRepo,
            SubmissionRepo submissionRepo,
            Authentication authentication
    ) {
        return testCaseValues(receiptSubmissions(userRepo, submissionRepo, authentication), Submission::getTestCasesFailed);
    }

    public static List<Integer> totalTestCasesForUser(
            UserRepo userRepo,
            SubmissionRepo submissionRepo,
            Authentication authentication
    ) {
        return testCaseValues(receiptSubmissions(userRepo, submissionRepo, authentication), SubmissionHelper::totalTestCases);
    }

    public static List<List<Integer>> submissionStats(SubmissionRepo submissionRepo, UUID userId) {
        return testCaseStats(submissionRepo.findPublicSubmissionsByUserId(userId));
    }

    private static List<Submission> receiptSubmissions(
            UserRepo userRepo,
            SubmissionRepo submissionRepo,
            Authentication authentication
    ) {
        return submissionRepo.findReceiptByUserId(CurrentUser.id(userRepo, authentication));
    }

    private static List<List<Integer>> testCaseStats(List<Submission> submissions) {
        return List.of(
                testCaseValues(submissions, Submission::getTestCasesPassed),
                testCaseValues(submissions, Submission::getTestCasesFailed),
                testCaseValues(submissions, SubmissionHelper::totalTestCases)
        );
    }

    private static List<Integer> testCaseValues(
            List<Submission> submissions,
            ToIntFunction<Submission> extractor
    ) {
        return submissions.stream()
                .mapToInt(extractor)
                .boxed()
                .toList();
    }

    private static int totalTestCases(Submission submission) {
        return submission.getChallenge().getNumberOfTestCases();
    }
}
