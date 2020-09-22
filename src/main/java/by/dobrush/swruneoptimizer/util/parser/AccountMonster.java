package by.dobrush.swruneoptimizer.util.parser;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonDeserialize(using = AccountMonsterDeserializer.class)
public class AccountMonster {

    @JsonProperty("id")
    private long id;

    @JsonProperty("com2usId")
    private long com2usId;

    @JsonProperty("stars")
    private int stars;

    @JsonProperty("runes")
    private AccountRune[] runes;

}
