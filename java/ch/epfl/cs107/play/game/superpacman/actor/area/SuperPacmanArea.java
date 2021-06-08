package ch.epfl.cs107.play.game.superpacman.actor.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.superpacman.actor.Ghost;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacman;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public abstract class SuperPacmanArea extends Area {

    private static boolean gameState = false;
    private static boolean startscreen = false;

    private SuperPacmanBehavior areaBehavior;
    private float CAMERA_SCALE_FACTOR = 25.f;
    private DiscreteCoordinates playerCoordinates;
    private SuperPacmanPlayer player;

    public float getCameraScaleFactor() {
        return CAMERA_SCALE_FACTOR;
    }

    public void setCameraScaleFactor(float test) {
        CAMERA_SCALE_FACTOR = test;
    }

    protected abstract void createArea();

    public abstract void createAreaAfterDark();

    public SuperPacmanBehavior getBehavior() {
        return this.areaBehavior;
    }

    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            // Set the behavior map
            areaBehavior = new SuperPacmanBehavior(window, getTitle());
            setBehavior(areaBehavior);
            createArea();
            return true;
        }
        return false;
    }

    public void playerCoordinatesSetter(DiscreteCoordinates coord) {
        playerCoordinates = coord;
    }

    public void setPlayer(SuperPacmanPlayer player) {
        this.player = player;
    }

    public SuperPacmanPlayer getPlayer() {
        return this.player;
    }

    public DiscreteCoordinates playerCoordinatesGetter() {
        return this.playerCoordinates;
    }

    public static boolean getGameState() {
        return gameState;
    }

    public static void changeGameState() {
        gameState = !gameState;
    }

    public void setStartscreen() {
        startscreen = true;
    }

    public boolean getStartScreen() {
        return startscreen;
    }

    public abstract void resetGhosts();

    public abstract void playerAteBonus();

    public abstract void playerFinishBonus();
}
