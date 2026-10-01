package minicode.com.controller;

import minicode.com.dto.User.SubmissionResponse;
import minicode.com.entity.Submissions.Submission;
import minicode.com.entity.User.User;
import minicode.com.entity.Roles.UserRole;
import minicode.com.repository.SubmissionRepo;
import minicode.com.repository.UserRepo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/submission")
public class submissionController {
    private final SubmissionRepo SubmissionRepo;
    private final UserRepo userRepo;


    public submissionController(SubmissionRepo submissionRepo, UserRepo userRepo) {
        this.SubmissionRepo = submissionRepo;
        this.userRepo = userRepo;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<SubmissionResponse> listUserSubmissions(Authentication authentication) {
        UUID userId = authenticatedUserId(authentication);
        return SubmissionRepo.findAllByUserId(userId).stream()
                .map(SubmissionResponse::from)
                .toList();
    }

    @GetMapping("/details")
    @Transactional(readOnly = true)
    public SubmissionResponse getDetailsFromSubmissionId(
            Authentication authentication,
            @RequestParam UUID submissionId
    ) {
        UUID userId = authenticatedUserId(authentication);

        Submission submission = SubmissionRepo.findByIdAndUser_UserId(submissionId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND,
                        "Submission not found"
                ));

        return SubmissionResponse.from(submission);
    }

    @GetMapping("/passed")
    @Transactional(readOnly = true)
    public List<Integer> listNumberOfPassedTestCases(Authentication authentication) {
        return testCaseValuesForUser(authentication, true);
    }

    @GetMapping("/failed")
    @Transactional(readOnly = true)
    public List<Integer> listNumberOfFailedTestCases(Authentication authentication) {
        return testCaseValuesForUser(authentication, false);
    }

    @GetMapping("/total")
    @Transactional(readOnly = true)
    public List<Integer> listNumberOfTotalTestCases(Authentication authentication) {
        UUID userId = authenticatedUserId(authentication);
        return SubmissionRepo.findTotalTestCasesByUserId(userId);
    }

    private List<Integer> testCaseValuesForUser(
            Authentication authentication,
            boolean passed
    ) {
        UUID userId = authenticatedUserId(authentication);
        return passed
                ? SubmissionRepo.findPassedTestCasesByUserId(userId)
                : SubmissionRepo.findFailedTestCasesByUserId(userId);
    }

    private UUID authenticatedUserId(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED,
                    "User not logged in"
            );
        }
        User user = userRepo.findFirstByUserName(authentication.getName()).orElseThrow(() ->
                new ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "User not logged in"));
        ensureNormalUser(user.getUserId());
        return user.getUserId();
    }

    private void ensureNormalUser(UUID userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResponseStatusException(
                org.springframework.http.HttpStatus.UNAUTHORIZED, "User not logged in"));
        if (user.getRole() == UserRole.ADMIN) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.FORBIDDEN,
                    "User access is not permitted for an admin session"
            );
        }
    }
}



