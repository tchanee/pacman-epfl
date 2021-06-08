package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Path;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.superpacman.actor.area.*;
import ch.epfl.cs107.play.game.superpacman.actor.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomEvent;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.*;

public class Inky extends Ghost {

    private DiscreteCoordinates refuged;
    private DiscreteCoordinates targetWhenScared = new DiscreteCoordinates(15, 6);
    private DiscreteCoordinates targetWhenNotScared = new DiscreteCoordinates(15, 6);
    private Queue<Orientation> pathWhenScared = new ArrayDeque<>();
    private Queue<Orientation> pathWhenNotScared = new ArrayDeque<>();
    private final int MAX_DISTANCE_WHEN_SCARED = 5;
    private final int MAX_DISTANCE_WHEN_NOT_SCARED = 10;
    private Path graphicPath;
    private int temp = 0;
    private boolean tempforUpdateScared = true;
    public boolean playerInSight = false;
    private boolean tempforUpdateNotScared = true;
    private boolean wasScaredBefore = false;
    private boolean tempghostInteraction = false;
    private boolean started = true;
    private boolean debug = false;
    public final InkyHandler handler = new InkyHandler();

    public Inky(Area area, Orientation orientation, DiscreteCoordinates position, String spriteName) {
        super(area, orientation, position, spriteName, true);
        this.refuged = position;

    }

    public void setPlayerInSight(boolean temp){
        playerInSight = temp;
    }

    public void update(float deltaTime) {

        if (!playerInSight && !isScared()) {

            if (pathWhenNotScared == null) {
                this.targetWhenNotScared = targetPos(MAX_DISTANCE_WHEN_NOT_SCARED, refuged);
                this.tempforUpdateNotScared = !tempforUpdateNotScared;
            }

            if (pathWhenNotScared != null) {
                if (pathWhenNotScared.size() == 0) {
                    this.targetWhenNotScared = targetPos(MAX_DISTANCE_WHEN_NOT_SCARED, refuged);
                    this.tempforUpdateNotScared = !tempforUpdateNotScared;
                }
            }
            if (!tempforUpdateNotScared) {
                this.pathWhenNotScared = getNextPath(targetWhenNotScared);
                if (this.pathWhenNotScared != null) {
                    this.graphicPath = new Path(this.getPosition(), new LinkedList<Orientation>(pathWhenNotScared));
                }
            }
            if (tempforUpdateNotScared) {
                if (!tempghostInteraction) {
                    this.pathWhenNotScared = getNextPath(refuged);
                } else {
                    tempghostInteraction = false;
                }
                if (this.pathWhenNotScared != null) {
                    this.graphicPath = new Path(this.getPosition(), new LinkedList<Orientation>(pathWhenNotScared));
                }
            }
        } else if (playerInSight && !isScared()) {

            if (targetWhenNotScared != ((SuperPacmanArea) getOwnerArea()).playerCoordinatesGetter()) {
                this.targetWhenNotScared = ((SuperPacmanArea) getOwnerArea()).playerCoordinatesGetter();
                if (targetWhenNotScared == this.getCurrentMainCellCoordinates()) {
                    this.pathWhenNotScared = getNextPath(targetWhenNotScared);
                    this.interactWith(((SuperPacmanArea) getOwnerArea()).getPlayer());
                } else {
                    this.pathWhenNotScared = getNextPath(targetWhenNotScared);
                    if (this.pathWhenNotScared != null) {
                        this.graphicPath = new Path(this.getPosition(), new LinkedList<Orientation>(pathWhenNotScared));
                    }
                }
            }
        } else if (isScared()) {
            playerInSight = false;
            if (pathWhenScared == null) {
                this.targetWhenScared = targetPos(MAX_DISTANCE_WHEN_SCARED, refuged);
                this.tempforUpdateScared = !tempforUpdateScared;
            }

            if (pathWhenScared != null) {
                if (pathWhenScared.size() == 0) {
                    this.targetWhenScared = targetPos(MAX_DISTANCE_WHEN_SCARED, refuged);
                    this.tempforUpdateScared = !tempforUpdateScared;
                }
            }
            if (!tempforUpdateScared) {
                this.pathWhenScared = getNextPath(targetWhenScared);
                if (this.pathWhenScared != null) {
                    this.graphicPath = new Path(this.getPosition(), new LinkedList<Orientation>(pathWhenScared));
                }
            }
            if (tempforUpdateScared) {

                if (!tempghostInteraction) {
                    this.pathWhenScared = getNextPath(refuged);
                } else {
                    tempghostInteraction = false;
                }
                if (this.pathWhenScared != null) {
                    this.graphicPath = new Path(this.getPosition(), new LinkedList<Orientation>(pathWhenScared));
                }
            }
        }

        super.update(deltaTime);
    }

    public Queue<Orientation> getNextPath(DiscreteCoordinates target) {
        Queue<Orientation> path = ((SuperPacmanArea) getOwnerArea()).getBehavior().getAreaGraph()
                .shortestPath(getCurrentMainCellCoordinates(), target);
        return path;
    }

    public Orientation getNextOrientation() {
        if (isScared()) {
            playerInSight = false;
            if (!wasScaredBefore) {
                wasScaredBefore = true;
                resetMotion();
            }
            return pathWhenScared.poll();
        } else {
            wasScaredBefore = false;
            return pathWhenNotScared.poll();
        }
    }

    public DiscreteCoordinates targetPos(int radius, DiscreteCoordinates refuge) {
        int i = RandomEvent.nextInt(refuge.x + 1, refuge.x + radius);
        int j = RandomEvent.nextInt(refuge.y + 1, refuge.y + radius);
        while (true) {
            if (((SuperPacmanArea) getOwnerArea()).getBehavior().getAreaGraph()
                    .nodeExists(new DiscreteCoordinates((int) i, (int) j))
                    && ((SuperPacmanArea) getOwnerArea()).getBehavior().getAreaGraph()
                            .getSignal(new DiscreteCoordinates((int) i, (int) j))) {
                if (!((refuge.x - i) * (refuge.x - i) + (refuge.y - j) * (refuge.y - j) >= radius * radius)) {
                    return new DiscreteCoordinates((int) i, (int) j);
                }
            }
            i = RandomEvent.nextInt(refuge.x + 1, refuge.x + radius);
            j = RandomEvent.nextInt(refuge.y + 1, refuge.y + radius);
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (graphicPath != null && debug == true) {
            graphicPath.draw(canvas);
        }
    }

    @Override
    public void atePlayer() {
        this.resetMotion();
        this.setCurrentPosition(new Vector(refuged.x, refuged.y));
        playerInSight = false;
        tempforUpdateScared = true;
        tempghostInteraction = true;
        if (isScared()) {
            this.targetWhenScared = targetPos(MAX_DISTANCE_WHEN_SCARED, refuged);
            this.pathWhenScared = getNextPath(targetWhenScared);
            if (pathWhenScared != null) {
                this.graphicPath = new Path(this.getPosition(), new LinkedList<Orientation>(pathWhenScared));
            }
        } else {
            this.targetWhenNotScared = targetPos(MAX_DISTANCE_WHEN_NOT_SCARED, refuged);
            this.pathWhenNotScared = getNextPath(targetWhenNotScared);
            if (pathWhenNotScared != null) {
                this.graphicPath = new Path(this.getPosition(), new LinkedList<Orientation>(pathWhenNotScared));
            }
        }

    }

    public void interactWith(Interactable other) {
        handler.interactWith(other);
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
                .nodeExists(new DiscreteCoordinates(temp.x, temp.y)));

        return cells;
    }

    public boolean wantsViewInteraction() {
        return true;
    }

    public void acceptInteraction(AreaInteractionVisitor v) {
        ((SuperPacmanInteractionVisitor) v).interactWith(this);
    }

    class InkyHandler extends GhostHandler {

        public void interactWith(SuperPacmanPlayer.SuperPacmanPlayerHandler pacmanPlayer) {
            playerInSight = true;
        }
    }
}