package ch.epfl.cs107.play.game.rpg.handler;

import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.Sign;
import ch.epfl.cs107.play.game.superpacman.actor.*;


public interface RPGInteractionVisitor extends AreaInteractionVisitor {

    /// Add Interaction method with all non Abstract Interactable

    /**
     * Simulate and interaction between RPG Interactor and a Door
     * @param door (Door), not null
     */
    default void interactWith(Door door){
        // by default the interaction is empty
    }

    default void interactWith(Interactable other){
        // by default the interaction is empty
    }

    default void interactWith(Bonus bonus){
    }

    default void interactWith(Cherry cherry){

    }
    default void interactWith(Diamond diamond){

    }
    default void interactWith(Key key){

    }
    default void interactWith(Gate gate){

    }
    /**
     *
     * Simulate and interaction between RPG Interactor and a Sign
     * @param sign (Sign), not null
     */
    default void interactWith(Sign sign){
        // by default the interaction is empty
    }

}
