package minicode.com.controller.Score.Diffculty;

public class Hard implements DiffcultyMultiplier {

    public String getDifficultyName() {
        return "Hard";
    }

    @Override
    public int getMultiplier() {
        return 3;
    }
}
