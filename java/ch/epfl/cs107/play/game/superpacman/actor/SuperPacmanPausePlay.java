package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;

public class SuperPacmanPausePlay extends ImageGraphics {

    int color;
    double position;
    String pause = "superpacman/pause";
    String play = "superpacman/play";

    public SuperPacmanPausePlay(int color, double position, String state) {

        super(ResourcePath.getSprite("superpacman/" + state), 1.f, 1.f, new RegionOfInterest(color, 0, 512, 512),
                new Vector((float) position, 0), 1, 5);

        this.color = color;
        this.position = position;
    }

    public int getColor() {
        return this.color;
    }

    public double getNumber() {
        return this.position;
    }

}
