package by.dobrush.swruneoptimizer.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Combination {

    private Monster monster;
    private List<Rune> runes;

    public static boolean isSuitable(Monster monster, List<Stat> minStats, List<RuneSet> runeSets,
                                     Rune r1, Rune r2, Rune r3, Rune r4, Rune r5, Rune r6) {
        if (runeSets != null) {
            if (!getRuneSets(r1, r2, r3, r4, r5, r6).containsAll(runeSets)) {
                return false;
            }
        }
        Stat[] totalStats = getTotalStats(monster, r1, r2, r3, r4, r5, r6);
        for (Stat minStat : minStats) {
            switch (minStat.getStatType()) {
                case HP: {
                    if (totalStats[0].getResultValue() < minStat.getResultValue()) {
                        return false;
                    }
                    break;
                }
                case ATK: {
                    if (totalStats[1].getResultValue() < minStat.getResultValue()) {
                        return false;
                    }
                    break;
                }
                case DEF: {
                    if (totalStats[2].getResultValue() < minStat.getResultValue()) {
                        return false;
                    }
                    break;
                }
                case SPD: {
                    if (totalStats[3].getResultValue() < minStat.getResultValue()) {
                        return false;
                    }
                    break;
                }
                case CR: {
                    if (totalStats[4].getResultValue() < minStat.getResultValue()) {
                        return false;
                    }
                    break;
                }
                case CD: {
                    if (totalStats[5].getResultValue() < minStat.getResultValue()) {
                        return false;
                    }
                    break;
                }
                case RES: {
                    if (totalStats[6].getResultValue() < minStat.getResultValue()) {
                        return false;
                    }
                    break;
                }
                case ACC: {
                    if (totalStats[7].getResultValue() < minStat.getResultValue()) {
                        return false;
                    }
                    break;
                }
            }
        }
        return true;
    }

    public Stat getStat(StatType type) {
        Stat[] stats = getTotalStats();
        for (Stat stat : stats) {
            if (stat.getStatType() == type) {
                return stat;
            }
        }
        return new Stat();
    }

    public static List<RuneSet> getRuneSets(Rune... runes) {
        List<RuneSet> runeSetsResult = new ArrayList<>();
        Map<RuneSet, Integer> runeSets = new HashMap<>();
        for (Rune rune : runes) {
            Integer value = runeSets.put(rune.getRuneSet(), 1);
            if (value != null) {
                runeSets.put(rune.getRuneSet(), ++value);
            }
        }
        for (Map.Entry<RuneSet, Integer> runeSet : runeSets.entrySet()) {
            if (runeSet.getKey().getSize() == runeSet.getValue()) {
                runeSetsResult.add(runeSet.getKey());
            }
            if (runeSet.getKey().getSize() >= runeSet.getValue()) {
                for (int i = 0; i < runeSet.getValue() / runeSet.getKey().getSize(); i++) {
                    if (runeSet.getKey().getSize() == 4 && !runeSetsResult.contains(runeSet.getKey())) {
                        runeSetsResult.add(runeSet.getKey());
                    }
                }
            }
        }
        return runeSetsResult;
    }

    private static void addRuneSetBonuses(Monster monster, Stat[] stats, List<RuneSet> runeSets) {
        for (RuneSet runeSet : runeSets) {
            addStat(monster, stats, new Stat(runeSet.getStatType(), runeSet.getStatBonus()));
        }
    }

    @JsonIgnore
    public Stat[] getTotalStats() {
        return getTotalStats(monster, runes.get(0), runes.get(1), runes.get(2), runes.get(3), runes.get(4), runes.get(5));
    }

    @SneakyThrows
    private static Stat[] getTotalStats(Monster monster, Rune r1, Rune r2, Rune r3, Rune r4, Rune r5, Rune r6) {
        Stat[] monsterStats = monster.getStats();
        Stat[] stats = new Stat[8];
        for (int i = 0; i < 8; i++) {
            stats[i] = monsterStats[i].clone();
        }
        addStats(monster, stats, r1);
        addStats(monster, stats, r2);
        addStats(monster, stats, r3);
        addStats(monster, stats, r4);
        addStats(monster, stats, r5);
        addStats(monster, stats, r6);
        addRuneSetBonuses(monster, stats, getRuneSets(r1, r2, r3, r4, r5, r6));
        return stats;
    }

    private static void addStats(Monster monster, Stat[] stats, Rune rune) {
        addStat(monster, stats, rune.getMainStat());
        if (rune.getLockedStat() != null) {
            addStat(monster, stats, rune.getLockedStat());
        }
        for (Stat stat : rune.getStats()) {
            addStat(monster, stats, stat);
        }
    }

    private static void addStat(Monster monster, Stat[] stats, Stat stat) {
        Stat[] monsterStats = monster.getStats();
        if (stat.getStatType() != null) {
            switch (stat.getStatType()) {
                case HP: {
                    stats[0].addValue(stat.getResultValue());
                    break;
                }
                case HP_PERCENT: {
                    stats[0].addValue((double) monsterStats[0].getResultValue() * stat.getResultValue() / 100);
                    break;
                }
                case ATK: {
                    stats[1].addValue(stat.getResultValue());
                    break;
                }
                case ATK_PERCENT: {
                    stats[1].addValue((double) monsterStats[1].getResultValue() * stat.getResultValue() / 100);
                    break;
                }
                case DEF: {
                    stats[2].addValue(stat.getResultValue());
                    break;
                }
                case DEF_PERCENT: {
                    stats[2].addValue((double) monsterStats[2].getResultValue() * stat.getResultValue() / 100);
                    break;
                }
                case SPD: {
                    stats[3].addValue(stat.getResultValue());
                    break;
                }
                case SPD_PERCENT: {
                    stats[3].addValue((double) monsterStats[3].getResultValue() * stat.getResultValue() / 100);
                    break;
                }
                case CR: {
                    stats[4].addValue(stat.getResultValue());
                    break;
                }
                case CD: {
                    stats[5].addValue(stat.getResultValue());
                    break;
                }
                case RES: {
                    stats[6].addValue(stat.getResultValue());
                    break;
                }
                case ACC: {
                    stats[7].addValue(stat.getResultValue());
                    break;
                }
            }
        }
    }
}
