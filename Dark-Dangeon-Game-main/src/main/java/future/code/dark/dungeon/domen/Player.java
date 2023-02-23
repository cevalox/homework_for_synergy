package future.code.dark.dungeon.domen;

import future.code.dark.dungeon.config.Configuration;
import future.code.dark.dungeon.service.GameMaster;

public class Player extends DynamicObject {
    private static final int stepSize = 1;

    public Player(int xPosition, int yPosition) {
        super(xPosition, yPosition, Configuration.PLAYER_SPRITE);
    }

    public void move(Direction direction) {
        super.move(direction, stepSize);
        if (GameMaster.getInstance().getCoins().stream().anyMatch(enemy -> this.collision(enemy))){
            GameMaster.getInstance().deleteCoins(this.xPosition,this.yPosition);
        };
    }

    @Override
    public String toString() {
        return "Player{[" + xPosition + ":" + yPosition + "]}";
    }
}
