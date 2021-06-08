package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;

public class SuperPacmanPlayerStatusGUI extends ImageGraphics {

    int color;
    int position;

    public SuperPacmanPlayerStatusGUI(int color, int position) {
        super(ResourcePath.getSprite("superpacman/lifeDisplay"), 1.f, 1.f, new RegionOfInterest(color, 0, 64, 64),
                new Vector((float) position, 0), 1, 5);
        this.color = color; // Either 0 or 64 (yellow or grey)
        this.position = position;
    }

    public int getColor() {
        return color;
    }

    public int getNumber() {
        return position;
    }
}