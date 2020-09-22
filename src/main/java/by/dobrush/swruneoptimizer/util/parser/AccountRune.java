package by.dobrush.swruneoptimizer.util.parser;

import by.dobrush.swruneoptimizer.beans.Rune;
import by.dobrush.swruneoptimizer.beans.RuneSet;
import by.dobrush.swruneoptimizer.beans.Stat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonDeserialize(using = AccountRuneDeserializer.class)
public class AccountRune extends Rune {

    AccountRune(long id, long equippedId, int slotNumber, int stars, int lvl,
                RuneSet runeSet, Stat mainStat, Stat lockedStat, Stat[] toArray) {
        super(id, equippedId, 0, slotNumber, stars, lvl, runeSet, mainStat, lockedStat, toArray);
    }
}
