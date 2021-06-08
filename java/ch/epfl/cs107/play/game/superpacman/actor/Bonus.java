package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.handler.RPGInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.List;

public class Bonus extends AreaEntity {

    private final List<DiscreteCoordinates> currentCells;
    private Sprite sprite[] = new Sprite[4];
    private Sprite offSprite;
    static double i = 0;

    public Bonus(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        this.currentCells = new ArrayList<>();
        this.currentCells.add(position);
        this.sprite[0] = new Sprite("superpacman/coin", 1.f, 1.f,this, new RegionOfInterest(0, 0, 15, 64));
        this.sprite[1] = new Sprite("superpacman/coin", 1.f, 1.f,this, new RegionOfInterest(15, 0, 15, 64));
        this.sprite[2] = new Sprite("superpacman/coin", 1.f, 1.f,this, new RegionOfInterest(30, 0, 15, 64));
        this.sprite[3] = new Sprite("superpacman/coin", 1.f, 1.f,this, new RegionOfInterest(45, 0, 15, 64));
    }

    @Override
    public void draw(Canvas canvas) {
        offSprite.draw(canvas);
    }

    public void update(float deltaTime) {
        i = i + 0.1;
        offSprite = sprite[(int)i % 4];

        super.update(deltaTime);

    }
    public List<DiscreteCoordinates> getCurrentCells() {
        return currentCells;
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
    public boolean isViewInteractable(){
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((RPGInteractionVisitor)v).interactWith(this);
    }
}
