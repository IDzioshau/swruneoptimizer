package by.dobrush.swruneoptimizer.util.parser;

import by.dobrush.swruneoptimizer.beans.Skill;
import by.dobrush.swruneoptimizer.beans.Stat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(using = SwarfarmMonsterDeserializer.class)
public class SwarfarmMonster {

    @JsonProperty("com2usId")
    private long com2usId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("image")
    private String image;

    @JsonProperty("canAwaken")
    private boolean canAwaken;

    @JsonProperty("isAwakened")
    private boolean awakened;

    @JsonProperty("skills")
    private Skill[] skills;

    @JsonProperty("stats")
    private Stat[] stats;

    @JsonIgnore
    public boolean canTwiceAwaken() {
        return canAwaken && isAwakened();
    }
}
