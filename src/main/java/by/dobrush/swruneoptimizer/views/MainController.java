package by.dobrush.swruneoptimizer.views;

import by.dobrush.swruneoptimizer.FXUtils.*;
import by.dobrush.swruneoptimizer.beans.*;
import by.dobrush.swruneoptimizer.service.OptimizerService;
import by.dobrush.swruneoptimizer.service.ParserService;
import by.dobrush.swruneoptimizer.util.Filter;
import by.dobrush.swruneoptimizer.util.OptimizeEntity;
import by.dobrush.swruneoptimizer.util.parser.AccountEntity;
import by.dobrush.swruneoptimizer.util.parser.SwarfarmMonster;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@NoArgsConstructor
public class MainController implements Initializable {

    @FXML
    TextField depth;

    @FXML
    Label optimizeStatus;

    @FXML
    Button optimizeBtn;

    @FXML
    ImageView monsterIcon;

    @FXML
    TabPane tabPane;

    @FXML
    Tab resultsTab;

    @FXML
    ChoiceBox<RuneSet> mainSet;

    @FXML
    ChoiceBox<RuneSet> additionalSet;

    @FXML
    ComboBox<Monster> monsters;

    @FXML
    ComboBox<Monster> monstersToLock;

    @FXML
    ListView<Monster> lockedMonsters;

    @FXML
    TableView<MonsterStatsTable> currentStats;
    @FXML
    TableColumn<MonsterStatsTable, StatType> stat;
    @FXML
    TableColumn<MonsterStatsTable, Integer> basic;
    @FXML
    TableColumn<MonsterStatsTable, Integer> value;
    @FXML
    TableColumn<MonsterStatsTable, Integer> total;

    @FXML
    TableView<MonsterStatsTable> resultStats;
    @FXML
    TableColumn<MonsterStatsTable, StatType> statR;
    @FXML
    TableColumn<MonsterStatsTable, Integer> basicR;
    @FXML
    TableColumn<MonsterStatsTable, Integer> valueR;
    @FXML
    TableColumn<MonsterStatsTable, Integer> totalR;

    @FXML
    TableView<MonsterStatsTable> buildStats;
    @FXML
    TableColumn<MonsterStatsTable, StatType> statB;
    @FXML
    TableColumn<MonsterStatsTable, Integer> basicB;
    @FXML
    TableColumn<MonsterStatsTable, Integer> valueB;
    @FXML
    TableColumn<MonsterStatsTable, Integer> totalB;

    @FXML
    TableView<ResultsTable> results;
    @FXML
    TableColumn<ResultsTable, RuneSet[]> runeSetsR;
    @FXML
    TableColumn<ResultsTable, Integer> hpR;
    @FXML
    TableColumn<ResultsTable, Integer> atkR;
    @FXML
    TableColumn<ResultsTable, Integer> defR;
    @FXML
    TableColumn<ResultsTable, Integer> spdR;
    @FXML
    TableColumn<ResultsTable, Integer> criRateR;
    @FXML
    TableColumn<ResultsTable, Integer> criDamageR;
    @FXML
    TableColumn<ResultsTable, Integer> resR;
    @FXML
    TableColumn<ResultsTable, Integer> accR;

    @FXML
    TableView<BuildsTable> builds;
    @FXML
    TableColumn<BuildsTable, Monster> monsterB;
    @FXML
    TableColumn<BuildsTable, RuneSet[]> runeSetsB;
    @FXML
    TableColumn<BuildsTable, Integer> hpB;
    @FXML
    TableColumn<BuildsTable, Integer> atkB;
    @FXML
    TableColumn<BuildsTable, Integer> defB;
    @FXML
    TableColumn<BuildsTable, Integer> spdB;
    @FXML
    TableColumn<BuildsTable, Integer> criRateB;
    @FXML
    TableColumn<BuildsTable, Integer> criDamageB;
    @FXML
    TableColumn<BuildsTable, Integer> resB;
    @FXML
    TableColumn<BuildsTable, Integer> accB;

    @FXML
    TableView<RunesTable> resultRunes;
    @FXML
    TableColumn<RunesTable, Rune> firstRuneR;
    @FXML
    TableColumn<RunesTable, Rune> secondRuneR;
    @FXML
    TableColumn<RunesTable, Rune> thirdRuneR;
    @FXML
    TableColumn<RunesTable, Rune> fourthRuneR;
    @FXML
    TableColumn<RunesTable, Rune> fifthRuneR;
    @FXML
    TableColumn<RunesTable, Rune> sixthRuneR;

    @FXML
    TableView<RunesTable> buildRunes;
    @FXML
    TableColumn<RunesTable, Rune> firstRuneB;
    @FXML
    TableColumn<RunesTable, Rune> secondRuneB;
    @FXML
    TableColumn<RunesTable, Rune> thirdRuneB;
    @FXML
    TableColumn<RunesTable, Rune> fourthRuneB;
    @FXML
    TableColumn<RunesTable, Rune> fifthRuneB;
    @FXML
    TableColumn<RunesTable, Rune> sixthRuneB;

    @FXML
    TextField hpFilter;
    @FXML
    TextField atkFilter;
    @FXML
    TextField defFilter;
    @FXML
    TextField spdFilter;
    @FXML
    TextField criRateFilter;
    @FXML
    TextField criDamageFilter;
    @FXML
    TextField resFilter;
    @FXML
    TextField accFilter;

    @FXML
    CheckBox onlyInventory;

    @FXML
    CheckBox useBuildsRunes;

    @FXML
    TextField monsterStars;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lockedMonsters.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        AutoCompleteBox.autoCompleteComboBoxPlus(monsters, (typedText, monster) -> monster.getName().toLowerCase().contains(typedText.toLowerCase()));
        AutoCompleteBox.autoCompleteComboBoxPlus(monstersToLock, (typedText, monster) -> monster.getName().toLowerCase().contains(typedText.toLowerCase()));
        fillRuneSets();
        fillTablesColumns();
        if (AccountEntity.getInstance() != null) {
            fillComponentsWithMonsters();
            fillBuilds();
            if (!AccountEntity.getInstance().getBuilds().isEmpty()) {
                builds.setItems(TableDataForJavaFX.getBuildsTable(AccountEntity.getInstance().getBuilds()));
            }
        }

    }

    private void fillTablesColumns() {
        stat.setCellValueFactory(new PropertyValueFactory<>("statType"));
        basic.setCellValueFactory(new PropertyValueFactory<>("basicValue"));
        value.setCellValueFactory(new PropertyValueFactory<>("value"));
        total.setCellValueFactory(new PropertyValueFactory<>("totalValue"));

        statR.setCellValueFactory(new PropertyValueFactory<>("statType"));
        basicR.setCellValueFactory(new PropertyValueFactory<>("basicValue"));
        valueR.setCellValueFactory(new PropertyValueFactory<>("value"));
        totalR.setCellValueFactory(new PropertyValueFactory<>("totalValue"));

        statB.setCellValueFactory(new PropertyValueFactory<>("statType"));
        basicB.setCellValueFactory(new PropertyValueFactory<>("basicValue"));
        valueB.setCellValueFactory(new PropertyValueFactory<>("value"));
        totalB.setCellValueFactory(new PropertyValueFactory<>("totalValue"));

        runeSetsR.setCellValueFactory(new PropertyValueFactory<>("runeSetsString"));
        hpR.setCellValueFactory(new PropertyValueFactory<>("hp"));
        atkR.setCellValueFactory(new PropertyValueFactory<>("atk"));
        defR.setCellValueFactory(new PropertyValueFactory<>("def"));
        spdR.setCellValueFactory(new PropertyValueFactory<>("spd"));
        criRateR.setCellValueFactory(new PropertyValueFactory<>("criRate"));
        criDamageR.setCellValueFactory(new PropertyValueFactory<>("criDamage"));
        resR.setCellValueFactory(new PropertyValueFactory<>("res"));
        accR.setCellValueFactory(new PropertyValueFactory<>("acc"));

        monsterB.setCellValueFactory(new PropertyValueFactory<>("monster"));
        runeSetsB.setCellValueFactory(new PropertyValueFactory<>("runeSetsString"));
        hpB.setCellValueFactory(new PropertyValueFactory<>("hp"));
        atkB.setCellValueFactory(new PropertyValueFactory<>("atk"));
        defB.setCellValueFactory(new PropertyValueFactory<>("def"));
        spdB.setCellValueFactory(new PropertyValueFactory<>("spd"));
        criRateB.setCellValueFactory(new PropertyValueFactory<>("criRate"));
        criDamageB.setCellValueFactory(new PropertyValueFactory<>("criDamage"));
        resB.setCellValueFactory(new PropertyValueFactory<>("res"));
        accB.setCellValueFactory(new PropertyValueFactory<>("acc"));

        firstRuneR.setCellValueFactory(new PropertyValueFactory<>("rune1"));
        secondRuneR.setCellValueFactory(new PropertyValueFactory<>("rune2"));
        thirdRuneR.setCellValueFactory(new PropertyValueFactory<>("rune3"));
        fourthRuneR.setCellValueFactory(new PropertyValueFactory<>("rune4"));
        fifthRuneR.setCellValueFactory(new PropertyValueFactory<>("rune5"));
        sixthRuneR.setCellValueFactory(new PropertyValueFactory<>("rune6"));

        firstRuneB.setCellValueFactory(new PropertyValueFactory<>("rune1"));
        secondRuneB.setCellValueFactory(new PropertyValueFactory<>("rune2"));
        thirdRuneB.setCellValueFactory(new PropertyValueFactory<>("rune3"));
        fourthRuneB.setCellValueFactory(new PropertyValueFactory<>("rune4"));
        fifthRuneB.setCellValueFactory(new PropertyValueFactory<>("rune5"));
        sixthRuneB.setCellValueFactory(new PropertyValueFactory<>("rune6"));
    }

    private void fillRuneSets() {
        mainSet.setItems(FXCollections.observableArrayList(
                null,
                RuneSet.VIOLENT,
                RuneSet.DESPAIR,
                RuneSet.FATAL,
                RuneSet.SWIFT,
                RuneSet.RAGE,
                RuneSet.VAMPIRE));
        additionalSet.setItems(FXCollections.observableArrayList(
                null,
                RuneSet.BLADE,
                RuneSet.DESTROY,
                RuneSet.ENERGY,
                RuneSet.WILL,
                RuneSet.FOCUS,
                RuneSet.GUARD,
                RuneSet.NEMESIS,
                RuneSet.SHIELD,
                RuneSet.REVENGE,
                RuneSet.ENDURE,
                RuneSet.FIGHT,
                RuneSet.ACCURACY,
                RuneSet.DETERMINATION,
                RuneSet.TOLERANCE,
                RuneSet.ENHANCE
        ));
    }

    @FXML
    private void updateBestiary() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Update bestiary");
        alert.setHeaderText("Please wait...");
        alert.setContentText("Now, monster database is updating.");
        alert.show();
        final Task task = new Task() {
            @Override
            public List call() {
                alert.getDialogPane().lookupButton(ButtonType.OK);
                alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
                ParserService.fullBestiaryToFile("bestiary.json");
                return null;
            }
        };

        task.setOnSucceeded(event -> alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(false));

        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }

    @FXML
    private void openFile() {
        if (!(new File("bestiary.json").exists())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Open file");
            alert.setHeaderText("Need bestiary.");
            alert.setContentText("First need to create a bestiary file! Click the Update bestiary button.");
            alert.showAndWait();
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file with Account");
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            ParserService.ownEntitiesFromFileToFile(file);
            AccountEntity.resetState();
        }
        fillComponentsWithMonsters();
    }

    private void fillComponentsWithMonsters() {
        ObservableList<Monster> monsterList =
                FXCollections.observableArrayList(AccountEntity.getInstance().getMonsters()
                        .stream().sorted(Comparator.comparing(SwarfarmMonster::getName)).collect(Collectors.toList()));
        monsters.setItems(monsterList);
        monstersToLock.setItems(monsterList.filtered(m -> m.getRunes().length != 0));
    }

    @FXML
    private void lockMonster() {
        Monster monsterToLock = AutoCompleteBox.getComboBoxValue(monstersToLock);
        if (monsterToLock != null) {
            if (!lockedMonsters.getItems().contains(monsterToLock)) {
                lockedMonsters.getItems().add(monsterToLock);
                lockedMonsters.setItems(FXCollections.observableArrayList(lockedMonsters.getItems().sorted()));
            }
        }
    }

    @FXML
    private void deleteLockedMonster() {
        if (lockedMonsters.getSelectionModel().getSelectedItems() != null) {
            lockedMonsters.getItems().removeAll(lockedMonsters.getSelectionModel().getSelectedItems());
        }
    }

    @FXML
    private void fillMonsterInfo() {
        Monster monster = AutoCompleteBox.getComboBoxValue(monsters);
        if (monster != null) {
            ObservableList<MonsterStatsTable> stats = TableDataForJavaFX.getMonsterStats(monster);
            currentStats.setItems(stats);

            final Task task = new Task() {
                @Override
                public List call() {
                    monsterIcon.setImage(new Image(monster.getImage()));
                    return null;
                }
            };

            Thread t = new Thread(task);
            t.setDaemon(true);
            t.start();
        }
    }

    @FXML
    private void filterMonsterList() {
        try {
            int stars = Integer.parseInt(monsterStars.getText());
            monsters.setItems(FXCollections.observableArrayList(AccountEntity.getInstance().getMonsters()
                    .stream().filter(m -> m.getStars() >= stars)
                    .sorted(Comparator.comparing(SwarfarmMonster::getName)).collect(Collectors.toList())));
            AutoCompleteBox.autoCompleteComboBoxPlus(monsters, (typedText, monster) -> monster.getName().toLowerCase().contains(typedText.toLowerCase()));
        }
        catch (NumberFormatException ignored) {

        }
    }

    @FXML
    private void optimize() {
        Monster monster = AutoCompleteBox.getComboBoxValue(monsters);
        if (monster == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Optimize");
            alert.setHeaderText("Monster isn't selected!");
            alert.setContentText("Please select monster.");
            alert.showAndWait();
            return;
        }
        if (depth.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Optimize");
            alert.setHeaderText("Depth isn't selected!");
            alert.setContentText("Please select depth.");
            alert.showAndWait();
            return;
        }
        optimizeBtn.setDisable(true);
        Filter filter = createFilter();
        final Task<List<Combination>> task = new Task<List<Combination>>() {
            @Override
            public List<Combination> call() {
                return new OptimizerService().optimize(new OptimizeEntity(filter, monster));
            }
        };

        task.setOnSucceeded(event -> {
            List<Combination> combinations = task.getValue();
            results.setItems(TableDataForJavaFX.getResultsTable(combinations));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Optimize");
            alert.setHeaderText("Done!");
            alert.setContentText(combinations.size() + " combinations founded.");
            alert.showAndWait();
            if (!combinations.isEmpty()) {
                SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
                selectionModel.select(resultsTab);
            }
            optimizeBtn.setDisable(false);
        });

        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }

    private Filter createFilter() {
        Filter filter = new Filter();
        filter.setDepth(Integer.parseInt(depth.getText()));
        if (mainSet.getValue() != null) {
            filter.getRuneSets().add(mainSet.getValue());
        }
        if (additionalSet.getValue() != null) {
            filter.getRuneSets().add(additionalSet.getValue());
        }
        if (lockedMonsters.getItems() != null) {
            filter.getLockedMonsters().addAll(lockedMonsters.getItems());
        }
        if (onlyInventory.isSelected()) {
            filter.setUseOnlyInventoryRunes(true);
        }
        if (useBuildsRunes.isSelected()) {
            filter.setUseBuildsRunes(true);
        }
        List<Stat> minStats = new ArrayList<>();
        if (hpFilter.getText() != null && !hpFilter.getText().isEmpty()) {
            minStats.add(new Stat(StatType.HP, Integer.parseInt(hpFilter.getText())));
        }
        if (atkFilter.getText() != null && !atkFilter.getText().isEmpty()) {
            minStats.add(new Stat(StatType.ATK, Integer.parseInt(atkFilter.getText())));
        }
        if (defFilter.getText() != null && !defFilter.getText().isEmpty()) {
            minStats.add(new Stat(StatType.DEF, Integer.parseInt(defFilter.getText())));
        }
        if (spdFilter.getText() != null && !spdFilter.getText().isEmpty()) {
            minStats.add(new Stat(StatType.SPD, Integer.parseInt(spdFilter.getText())));
        }
        if (criRateFilter.getText() != null && !criRateFilter.getText().isEmpty()) {
            minStats.add(new Stat(StatType.CR, Integer.parseInt(criRateFilter.getText())));
        }
        if (criDamageFilter.getText() != null && !criDamageFilter.getText().isEmpty()) {
            minStats.add(new Stat(StatType.CD, Integer.parseInt(criDamageFilter.getText())));
        }
        if (resFilter.getText() != null && !resFilter.getText().isEmpty()) {
            minStats.add(new Stat(StatType.RES, Integer.parseInt(resFilter.getText())));
        }
        if (accFilter.getText() != null && !accFilter.getText().isEmpty()) {
            minStats.add(new Stat(StatType.ACC, Integer.parseInt(accFilter.getText())));
        }
        filter.getMinStats().addAll(minStats);
        return filter;
    }

    @FXML
    private void getResultInfo() {
        ResultsTable resultsTable = results.getSelectionModel().getSelectedItem();
        if (resultsTable != null) {
            resultRunes.setItems(TableDataForJavaFX.getCombinationRunes(resultsTable));
            resultStats.setItems(TableDataForJavaFX.getMonsterStats(resultsTable.getCombination()));
        }
    }

    @FXML
    private void getBuildInfo() {
        BuildsTable buildsTable = builds.getSelectionModel().getSelectedItem();
        if (buildsTable != null) {
            buildRunes.setItems(TableDataForJavaFX.getCombinationRunes(buildsTable));
            buildStats.setItems(TableDataForJavaFX.getMonsterStats(buildsTable.getCombination()));
        }
    }

    @FXML
    private void saveResult() {
        ResultsTable resultsTable = results.getSelectionModel().getSelectedItem();
        if (resultsTable != null) {
            builds.getItems().add(TableDataForJavaFX.getOneBuildsTable(resultsTable));
            AccountEntity accountEntity = AccountEntity.getInstance();
            accountEntity.getBuilds().add(resultsTable.getCombination());
        }
    }

    @FXML
    private void removeBuild() {
        BuildsTable buildsTable = builds.getSelectionModel().getSelectedItem();
        if (buildsTable != null) {
            builds.getItems().remove(builds.getSelectionModel().getSelectedIndex());
            AccountEntity.getInstance().getBuilds().remove(buildsTable.getCombination());
        }
    }

    private void fillBuilds() {
        builds.setItems(TableDataForJavaFX.getBuildsTable(AccountEntity.getInstance().getBuilds()));
    }
}
