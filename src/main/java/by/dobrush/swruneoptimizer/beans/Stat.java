package by.dobrush.swruneoptimizer.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Stat implements Cloneable {
    @JsonProperty("statType")
    private StatType statType;
    @JsonProperty("value")
    private int value;
    @JsonProperty("isReforged")
    private boolean isReforged;
    @JsonProperty("sandingValue")
    private int sandingValue;

    public Stat(StatType statType, int value) {
        this.statType = statType;
        this.value = value;
        this.isReforged = false;
        this.sandingValue = 0;
    }

    public Stat(int[] data) {
        this.statType = StatType.values()[(data[0] == 0) ? 0 : (data[0] - 1)];
        this.value = data[1];
        if (data.length == 4) {
            this.isReforged = data[2] == 1;
            this.sandingValue = data[3];
        }
    }

    @JsonIgnore
    public int getResultValue() {
        return value + sandingValue;
    }

    @Override
    protected Stat clone() throws CloneNotSupportedException {
        return (Stat) super.clone();
    }

    void addValue(double value) {
        this.value += Math.round(value);
    }

}
