package minicode.com.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import minicode.com.entity.Submission;
import minicode.com.repository.SubmissionRepo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/submission")
public class submissionController {
    private final SubmissionRepo SubmissionRepo;


    public submissionController(SubmissionRepo submissionRepo) {
        this.SubmissionRepo = submissionRepo;
    }

    public List<Submission> listUserSubmissions(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "User not logged in");
        }

        UUID userId = (UUID) session.getAttribute("user_id");
        return SubmissionRepo.findAllByUserId(userId);
    }
}
