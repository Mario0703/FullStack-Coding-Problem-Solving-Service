package minicode.com.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import minicode.com.dto.SubmissionResponse;
import minicode.com.entity.Submission;
import minicode.com.entity.User;
import minicode.com.entity.UserRole;
import minicode.com.repository.SubmissionRepo;
import minicode.com.repository.UserRepo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

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
    public List<SubmissionResponse> listUserSubmissions(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session == null) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "User not logged in");
        }
        if (!(session.getAttribute("user_id") instanceof UUID userId)) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "User not logged in");
        }

        ensureNormalUser(userId);
        return SubmissionRepo.findAllByUserId(userId).stream()
                .map(SubmissionResponse::from)
                .toList();
    }

    @GetMapping("/details")
    @Transactional(readOnly = true)
    public SubmissionResponse getDetailsFromSubmissionId(
            HttpServletRequest httpRequest,
            @RequestParam UUID submissionId
    ) {
        UUID userId = authenticatedUserId(httpRequest);

        Submission submission = SubmissionRepo.findByIdAndUser_UserId(submissionId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND,
                        "Submission not found"
                ));

        return SubmissionResponse.from(submission);
    }

    @GetMapping("/passed")
    @Transactional(readOnly = true)
    public List<Integer> listNumberOfPassedTestCases(HttpServletRequest httpRequest) {
        return testCaseValuesForUser(httpRequest, true);
    }

    @GetMapping("/failed")
    @Transactional(readOnly = true)
    public List<Integer> listNumberOfFailedTestCases(HttpServletRequest httpRequest) {
        return testCaseValuesForUser(httpRequest, false);
    }

    @GetMapping("/total")
    @Transactional(readOnly = true)
    public List<Integer> listNumberOfTotalTestCases(HttpServletRequest httpRequest) {
        UUID userId = authenticatedUserId(httpRequest);
        return SubmissionRepo.findTotalTestCasesByUserId(userId);
    }

    private List<Integer> testCaseValuesForUser(
            HttpServletRequest httpRequest,
            boolean passed
    ) {
        UUID userId = authenticatedUserId(httpRequest);
        return passed
                ? SubmissionRepo.findPassedTestCasesByUserId(userId)
                : SubmissionRepo.findFailedTestCasesByUserId(userId);
    }

    private UUID authenticatedUserId(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session == null) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED,
                    "User not logged in"
            );
        }
        Object userId = session.getAttribute("user_id");
        if (!(userId instanceof UUID id)) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED,
                    "User not logged in"
            );
        }
        ensureNormalUser(id);
        return id;
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
