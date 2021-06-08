package ch.epfl.cs107.play.game.superpacman.actor.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.superpacman.actor.area.SuperPacmanArea;

public class GameOver extends SuperPacmanArea {

    public void createArea() {
        setCameraScaleFactor(700.f);
        registerActor(new Background(this, "superpacman/backgroundend"));

    }

    public void createAreaAfterDark() {}

    public String getTitle() {
        return "superpacman/GameOver";
    }

    public void resetGhosts(){}

    public void playerAteBonus(){};

    public void playerFinishBonus(){};

}