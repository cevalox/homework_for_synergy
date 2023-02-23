package future.code.dark.dungeon.service;

import future.code.dark.dungeon.config.Configuration;
import future.code.dark.dungeon.domen.Coin;
import future.code.dark.dungeon.domen.DynamicObject;
import future.code.dark.dungeon.domen.Enemy;
import future.code.dark.dungeon.domen.Exit;
import future.code.dark.dungeon.domen.GameObject;
import future.code.dark.dungeon.domen.Map;
import future.code.dark.dungeon.domen.Player;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static future.code.dark.dungeon.config.Configuration.*;

public class GameMaster {

    private static GameMaster instance;

    private final Map map;
    private final List<GameObject> gameObjects;
    public  Integer coinsKilled=0;
    public String minusKilledCoins="Вы собрали монет:";
    public  Integer coins=9;
    public String coinsStr="Вам осталось собрать монет:";
    public Boolean exitEnable=false;
    private final Image victory=new ImageIcon(VICTORY_SPRITE).getImage();


    public static synchronized GameMaster getInstance() {
        if (instance == null) {
            instance = new GameMaster();
        }
        return instance;
    }

    private GameMaster() {
        try {
            this.map = new Map(Configuration.MAP_FILE_PATH);
            this.gameObjects = initGameObjects(map.getMap());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private List<GameObject> initGameObjects(char[][] map) {
        List<GameObject> gameObjects = new ArrayList<>();
        Consumer<GameObject> addGameObject = gameObjects::add;
        Consumer<Enemy> addEnemy = enemy -> {if (ENEMIES_ACTIVE) gameObjects.add(enemy);};

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                switch (map[i][j]) {
                    case EXIT_CHARACTER -> addGameObject.accept(new Exit(j, i));
                    case COIN_CHARACTER -> addGameObject.accept(new Coin(j, i));
                    case ENEMY_CHARACTER -> addEnemy.accept(new Enemy(j, i));
                    case PLAYER_CHARACTER -> addGameObject.accept(new Player(j, i));
                }
            }
        }

        return gameObjects;
    }

    public void renderFrame(Graphics graphics) {
        if (getPlayer().toString().equals("Player{[6:2]}")){
            graphics.drawImage(victory,0,0,null);
        }
        else{
            getMap().render(graphics);
            getStaticObjects().forEach(gameObject -> gameObject.render(graphics));
            getCoins().forEach(gameObject -> gameObject.render(graphics));
            getPlayer().render(graphics);
            graphics.setColor(Color.WHITE);
            graphics.drawString(getPlayer().toString(), 10, 20);
            graphics.drawString(coinsKilled.toString(), 250, 20);
            graphics.drawString(minusKilledCoins, 90, 20);
            graphics.drawString(coinsStr, 290, 20);
            graphics.drawString(coins.toString(), 500, 20);
        }

    }

    public Player getPlayer() {
        return (Player) gameObjects.stream()
                .filter(gameObject -> gameObject instanceof Player)
                .findFirst()
                .orElseThrow();
    }
    public Exit getExit() {
        return (Exit) gameObjects.stream()
                .filter(gameObject -> gameObject instanceof Exit)
                .findFirst()
                .orElseThrow();
    }

    private List<GameObject> getStaticObjects() {
        return gameObjects.stream()
                .filter(gameObject -> !(gameObject instanceof DynamicObject))
                .collect(Collectors.toList());
    }

    public List<Coin> getCoins() {

        return gameObjects.stream()
                .filter(gameObject -> gameObject instanceof Coin)
                .map(gameObject -> (Coin) gameObject)
                .collect(Collectors.toList());
    }

    public void deleteCoins(int x,int y){
        coinsKilled++;
        coins--;
        if (coinsKilled==9) {
            exitEnable=true;
        }
        this.gameObjects.removeIf(coin-> coin instanceof Coin
        &&  coin.getXPosition()==x&& coin.getYPosition()==y);
    }

    public Map getMap() {
        return map;
    }

}
