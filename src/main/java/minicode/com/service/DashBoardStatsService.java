package minicode.com.service;

import minicode.com.entity.Submission;
import minicode.com.entity.SubmissionStatus;
import minicode.com.entity.User;
import minicode.com.repository.ChallengeRepo;
import minicode.com.repository.SubmissionRepo;
import minicode.com.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

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

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<Submission> getAllSubmissions() {
        return submissionRepository.findAll();
    }

    public void DisableUser(UUID userId){
        // disable the user by restring their acces to pages and challenges
    }

    public User searchForUserByName(String userName){
        return userRepository.findFirstByUserName(userName).orElse(null);
    }

    public void deleteUser(UUID userId){
        userRepository.deleteById(userId);
    }

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



}
