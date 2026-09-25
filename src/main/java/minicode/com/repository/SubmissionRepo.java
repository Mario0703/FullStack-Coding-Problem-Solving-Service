package minicode.com.repository;

import minicode.com.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubmissionRepo extends JpaRepository<Submission, UUID> {
}
