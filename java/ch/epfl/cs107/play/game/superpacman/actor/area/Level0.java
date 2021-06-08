package ch.epfl.cs107.play.game.superpacman.actor.area;


import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.superpacman.actor.Blinky;
import ch.epfl.cs107.play.game.superpacman.actor.Gate;
import ch.epfl.cs107.play.game.superpacman.actor.Key;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;


public class Level0 extends SuperPacmanArea{

    public DiscreteCoordinates playerSpawn =  new DiscreteCoordinates(10,1);
    private Key key = new Key(this, "key",Orientation.RIGHT, new DiscreteCoordinates(3, 4), new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(5, 8)), new Gate(this, Orientation.LEFT, new DiscreteCoordinates(6, 8)));


    public void createArea(){
        registerActor(new Background(this,"superpacman/background"));
        this.getBehavior().registerActors(this);
        Door door1 = new Door("superpacman/Level1",new DiscreteCoordinates(15,6), Logic.TRUE,this, Orientation.UP, new DiscreteCoordinates(5,9), new DiscreteCoordinates(6,9));
        this.registerActor(door1);
        this.registerActor(key);
        this.registerActor(key.getGate(1));
        this.registerActor(key.getGate(2));
    }

    public void createAreaAfterDark(){
        this.getBehavior().registerActors(this);
        Door door1 = new Door("superpacman/Level1",new DiscreteCoordinates(15,6), Logic.TRUE,this, Orientation.UP, new DiscreteCoordinates(5,9), new DiscreteCoordinates(6,9));
        this.registerActor(door1);
        this.registerActor(key);
        this.registerActor(key.getGate(1));
        this.registerActor(key.getGate(2));
    }

    public String getTitle(){
        return "superpacman/Level0";
    }

    public void resetGhosts(){}

    public void playerAteBonus(){};

    public void playerFinishBonus(){};
}
