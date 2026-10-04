package minicode.com.controller.Score.Diffculty;

public class Hard implements DifficultyMultiplier {

    public String getDifficultyName() {
        return "Hard";
    }

    @Override
    public int getMultiplier() {
        return 3;
    }
}
