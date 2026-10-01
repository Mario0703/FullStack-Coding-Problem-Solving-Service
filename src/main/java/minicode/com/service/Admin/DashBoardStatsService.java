package minicode.com.service.Admin;

import minicode.com.entity.Submissions.Submission;
import minicode.com.entity.Submissions.SubmissionStatus;
import minicode.com.entity.User.User;
import minicode.com.dto.Admin.AdminUserStatistics;
import minicode.com.repository.ChallengeRepo;
import minicode.com.repository.SubmissionRepo;
import minicode.com.repository.UserRepo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DashBoardStatsService {

    private final UserRepo userRepository;
    private final ChallengeRepo challengeRepository;
    private final SubmissionRepo submissionRepository;

    public DashBoardStatsService(UserRepo userRepository, ChallengeRepo challengeRepository, SubmissionRepo submissionRepository) {
        this.userRepository = userRepository;
        this.challengeRepository = challengeRepository;
        this.submissionRepository = submissionRepository;
    }

    @PreAuthorize("hasAuthority('VIEW_USERS')")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PreAuthorize("hasAuthority('VIEW_SUBMISSIONS')")
    public List<Submission> getAllSubmissions() {
        return submissionRepository.findAll();
    }

    @PreAuthorize("hasAuthority('DISABLE_USER')")
    public void DisableUser(UUID userId){
        // disable the user by restring their acces to pages and challenges
    }

    @PreAuthorize("hasAuthority('Search for user')")
    public User searchForUserByName(String userName){
        return userRepository.findFirstByUserName(userName).orElse(null);
    }

    @PreAuthorize("hasAuthority('Delete user')")
    public void deleteUser(UUID userId){
        userRepository.deleteById(userId);
    }

    @PreAuthorize("hasAuthority('VIEW_SUBMISSIONS')")
    public List<Submission> getAllSubmissionsByUser(UUID userId){
        return submissionRepository.findAllByUser_UserId(userId);
    }

    public long getNumberOfUsers() {
        return userRepository.count();
    }

    public long getNumberOfChallenges() {
        return challengeRepository.count();
    }

    public long getNumberOfSubmittedSolutions() {
        return submissionRepository.count();
    }

    public long getNumberOfSolvedChallenges() {
        return submissionRepository.countDistinctChallengesByStatus(SubmissionStatus.ACCEPTED);
    }

    public Map<String, Long> getPlatformStatistics() {
        Map<String, Long> statistics = new LinkedHashMap<>();
        statistics.put("numberOfUsers", getNumberOfUsers());
        statistics.put("numberOfChallenges", getNumberOfChallenges());
        statistics.put("numberOfSubmittedSolutions", getNumberOfSubmittedSolutions());
        statistics.put("numberOfSolvedChallenges", getNumberOfSolvedChallenges());
        return statistics;
    }

    public List<AdminUserStatistics> getUserStatistics() {
        return userRepository.findAll().stream()
                .map(user -> new AdminUserStatistics(
                        user.getUserId(),
                        user.getUserName(),
                        user.getEmail(),
                        submissionRepository.countByUser_UserId(user.getUserId()),
                        submissionRepository.countDistinctChallengesByUserIdAndStatus(
                                user.getUserId(),
                                SubmissionStatus.ACCEPTED
                        )
                ))
                .collect(Collectors.toList());
    }


}




