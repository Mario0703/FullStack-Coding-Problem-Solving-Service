package minicode.com.controller.Score.Diffculty;

public class Easy implements DifficultyMultiplier {

    public String getDifficultyName() {
        return "Easy";
    }

    @Override
    public int getMultiplier() {
        return 1;
    }
}
