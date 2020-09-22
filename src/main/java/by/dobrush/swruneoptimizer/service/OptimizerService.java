package by.dobrush.swruneoptimizer.service;

import by.dobrush.swruneoptimizer.util.Filter;
import by.dobrush.swruneoptimizer.beans.Combination;
import by.dobrush.swruneoptimizer.beans.Monster;
import by.dobrush.swruneoptimizer.beans.Rune;
import by.dobrush.swruneoptimizer.beans.RuneSet;
import by.dobrush.swruneoptimizer.util.ComparatorFactory;
import by.dobrush.swruneoptimizer.util.OptimizeEntity;
import by.dobrush.swruneoptimizer.util.parser.AccountEntity;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OptimizerService {

    private List<Rune> filteredRunes;
    private AccountEntity accountEntity = AccountEntity.getInstance();


    public List<Monster> getMonsters() {
        return accountEntity.getMonsters();
    }

    public List<Combination> optimize(OptimizeEntity optimizeEntity) {
        Monster monster = optimizeEntity.getMonster();
        Filter filter = optimizeEntity.getFilter();
        filterRunes(monster, filter);
        filter.initWeights(monster);
        return permute(monster, filter);
    }

    private void filterRunes(Monster monster, Filter filter) {
        filteredRunes = new ArrayList<>(accountEntity.getRunes());

        if (filter.isUseOnlyInventoryRunes()) {
            filteredRunes = filteredRunes.parallelStream()
                    .filter(r -> (r.getEquippedId() == monster.getId()) || (r.getEquippedId() == 0))
                    .collect(Collectors.toList());
        }

        if (!filter.isUseBuildsRunes()) {
            accountEntity.getBuilds().parallelStream().forEach(c -> filteredRunes.removeAll(c.getRunes()));
        }

        if (!filter.getRuneSets().isEmpty()) {
            int sumSetSize = 0;
            for (RuneSet runeSet : filter.getRuneSets()) {
                sumSetSize += runeSet.getSize();
            }
            if (sumSetSize == 6) {
                filteredRunes = filteredRunes.parallelStream()
                        .filter(r -> filter.getRuneSets().contains(r.getRuneSet()))
                        .collect(Collectors.toList());
            }
        }

        if (!filter.getLockedMonsters().isEmpty()) {
            filter.getLockedMonsters().parallelStream()
                    .forEach(m -> filteredRunes.removeAll(Arrays.asList(m.getRunes())));
        }

        filteredRunes = filteredRunes.parallelStream()
                .filter(r -> r.containsNeededStats(filter.getMinStats())).collect(Collectors.toList());
    }

    private List<Combination> permute(Monster monster, Filter filter) {
        List<Combination> combinations = new ArrayList<>();
        Map<Integer, List<Rune>> slots;

        Comparator<Rune> runeComparator = ComparatorFactory.getRuneComparator(filter);
        Comparator<Combination> combinationComparator = ComparatorFactory.getCombinationComparator(filter);

        int setsSize = 0;
        for (RuneSet runeSet : filter.getRuneSets()) {
            setsSize += runeSet.getSize();
        }

        switch (setsSize) {
            case 6: {
                List<int[]> slotCombinations = getSlotCombinations(4);
                RuneSet bigSet;
                RuneSet smallSet;
                if (filter.getRuneSets().get(0).getSize() == 4) {
                    bigSet = filter.getRuneSets().get(0);
                    smallSet = filter.getRuneSets().get(1);
                } else {
                    bigSet = filter.getRuneSets().get(1);
                    smallSet = filter.getRuneSets().get(0);
                }
                for (int[] slotCombination : slotCombinations) {
                    slots = new HashMap<>();
                    for (int slot : slotCombination) {
                        slots.put(slot, filteredRunes.parallelStream()
                                .filter(r -> r.getSlotNumber() == slot && r.getRuneSet() == bigSet)
                                .sorted(runeComparator).collect(Collectors.toList()));
                    }
                    for (int i = 1; i <= 6; i++) {
                        if (!slots.containsKey(i)) {
                            int finalI = i;
                            slots.put(i, filteredRunes.parallelStream()
                                    .filter(r -> r.getSlotNumber() == finalI && r.getRuneSet() == smallSet)
                                    .sorted(runeComparator).collect(Collectors.toList()));
                        }
                    }
                    combinations.addAll(getSuitableCombinations(filter, monster, slots));
                }
                break;
            }
            case 4:
            case 2: {
                List<int[]> slotCombinations = getSlotCombinations(setsSize);
                buildSlotCombinationsAndCalculate(monster, filter, combinations, runeComparator, slotCombinations);
                break;
            }
            default: {
                slots = new HashMap<>();
                for (int i = 1; i <= 6; i++) {
                    int finalI = i;
                    slots.put(i, filteredRunes.parallelStream()
                            .filter(r -> r.getSlotNumber() == finalI)
                            .sorted(runeComparator).collect(Collectors.toList()));
                }
                combinations.addAll(getSuitableCombinations(filter, monster, slots));
            }
        }

        return combinations.parallelStream().sorted(combinationComparator).limit(filter.getCombinationLimit()).collect(Collectors.toList());
    }

    private void buildSlotCombinationsAndCalculate(Monster monster,
                                                   Filter filter,
                                                   List<Combination> combinations,
                                                   Comparator<Rune> runeComparator,
                                                   List<int[]> slotCombinations) {
        Map<Integer, List<Rune>> slots;
        for (int[] slotCombination : slotCombinations) {
            slots = new HashMap<>();
            for (int slot : slotCombination) {
                slots.put(slot, filteredRunes.parallelStream()
                        .filter(r -> r.getSlotNumber() == slot && r.getRuneSet() == filter.getRuneSets().get(0))
                        .sorted(runeComparator).collect(Collectors.toList()));
            }
            for (int i = 1; i <= 6; i++) {
                if (!slots.containsKey(i)) {
                    int finalI = i;
                    slots.put(i, filteredRunes.parallelStream()
                            .filter(r -> r.getSlotNumber() == finalI)
                            .sorted(runeComparator).collect(Collectors.toList()));
                }
            }
            combinations.addAll(getSuitableCombinations(filter, monster, slots));
        }
    }

    private List<Combination> getSuitableCombinations(Filter filter, Monster monster, Map<Integer, List<Rune>> slots) {
        List<Combination> combinations = new ArrayList<>();

        double start = System.currentTimeMillis()/1000.0;

        slots.get(1).parallelStream().forEach(r1 -> {
            int count2 = 0;
            int count3;
            int count4;
            int count5;
            int count6;
            loop2:
            for (Rune r2 : slots.get(2)) {
                count3 = 0;
                loop3:
                for (Rune r3 : slots.get(3)) {
                    count4 = 0;
                    loop4:
                    for (Rune r4 : slots.get(4)) {
                        count5 = 0;
                        loop5:
                        for (Rune r5 : slots.get(5)) {
                            count6 = 0;
                            for (Rune r6 : slots.get(6)) {
                                if (System.currentTimeMillis()/1000.0 - start >= filter.getTimeOut()){
                                    return;
                                }
                                if (Combination.isSuitable(monster, filter.getMinStats(), filter.getRuneSets(),
                                        r1, r2, r3, r4, r5, r6)) {
                                    count2 = 0;
                                    count3 = 0;
                                    count4 = 0;
                                    count5 = 0;
                                    count6 = 0;
                                    Combination combination = new Combination(monster, Arrays.asList(r1, r2, r3, r4, r5, r6));
                                    combinations.add(combination);
                                } else {
                                    if (needBreak(slots, r2, r3, r4, r5, r6)) {
                                        count2++;
                                        if (count2 >= filter.getDepth()) {
                                            break loop2;
                                        }
                                    }
                                    if (needBreak(slots, r3, r4, r5, r6)) {
                                        count3++;
                                        if (count3 >= filter.getDepth()) {
                                            break loop3;
                                        }
                                    }
                                    if (needBreak(slots, r4, r5, r6)) {
                                        count4++;
                                        if (count4 >= filter.getDepth()) {
                                            break loop4;
                                        }
                                    }
                                    if (needBreak(slots, r5, r6)) {
                                        count5++;
                                        if (count5 >= filter.getDepth()) {
                                            break loop5;
                                        }
                                    }
                                    count6++;
                                    if (count6 >= filter.getDepth()) {
                                        break;
                                    }
                                }
                                count6++;
                            }
                            count5++;
                        }
                        count4++;
                    }
                    count3++;
                }
                count2++;
            }
        });

        return combinations;
    }

    private boolean needBreak(Map<Integer, List<Rune>> slots, Rune... runes) {
        boolean result = runes[0] != slots.get(runes[0].getSlotNumber()).get(0);
        for (int i = 1; i < runes.length; i++) {
            result = result && (slots.get(runes[i].getSlotNumber()).get(0) == runes[i]);
        }
        return result;
    }

    private List<int[]> getSlotCombinations(int size) {
        List<int[]> result = new ArrayList<>();
        int[] array = IntStream.range(0, size).map(i -> i + 1).toArray();
        if (size <= 6) {
            do {
                result.add(array.clone());
            }
            while (nextSet(array, size));
        }
        return result;
    }

    private boolean nextSet(int[] a, int size) {
        for (int i = size - 1; i >= 0; --i) {
            if (a[i] < 6 - size + i + 1) {
                ++a[i];
                IntStream.range(i + 1, size).forEach(j -> a[j] = a[j - 1] + 1);
                return true;
            }
        }
        return false;
    }
}
