package ch.epfl.cs107.play.game.rpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.superpacman.actor.area.Congrats;
import ch.epfl.cs107.play.game.superpacman.actor.area.GameOver;
import ch.epfl.cs107.play.game.superpacman.actor.area.Leveln;
import ch.epfl.cs107.play.game.superpacman.actor.area.StartScreen;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

/**
 * Player is a Specific RPG actor
 */
public abstract class Player extends MovableAreaEntity implements Interactor {

    private boolean isPassingADoor;
    public boolean regulartest = false;
    public boolean speedruntest = false;
    public boolean mainmenu = false;
    private boolean eaten = false;
    private boolean won = false;
    private Door passedDoor;

    /**
     * Default Player constructor
     * @param area (Area): Owner Area, not null
     * @param orientation (Orientation): Initial player orientation, not null
     * @param coordinates (Coordinates): Initial position, not null
     */
    public Player(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
        super(area, orientation, coordinates);
        passedDoor = null;
        isPassingADoor = false;
    }

    /**
     * Leave an area by unregister this player
     */
    public void leaveArea(){
        getOwnerArea().unregisterActor(this);
    }

    /**
     *
     * @param area (Area): initial area, not null
     * @param position (DiscreteCoordinates): initial position, not null
     */
    public void enterArea(Area area, DiscreteCoordinates position){
        area.registerActor(this);
        if(!(area instanceof StartScreen || area instanceof GameOver || area instanceof Congrats)) {
            area.setViewCandidate(this);
        }
        setOwnerArea(area);
        setCurrentPosition(position.toVector());
        if(!(area instanceof Leveln)){
            resetDoorStates();
        }
        resetMotion();
    }

    /**
     * Reset the door state
     */
    private void resetDoorStates(){
        passedDoor = null;
        isPassingADoor = false;
    }

    /// Getter and setter for interaction

    /**
     * Indicate the player just passed a door
     * @param door (Door): the door passed, not null
     */
    protected void setIsPassingADoor(Door door){
        this.passedDoor = door;
        isPassingADoor = true;
    }

    /**@return (boolean): true if the player is passing a door*/
    public boolean isPassingADoor(){
        return isPassingADoor;
    }

    /**
     * Getter of the passing door
     * @return (Door)
     */
    public Door passedDoor(){
        return passedDoor;
    }

    public void setRegulartest(boolean regulartest) {
        this.regulartest = regulartest;
    }
    public void setSpeedruntest(boolean speedruntest) {
        this.speedruntest = speedruntest;
    }
    public void setMainmenu(boolean temp){
        mainmenu = temp;
    }
    public void setEaten(boolean temp) {
        eaten = temp;
    }
    public void setWon(boolean temp){ won = temp;}
    public boolean getRegulartest(){
        return regulartest;
    }
    public boolean getSpeedruntest(){
        return speedruntest;
    }
    public boolean getMainmenu(){
        return mainmenu;
    }
    public boolean getEaten() { return eaten; }
    public boolean getWon() {return won;}



}
