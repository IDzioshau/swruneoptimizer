package by.dobrush.swruneoptimizer.beans;

import by.dobrush.swruneoptimizer.util.parser.SwarfarmMonster;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize
public class Monster extends SwarfarmMonster implements Cloneable {

    @JsonProperty("id")
    private long id;

    @JsonProperty("runes")
    private Rune[] runes;

    @JsonProperty("rtaRunes")
    private Rune[] rtaRunes;

    @JsonProperty("stars")
    private int stars;

    @JsonIgnore
    private boolean locked;

    @Override
    public Monster clone() throws CloneNotSupportedException {
        return (Monster) super.clone();
    }

    @Override
    public String toString() {
        return super.getName();
    }
}
