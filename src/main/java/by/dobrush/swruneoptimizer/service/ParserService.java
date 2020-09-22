package by.dobrush.swruneoptimizer.service;

import by.dobrush.swruneoptimizer.beans.Combination;
import by.dobrush.swruneoptimizer.beans.Monster;
import by.dobrush.swruneoptimizer.beans.Rune;
import by.dobrush.swruneoptimizer.beans.Skill;
import by.dobrush.swruneoptimizer.util.parser.AccountEntity;
import by.dobrush.swruneoptimizer.util.parser.AccountMonster;
import by.dobrush.swruneoptimizer.util.parser.AccountRune;
import by.dobrush.swruneoptimizer.util.parser.SwarfarmMonster;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;

import java.io.File;
import java.util.*;

public class ParserService {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static void fullBestiaryToFile(String bestiaryFile) {
        Map<Long, SwarfarmMonster> swarfarmMonster = getMonstersFromSwarfarm();
        FileService.writeToFile(bestiaryFile, swarfarmMonster);
    }

    @SneakyThrows
    public static void getOwnEntities(AccountEntity accountEntity) {
        String content = FileService.readFile("account.json");
        accountEntity.setMonsters(MAPPER.readValue(MAPPER.readTree(content).get("monsters").toString(), new TypeReference<List<Monster>>() {}));
        accountEntity.setRunes(MAPPER.readValue(MAPPER.readTree(content).get("runes").toString(), new TypeReference<List<Rune>>() {}));
    }

    @SneakyThrows
    public static void restoreBuilds(AccountEntity accountEntity) {
        String content = FileService.readFile("builds.json");
        accountEntity.setBuilds(MAPPER.readValue(MAPPER.readTree(content).get("builds").toString(), new TypeReference<List<Combination>>() {}));
    }

    public static void saveBuilds() {
        Map<String, Object> rootElement = new HashMap<>();
        rootElement.put("builds", AccountEntity.getInstance().getBuilds());
        FileService.writeToFile(new File("builds.json").getPath(), rootElement);
    }

    public static void ownEntitiesFromFileToFile(File file) {
        String content = FileService.readFile(file.getPath());
        List<AccountMonster> accountMonsters = getOwnMonsters(content);
        Map<Long, AccountRune> accountRunes = getOwnRunes(content);
        Map<Long, Long> rtaRunesInfo = getOwnRtaRunesInfo(content);
        Map<Long, Monster> upgradedMonsters = getUpgradedMonsters("bestiary.json", accountMonsters);
        upgradedMonsters.forEach((key, value) -> {
            for (AccountRune accountRune : (AccountRune[]) value.getRunes()) {
                accountRunes.put(accountRune.getId(), accountRune);
            }
        });
        fillMonstersWithRtaRunes(upgradedMonsters, accountRunes, rtaRunesInfo);
        Map<String, Object> rootElement = new HashMap<>();
        rootElement.put("monsters", upgradedMonsters.values());
        rootElement.put("runes", accountRunes.values());
        FileService.writeToFile(new File("account.json").getPath(), rootElement);
    }

    @SneakyThrows
    private static Map<Long, SwarfarmMonster> getMonstersFromSwarfarm() {
        Content response;
        Map<Long, SwarfarmMonster> monsters = new HashMap<>();
        Map<Long, Skill> skills = getSkillsFromSwarfarm();
        String monstersPageUrl = "https://swarfarm.com/api/v2/monsters/?format=json&natural_stars__gte=2";
        JsonNode monsterPage;
        do {
            response = Request.Get(monstersPageUrl).execute().returnContent();
            monsterPage = MAPPER.readTree(Objects.requireNonNull(response.asString()));
            JsonNode jsonMonsters = monsterPage.get("results");
            jsonMonsters.forEach(m -> {
                SwarfarmMonster monster = null;
                try {
                    monster = MAPPER.readValue(m.toString(), SwarfarmMonster.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                List<Skill> monsterSkills = new ArrayList<>();
                try {
                    for (long skillId : MAPPER.readValue(m.get("skills").toString(), long[].class)) {
                        monsterSkills.add(skills.get(skillId));
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                assert monster != null;
                monster.setSkills(monsterSkills.toArray(new Skill[0]));
                monsters.put(m.get("com2us_id").asLong(), monster);
            });
        } while (!(monstersPageUrl = monsterPage.get("next").asText()).equals("null"));
        return monsters;
    }

    private static void fillMonstersWithRtaRunes(Map<Long, Monster> monsters, Map<Long, AccountRune> accountRunes, Map<Long, Long> rtaRunesInfo) {
        rtaRunesInfo.forEach((key, value) -> {
            Monster monster = monsters.get(value);
            Rune[] rtaRunes = monster.getRtaRunes();
            List<Rune> runes = new ArrayList<>();
            if (rtaRunes != null) {
                runes.addAll(Arrays.asList(monster.getRtaRunes()));
            }
            runes.add(accountRunes.get(key));
            monster.setRtaRunes(runes.toArray(new Rune[0]));
        });
    }

    @SneakyThrows
    private static Map<Long, Skill> getSkillsFromSwarfarm() {
        Content response;
        Map<Long, Skill> skills = new HashMap<>();
        String skillsPageUrl = "https://swarfarm.com/api/v2/skills/?format=json";
        JsonNode skillPage;
        do {
            response = Request.Get(skillsPageUrl).execute().returnContent();
            skillPage = MAPPER.readTree(Objects.requireNonNull(response.asString()));
            JsonNode jsonSkills = skillPage.get("results");
            jsonSkills.forEach(s -> {
                try {
                    skills.put(s.get("id").asLong(), MAPPER.readValue(s.toString(), Skill.class));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
        } while (!(skillsPageUrl = skillPage.get("next").asText()).equals("null"));
        return skills;
    }

    @SneakyThrows
    private static List<AccountMonster> getOwnMonsters(String content) {
        return MAPPER.readValue(MAPPER.readTree(content).get("unit_list").toString(),
                new TypeReference<List<AccountMonster>>() {
                });
    }

    @SneakyThrows
    private static Map<Long, AccountRune> getOwnRunes(String content) {
        List<AccountRune> runes = MAPPER.readValue(MAPPER.readTree(content).get("runes").toString(),
                new TypeReference<List<AccountRune>>() {
                });
        Map<Long, AccountRune> result = new HashMap<>();
        runes.forEach(r -> result.put(r.getId(), r));
        return result;
    }

    @SneakyThrows
    private static Map<Long, Long> getOwnRtaRunesInfo(String content) {
        Map<Long, Long> result = new HashMap<>();
        for (JsonNode jsonNode: MAPPER.readTree(content).get("world_arena_rune_equip_list")) {
            result.put(jsonNode.get("rune_id").asLong(), jsonNode.get("occupied_id").asLong());
        }
        return result;
    }

    @SneakyThrows
    private static Map<Long, Monster> getUpgradedMonsters(String filePath, List<AccountMonster> accountMonsters) {
        Map<Long, Monster> result = new HashMap<>();
        String content = FileService.readFile(filePath);
        Map<Long, Monster> monsters = new ObjectMapper().readValue(content, new TypeReference<Map<Long, Monster>>() {
        });
        for (AccountMonster monster : accountMonsters) {
            Monster monsterInfo;
            long com2usId = monster.getCom2usId();
            if (monsters.get(com2usId) != null) {
                if (monsters.get(com2usId).canTwiceAwaken()) {
                    monsterInfo = monsters.get(com2usId + 20);
                } else if (monsters.get(monster.getCom2usId()).isCanAwaken()) {
                    monsterInfo = monsters.get(com2usId + 10);
                } else {
                    monsterInfo = monsters.get(com2usId);
                }
                monsterInfo = monsterInfo.clone();
                monsterInfo.setId(monster.getId());
                monsterInfo.setStars(monster.getStars());
                monsterInfo.setRunes(monster.getRunes());
                result.put(monsterInfo.getId(), monsterInfo);
            }
        }
        return result;
    }
}
