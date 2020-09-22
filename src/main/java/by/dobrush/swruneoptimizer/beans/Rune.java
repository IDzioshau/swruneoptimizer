package by.dobrush.swruneoptimizer.beans;

import by.dobrush.swruneoptimizer.util.parser.AccountEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rune {

    @JsonProperty("id")
    private long id;

    @JsonProperty("equippedId")
    private long equippedId;

    @JsonProperty("rtaEquippedId")
    private long rtaEquippedId;

    @JsonProperty("slotNumber")
    private int slotNumber;

    @JsonProperty("stars")
    private int stars;

    @JsonProperty("lvl")
    private int lvl;

    @JsonProperty("runeSet")
    private RuneSet runeSet;

    @JsonProperty("mainStat")
    private Stat mainStat;

    @JsonProperty("lockedStat")
    private Stat lockedStat;

    @JsonProperty("stats")
    private Stat[] stats;

    private List<StatType> getStatTypeList() {
        List<StatType> list = new ArrayList<>();
        list.add(mainStat.getStatType());
        if (lockedStat != null) {
            list.add(lockedStat.getStatType());
        }
        for (Stat stat: stats) {
            list.add(stat.getStatType());
        }
        return list;
    }

    public boolean containsNeededStats(List<Stat> stats) {
        List<StatType> statTypes = getStatTypeList();
        for (Stat stat: stats) {
            if (statTypes.contains(stat.getStatType()) || runeSet.hasStatType(stat.getStatType())) {
                return true;
            }
        }
        return false;
    }

    public Stat getStatByStatType(StatType statType) {
        if (mainStat.getStatType() == statType) {
            return mainStat;
        }
        if (lockedStat != null && lockedStat.getStatType() == statType) {
            return lockedStat;
        }
        for (Stat stat: stats) {
            if (stat.getStatType() == statType) {
                return stat;
            }
        }
        return new Stat();
    }

    public void up() {
        this.mainStat.setValue(this.mainStat.getStatType()
                .getMaxMainStatValue(this.stars > 6 ? this.stars - 11 : this.stars - 1));
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(stars).append("★\n");
        stringBuilder.append(runeSet).append("\n");
        stringBuilder.append(mainStat.getStatType()).append(" ").append(mainStat.getValue()).append("\n\n");
        if (lockedStat != null) {
            stringBuilder.append(lockedStat.getStatType()).append(" ").append(lockedStat.getValue()).append("\n\n");
        } else {
            stringBuilder.append("\n\n");
        }
        for (Stat stat: stats) {
            if (stat.isReforged()) {
                stringBuilder.append("\uD83D\uDDD8 ");
            }
            stringBuilder.append(stat.getStatType()).append(" ").append(stat.getResultValue());
            if (stat.getSandingValue() != 0) {
                stringBuilder.append(" (+").append(stat.getSandingValue()).append(")");
            }
            stringBuilder.append("\n");
        }
        stringBuilder.append("\n");
        if (equippedId != 0) {
            stringBuilder.append(AccountEntity.getInstance().getMonsterById(equippedId).getName());
        }
        return  stringBuilder.toString();
    }
}
