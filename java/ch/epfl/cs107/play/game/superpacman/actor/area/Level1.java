package ch.epfl.cs107.play.game.superpacman.actor.area;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.superpacman.actor.*;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Window;

public class Level1 extends SuperPacmanArea {

    public int numDiamonds;
    public DiscreteCoordinates playerSpawn = new DiscreteCoordinates(15, 6);
    private Gate gate1 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(14, 3));
    private Gate gate2 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(15, 3));

    public void createArea() {
        registerActor(new Background(this, "superpacman/background"));
        this.getBehavior().registerActors(this);
        registerActor(new Door("superpacman/Level2", new DiscreteCoordinates(15, 29), Logic.TRUE, this,
                Orientation.DOWN, new DiscreteCoordinates(14, 0), new DiscreteCoordinates(15, 0)));
        this.numDiamonds = getBehavior().getNumDiamonds();
        registerActor(gate1);
        registerActor(gate2);
    }
    public void createAreaAfterDark() {
        this.getBehavior().registerActors(this);
        registerActor(new Door("superpacman/Level2", new DiscreteCoordinates(15, 29), Logic.TRUE, this,
                Orientation.DOWN, new DiscreteCoordinates(14, 0), new DiscreteCoordinates(15, 0)));
        this.numDiamonds = getBehavior().getNumDiamonds();
        registerActor(gate1);
        registerActor(gate2);
    }

    public void registerActorGhost(Ghost ghost) {
        for (Ghost g : this.getBehavior().getGhosts()) {
            if (ghost == g) {
                registerActor(g);
            }
        }
    }

    public void playerAteBonus() {
        for (Ghost g : this.getBehavior().getGhosts()) {
            g.ateBonus();
        }
    }

    public void playerFinishBonus() {
        for (Ghost g : this.getBehavior().getGhosts()) {
            g.bonusDone();
        }
    }

    public void openGates() {
        unregisterActor(gate1);
        unregisterActor(gate2);
    }

    public int getNumDiamonds() {
        return numDiamonds;
    }

    public String getTitle() {
        return "superpacman/Level1";
    }

    public void resetGhosts() {
        for (Ghost g : this.getBehavior().getGhosts()) {
            if (g instanceof Pinky) {
                ((Pinky) g).playerInSight = false;
                ((Pinky) g).atePlayer();
            } else if (g instanceof Inky) {
                ((Inky) g).playerInSight = false;
                ((Inky) g).atePlayer();
            } else {
                g.atePlayer();
            }
        }
    }
}
