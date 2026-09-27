package minicode.com.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import minicode.com.dto.SubmissionResponse;
import minicode.com.repository.SubmissionRepo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/submission")
public class submissionController {
    private final SubmissionRepo SubmissionRepo;


    public submissionController(SubmissionRepo submissionRepo) {
        this.SubmissionRepo = submissionRepo;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<SubmissionResponse> listUserSubmissions(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "User not logged in");
        }

        UUID userId = (UUID) session.getAttribute("user_id");
        return SubmissionRepo.findAllByUserId(userId).stream()
                .map(SubmissionResponse::from)
                .toList();
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
        if (session == null || session.getAttribute("user_id") == null) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED,
                    "User not logged in"
            );
        }
        return (UUID) session.getAttribute("user_id");
    }

}
