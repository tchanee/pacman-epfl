package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.handler.RPGInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.List;

public class Cherry extends AreaEntity {

    private final List<DiscreteCoordinates> currentCells;
    private Sprite sprite;
    private final int CHERRY_SCORE = 15;

    public Cherry(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        this.currentCells = new ArrayList<>();
        this.currentCells.add(position);
        this.sprite = new Sprite("superpacman/cherry", 1.f, 1.f, this);

    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    public List<DiscreteCoordinates> getCurrentCells() {
        return currentCells;
    }

    public int getScore(){
        return CHERRY_SCORE;
    }

    @Override
    public boolean takeCellSpace() {
        return false;
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
