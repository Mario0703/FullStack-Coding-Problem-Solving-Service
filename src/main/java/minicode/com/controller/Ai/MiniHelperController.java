package minicode.com.controller.Ai;

import jakarta.validation.Valid;
import minicode.com.controller.Challenges.SubmissionHelper;
import minicode.com.dto.Ai.MiniHelperRequest;
import minicode.com.dto.Ai.MiniHelperResponse;
import minicode.com.repository.UserRepo;
import minicode.com.service.Ai.MiniHelperService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST entry point for miniHelper. The frontend sends the question (and the code
 * the user is editing); everything else — the challenge data, the prompt and the
 * OpenAI call — happens server-side.
 */
@RestController
@RequestMapping("/api/ai")
public class MiniHelperController {

    private final MiniHelperService miniHelperService;
    private final UserRepo userRepo;

    public MiniHelperController(MiniHelperService miniHelperService, UserRepo userRepo) {
        this.miniHelperService = miniHelperService;
        this.userRepo = userRepo;
    }

    @PostMapping("/hint")
    public MiniHelperResponse hint(
            @Valid @RequestBody MiniHelperRequest request,
            Authentication authentication
    ) {
        UUID userId = SubmissionHelper.authenticatedUserId(userRepo, authentication);
        return new MiniHelperResponse(miniHelperService.ask(request, userId));
    }
}
