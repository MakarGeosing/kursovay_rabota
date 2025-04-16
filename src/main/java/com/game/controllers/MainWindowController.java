package com.game.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import com.game.Actions;
import com.game.Gamelogs;
import com.game.Mob;
import com.game.Player;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
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
    private Button LomAttackBtn, attackBtn, retreatBtn, exitBtn;
    @FXML
    private SplitMenuButton clearGameLogsBtn, submitMenuBtn;
    @FXML
    private TextArea gameLogsTA, playerLogsTA, playerStatsTA, mobStatsTA;
    @FXML
    private ImageView mobAvatarGame, playerAvatar, playerAvatarGame, mobAvatar, lomAvatarGame;
    @FXML
    private Label playerAvatarLbl, mobAvatarLbl, lomQuantityLbl;
    @FXML
    private AnchorPane mainPaneMobFight, actionsPane, mainPaneShop, mainPaneBlank, actionsPaneBlank, mainPaneQuest1;


    static Player player1 = new Player(1,"Makar" ,100, 10, 20);
    static Mob mob = new Mob(1, "Abracadabra", 100, 10);
    private final StringProperty playerStats = new SimpleStringProperty();
    private final StringProperty mobStats = new SimpleStringProperty();
    private final StringProperty mobName = new SimpleStringProperty();
    private Gamelogs gameLogs;
    private Gamelogs playerLogs;
    private final Image mobImage = new Image(Objects.requireNonNull(getClass().getResource("/com/game/monster.png")).toString());
    private final Image playerImage = new Image(Objects.requireNonNull(getClass().getResource("/com/game/player.png")).toString());
    private final Image shopImage = new Image(Objects.requireNonNull(getClass().getResource("/com/game/shop.png")).toString());
    private final Image LomImage = new Image(Objects.requireNonNull(getClass().getResource("/com/game/lom.png")).toString());
    private String prevGameLogsText, prevPlayerLogsText;
    private Actions actions;
    private List<String> items = new ArrayList<String>();
    private int cart;

    @FXML
    public void initialize() {
        gameLogs = new Gamelogs(gameLogsTA);
        playerLogs = new Gamelogs(playerLogsTA);
        playerStatsTA.textProperty().bind(playerStats);
        mobStatsTA.textProperty().bind(mobStats);
        playerAvatar.setImage(playerImage);

        mobAvatarGame.imageProperty().bind(mobAvatar.imageProperty());

        playerAvatarGame.imageProperty().bind(playerAvatar.imageProperty());
        mobStatsTA.visibleProperty().bind(mobAvatar.visibleProperty());
        actions = new Actions(this);
        actions.setPlayer(player1);

        updateStats("player",player1.getName(), player1.getHp(), player1.getDmg(), player1.getMoney());

        Actions.mobFightStart();

    }



    @FXML
    public void submitMenuAction(ActionEvent actionEvent) {
        if (getMoveFieldText().equals("Отступить") && mainPaneMobFight.isVisible())
        {
            playerAvatarGame.setVisible(false);
            mobAvatar.setVisible(false);
            playerLogs.appendLogs("Вы испугались %s и отступили.\n", mob.getName());
            Actions.shopStart();
        }
        else if(getMoveFieldText().equals("Уйти"))
        {
            playerLogs.appendLogs("Вы ушли.\n");
            Actions.mobFightStart();
        }
        else if (getMoveFieldText().equals("Атака ломом") && mainPaneMobFight.isVisible())
        {
            gameLogs.appendLogs("Вы ударили %s ломом на %d.\n", mob.getName(), (player1.getDmg() * 2));
        }
        else if (getMoveFieldText().equals("Обычная атака") && mainPaneMobFight.isVisible()) {
            mob.setHp(-(player1.getDmg()));
            gameLogs.appendLogs("Вы нанесли %s %d урона.\n", mob.getName(), player1.getDmg());
            updateStats("mob", mob.getName(), mob.getHp(), mob.getDmg(),0);
            Actions.mobFightStart();
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
    void playerAvatarEntered(MouseEvent event) {
        playerAvatarLbl.setVisible(true);
    }

    @FXML
    void mobAvatarEntered(MouseEvent event) {
        mobName.set(String.format("Это монстр %s", mob.getName()));
        mobAvatarLbl.textProperty().bind(mobName);
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
        if (gameLogs.getGameLogsText().length() > prevGameLogsText.length()) {
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
    public void exitBtnAction(ActionEvent actionEvent) {
        javafx.application.Platform.exit();
    }

    @FXML
    public void lomClicked(MouseEvent mouseEvent) throws InterruptedException {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            cart += 1;
            playerLogs.appendLogs("Лом добавлен в корзину\n");
            gameLogs.appendLogs("""
                    Корзина: Лом %d
                    """, cart);
            items.add("lom");

        }
        else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            if (!items.isEmpty()){
                cart -= 1;
                items.removeLast();
                playerLogs.appendLogs("Последний предмет удалён из корзины\n");
                gameLogs.appendLogs("""
                    Корзина: Лом %d
                    """, cart);
            }
            else {
                cart = 0;
                playerLogs.appendLogs("Корзина пуста\n");
            }

        }
    }

    public void updateStats(String object, String name, int hp, int dmg, int money) {
        if (object.equals("player")) {
            playerStats.set(String.format("ИМЯ: %s\nХП: %d\nУРОН: %d\nДЕНЬГИ: %d", name, hp, dmg, money));
        } else {
            mobStats.set(String.format("ИМЯ: %s\nХП: %d\nУРОН: %d", name, hp, dmg));
        }
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
    public Player getPlayer() {
        return player1;
    }
    public Mob getMob() {
        return mob;
    }
    public Image getShopImage() {
        return shopImage;
    }
    public SimpleStringProperty getMobStats() {
        return (SimpleStringProperty) mobStats;
    }
    public Gamelogs getGameLogs() {
        return gameLogs;
    }
    public Gamelogs getPlayerLogs() {
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
    public Label getLomQuantityLbl() {
        return lomQuantityLbl;
    }

    public void setPlayerLogsTA(TextArea playerLogsTA) {
        this.playerLogsTA = playerLogsTA;
    }


}

