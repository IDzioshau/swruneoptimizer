package by.dobrush.swruneoptimizer.util;

import by.dobrush.swruneoptimizer.beans.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
public class Filter {

    private Map<StatType, Double> statWeights;
    private List<RuneSet> runeSets;
    private List<Stat> minStats;
    private List<Monster> lockedMonsters;
    private int depth;
    private int timeOut;
    private int combinationLimit;
    private boolean useOnlyInventoryRunes;
    private boolean useBuildsRunes;

    public Filter() {
        this.statWeights = new LinkedHashMap<>();
        this.runeSets = new ArrayList<>();
        this.minStats = new ArrayList<>();
        this.lockedMonsters = new ArrayList<>();
        this.depth = 30;
        this.timeOut = 90;
        this.combinationLimit = 100;
        this.useOnlyInventoryRunes = false;
        this.useBuildsRunes = false;
    }

    public void initWeights(Monster monster) {
        for (StatType statType : StatType.values()) {
            statWeights.put(statType, 1.0);
        }
        for (Stat minStat : this.minStats) {
            switch (minStat.getStatType()) {
                case HP: {
                    double weight = (double) minStat.getValue() / Arrays.stream(monster.getStats())
                            .filter(s -> s.getStatType() == StatType.HP).findFirst().get().getValue();
                    this.statWeights.replace(StatType.HP_PERCENT, weight);
                    this.statWeights.replace(StatType.HP, weight / 100);
                    break;
                }
                case ATK: {
                    double weight = (double) minStat.getValue() / Arrays.stream(monster.getStats())
                            .filter(s -> s.getStatType() == StatType.ATK).findFirst().get().getValue();
                    this.statWeights.replace(StatType.ATK_PERCENT, weight);
                    this.statWeights.replace(StatType.ATK, weight / 10);
                    break;
                }
                case DEF: {
                    double weight = (double) minStat.getValue() / Arrays.stream(monster.getStats())
                            .filter(s -> s.getStatType() == StatType.DEF).findFirst().get().getValue();
                    this.statWeights.replace(StatType.DEF_PERCENT, weight);
                    this.statWeights.replace(StatType.DEF, weight / 10);
                    break;
                }
                case SPD: {
                    double weight = (double) minStat.getValue() / Arrays.stream(monster.getStats())
                            .filter(s -> s.getStatType() == StatType.SPD).findFirst().get().getValue();
                    this.statWeights.replace(StatType.SPD, weight);
                    this.statWeights.replace(StatType.SPD_PERCENT, weight);
                    break;
                }
                case ACC: {
                    this.statWeights.replace(minStat.getStatType(),
                            (double) (minStat.getValue() + 15) / Math.max(Arrays.stream(monster.getStats())
                                    .filter(s -> s.getStatType() == minStat.getStatType())
                                    .findFirst().get().getValue(), 15));
                    break;
                }
                default: {
                    this.statWeights.replace(minStat.getStatType(),
                            (double) minStat.getValue() / Arrays.stream(monster.getStats())
                                    .filter(s -> s.getStatType() == minStat.getStatType())
                                    .findFirst().get().getValue());
                }
            }
        }
    }

    public boolean hasStatTypeInMinStats(StatType statType) {
        List<StatType> statTypes = new ArrayList<>();
        for (Stat stat : this.getMinStats()) {
            switch (stat.getStatType()) {
                case HP: {
                    statTypes.add(StatType.HP_PERCENT);
                    break;
                }
                case ATK: {
                    statTypes.add(StatType.ATK_PERCENT);
                    break;
                }
                case DEF: {
                    statTypes.add(StatType.DEF_PERCENT);
                    break;
                }
                case SPD: {
                    statTypes.add(StatType.SPD_PERCENT);
                    break;
                }
                default: {
                    statTypes.add(stat.getStatType());
                }
            }
        }
        return statTypes.contains(statType);
    }
}
