package by.dobrush.swruneoptimizer.util.parser;

import by.dobrush.swruneoptimizer.beans.Combination;
import by.dobrush.swruneoptimizer.beans.Monster;
import by.dobrush.swruneoptimizer.beans.Rune;
import by.dobrush.swruneoptimizer.service.ParserService;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class AccountEntity {

    private List<Rune> runes;
    private List<Monster> monsters;
    private List<Long> idMonsters;
    private List<Combination> builds;

    private static AccountEntity accountEntity;

    private AccountEntity() {
        this.runes = new ArrayList<>();
        this.monsters = new ArrayList<>();
        this.builds = new ArrayList<>();
    }

    public static AccountEntity getInstance() {
        if (accountEntity == null) {
            if (new File("account.json").exists()) {
                accountEntity = new AccountEntity();
                ParserService.getOwnEntities(accountEntity);
                accountEntity.idMonsters = new ArrayList<>();
                Long[] ids = new Long[accountEntity.monsters.size()];
                accountEntity.monsters.parallelStream().forEach(m -> ids[accountEntity.monsters.indexOf(m)] = m.getId());
                accountEntity.idMonsters.addAll(Arrays.asList(ids));
                if (new File("builds.json").exists()) {
                    ParserService.restoreBuilds(accountEntity);
                }
            }
        }
        return accountEntity;
    }

    public static void resetState() {
        accountEntity = null;
    }

    public Monster getMonsterById(long id) {
        return monsters.get(idMonsters.indexOf(id));
    }

}
