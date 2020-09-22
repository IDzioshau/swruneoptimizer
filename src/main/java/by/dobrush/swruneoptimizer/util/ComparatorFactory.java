package by.dobrush.swruneoptimizer.util;

import by.dobrush.swruneoptimizer.beans.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ComparatorFactory {

    public static Comparator<Rune> getRuneComparator(Filter filter) {
        List<Stat> minStatWithPercent = new ArrayList<>();
        for (Stat stat : filter.getMinStats()) {
            switch (stat.getStatType()) {
                case HP: {
                    minStatWithPercent.add(new Stat(StatType.HP_PERCENT, 0));
                    minStatWithPercent.add(stat);
                    break;
                }
                case DEF: {
                    minStatWithPercent.add(new Stat(StatType.DEF_PERCENT, 0));
                    minStatWithPercent.add(stat);
                    break;
                }
                case ATK: {
                    minStatWithPercent.add(new Stat(StatType.ATK_PERCENT, 0));
                    minStatWithPercent.add(stat);
                    break;
                }
                default: {
                    minStatWithPercent.add(stat);
                    break;
                }
            }
        }
        return (r2, r1) -> {
            double result = 0;
            List<RuneSet> runeSets = filter.getRuneSets();
            if (runeSets.contains(r1.getRuneSet()) && !runeSets.contains(r2.getRuneSet())) {
                return 1;
            } else if (!runeSets.contains(r1.getRuneSet()) && runeSets.contains(r2.getRuneSet())) {
                return -1;
            } else if (runeSets.contains(r1.getRuneSet()) && runeSets.contains(r2.getRuneSet())
                    && r1.getRuneSet().getSize() > r2.getRuneSet().getSize()) {
                return 1;
            } else if (runeSets.contains(r1.getRuneSet()) && runeSets.contains(r2.getRuneSet())
                    && r1.getRuneSet().getSize() < r2.getRuneSet().getSize()) {
                return -1;
            } else if (r1.getRuneSet() == r2.getRuneSet()) {
                for (Stat stat : minStatWithPercent) {
                    result += Integer.compare(
                            r1.getStatByStatType(stat.getStatType()).getResultValue(),
                            r2.getStatByStatType(stat.getStatType()).getResultValue()
                    ) * filter.getStatWeights().get(stat.getStatType());
                }
            } else {
                int freeSize = 6;
                for (RuneSet runeSet : filter.getRuneSets()) {
                    freeSize -= runeSet.getSize();
                }
                for (Stat stat : minStatWithPercent) {
                    result += Integer.compare(
                            r1.getStatByStatType(stat.getStatType()).getResultValue(),
                            r2.getStatByStatType(stat.getStatType()).getResultValue()
                    ) * filter.getStatWeights().get(stat.getStatType());
                }
                if (filter.hasStatTypeInMinStats(r1.getRuneSet().getStatType())) {
                    result += Integer.compare(
                            r1.getStatByStatType(r1.getRuneSet().getStatType()).getResultValue()
                                    + ((freeSize / r1.getRuneSet().getSize()) * r1.getRuneSet().getStatBonus()),
                            r2.getStatByStatType(r1.getRuneSet().getStatType()).getResultValue()
                    ) * filter.getStatWeights().get(r1.getRuneSet().getStatType());
                }
                if (filter.hasStatTypeInMinStats(r2.getRuneSet().getStatType())) {
                    result += Integer.compare(
                            r1.getStatByStatType(r2.getRuneSet().getStatType()).getResultValue(),
                            r2.getStatByStatType(r2.getRuneSet().getStatType()).getResultValue()
                                    + ((freeSize / r2.getRuneSet().getSize()) * r2.getRuneSet().getStatBonus())
                    ) * filter.getStatWeights().get(r2.getRuneSet().getStatType());
                }
            }
            return Double.compare(result, 0);
        };
    }

    public static Comparator<Combination> getCombinationComparator(Filter filter) {
        return (c2, c1) -> {
            if (c2 == null && c1 == null) return 0;
            if (c1 == null) return -1;
            if (c2 == null) return 1;
            double result = 0.0;
            for (Stat stat: filter.getMinStats()) {
                result += Integer.compare(c1.getStat(stat.getStatType()).getResultValue(),
                        c2.getStat(stat.getStatType()).getResultValue()) * filter.getStatWeights().get(stat.getStatType());
            }
            return Double.compare(result, 0.0);
        };
    }
}
