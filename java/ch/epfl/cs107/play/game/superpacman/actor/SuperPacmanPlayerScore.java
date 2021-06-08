package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;

public class SuperPacmanPlayerScore extends TextGraphics {

    double position;

    public SuperPacmanPlayerScore(double position) {
        super(ResourcePath.getSprite("Score: " + Integer.toString((int) 0)), 1f, Color.YELLOW, null, 2, false, false,
                new Vector((float) position, 0));
        this.position = position;
    }

    public double getNumber() {
        return position;
    }
}
