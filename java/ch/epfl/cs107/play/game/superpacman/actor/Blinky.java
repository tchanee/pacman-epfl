package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;

public class Blinky extends Ghost{

    public boolean playerInSight;

    public Blinky(Area area, Orientation orientation, DiscreteCoordinates position, String spriteName){
        super(area,orientation, position,spriteName,false);
    }

    public void setPlayerInSight(boolean temp){
        playerInSight = temp;
    }

    public void atePlayer(){
        this.resetMotion();
        this.setCurrentPosition(new Vector(getOriginalCoordinates().x, getOriginalCoordinates().y));
    }
    @Override
    public Orientation getNextOrientation() {
        int randomInt = RandomGenerator.getInstance().nextInt(4);
        return Orientation.fromInt(randomInt);
    }
}
