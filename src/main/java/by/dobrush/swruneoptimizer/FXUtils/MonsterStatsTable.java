package by.dobrush.swruneoptimizer.FXUtils;

import by.dobrush.swruneoptimizer.beans.StatType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonsterStatsTable {

    private StatType statType;

    private int basicValue;

    private int value;

    private int totalValue;
}
