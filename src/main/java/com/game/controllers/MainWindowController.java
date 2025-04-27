package com.game.controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import com.game.Actions;
import com.game.Gamelogs;
import com.game.Player;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class MainWindowController {
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private TextField moveField;
    @FXML
    private MenuItem firstMenuItem, secMenuItem, thirdMenuItem, fourthMenuItem;
    @FXML
    private SplitMenuButton clearGameLogsBtn, submitMenuBtn;
    @FXML
    private TextArea gameLogsTA, playerLogsTA, playerStatsTA, mobStatsTA;
    @FXML
    private ImageView mobAvatarGame, playerAvatar, playerAvatarGame, mobAvatar, lomAvatarGame, lomImageViewInventory,
            hpPotionViewInventory;
    @FXML
    private Label playerAvatarLbl, mobAvatarLbl, lomCostLbl, hpPotionCostLbl, lomQuantityLbl, hpPotionQuantityLbl;
    @FXML
    private AnchorPane mainPaneMobFight, actionsPane, mainPaneShop, mainPaneBlank, actionsPaneBlank, mainPaneQuest1, mainPaneQuest2;




    private static MainWindowController instance;
    private static Player player1; //= new Player(1,"Makar",100,10,20, 1, 0);
    private final StringProperty playerStats = new SimpleStringProperty();
    private final StringProperty mobStats = new SimpleStringProperty();
    private final StringProperty mobName = new SimpleStringProperty();
    private static Gamelogs gameLogs, playerLogs;
    private static final Image mobImage = new Image(Objects.requireNonNull(MainWindowController.class.getResource("/com/game/monster.png")).toString());
    private static final Image mobCatImage = new Image(Objects.requireNonNull(MainWindowController.class.getResource("/com/game/monsterCat.png")).toString());
    private static final Image playerImage = new Image(Objects.requireNonNull(MainWindowController.class.getResource("/com/game/player.png")).toString());
    private static final Image shopImage = new Image(Objects.requireNonNull(MainWindowController.class.getResource("/com/game/shop.png")).toString());
    private static final Image LomImage = new Image(Objects.requireNonNull(MainWindowController.class.getResource("/com/game/lom.png")).toString());
    private static final Image quest1CharImage = new Image(Objects.requireNonNull(MainWindowController.class.getResource("/com/game/beck.png")).toString());
    private static final Image quest2CharImage = new Image(Objects.requireNonNull(MainWindowController.class.getResource("/com/game/villager.png")).toString());
    private String prevGameLogsText = "", prevPlayerLogsText = "";
    private final List<String> items = new ArrayList<>();
    private int lomCart, hpPotionCart;

    private boolean cat, isWelcome;
    private static String quest = "NULL";

    public MainWindowController() {
        instance = this;
    }

    @FXML
    public void initialize() throws SQLException {

        gameLogs = new Gamelogs(gameLogsTA);
        playerLogs = new Gamelogs(playerLogsTA);
        playerAvatar.setImage(playerImage);

        playerStatsTA.textProperty().bind(playerStats);
        mobStatsTA.textProperty().bind(mobStats);
        mobAvatarGame.imageProperty().bind(mobAvatar.imageProperty());
        playerAvatarGame.imageProperty().bind(playerAvatar.imageProperty());

        mobStatsTA.visibleProperty().bind(mobAvatar.visibleProperty());


        Actions actions = new Actions(this);

        switch (Actions.loadGameParameters()){
            case "mainPaneMobFight": {
                Actions.mobFightStart();
                Actions.loadGameParameters();
                break;
            }
            case "mainPaneShop":{
                Actions.shopStart();
                Actions.loadGameParameters();
                break;
            }
            case "mainPaneQuest1":{
                switch (player1.getIsNew()){
                    case "true": {
                        Actions.quest1Start();
                        Actions.loadGameParameters();
                        break;
                    }
                    case "false": {
                        Actions.rndEvent();
                        break;
                    }
                }
                break;
            }
            case "mainPaneQuest2":{
                Actions.quest2Start();
                Actions.loadGameParameters();
                break;
            }
            default:{
                Actions.quest1Start();
                Actions.loadGameParameters();
                break;
            }
        }

        Actions.updateStats("player",player1.getName(), player1.getHp(), player1.getDmg(), player1.getMoney());


    }

    @FXML
    public void submitMenuAction(ActionEvent actionEvent) throws SQLException {
        String move = getMoveFieldText();
        if (mainPaneMobFight.isVisible()) {
            handleMobFight(move);
        }
        else if (mainPaneShop.isVisible()) {
            handleShop(move);
        }
        else if (mainPaneQuest1.isVisible()) {
            handleQuest1(move);

        } else if (mainPaneQuest2.isVisible() & !cat) {
            handleQuest2(move);
        }
        else if (cat){
            handleCat(move);
        }
        else {
            showInvalidMoveError();
        }
    }
    private void handleMobFight(String move) throws SQLException {
        switch (move) {
            case "Отступить":
                Actions.setMenuItemsVisibility(false);
                playerAvatarGame.setVisible(false);
                mobAvatar.setVisible(false);
                playerLogs.appendLogs("Вы испугались %s и отступили.\n", Actions.getMob().getName());
                Actions.getMob().mobAttack();
                break;
            case "Атака ломом":
                Actions.getMob().mobTakeDmg("Ломом", (getPlayer().getDmg()), Actions.getMob().getHp());
                break;
            case "Обычная атака":
                Actions.getMob().mobTakeDmg("Обычная", (getPlayer().getDmg()), Actions.getMob().getHp());
                break;
            default:
                showInvalidMoveError(); break;
        }
    }
    private void handleShop(String move) {
        switch (move) {
            case "Уйти":
                Actions.setMenuItemsVisibility(false);
                playerLogs.appendLogs("Вы ушли.\n");
                setCarts(0);
                Actions.rndEvent();
                break;
            case "Купить предмет":
                Actions.shopBuy(items);
                break;
            default:
                showInvalidMoveError(); break;
        }
    }
    private void handleQuest1(String move) {
        switch (move){
            case "Привет":{
                isWelcome = true;
                playerLogs.appendLogs("%s: %s\n", player1.getName(), move);
                gameLogs.appendLogs("?: Привет, расскажи что тебя привело сюда\n");
                firstMenuItem.setVisible(false);
                secMenuItem.setVisible(true);
                moveField.clear();
                break;
            }
            case "Кто ты?": {
                if (isWelcome){
                    isWelcome = false;
                    gameLogs.appendLogs("Бек: Я ПОПРОСИЛ ТЕБЯ РАССКАЗАТЬ МНЕ ИСТОРИЮ!!!!!!!\n");
                    playerLogs.appendLogs("Бек нанес вам 100 урона\n");
                    player1.setHp(getPlayer().getHp() - 100);
                    Actions.updateStats("player", player1.getName(), player1.getHp(), player1.getDmg(), player1.getMoney());
                }
                else {

                    playerLogs.appendLogs("%s: %s?\n", player1.getName(), move);
                    gameLogs.appendLogs("Бек: Я Бек, странствующий педофил.\n");
                    secMenuItem.setVisible(false);
                    thirdMenuItem.setVisible(true);
                    moveField.clear();
                }


                break;
            }
            case "Что тебе надо?": {
                playerLogs.appendLogs("%s: %s?\n", player1.getName(), move);
                gameLogs.appendLogs("Бек: Я хочу тебя трахнуть.....\n");
                thirdMenuItem.setVisible(false);
                moveField.clear();
                break;
            }
            case "Уйти": {
                Actions.setMenuItemsVisibility(false);
                playerLogs.appendLogs("Вы ушли\n");
                player1.setIsNew("false");
                Actions.rndEvent();
                break;
            }
            default: {
                if(!isWelcome){
                    showInvalidMoveError();
                    break;
                }
                else
                {
                    playerLogs.appendLogs("%s: %s", player1.getName(), moveField.getText() + "\n");
                    gameLogs.appendLogs("Бек: Ого, это очень интересно!\n");
                    isWelcome = false;
                    moveField.clear();
                    break;
                }
            }
        }
    }
    public void handleQuest2(String move){
        switch (move){
            case "Привет, что это за деревня?":{
                moveField.clear();
                playerLogs.appendLogs("%s: %s\n",player1.getName(), move);
                gameLogs.appendLogs("Житель: Это деревня пшеницы! Здесь она растёт лучше всего! Кстати, слушай, у меня к тебе просьба, не мог бы ты помочь мне?\n");
                firstMenuItem.setVisible(false);
                secMenuItem.setVisible(true);
                break;
            }
            case "Чем?":{
                moveField.clear();
                playerLogs.appendLogs("%s: %s\n", player1.getName(), move);
                gameLogs.appendLogs("Житель: Мою любимую кошечку украл монстр(, помоги мне убить его, пожалуйста(\n");
                secMenuItem.setVisible(false);
                thirdMenuItem.setVisible(true);
                break;
            }
            case "Я согласен":{
                moveField.clear();
                playerLogs.appendLogs("%s: %s", player1.getName(), move);
                gameLogs.appendLogs("Житель: Спасибо огромное! ты сможешь найти его дальше по тропинке!\n");
                thirdMenuItem.setVisible(false);
                fourthMenuItem.setVisible(true);
                break;
            }
            case "Идти по тропинке":{
                moveField.clear();
                Actions.setMenuItemsVisibility(false);
                playerLogs.appendLogs("Вы пошли по тропинке.\n");
                Actions.mobFightStart();
                thirdMenuItem.setVisible(false);
                break;
            }
            case "Кошка":{
                moveField.clear();
                Actions.updateStats("Char", "Житель", 0, 0,0);
                mobAvatar.setImage(MainWindowController.getQuest2CharImage());
                mobAvatar.setVisible(true);
                mobAvatarGame.setVisible(true);
                playerAvatarGame.setVisible(true);

                Actions.setMenuItemText("Привет, да, держи!", "0", "0", "Уйти");

                Actions.setMenuItemsVisibility(false);
                firstMenuItem.setVisible(true);
                gameLogs.appendLogs("Вы вернулись в деревню.\nЖитель: И снова привет, ты нашел мою кошечку?!\n");
                AnchorPane currentMainPane = Actions.getVisiblePane(Actions.getAllMainPanes());
                Actions.changePane(currentMainPane, getMainPaneQuest2());
                break;
            }
            case "Уйти":{
                moveField.clear();
                Actions.setMenuItemsVisibility(false);
                playerLogs.appendLogs("Вы ушли.\n");
                setQuest("NULL");
                break;
            }
            default: showInvalidMoveError(); break;
        }
    }
    public void handleCat(String move){
        switch (move){
            case"Привет, да, держи!":{
                moveField.clear();
                playerLogs.appendLogs("%s: %s\n", player1.getName(), move);
                gameLogs.appendLogs("Житель: Это чудесно, спасибо тебе! Вот небольшое вознаграждение!\n");
                player1.setMoney(player1.getMoney() + 15);
                playerLogs.appendLogs("Получено 15 монет\n");
                firstMenuItem.setVisible(false);
                fourthMenuItem.setVisible(true);
                break;
            }
            case "Уйти":{
                moveField.clear();
                Actions.setMenuItemsVisibility(false);
                playerLogs.appendLogs("Вы ушли.\n");
                setQuest("NULL");
                setCat(false);
                Actions.rndEvent();
                break;
            }
        }
    }

    private void showInvalidMoveError() {
        RegLogController.showAlert(Alert.AlertType.ERROR, """
        Введено неверное действие
        Подсказка: Вы можете нажать на стрелочку справа от кнопки "Подтвердить ход" и узнать все доступные на
        данный момент действия
        """);
    }


    @FXML
    public void firstMenuItemAction(ActionEvent actionEvent) {
        Actions.setMove(getMoveField(), getFirstMenuItem());
    }
    @FXML
    public void secMenuItemAction(ActionEvent actionEvent) {
        Actions.setMove(getMoveField(), getSecMenuItemMenu());
    }
    @FXML
    public void thirdMenuItemAction(ActionEvent actionEvent) {
        Actions.setMove(getMoveField(), getThirdMenuItem());
    }
    public void fourthMenuItemAction(ActionEvent actionEvent) {
        Actions.setMove(getMoveField(), getFourthMenuItem());
    }

    @FXML
    public void lomShopClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            lomCart += 1;
            playerLogs.appendLogs("Лом добавлен в корзину\n");
            gameLogs.appendLogs("""
                    Корзина: Лом %d
                    """, lomCart);
            items.add("lom");

        }
        else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            if (!items.isEmpty() & lomCart > 0){
                lomCart -= 1;
                items.removeLast();
                playerLogs.appendLogs("Последний предмет удалён из корзины\n");
                gameLogs.appendLogs("""
                    Корзина: Лом %d
                    """, lomCart);
            }
            else {
                lomCart = 0;
                playerLogs.appendLogs("Корзина пуста\n");
            }

        }
    }
    @FXML
    public void hpPotionShopClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            hpPotionCart += 1;
            playerLogs.appendLogs("Зелье здоровья добавлено в корзину\n");
            gameLogs.appendLogs("""
                    Корзина: Зелье здоровья %d
                    """, hpPotionCart);
            items.add("hpPotion");

        }
        else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            if (!items.isEmpty() & hpPotionCart > 0){
                hpPotionCart -= 1;
                items.removeLast();
                playerLogs.appendLogs("Последний предмет удалён из корзины\n");
                gameLogs.appendLogs("""
                    Корзина: Зелье здоровья %d
                    """, hpPotionCart);
            }
            else {
                hpPotionCart = 0;
                playerLogs.appendLogs("Корзина пуста\n");
            }

        }
    }
    @FXML
    public void hpPotionInventoryClicked(MouseEvent mouseEvent) {
        if(player1.getHp() < 100){
            if (player1.getInventoryValue("hpPotion") > 0){
                player1.setInventory("hpPotion", -1);
                player1.setHp(100);
                playerLogs.appendLogs("Вы выпили зелье здоровья и восстановили здоровье.\n");
                Actions.updateStats("player",player1.getName(), player1.getHp(), player1.getDmg(), player1.getMoney());
                player1.invUpdate();
            }
            else
            {
                playerLogs.appendLogs("У вас нет зелья здоровья.\n");
            }
        }
        else {
            playerLogs.appendLogs("У вас уже максимум здоровья.\n");
        }
    }

    @FXML
    void playerAvatarEntered(MouseEvent event) {
        playerAvatarLbl.setVisible(true);
    }
    @FXML
    void mobAvatarEntered(MouseEvent event) {
        if(mainPaneMobFight.isVisible()){
            mobAvatarLbl.setText(String.format("Это монстр %s", Actions.getMob().getName()));
            mobAvatarLbl.setVisible(true);

        } else if (mainPaneQuest1.isVisible()) {
            mobAvatarLbl.setText("Это Бек");
            mobAvatarLbl.setVisible(true);
        }
    }
    @FXML
    void playerAvatarExited(MouseEvent event) {
        playerAvatarLbl.setVisible(false);
    }
    @FXML
    void mobAvatarExited(MouseEvent event) {
        mobAvatarLbl.setVisible(false);
    }

    @FXML
    public void clearGameLogsBtnAction(ActionEvent actionEvent) {
        try {
            if (gameLogs != null) {
                String prevGameLogsTextCheck = gameLogs.getGameLogsText();
                if (!prevGameLogsTextCheck.isEmpty()) {
                    prevGameLogsText = gameLogs.getGameLogsText();
                    gameLogs.clearLogs();
                }
            }
            if (playerLogs != null) {
                String prevPlayerLogsTextCheck = playerLogs.getGameLogsText();
                if (!prevPlayerLogsTextCheck.isEmpty()) {
                    prevPlayerLogsText = playerLogs.getGameLogsText();
                    playerLogs.clearLogs();
                }
            }
        } catch (Exception e) {/*pass*/}

    }
    @FXML
    public void undoClearGameLogsAction(ActionEvent actionEvent) {
        if (gameLogs.getGameLogsText().length() > prevGameLogsText.length())  {
            //pass
        }
        else if (playerLogs.getGameLogsText().length() > prevPlayerLogsText.length()) {
            //pass
        }
        else {
            gameLogs.setLogs(prevGameLogsText);
            playerLogs.setLogs(prevPlayerLogsText);
        }
    }

    @FXML
    public void invTabSelected(Event event) {
        player1.invUpdate();
    }

    @FXML
    public void exitBtnAction(ActionEvent actionEvent) throws SQLException {
        player1.save();
        Actions.saveGameParameters();
        RegLogController.showAlert(Alert.AlertType.INFORMATION,"Все данные были сохранены.", "Выход", "Выход из игры.");
        javafx.application.Platform.exit();
    }

    

    //геттеры
    public TextArea getMobStatsTA() {
        return mobStatsTA;
    }
    public TextField getMoveField() {
        return moveField;
    }
    public String getMoveFieldText() {
        return moveField.getText();
    }
    public MenuItem getFirstMenuItem() {
        return firstMenuItem;
    }
    public MenuItem getSecMenuItemMenu() {
        return secMenuItem;
    }
    public MenuItem getThirdMenuItem() {
        return thirdMenuItem;
    }
    public MenuItem getFourthMenuItem() {
        return fourthMenuItem;
    }
    public TextArea getGameLogsTA() {
        return gameLogsTA;
    }
    public AnchorPane getActionsPane() {
        return actionsPane;
    }
    public AnchorPane getActionsPaneBlank() {
        return actionsPaneBlank;
    }
    public AnchorPane getMainPaneBlank() {
        return mainPaneBlank;
    }
    public AnchorPane getMainPaneMobFight() {
        return mainPaneMobFight;
    }
    public AnchorPane getMainPaneQuest1() {
        return mainPaneQuest1;
    }
    public AnchorPane getMainPaneQuest2() {
        return mainPaneQuest2;
    }
    public AnchorPane getMainPaneShop() {
        return mainPaneShop;
    }
    public ImageView getMobAvatar() {
        return mobAvatar;
    }
    public ImageView getMobAvatarGame() {
        return mobAvatarGame;
    }
    public ImageView getPlayerAvatar() {
        return playerAvatar;
    }
    public ImageView getPlayerAvatarGame() {
        return playerAvatarGame;
    }
    public TextArea getPlayerLogsTA() {
        return playerLogsTA;
    }
    public static Player getPlayer() {
        return player1;
    }
    public static Image getShopImage() {
        return shopImage;
    }
    public static Image getQuest1CharImage() {
        return quest1CharImage;
    }
    public static Image getQuest2CharImage() {
        return quest2CharImage;
    }
    public static Image getMobImage() {
        return mobImage;
    }
    public static Image getMobCatImage() {
        return mobCatImage;
    }
    public static Image getPlayerImage() {
        return playerImage;
    }
    public static Image getLomImage() {
        return LomImage;
    }
    public ImageView getLomAvatarGame() {
        return lomAvatarGame;
    }
    public SimpleStringProperty getMobStats() {
        return (SimpleStringProperty) mobStats;
    }
    public static Gamelogs getGameLogs() {
        return gameLogs;
    }
    public static Gamelogs getPlayerLogs() {
        return playerLogs;
    }
    public Label getLomCostLbl() {
        return lomCostLbl;
    }
    public String getPlayerStats() {
        return playerStats.get();
    }
    public StringProperty getPlayerStatsProperty() {
        return playerStats;
    }
    public Label getHpPotionCostLbl() {
        return hpPotionCostLbl;
    }
    public Label getHpPotionQuantityLbl() {
        return hpPotionQuantityLbl;
    }
    public Label getLomQuantityLbl() {
        return lomQuantityLbl;
    }
    public static String getQuest() {
        return quest;
    }
    public static MainWindowController getInstance() {
        return instance;
    }
    public boolean IsCat(){
        return cat;
    }

    //сеттеры
    public void setCarts(int value) {
        lomCart = value;
        hpPotionCart = value;
    }
    public void setPlayerLogsTA(TextArea playerLogsTA) {
        this.playerLogsTA = playerLogsTA;
    }
    public static void setPlayer(Player player) {
        player1 = player;
    }
    public static void setQuest(String  quest) {
        MainWindowController.quest = quest;
    }
    public void setCat(boolean cat) {
        this.cat = cat;
    }
}

