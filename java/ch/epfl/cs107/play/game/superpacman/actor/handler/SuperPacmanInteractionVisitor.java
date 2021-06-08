package ch.epfl.cs107.play.game.superpacman.actor.handler;

import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.handler.RPGInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.actor.*;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.List;

public interface SuperPacmanInteractionVisitor extends RPGInteractionVisitor {

    default void interactWith(SuperPacmanPlayer other){}
    default void interactWith(Interactable other){}
    default void interactWith(Bonus bonus){}
    default void interactWith(Cherry cherry){}
    default void interactWith(Key key){}
    default void interactWith(Gate gate){}
    default void interactWith(Ghost ghost){}
    default List<DiscreteCoordinates> getCurrentCells(){
        return null;
    }
}
