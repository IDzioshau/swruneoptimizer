package by.dobrush.swruneoptimizer.FXUtils;

import by.dobrush.swruneoptimizer.beans.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableDataForJavaFX {

    public static ObservableList<MonsterStatsTable> getMonsterStats(Monster monster) {
        return getObsListWithStats(monster, Arrays.asList(monster.getRunes()));
    }

    public static ObservableList<MonsterStatsTable> getMonsterStats(Combination combination) {
        return getObsListWithStats(combination.getMonster(), combination.getRunes());
    }

    public static ObservableList<RunesTable> getCombinationRunes(ResultsTable resultsTable) {
        List<Rune> runes = resultsTable.getCombination().getRunes();
        return FXCollections.observableArrayList(new RunesTable(runes.get(0), runes.get(1), runes.get(2), runes.get(3), runes.get(4), runes.get(5)));
    }

    public static ObservableList<ResultsTable> getResultsTable(List<Combination> combinations) {
        List<ResultsTable> resultsTable = new ArrayList<>();
        for (Combination combination: combinations) {
            Stat[] stats = combination.getTotalStats();
            resultsTable.add(new ResultsTable(combination,
                    Combination.getRuneSets(combination.getRunes().toArray(new Rune[0])).toArray(new RuneSet[0]),
                    stats[0].getResultValue(), stats[1].getResultValue(), stats[2].getResultValue(),
                    stats[3].getResultValue(), stats[4].getResultValue(), stats[5].getResultValue(),
                    stats[6].getResultValue(), stats[7].getResultValue()));
        }
        return FXCollections.observableList(resultsTable);
    }

    public static ObservableList<BuildsTable> getBuildsTable(List<Combination> combinations) {
        List<BuildsTable> buildsTable = new ArrayList<>();
        for (Combination combination: combinations) {
            Stat[] stats = combination.getTotalStats();
            buildsTable.add(new BuildsTable(combination,
                    Combination.getRuneSets(combination.getRunes().toArray(new Rune[0])).toArray(new RuneSet[0]),
                    stats[0].getResultValue(), stats[1].getResultValue(), stats[2].getResultValue(),
                    stats[3].getResultValue(), stats[4].getResultValue(), stats[5].getResultValue(),
                    stats[6].getResultValue(), stats[7].getResultValue(), combination.getMonster()));
        }
        return FXCollections.observableList(buildsTable);
    }

    public static BuildsTable getOneBuildsTable(ResultsTable resultsTable) {
        return new BuildsTable(resultsTable);
    }

    private static ObservableList<MonsterStatsTable> getObsListWithStats(Monster monster, List<Rune> runes) {
        List<MonsterStatsTable> monsterStats = new ArrayList<>();
        if (runes.isEmpty()) {
            for (int i = 0; i < 8; i++) {
                monsterStats.add(new MonsterStatsTable(monster.getStats()[i].getStatType(),
                        monster.getStats()[i].getValue(),
                        0,
                        monster.getStats()[i].getValue()));
            }
        } else {
            Combination combination = new Combination(monster, runes);
            Stat[] totalStats = combination.getTotalStats();
            for (int i = 0; i < 8; i++) {
                monsterStats.add(new MonsterStatsTable(monster.getStats()[i].getStatType(),
                        monster.getStats()[i].getValue(),
                        totalStats[i].getResultValue() - monster.getStats()[i].getValue(),
                        totalStats[i].getResultValue()));
            }
        }
        return FXCollections.observableList(monsterStats);
    }
}
