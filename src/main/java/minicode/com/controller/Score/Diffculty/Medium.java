package minicode.com.controller.Score.Diffculty;

public class Medium implements DiffcultyMultiplier {
    @Override
    public int getMultiplier(String difficultyLevel) {
        return 2;
    }
}
