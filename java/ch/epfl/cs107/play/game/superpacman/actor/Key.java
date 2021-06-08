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

public class Key extends AreaEntity {

    private final List<DiscreteCoordinates> currentCells;
    private Sprite sprite;
    private Gate gate1;
    private Gate gate2;
    private Gate gate3;
    private Gate gate4;
    private Gate testerGate;
    private String name;

    public Key(Area area, String name, Orientation orientation, DiscreteCoordinates position, Gate gate1, Gate gate2) {
        super(area, orientation, position);
        this.name = name;
        this.currentCells = new ArrayList<>();
        this.currentCells.add(position);
        this.sprite = new Sprite("superpacman/key", 1.f, 1.f, this);
        this.gate1 = gate1;
        this.gate2 = gate2;

    }
    public Key(Area area, String name, Orientation orientation, DiscreteCoordinates position, Gate gate1, Gate gate2, Gate gate3, Gate gate4) {
        super(area, orientation, position);
        this.currentCells = new ArrayList<>();
        this.currentCells.add(position);
        this.name = name;
        this.sprite = new Sprite("superpacman/key", 1.f, 1.f, this);
        this.gate1 = gate1;
        this.gate2 = gate2;
        this.gate3 = gate3;
        this.gate4 = gate4;

    }

    public Gate getGate(int i){
        switch(i) {
            case 1:
                return gate1;
            case 2:
                return gate2;
            case 3:
                return gate3;
            case 4:
                return gate4;
            default:
                return testerGate; //when there is no case;
        }
    }
    public String getName(){
        return name;
    }
    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
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
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((RPGInteractionVisitor) v).interactWith(this);
    }
}
