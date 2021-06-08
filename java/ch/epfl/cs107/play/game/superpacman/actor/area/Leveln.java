package ch.epfl.cs107.play.game.superpacman.actor.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.superpacman.actor.Gate;
import ch.epfl.cs107.play.game.superpacman.actor.Ghost;
import ch.epfl.cs107.play.game.superpacman.actor.Inky;
import ch.epfl.cs107.play.game.superpacman.actor.Pinky;
import ch.epfl.cs107.play.game.superpacman.actor.area.maze.CreateImage;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.io.IOException;

public class Leveln extends SuperPacmanArea{

    public int numDiamonds;
    public static int size = 20;
    public DiscreteCoordinates playerSpawn =  new DiscreteCoordinates(1,size-2);
    CreateImage maze = new CreateImage(size,size);
    /**
    Need to change three things when changing the size:
    here the size, then the size in the play and finally in the SuperPacman change the starting position to (1,size-2)
     **/
    @Override
    public void createArea(){
        CreateImage.drawMaze(size, 2, 2, 1, 0, 1); //the problem is because of the ghost additions; with pinky in particular
        this.getBehavior().registerActors(this);
        registerActor(new Background(this, "superpacman/background"));
        this.numDiamonds = getBehavior().getNumDiamonds();
    }

    public void createAreaAfterDark(){
        this.getBehavior().registerActors(this);
        registerActor(new Background(this, "superpacman/background"));
        this.numDiamonds = getBehavior().getNumDiamonds();
    }
    public SuperPacmanBehavior getBehavior(){
        return super.getBehavior();
    }

    public void playerAteBonus(){
        for(Ghost g : this.getBehavior().getGhosts()){
            g.ateBonus();
        }
    }
    public void playerFinishBonus(){
        for(Ghost g : this.getBehavior().getGhosts()){
            g.bonusDone();
        }
    }

    public int getNumDiamonds() {
        return numDiamonds;
    }

    public void resetGhosts(){
        for(Ghost g : this.getBehavior().getGhosts()){
            if(g instanceof Pinky){
                ((Pinky)g).playerInSight = false;
                ((Pinky)g).atePlayer();
            }
            else if(g instanceof Inky){
                ((Inky)g).playerInSight = false;
                ((Inky)g).atePlayer();
            }
            else {
                g.atePlayer();
            }
        }
    }

    public String getTitle(){
        return "superpacman/Leveln";
    }
}
