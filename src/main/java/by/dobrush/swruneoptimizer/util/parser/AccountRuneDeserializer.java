package by.dobrush.swruneoptimizer.util.parser;

import by.dobrush.swruneoptimizer.beans.RuneSet;
import by.dobrush.swruneoptimizer.beans.Stat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccountRuneDeserializer extends StdDeserializer<AccountRune> {

    public AccountRuneDeserializer() {
        this(null);
    }

    public AccountRuneDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public AccountRune deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        return getAccountRuneFromJson(node);
    }

    public static AccountRune getAccountRuneFromJson(JsonNode runeAccountJson) throws JsonProcessingException {
        long id = runeAccountJson.get("rune_id").longValue();
        long equippedId = runeAccountJson.get("occupied_id").longValue();
        int slotNumber = runeAccountJson.get("slot_no").intValue();
        int stars = runeAccountJson.get("class").intValue();
        int lvl = runeAccountJson.get("upgrade_curr").intValue();
        RuneSet runeSet = RuneSet.values()[runeAccountJson.get("set_id").intValue() - 1];
        ObjectMapper mapper = new ObjectMapper();
        int[] mainStatArray = mapper.readValue(runeAccountJson.get("pri_eff").toString(), int[].class);
        Stat mainStat = new Stat(mainStatArray);
        int[] lockedStatArray = mapper.readValue(runeAccountJson.get("prefix_eff").toString(), int[].class);
        Stat lockedStat = (lockedStatArray[1] == 0) ? null : new Stat(lockedStatArray);
        int[][] additionStatsArray = mapper.readValue(runeAccountJson.get("sec_eff").toString(), int[][].class);
        List<Stat> stats = new ArrayList<>();
        for (int[] stat: additionStatsArray) {
            stats.add(new Stat(stat));
        }
        return new AccountRune(id, equippedId, slotNumber, stars, lvl, runeSet,
                mainStat, lockedStat, stats.toArray(new Stat[0]));
    }
}
