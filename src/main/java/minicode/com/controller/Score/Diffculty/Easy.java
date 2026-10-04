package minicode.com.controller.Score.Diffculty;

public class Easy implements DiffcultyMultiplier {
    @Override
    public int getMultiplier(String difficultyLevel) {
        return 1;
    }
}
