package minicode.com.controller.Score.Diffculty;

public class Hard implements DiffcultyMultiplier {
    @Override
    public int getMultiplier(String difficultyLevel) {
        return 3;
    }
}
