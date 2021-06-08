package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.handler.RPGInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.List;

public class Gate extends AreaEntity {

    private final List<DiscreteCoordinates> currentCells;
    private Sprite sprite;
    public Logic signal;

    public Gate(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        this.currentCells = new ArrayList<>();
        this.currentCells.add(position);
        this.signal = Logic.FALSE;
        if (orientation == Orientation.RIGHT || orientation == Orientation.LEFT){
            this.sprite = new Sprite("superpacman/gate", 1.f, 1.f, this, new RegionOfInterest(0, 64, 64, 64));
        }
        else {
            this.sprite = new Sprite("superpacman/gate", 1.f, 1.f, this, new RegionOfInterest(0, 0, 64, 64));
        }

    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    public List<DiscreteCoordinates> getCurrentCells() {
        return currentCells;
    }

    public Logic getSignal() {
        return signal;
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }
    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((RPGInteractionVisitor) v).interactWith(this);
    }
}
