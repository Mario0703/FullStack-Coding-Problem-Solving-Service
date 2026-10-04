package minicode.com.controller.Score.Diffculty;

public class Medium implements DiffcultyMultiplier {

    public String getDifficultyName() {
        return "Medium";
    }

    @Override
    public int getMultiplier() {
        return 2;
    }
}
