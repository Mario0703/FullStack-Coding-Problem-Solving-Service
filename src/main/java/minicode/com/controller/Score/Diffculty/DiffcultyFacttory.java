package minicode.com.controller.Score.Diffculty;

import java.util.ArrayList;
import java.util.List;

public class DiffcultyFacttory {

    private List<DiffcultyMultiplier> diffcultyMultipliers;

    public DiffcultyFacttory(){
        this.diffcultyMultipliers = new ArrayList<>();
        diffcultyMultipliers.add(new Easy());
        diffcultyMultipliers.add(new Medium());
        diffcultyMultipliers.add(new Hard());
    }

    public List<DiffcultyMultiplier> getDiffcultyMultiplier(String diffcultyName) {
        for(DiffcultyMultiplier diffcultyMultiplier : diffcultyMultipliers) {
            if(diffcultyMultiplier.getDifficultyName().equalsIgnoreCase(diffcultyName)) {
                return List.of(diffcultyMultiplier);
            }
        }
        return List.of();
    }
}
