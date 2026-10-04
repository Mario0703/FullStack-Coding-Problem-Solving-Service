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

    public int getMultiPlier(String difficultyLevel){
        for(DiffcultyMultiplier diffcultyMultiplier : diffcultyMultipliers){
            if(diffcultyMultiplier.getMultiplier(difficultyLevel) != 0){
                return diffcultyMultiplier.getMultiplier(difficultyLevel);
            }
        }
        return 0;
    }
}
