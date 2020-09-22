package by.dobrush.swruneoptimizer.util.parser;

import by.dobrush.swruneoptimizer.beans.Stat;
import by.dobrush.swruneoptimizer.beans.StatType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SwarfarmMonsterDeserializer extends StdDeserializer<SwarfarmMonster> {

    public SwarfarmMonsterDeserializer() {
        this(null);
    }

    public SwarfarmMonsterDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public SwarfarmMonster deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        long id = node.get("com2us_id").longValue();
        String name = node.get("name").asText();
        String image = "https://swarfarm.com/static/herders/images/monsters/" + node.get("image_filename").asText();
        boolean canAwaken = !node.get("awakens_to").isNull();
        boolean isAwakened = !node.get("awakens_from").isNull();
        List<Stat> stats = new ArrayList<>();
        stats.add(new Stat(StatType.HP, node.get("max_lvl_hp").intValue()));
        stats.add(new Stat(StatType.ATK, node.get("max_lvl_attack").intValue()));
        stats.add(new Stat(StatType.DEF, node.get("max_lvl_defense").intValue()));
        stats.add(new Stat(StatType.SPD, node.get("speed").intValue()));
        stats.add(new Stat(StatType.CR, node.get("crit_rate").intValue()));
        stats.add(new Stat(StatType.CD, node.get("crit_damage").intValue()));
        stats.add(new Stat(StatType.RES, node.get("resistance").intValue()));
        stats.add(new Stat(StatType.ACC, node.get("accuracy").intValue()));

        return new SwarfarmMonster(id, name, image, canAwaken, isAwakened,
                null, stats.toArray(new Stat[0]));
    }
}
