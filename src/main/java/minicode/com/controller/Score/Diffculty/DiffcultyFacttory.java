package minicode.com.controller.Score.Diffculty;

import java.util.ArrayList;
import java.util.List;

public class DiffcultyFacttory {

    private List<DifficultyMultiplier> difficultyMultipliers;

    public DiffcultyFacttory(){
        this.difficultyMultipliers = new ArrayList<>();
        difficultyMultipliers.add(new Easy());
        difficultyMultipliers.add(new Medium());
        difficultyMultipliers.add(new Hard());
    }

    public List<DifficultyMultiplier> getDiffcultyMultiplier(String diffcultyName) {
        for(DifficultyMultiplier difficultyMultiplier : difficultyMultipliers) {
            if(difficultyMultiplier.getDifficultyName().equalsIgnoreCase(diffcultyName)) {
                return List.of(difficultyMultiplier);
            }
        }
        return List.of();
    }
}
