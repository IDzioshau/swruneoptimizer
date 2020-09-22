package by.dobrush.swruneoptimizer.util.parser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccountMonsterDeserializer extends StdDeserializer<AccountMonster> {

    public AccountMonsterDeserializer() {
        this(null);
    }

    public AccountMonsterDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public AccountMonster deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        long id = node.get("unit_id").longValue();
        int stars = node.get("class").intValue();
        long com2usId = node.get("unit_master_id").longValue();
        List<AccountRune> runes = new ArrayList<>();
        for (JsonNode rune: node.get("runes")) {
            runes.add(AccountRuneDeserializer.getAccountRuneFromJson(rune));
        }
        return new AccountMonster(id, com2usId, stars, runes.toArray(new AccountRune[0]));
    }
}
