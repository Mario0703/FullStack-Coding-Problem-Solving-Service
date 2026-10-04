package minicode.com.controller.Score.Diffculty;

public class Medium implements DifficultyMultiplier {

    public String getDifficultyName() {
        return "Medium";
    }

    @Override
    public int getMultiplier() {
        return 2;
    }
}
