package ch.epfl.cs107.play.game.superpacman.actor.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.superpacman.actor.area.SuperPacmanArea;
import ch.epfl.cs107.play.math.RegionOfInterest;

public class Congrats extends SuperPacmanArea {

    public void createArea() {
        setCameraScaleFactor(700.f);
        registerActor(new Background(this, "superpacman/backgroundwon"));
    }

    public void createAreaAfterDark() {}

    public String getTitle() {
        return "superpacman/Congrats";
    }

    public void resetGhosts(){}

    public void playerAteBonus(){};

    public void playerFinishBonus(){};
}