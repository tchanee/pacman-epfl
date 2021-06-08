package ch.epfl.cs107.play.game.superpacman.actor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.superpacman.actor.area.*;
import ch.epfl.cs107.play.game.superpacman.actor.area.maze.CreateImage;
import ch.epfl.cs107.play.game.superpacman.actor.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public class SuperPacmanPlayer extends Player {

    private float initialscore;
    private int GHOST_SCORE = 500;
    private Sprite sprite;
    private Area tempArea;
    private ArrayList<SuperPacmanArea> areas_visited = new ArrayList<SuperPacmanArea>();
    public static float delta_for_pause = 0.f;
    public static int sprite_drawer_iterator = 0;
    public static int sprite_drawer_iterator_count = 0;
    private boolean ateBonus = false;
    private float delta_for_change = 0.f;
    private static boolean removed = false;
    public int numDiamondsCollected = 0;
    private boolean didwin = false;
    public static int i = 200;

    private DiscreteCoordinates PLAYER_SPAWN_POSITION[] = { new DiscreteCoordinates(10, 1), new DiscreteCoordinates(10, 1), new DiscreteCoordinates(15, 6), new DiscreteCoordinates(15, 29),new DiscreteCoordinates(10, 1),new DiscreteCoordinates(10, 1), };
    private final int SPEED = 6;
    private Orientation desiredOrientation = getOrientation();
    private final SuperPacmanPlayerHandler handler = new SuperPacmanPlayerHandler();
    private boolean locked = false;
    private final Sprite sprites[][];
    private List<SuperPacmanPlayerStatusGUI> lives = new ArrayList<SuperPacmanPlayerStatusGUI>(
            Arrays.asList(new SuperPacmanPlayerStatusGUI(0, 0), new SuperPacmanPlayerStatusGUI(0, 1),
                    new SuperPacmanPlayerStatusGUI(0, 2), new SuperPacmanPlayerStatusGUI(64, 3),
                    new SuperPacmanPlayerStatusGUI(64, 4)));
    private SuperPacmanPlayerScore score = new SuperPacmanPlayerScore(5.5);
    private SuperPacmanPausePlay pause = new SuperPacmanPausePlay(0, 6.5, "pause1");
    private SuperPacmanPausePlay play = new SuperPacmanPausePlay(0, 6.5, "play1");

    public SuperPacmanPlayer(Area owner, DiscreteCoordinates coordinates) {
        super(owner, Orientation.RIGHT, coordinates);
        this.initialscore = 0;
        tempArea = owner;
        sprite = new Sprite("superpacman/pacman", 1.f, 1.f, this, new RegionOfInterest(0, 0, 64, 64));
        Sprite[][] tempsprites = {
                { new Sprite("superpacman/pacman", 1.f, 1.f, this, new RegionOfInterest(0, 0, 64, 64)),
                        new Sprite("superpacman/pacman", 1.f, 1.f, this, new RegionOfInterest(64, 0, 64, 64)),
                        new Sprite("superpacman/pacman", 1.f, 1.f, this, new RegionOfInterest(128, 0, 64, 64)),
                        new Sprite("superpacman/pacman", 1.f, 1.f, this, new RegionOfInterest(192, 0, 64, 64)), },
                { new Sprite("superpacman/pacman", 1.f, 1.f, this, new RegionOfInterest(0, 64, 64, 64)),
                        new Sprite("superpacman/pacman", 1.f, 1.f, this, new RegionOfInterest(64, 64, 64, 64)),
                        new Sprite("superpacman/pacman", 1.f, 1.f, this, new RegionOfInterest(128, 64, 64, 64)),
                        new Sprite("superpacman/pacman", 1.f, 1.f, this, new RegionOfInterest(192, 64, 64, 64)), },
                { new Sprite("superpacman/pacman", 1.f, 1.f, this, new RegionOfInterest(0, 128, 64, 64)),
                        new Sprite("superpacman/pacman", 1.f, 1.f, this, new RegionOfInterest(64, 128, 64, 64)),
                        new Sprite("superpacman/pacman", 1.f, 1.f, this, new RegionOfInterest(128, 128, 64, 64)),
                        new Sprite("superpacman/pacman", 1.f, 1.f, this, new RegionOfInterest(192, 128, 64, 64)), },
                { new Sprite("superpacman/pacman", 1.f, 1.f, this, new RegionOfInterest(0, 192, 64, 64)),
                        new Sprite("superpacman/pacman", 1.f, 1.f, this, new RegionOfInterest(64, 192, 64, 64)),
                        new Sprite("superpacman/pacman", 1.f, 1.f, this, new RegionOfInterest(128, 192, 64, 64)),
                        new Sprite("superpacman/pacman", 1.f, 1.f, this, new RegionOfInterest(192, 192, 64, 64)), } };
        this.sprites = tempsprites;
        resetMotion();
    }

    @Override
    public void update(float deltaTime) {

        openGated();
        changedAreas();
        getAteBonus();


        Keyboard keyboard = getOwnerArea().getKeyboard();

        switch(getOwnerArea().getTitle()) {
            case "superpacman/StartScreen":
                ch.epfl.cs107.play.window.Button regular = keyboard.get(Keyboard.R);
                ch.epfl.cs107.play.window.Button speedrun = keyboard.get(Keyboard.S);
                if (regular.isDown()) {
                    this.setRegulartest(true);
                } else if (speedrun.isDown()) {
                    for(int i = 0; i < 1000; ++i){
                        //This is to allow the maze to generate when creating a JAR file so it doesn't crash
                        //Otherwise, when running it from an IDE it works perfectly.
                    }
                    this.setSpeedruntest(true);
                }
                break;
            case "superpacman/GameOver":
                ch.epfl.cs107.play.window.Button home = keyboard.get(Keyboard.H);
                if (home.isDown()) {
                    this.setMainmenu(true);
                }
                break;
            case "superpacman/Congrats":
                ch.epfl.cs107.play.window.Button menu = keyboard.get(Keyboard.M);
                if (menu.isDown()) {
                    this.setMainmenu(true);
                }
                break;
            case "superpacman/Level0":
            case "superpacman/Level1":
            case "superpacman/Level2":
            case "superpacman/Leveln":
                changedGeneralAreas();
                ch.epfl.cs107.play.window.Button pp = keyboard.get(Keyboard.P);
                delta_for_pause += deltaTime;
                if (pp.isDown() && (delta_for_pause > 3 * deltaTime)) {
                    delta_for_pause = 0;
                    SuperPacmanArea.changeGameState();
                }
                if (didwin) {
                    setWon(true);
                }
                if (SuperPacmanArea.getGameState() == false) {
                    giveOrientation(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
                    giveOrientation(Orientation.UP, keyboard.get(Keyboard.UP));
                    giveOrientation(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
                    giveOrientation(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
                    ((SuperPacmanArea) getOwnerArea()).playerCoordinatesSetter(new DiscreteCoordinates((int) this.getPosition().x, (int) this.getPosition().y));
                    if (!isDisplacementOccurs() && getOwnerArea().canEnterAreaCells(this, Collections.singletonList(getCurrentMainCellCoordinates().jump(desiredOrientation.toVector()))) && !locked) {
                        orientate(desiredOrientation);
                        move(SPEED);
                    }
                    ((SuperPacmanArea) getOwnerArea()).playerCoordinatesSetter(new DiscreteCoordinates((int) this.getPosition().x, (int) this.getPosition().y));
                    super.update(deltaTime);
                }
                break;
        }
    }


    private void giveOrientation(Orientation orientation, ch.epfl.cs107.play.window.Button b) {
        if (b.isDown()) {
            if (desiredOrientation != orientation)
                desiredOrientation = orientation;
        }
    }

    /**
     * Leave an area by unregister this player
     */

    public void leaveArea() {
        getOwnerArea().unregisterActor(this);
    }

    public Sprite spriteDrawer(Orientation orientation) {
        if (isDisplacementOccurs()) {
            if (orientation == Orientation.UP) {
                return sprites[2][sprite_drawer_iterator % 4];
            } else if (orientation == Orientation.RIGHT) {
                return sprites[3][sprite_drawer_iterator % 4];
            } else if (orientation == Orientation.DOWN) {
                return sprites[0][sprite_drawer_iterator % 4];
            } else {
                return sprites[1][sprite_drawer_iterator % 4];
            }

        } else {
            return sprites[1][0];
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (!(getOwnerArea() instanceof StartScreen || getOwnerArea() instanceof Congrats)) {
            spriteDrawer(getOrientation()).draw(canvas);
            if (((SuperPacmanArea) getOwnerArea()).getGameState() == false) {
                ++sprite_drawer_iterator_count;
                if (sprite_drawer_iterator_count == 3) {
                    ++sprite_drawer_iterator;
                    sprite_drawer_iterator_count = 0;
                }
            }
            for (int i = 0; i < lives.size(); i++) {
                SuperPacmanPlayerStatus(canvas, lives.get(i), i);
            }
            SuperPacmanPlayerScore(canvas);
            SuperPacmanPause(canvas, pause);
        }
    }

    public void SuperPacmanPlayerStatus(Canvas canvas, SuperPacmanPlayerStatusGUI life, int number) {
        float width = canvas.getScaledWidth();
        float height = canvas.getScaledHeight();
        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector((width / 2 - 0.5f), -((height / 2) - 1.375f)));
        life.setAnchor(anchor.add(new Vector((float) number, 0)));
        life.draw(canvas);

    }

    public void SuperPacmanPause(Canvas canvas, SuperPacmanPausePlay pause) {
        float width = canvas.getScaledWidth();
        float height = canvas.getScaledHeight();
        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector((width / 2 - 0.5f), -((height / 2) - 1.375f)));
        pause.setAnchor(anchor.add(new Vector(width - 2.f, 0)));
        play.setAnchor(anchor.add(new Vector(width - 2.f, 0)));
        if (SuperPacmanArea.getGameState()) {
            pause.draw(canvas);
        } else {
            play.draw(canvas);
        }
    }

    public void SuperPacmanPlayerScore(Canvas canvas) {
        float width = canvas.getScaledWidth();
        float height = canvas.getScaledHeight();
        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector((width / 2 - 0.5f), -((height / 2) - 1.375f)));
        score.setText("Score: " + Integer.toString((int) initialscore));
        score.setAnchor(anchor.add(new Vector((float) score.getNumber(), 0)));
        score.draw(canvas);

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
    /**
     * Only the player can command a view interaction as he is the only interactor.
     * An area entity is an interactable and not an interactor
     **/
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        Orientation tempOrientation = getOrientation();
        List<DiscreteCoordinates> cells = new ArrayList<DiscreteCoordinates>();
        DiscreteCoordinates temp = getCurrentMainCellCoordinates();
        do {
            cells.add(temp);
            temp = temp.jump(tempOrientation.toVector());
        } while (((SuperPacmanArea) getOwnerArea()).getBehavior().getAreaGraph()
                .nodeExists(new DiscreteCoordinates(temp.x, temp.y))
                && ((SuperPacmanArea) getOwnerArea()).getBehavior().getAreaGraph()
                        .getSignal(new DiscreteCoordinates(temp.x, temp.y)));
        return cells;
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        if (lives.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void interactWith(Interactable other) {
        handler.acceptInteraction(other);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((SuperPacmanInteractionVisitor) v).interactWith(this);
    }

    public void openGated() {
        if (getOwnerArea() instanceof Level1) {
            if (numDiamondsCollected == ((Level1) getOwnerArea()).getNumDiamonds() && !removed) {
                ((Level1) getOwnerArea()).openGates();
                removed = true;
            }
        }
        if (getOwnerArea() instanceof Level2) {
            if (numDiamondsCollected == ((Level2) getOwnerArea()).getNumDiamonds() && !removed) {
                ((Level2) getOwnerArea()).openGates();
                removed = true;
            }
        }
        if (getOwnerArea() instanceof Leveln) {
            if (numDiamondsCollected == ((Leveln) getOwnerArea()).getNumDiamonds()) {
                didwin = true;
            }
        }
    }

    public void getAteBonus() {
        if (i > 0 && ateBonus) {
            ((SuperPacmanArea) getOwnerArea()).playerAteBonus();
            --i;
        } else if (i == 0 && ateBonus) {
            i = 200;
            ateBonus = false;
            ((SuperPacmanArea)getOwnerArea()).playerFinishBonus();
        }
    }

    public void changedAreas() {
        if (getOwnerArea() != tempArea) {
            tempArea = getOwnerArea();
            removed = false;
            ateBonus = false;
            numDiamondsCollected = 0;
            areas_visited.add((SuperPacmanArea)getOwnerArea());
        }
    }

    public void changedGeneralAreas(){
        this.setEaten(false);
        this.setSpeedruntest(false);
        this.setRegulartest(false);
        this.setMainmenu(false);
        this.setWon(false);
    }

    class SuperPacmanPlayerHandler implements SuperPacmanInteractionVisitor {

        public void acceptInteraction(Interactable other) {
            other.acceptInteraction(this);
        }

        public void interactWith(Door door) {
            if(!(getOwnerArea() instanceof StartScreen || getOwnerArea() instanceof GameOver || getOwnerArea() instanceof Congrats))
            {
                ((SuperPacmanArea) getOwnerArea()).createAreaAfterDark();

            }
            setIsPassingADoor(door);
        }

        public void interactWith(Bonus bonus) {
            ateBonus = true;
            getOwnerArea().unregisterActor(bonus);

        }

        public void interactWith(Cherry cherry) {
            initialscore = initialscore + cherry.getScore();
            getOwnerArea().unregisterActor(cherry);
        }

        public void interactWith(Diamond diamond) {
            initialscore = initialscore + 1;
            ++numDiamondsCollected;
            getOwnerArea().unregisterActor(diamond);
        }

        public void interactWith(Key key) {
            switch (getOwnerArea().getTitle()) {
                case "superpacman/Level0":
                    getOwnerArea().unregisterActor(key);
                    getOwnerArea().unregisterActor(key.getGate(1));
                    getOwnerArea().unregisterActor(key.getGate(2));
                    break;
                case "superpacman/Level2":
                    switch (key.getName()) {
                        case "key":
                        case "key1":
                            ((Level2) getOwnerArea()).keyGateManagement(key, "unregister");
                            getOwnerArea().unregisterActor(key);
                            break;
                        case "key2":
                            if (!((Level2) getOwnerArea()).firstKey.isOn()) {
                                getOwnerArea().unregisterActor(key);
                                ((Level2) getOwnerArea()).firstKey = Logic.TRUE;
                            }
                            if (((Level2) getOwnerArea()).secondKey.isOn()){
                                ((Level2) getOwnerArea()).keyGateManagement(key, "unregister");
                                ((Level2) getOwnerArea()).keyGateManagement(((Level2) getOwnerArea()).key3, "unregister");
                            }
                            break;
                        case "key3":
                            if (!((Level2) getOwnerArea()).secondKey.isOn()) {
                                getOwnerArea().unregisterActor(key);
                                ((Level2) getOwnerArea()).secondKey = Logic.TRUE;
                            }
                            if (((Level2) getOwnerArea()).firstKey.isOn()){
                                ((Level2) getOwnerArea()).keyGateManagement(key, "unregister");
                                ((Level2) getOwnerArea()).keyGateManagement(((Level2) getOwnerArea()).key2, "unregister");
                            }
                            break;
                    }
                    break;
            }
        }


        public void interactWith(Ghost ghost) {
            if (DiscreteCoordinates.distanceBetween(ghost.getCurrentMainCellCoordinates(), getCurrentMainCellCoordinates()) <= 1) {
                locked = true;
                switch(getOwnerArea().getTitle()){
                    case "superpacman/Level1":
                        if (ghost.isScared()) {
                            initialscore = initialscore + GHOST_SCORE;
                            ghost.setPlayerInSight(false);
                            ghost.atePlayer();
                        } else {
                            if(lives.size() == 1){
                                initialscore = 0;
                                setEaten(true);
                            }
                            if (lives.size() > 0) {
                                lives.remove(0);
                                ghost.setPlayerInSight(false);
                                resetMotion();
                                setCurrentPosition(new Vector(((Level1) getOwnerArea()).playerSpawn.x, ((Level1) getOwnerArea()).playerSpawn.y));
                                ((Level1) getOwnerArea()).resetGhosts();
                            }
                        }
                        break;
                    case "superpacman/Level2":
                        if (ghost.isScared()) {
                            initialscore = initialscore + GHOST_SCORE;
                            ghost.setPlayerInSight(false);
                            ghost.atePlayer();
                        } else {
                            if(lives.size() == 1){
                                initialscore = 0;
                                setEaten(true);
                            }
                            if (lives.size() > 0) {
                                lives.remove(0);
                                resetMotion();
                                ghost.setPlayerInSight(false);
                                setCurrentPosition(new Vector(((Level2) getOwnerArea()).playerSpawn.x, ((Level2) getOwnerArea()).playerSpawn.y));
                                ((SuperPacmanArea) getOwnerArea()).resetGhosts();
                            }
                        }
                        break;
                    case "superpacman/Leveln":
                        if (ghost.isScared()) {
                            initialscore = initialscore + GHOST_SCORE;
                            ghost.setPlayerInSight(false);
                            ghost.atePlayer();
                        } else {
                            initialscore = 0;
                            ghost.setPlayerInSight(false);
                            ((SuperPacmanArea) getOwnerArea()).getBehavior().registerActors(getOwnerArea());
                            setEaten(true);
                        }
                        break;
                }
                locked = false;
            } else {
                if (ghost instanceof Inky) {
                    ghost.setPlayerInSight(true);
                    ((Inky) ghost).handler.interactWith(this);
                }
                if (ghost instanceof Pinky) {
                    ghost.setPlayerInSight(true);
                    ((Pinky) ghost).handler.interactWith(this);
                }
            }
        }
    }
}
