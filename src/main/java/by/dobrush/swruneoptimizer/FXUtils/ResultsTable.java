package by.dobrush.swruneoptimizer.FXUtils;

import by.dobrush.swruneoptimizer.beans.Combination;
import by.dobrush.swruneoptimizer.beans.RuneSet;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Data
@NoArgsConstructor
public class ResultsTable {

    private Combination combination;

    private RuneSet[] runeSets;

    private String runeSetsString;

    private int hp;

    private int atk;

    private int def;

    private int spd;

    private int criRate;

    private int criDamage;

    private int res;

    private int acc;

    public ResultsTable(Combination combination, RuneSet[] runeSets, int hp, int atk, int def, int spd, int criRate, int criDamage, int res, int acc) {
        this.combination = combination;
        this.runeSets = runeSets;
        this.runeSetsString = Arrays.toString(runeSets);
        this.hp = hp;
        this.atk = atk;
        this.def = def;
        this.spd = spd;
        this.criRate = criRate;
        this.criDamage = criDamage;
        this.res = res;
        this.acc = acc;
    }
}
