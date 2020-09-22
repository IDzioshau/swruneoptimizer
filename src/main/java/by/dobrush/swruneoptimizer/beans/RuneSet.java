package by.dobrush.swruneoptimizer.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum RuneSet {
    @JsonProperty("ENERGY")
    ENERGY(StatType.HP_PERCENT, 15, 2),
    @JsonProperty("GUARD")
    GUARD(StatType.DEF_PERCENT, 15, 2),
    @JsonProperty("SWIFT")
    SWIFT(StatType.SPD_PERCENT, 25, 4),
    @JsonProperty("BLADE")
    BLADE(StatType.CR, 12, 2),
    @JsonProperty("RAGE")
    RAGE(StatType.CD, 40, 4),
    @JsonProperty("FOCUS")
    FOCUS(StatType.ACC, 20, 2),
    @JsonProperty("ENDURE")
    ENDURE(StatType.RES, 20, 2),
    @JsonProperty("FATAL")
    FATAL(StatType.ATK_PERCENT, 35, 4),
    @JsonProperty("EMPTY1")
    EMPTY1(null, 0, 0),
    @JsonProperty("DESPAIR")
    DESPAIR(null, 25, 4),
    @JsonProperty("VAMPIRE")
    VAMPIRE(null, 35, 4),
    @JsonProperty("EMPTY2")
    EMPTY2(null, 0, 0),
    @JsonProperty("VIOLENT")
    VIOLENT(null, 22, 4),
    @JsonProperty("NEMESIS")
    NEMESIS(null, 4, 2),
    @JsonProperty("WILL")
    WILL(null, 1, 2),
    @JsonProperty("SHIELD")
    SHIELD(null, 15, 2),
    @JsonProperty("REVENGE")
    REVENGE(null, 15, 2),
    @JsonProperty("DESTROY")
    DESTROY(null, 4, 2),
    @JsonProperty("FIGHT")
    FIGHT(StatType.ATK_PERCENT, 8, 2),
    @JsonProperty("DETERMINATION")
    DETERMINATION(StatType.DEF_PERCENT, 8, 2),
    @JsonProperty("ENHANCE")
    ENHANCE(StatType.HP_PERCENT, 8, 2),
    @JsonProperty("ACCURACY")
    ACCURACY(StatType.ACC, 10, 2),
    @JsonProperty("TOLERANCE")
    TOLERANCE(StatType.RES, 10, 2);

    private StatType statType;
    private int statBonus;
    private int size;

    public boolean hasStatType(StatType statType) {
        switch (statType) {
            case HP:
                return this.statType == StatType.HP || this.statType == StatType.HP_PERCENT;
            case DEF:
                return this.statType == StatType.DEF || this.statType == StatType.DEF_PERCENT;
            case ATK:
                return this.statType == StatType.ATK || this.statType == StatType.ATK_PERCENT;
            case SPD:
                return this.statType == StatType.SPD || this.statType == StatType.SPD_PERCENT;
            default:
                return this.statType == statType;
        }
    }
}
