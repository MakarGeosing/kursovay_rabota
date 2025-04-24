package com.game.controllers;

import java.net.URL;
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
    private MenuItem firstMenuItem, secMenuItem, thirdMenuItem, undoClearGameLogsMenuItem;
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
    private AnchorPane mainPaneMobFight, actionsPane, mainPaneShop, mainPaneBlank, actionsPaneBlank, mainPaneQuest1;

    private static final Player player1 = new Player(1,"Makar",100,10,20);
    private final StringProperty playerStats = new SimpleStringProperty();
    private final StringProperty mobStats = new SimpleStringProperty();
    private final StringProperty mobName = new SimpleStringProperty();
    private static Gamelogs gameLogs, playerLogs;
    private final Image mobImage = new Image(Objects.requireNonNull(getClass().getResource("/com/game/monster.png")).toString());
    private final Image playerImage = new Image(Objects.requireNonNull(getClass().getResource("/com/game/player.png")).toString());
    private final Image shopImage = new Image(Objects.requireNonNull(getClass().getResource("/com/game/shop.png")).toString());
    private final Image LomImage = new Image(Objects.requireNonNull(getClass().getResource("/com/game/lom.png")).toString());
    private String prevGameLogsText = "", prevPlayerLogsText = "";
    private final List<String> items = new ArrayList<String>();
    private int lomCart, hpPotionCart;

    @FXML
    public void initialize() {
        gameLogs = new Gamelogs(gameLogsTA);
        playerLogs = new Gamelogs(playerLogsTA);
        playerAvatar.setImage(playerImage);

        playerStatsTA.textProperty().bind(playerStats);
        mobStatsTA.textProperty().bind(mobStats);
        mobAvatarGame.imageProperty().bind(mobAvatar.imageProperty());
        playerAvatarGame.imageProperty().bind(playerAvatar.imageProperty());

        mobStatsTA.visibleProperty().bind(mobAvatar.visibleProperty());

        Actions actions = new Actions(this);
        Actions.updateStats("player",player1.getName(), player1.getHp(), player1.getDmg(), player1.getMoney());

        Actions.rndEvent();
    }


    @FXML
    public void submitMenuAction(ActionEvent actionEvent) {
        if (getMoveFieldText().equals("Отступить") && mainPaneMobFight.isVisible())
        {
            playerAvatarGame.setVisible(false);
            mobAvatar.setVisible(false);
            playerLogs.appendLogs("Вы испугались %s и отступили.\n", Actions.getMob().getName());
            Actions.getMob().mobAttack();
            Actions.rndEvent();
        }
        else if(getMoveFieldText().equals("Уйти") && mainPaneShop.isVisible())
        {
            playerLogs.appendLogs("Вы ушли.\n");
            setCarts(0);
            Actions.rndEvent();
        }
        else if (getMoveFieldText().equals("Атака ломом") && mainPaneMobFight.isVisible())
        {
            Actions.getMob().mobTakeDmg("Ломом", -(getPlayer().getDmg()), Actions.getMob().getHp());
        }
        else if (getMoveFieldText().equals("Обычная атака") && mainPaneMobFight.isVisible()) {
            Actions.getMob().mobTakeDmg("Обычная", -(getPlayer().getDmg()), Actions.getMob().getHp());
        }
        else if (getMoveFieldText().equals("Купить предмет") && mainPaneShop.isVisible()) {
            Actions.shopBuy(items);

        }
        else if (getMoveFieldText().equals("Продать предмет") && mainPaneShop.isVisible()) {
            playerLogs.appendLogs("Предмет продан\n");
        }
        else {
            RegLogController.showAlert(Alert.AlertType.ERROR, """
                    Введено неверное действие
                    Подсказка: Вы можете нажать на стрелочку справа от кнопки "Подвердить ход" и узнать все доступные на
                    данный момент действия
                    """);
        }
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

    @FXML
    public void lomShopClicked(MouseEvent mouseEvent) throws InterruptedException {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            lomCart += 1;
            playerLogs.appendLogs("Лом добавлен в корзину\n");
            gameLogs.appendLogs("""
                    Корзина: Лом %d
                    """, lomCart);
            items.add("lom");

        }
        else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            if (!items.isEmpty()){
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
    public void hpPotionShopClicked(MouseEvent mouseEvent) throws InterruptedException {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            hpPotionCart += 1;
            playerLogs.appendLogs("Зелье здоровья добавлено в корзину\n");
            gameLogs.appendLogs("""
                    Корзина: Зелье здоровья %d
                    """, hpPotionCart);
            items.add("hpPotion");

        }
        else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            if (!items.isEmpty()){
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
        mobAvatarLbl.setText(String.format("Это монстр %s", Actions.getMob().getName()));
        mobAvatarLbl.setVisible(true);
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
    public void exitBtnAction(ActionEvent actionEvent) {
        javafx.application.Platform.exit();
    }


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
    public Image getShopImage() {
        return shopImage;
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
    public Image getMobImage() {
        return mobImage;
    }
    public Image getPlayerImage() {
        return playerImage;
    }
    public Image getLomImage() {
        return LomImage;
    }
    public ImageView getLomAvatarGame() {
        return lomAvatarGame;
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

    public void setCarts(int value) {
        lomCart = value;
        hpPotionCart = value;
    }
    public void setPlayerLogsTA(TextArea playerLogsTA) {
        this.playerLogsTA = playerLogsTA;
    }

}

