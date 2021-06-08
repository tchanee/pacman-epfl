package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.actor.area.SuperPacmanArea;
import ch.epfl.cs107.play.game.superpacman.actor.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public abstract class Ghost extends MovableAreaEntity {

    private final int SPEED_GHOST = 9;
    private final boolean movesensitive;
    private final Sprite sprites[];
    private final DiscreteCoordinates originalPosition;

    protected String spriteName;
    public Logic isScared;
    private final GhostHandler handler = new GhostHandler();

    public Ghost(Area area, Orientation orientation, DiscreteCoordinates position, String spriteName,
            boolean movesensitive) {
        super(area, orientation, position);
        this.isScared = Logic.FALSE;
        this.spriteName = spriteName;
        Sprite[] tempSprites = { new Sprite(spriteName, 1.f, 1.f, this, new RegionOfInterest(0, 0, 16, 16)),
                new Sprite(spriteName, 1.f, 1.f, this, new RegionOfInterest(0, 16, 16, 16)),
                new Sprite(spriteName, 1.f, 1.f, this, new RegionOfInterest(0, 32, 16, 16)),
                new Sprite(spriteName, 1.f, 1.f, this, new RegionOfInterest(0, 48, 16, 16)),
                new Sprite("superpacman/ghost.afraid", 1.f, 1.f, this, new RegionOfInterest(0, 0, 16, 16)) };
        this.sprites = tempSprites;
        this.originalPosition = position;
        this.movesensitive = movesensitive;
    }

    public void update(float deltaTime) {

        if (!SuperPacmanArea.getGameState()) {
            if (!isDisplacementOccurs() && movesensitive) {
                orientate(getNextOrientation());
                move(SPEED_GHOST);
            } else if (!isDisplacementOccurs() && !movesensitive && getOwnerArea().canEnterAreaCells(this,
                    Collections.singletonList(getCurrentMainCellCoordinates().jump(getNextOrientation().toVector())))) {
                orientate(getNextOrientation());
                move(SPEED_GHOST);
            }
            super.update(deltaTime);
        }
    }

    public void draw(Canvas canvas) {
        spriteDrawer(getOrientation()).draw(canvas);
    }

    public Sprite spriteDrawer(Orientation orientation) {
        if (!isScared.isOn()) {
            if (orientation == Orientation.UP) {
                return sprites[0];
            } else if (orientation == Orientation.RIGHT) {
                return sprites[1];
            } else if (orientation == Orientation.DOWN) {
                return sprites[2];
            } else {
                return sprites[3];
            }
        } else {
            return sprites[4];
        }
    }

    public abstract Orientation getNextOrientation();

    public void ateBonus() {
        isScared = Logic.TRUE;
    }

    public void bonusDone() {
        isScared = Logic.FALSE;
    }

    public boolean isScared() {
        return isScared.isOn();
    }

    public abstract void atePlayer();

    public DiscreteCoordinates getOriginalCoordinates(){
        return originalPosition;
    }

    public abstract void setPlayerInSight(boolean temp);

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
        return true;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() { return Collections.singletonList(getCurrentMainCellCoordinates()); }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((SuperPacmanInteractionVisitor) v).interactWith(this);
    }

    public void interactWith(Interactable other) {
        handler.acceptInteraction(other);
    }

    public List<DiscreteCoordinates> getFieldOfViewCells() { return getCurrentCells().get(0).getNeighbours(); }

    public boolean wantsCellInteraction() {
        return true;
    }

    public boolean wantsViewInteraction() {
        return false;
    }

    public DiscreteCoordinates getCurrentMainCellCoordinates() {
        return super.getCurrentMainCellCoordinates();
    }

    class GhostHandler implements SuperPacmanInteractionVisitor {

        public void acceptInteraction(Interactable other) {
            if (other instanceof SuperPacmanPlayer) {
                other.acceptInteraction(this);
            }
        }

        public void interactWith(SuperPacmanPlayer pacmanPlayer) {
        }
    }

}
