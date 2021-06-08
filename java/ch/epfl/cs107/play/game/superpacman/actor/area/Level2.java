package ch.epfl.cs107.play.game.superpacman.actor.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.superpacman.actor.*;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Window;

public class Level2 extends SuperPacmanArea {

    public int numDiamonds;

    public Logic firstKey = Logic.FALSE;
    public Logic secondKey = Logic.FALSE;

    public DiscreteCoordinates playerSpawn = new DiscreteCoordinates(15, 29);
    private Gate gate1 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(14, 3));
    private Gate gate2 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(15, 3));
    public Key key = new Key(this, "key", Orientation.RIGHT, new DiscreteCoordinates(3, 16),
            new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(8, 14)),
            new Gate(this, Orientation.DOWN, new DiscreteCoordinates(5, 12)),
            new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(8, 10)),
            new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(8, 8)));
    public Key key1 = new Key(this, "key1", Orientation.RIGHT, new DiscreteCoordinates(26, 16),
            new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(21, 14)),
            new Gate(this, Orientation.DOWN, new DiscreteCoordinates(24, 12)),
            new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(21, 10)),
            new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(21, 8)));
    public Key key2 = new Key(this, "key2", Orientation.RIGHT, new DiscreteCoordinates(2, 8),
            new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(10, 2)),
            new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(19, 2)),
            new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(12, 8)),
            new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(17, 8)));
    public Key key3 = new Key(this, "key3", Orientation.RIGHT, new DiscreteCoordinates(27, 8),
            new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(10, 2)),
            new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(19, 2)),
            new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(12, 8)),
            new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(17, 8)));

    public void createArea() {

        registerActor(new Background(this, "superpacman/background"));
        this.getBehavior().registerActors(this);
        this.numDiamonds = getBehavior().getNumDiamonds();

        registerActor(new Door("superpacman/Congrats", new DiscreteCoordinates(1, 1), Logic.TRUE, this,
                Orientation.DOWN, new DiscreteCoordinates(14, 0), new DiscreteCoordinates(15, 0)));

        registerActor(gate1);
        this.getBehavior().getAreaGraph().setSignal(
                new DiscreteCoordinates((int) gate1.getPosition().x, (int) gate1.getPosition().y), Logic.FALSE);
        registerActor(gate2);
        this.getBehavior().getAreaGraph().setSignal(
                new DiscreteCoordinates((int) gate2.getPosition().x, (int) gate2.getPosition().y), Logic.FALSE);

        this.registerActor(key1);
        keyGateManagement(key1, "register");
        this.registerActor(key2);
        keyGateManagement(key2, "register");
        this.registerActor(key3);
        keyGateManagement(key3, "register");
        this.registerActor(key);
        keyGateManagement(key, "register");

    }

    public void createAreaAfterDark() {

        this.getBehavior().registerActors(this);
        this.numDiamonds = getBehavior().getNumDiamonds();

        registerActor(new Door("superpacman/Congrats", new DiscreteCoordinates(1, 1), Logic.TRUE, this,
                Orientation.DOWN, new DiscreteCoordinates(14, 0), new DiscreteCoordinates(15, 0)));

        registerActor(gate1);
        this.getBehavior().getAreaGraph().setSignal(
                new DiscreteCoordinates((int) gate1.getPosition().x, (int) gate1.getPosition().y), Logic.FALSE);
        registerActor(gate2);
        this.getBehavior().getAreaGraph().setSignal(
                new DiscreteCoordinates((int) gate2.getPosition().x, (int) gate2.getPosition().y), Logic.FALSE);

        this.registerActor(key1);
        keyGateManagement(key1, "register");
        this.registerActor(key2);
        keyGateManagement(key2, "register");
        this.registerActor(key3);
        keyGateManagement(key3, "register");
        this.registerActor(key);
        keyGateManagement(key, "register");

    }

    public void openGates() {
        unregisterActor(gate1);
        unregisterActor(gate2);
    }

    public int getNumDiamonds() {
        return numDiamonds;
    }

    public String getTitle() {
        return "superpacman/Level2";
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

    public void keyGateManagement(Key temp_key, String desired) {
        switch (desired) {
        case "register":
            this.registerActor(temp_key.getGate(1));
            this.getBehavior().getAreaGraph().setSignal(new DiscreteCoordinates(
                    (int) temp_key.getGate(1).getPosition().x, (int) temp_key.getGate(1).getPosition().y), Logic.FALSE);
            this.registerActor(temp_key.getGate(2));
            this.getBehavior().getAreaGraph().setSignal(new DiscreteCoordinates(
                    (int) temp_key.getGate(2).getPosition().x, (int) temp_key.getGate(2).getPosition().y), Logic.FALSE);
            this.registerActor(temp_key.getGate(3));
            this.getBehavior().getAreaGraph().setSignal(new DiscreteCoordinates(
                    (int) temp_key.getGate(3).getPosition().x, (int) temp_key.getGate(3).getPosition().y), Logic.FALSE);
            this.registerActor(temp_key.getGate(4));
            this.getBehavior().getAreaGraph().setSignal(new DiscreteCoordinates(
                    (int) temp_key.getGate(4).getPosition().x, (int) temp_key.getGate(4).getPosition().y), Logic.FALSE);
            break;
        case "unregister":
            this.getBehavior().getAreaGraph().setSignal(new DiscreteCoordinates(
                    (int) temp_key.getGate(1).getPosition().x, (int) temp_key.getGate(1).getPosition().y), Logic.TRUE);
            this.unregisterActor(temp_key.getGate(1));
            this.getBehavior().getAreaGraph().setSignal(new DiscreteCoordinates(
                    (int) temp_key.getGate(2).getPosition().x, (int) temp_key.getGate(2).getPosition().y), Logic.TRUE);
            this.unregisterActor(temp_key.getGate(2));
            this.getBehavior().getAreaGraph().setSignal(new DiscreteCoordinates(
                    (int) temp_key.getGate(3).getPosition().x, (int) temp_key.getGate(3).getPosition().y), Logic.TRUE);
            this.unregisterActor(temp_key.getGate(3));
            this.getBehavior().getAreaGraph().setSignal(new DiscreteCoordinates(
                    (int) temp_key.getGate(4).getPosition().x, (int) temp_key.getGate(4).getPosition().y), Logic.TRUE);
            this.unregisterActor(temp_key.getGate(4));
            break;
        }

    }

}
