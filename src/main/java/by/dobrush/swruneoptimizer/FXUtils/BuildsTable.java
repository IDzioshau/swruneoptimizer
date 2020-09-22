package by.dobrush.swruneoptimizer.FXUtils;

import by.dobrush.swruneoptimizer.beans.Combination;
import by.dobrush.swruneoptimizer.beans.Monster;
import by.dobrush.swruneoptimizer.beans.RuneSet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuildsTable extends ResultsTable {

    private Monster monster;

    public BuildsTable(Combination combination, RuneSet[] runeSets, int hp, int atk, int def, int spd, int criRate, int criDamage, int res, int acc, Monster monster) {
        super(combination, runeSets, hp, atk, def, spd, criRate, criDamage, res, acc);
        this.monster = monster;
    }

    public BuildsTable(ResultsTable resultsTable) {
        this(resultsTable.getCombination(),
                resultsTable.getRuneSets(),
                resultsTable.getHp(),
                resultsTable.getAtk(),
                resultsTable.getDef(),
                resultsTable.getSpd(),
                resultsTable.getCriRate(),
                resultsTable.getCriDamage(),
                resultsTable.getRes(),
                resultsTable.getAcc(),
                resultsTable.getCombination().getMonster());
    }
}
