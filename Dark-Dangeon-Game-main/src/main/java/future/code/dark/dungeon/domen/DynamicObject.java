package future.code.dark.dungeon.domen;

import future.code.dark.dungeon.config.Configuration;
import future.code.dark.dungeon.service.GameMaster;

public abstract class DynamicObject extends GameObject {

    public DynamicObject(int xPosition, int yPosition, String imagePath) {
        super(xPosition, yPosition, imagePath);
    }

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    protected void move(Direction direction, int distance) {
        int tmpXPosition = getXPosition();
        int tmpYPosition = getYPosition();

        switch (direction) {
            case UP -> tmpYPosition -= distance;
            case DOWN -> tmpYPosition += distance;
            case LEFT -> tmpXPosition -= distance;
            case RIGHT -> tmpXPosition += distance;
        }

        if (isAllowedSurface(tmpXPosition, tmpYPosition) && isExitEnable(tmpXPosition, tmpYPosition)) {
            xPosition = tmpXPosition;
            yPosition = tmpYPosition;
        }
    }

    public boolean isExitEnable(int x,int y){
        if(!(GameMaster.getInstance().getExit().xPosition==x
                && GameMaster.getInstance().getExit().yPosition==y)) {
            return true;
        }
        return GameMaster.getInstance().exitEnable;
    }

    public boolean collision(GameObject gameObject){
        return this.xPosition==gameObject.xPosition
            && this.yPosition==gameObject.yPosition;
    }

    private Boolean isAllowedSurface(int x, int y) {
        return GameMaster.getInstance().getMap().getMap()[y][x] != Configuration.WALL_CHARACTER;
    }

}
