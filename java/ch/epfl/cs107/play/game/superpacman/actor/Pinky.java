package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Path;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.actor.area.*;
import ch.epfl.cs107.play.game.superpacman.actor.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomEvent;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.*;

public class Pinky extends Ghost {

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
    private boolean debug = false;
    public final PinkyHandler handler = new PinkyHandler();


    public Pinky(Area area, Orientation orientation, DiscreteCoordinates position, String spriteName) {
        super(area, orientation, position, spriteName, true);
        this.refuged = position;

    }
    public void setPlayerInSight(boolean temp){
        playerInSight = temp;
    }
    public void update(float deltaTime) {

        if (!playerInSight && !isScared()) {

            if(pathWhenNotScared == null){
                this.targetWhenNotScared = targetPos(2, refuged);
                this.tempforUpdateNotScared = !tempforUpdateNotScared;
            }

            if (pathWhenNotScared != null && pathWhenNotScared.size() == 0) {
                this.targetWhenNotScared = targetPos(2, refuged);
                this.tempforUpdateNotScared = !tempforUpdateNotScared;
            }
            if (!tempforUpdateNotScared) {
                this.pathWhenNotScared = getNextPath(targetWhenNotScared);
                if(this.pathWhenNotScared != null){
                    this.graphicPath = new Path(this.getPosition(), new LinkedList<Orientation>(pathWhenNotScared));
                }
            }
            if (tempforUpdateNotScared) {
                if(!tempghostInteraction) {
                    this.pathWhenNotScared = getNextPath(refuged);
                }
                else{
                    tempghostInteraction = false;
                }
                if (this.pathWhenNotScared != null) {
                    this.graphicPath = new Path(this.getPosition(), new LinkedList<Orientation>(pathWhenNotScared));
                }
            }
        }
        else if (playerInSight && !isScared()){

            if (targetWhenNotScared != ((SuperPacmanArea) getOwnerArea()).playerCoordinatesGetter()) {
                this.targetWhenNotScared = ((SuperPacmanArea) getOwnerArea()).playerCoordinatesGetter();
                if (targetWhenNotScared == this.getCurrentMainCellCoordinates()) {
                    this.pathWhenNotScared = getNextPath(targetWhenNotScared);
                    this.interactWith(((SuperPacmanArea) getOwnerArea()).getPlayer());
                }
                else {
                    this.pathWhenNotScared = getNextPath(targetWhenNotScared);
                    if (this.pathWhenNotScared != null) {
                        this.graphicPath = new Path(this.getPosition(), new LinkedList<Orientation>(pathWhenNotScared));
                    }
                }

                }
            }
        else if (isScared()){
                playerInSight = false;

                if(pathWhenScared == null){
                    this.targetWhenScared = targetPos1(MAX_DISTANCE_WHEN_SCARED, ((SuperPacmanArea) getOwnerArea()).playerCoordinatesGetter());
                    this.tempforUpdateScared = !tempforUpdateScared;
                }

                if (pathWhenScared != null) {
                    if (pathWhenScared.size() == 0) {
                        this.targetWhenScared = targetPos1(MAX_DISTANCE_WHEN_SCARED, ((SuperPacmanArea) getOwnerArea()).playerCoordinatesGetter());
                        this.tempforUpdateScared = !tempforUpdateScared;
                    }
                }
                if (!tempforUpdateScared) {
                    this.pathWhenScared = getNextPath(targetWhenScared);
                    if(this.pathWhenScared != null){
                        this.graphicPath = new Path(this.getPosition(), new LinkedList<Orientation>(pathWhenScared));
                    }
                }
                if (tempforUpdateScared) {
                        if(!tempghostInteraction) {
                            this.pathWhenScared = getNextPath(refuged);
                        }
                        else{
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
        Queue<Orientation> path = ((SuperPacmanArea) getOwnerArea()).getBehavior().getAreaGraph().shortestPath(getCurrentMainCellCoordinates(), target);
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
            //it's important to note that whenever you call pathWhenNotScared.poll()
            //whether it is to print it or not it actually does it so pay attention to it
            //Also this gives us an error when the queue no longer contains anything
            //System.out.println("PInky Else");
            if (playerInSight) {
                wasScaredBefore = false;
                return pathWhenNotScared.poll();

            } else {
                int randomInt = RandomGenerator.getInstance().nextInt(4);
                return Orientation.fromInt(randomInt);
            }
        }
    }

    public DiscreteCoordinates targetPos(int radius, DiscreteCoordinates refuge) {
        int i = RandomEvent.nextInt(refuge.x + 1,refuge.x + radius);
        int j = RandomEvent.nextInt(refuge.y + 1,refuge.y + radius);
        while (true) {
            if (((SuperPacmanArea) getOwnerArea()).getBehavior().getAreaGraph().nodeExists(new DiscreteCoordinates((int) i, (int) j)) && ((SuperPacmanArea) getOwnerArea()).getBehavior().getAreaGraph().getSignal(new DiscreteCoordinates((int) i, (int) j))) {
                if(!((refuge.x - i) * (refuge.x - i) + (refuge.y - j) * (refuge.y - j) >= radius * radius)){
                    return new DiscreteCoordinates((int) i, (int) j);
                }
            }
            i = RandomEvent.nextInt(refuge.x + 1,refuge.x + radius);
            j = RandomEvent.nextInt(refuge.y + 1,refuge.y + radius);
        }
    }

    public DiscreteCoordinates targetPos1(int radius, DiscreteCoordinates refuge) {
        int i = RandomEvent.nextInt(refuge.x - radius,refuge.x + radius);
        int j = RandomEvent.nextInt(refuge.y - radius,refuge.y + radius);
        int k = 200;
        float distance = 0;
        DiscreteCoordinates max = new DiscreteCoordinates(refuge.x,refuge.y);
        while (k > 0) {
            if (((SuperPacmanArea) getOwnerArea()).getBehavior().getAreaGraph().nodeExists(new DiscreteCoordinates((int) i, (int) j)) && ((SuperPacmanArea) getOwnerArea()).getBehavior().getAreaGraph().getSignal(new DiscreteCoordinates((int) i, (int) j))) {
                if(getNextPath(((SuperPacmanArea)getOwnerArea()).playerCoordinatesGetter()) != null) {
                    if (!((refuge.x - i) * (refuge.x - i) + (refuge.y - j) * (refuge.y - j) <= radius * radius)) {
                        if (getNextPath(new DiscreteCoordinates((int) i, (int) j)) != null) {
                            DiscreteCoordinates temp = new DiscreteCoordinates((int) i, (int) j);
                            if (DiscreteCoordinates.distanceBetween(refuge, temp) > distance) {
                                max = temp;
                                distance = DiscreteCoordinates.distanceBetween(refuge, temp);
                            }
                        }
                    }
                }
                else {
                    max = new DiscreteCoordinates(refuged.x,refuged.y);
                    for(int q = refuged.x - 1; q < refuged.x + 2; q++){
                        for(int l = refuged.y - 1; l < refuged.y + 2; l++){
                            if (getNextPath(new DiscreteCoordinates((int) q, (int) l)) != null) {
                                DiscreteCoordinates temp = new DiscreteCoordinates((int) q, (int) l);
                                max = temp;
                                distance = DiscreteCoordinates.distanceBetween(refuged, temp);
                            }
                        }
                    }
                }
            }
            i = RandomEvent.nextInt(refuge.x - radius,refuge.x + radius);
            j = RandomEvent.nextInt(refuge.y - radius,refuge.y + radius);
            k--;

        }
        return max;
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (graphicPath != null && debug == true) {
            graphicPath.draw(canvas);
        }
    }
    @Override
    public void atePlayer() {
        this.wasScaredBefore = false;
        this.setCurrentPosition(new Vector(refuged.x, refuged.y));
        playerInSight = false;
        tempforUpdateScared = true;
        tempghostInteraction = true;
        if(isScared()){
            this.targetWhenScared = targetPos1(MAX_DISTANCE_WHEN_SCARED, refuged);
            this.pathWhenScared = getNextPath(targetWhenScared);
            if(pathWhenScared != null) {
                this.graphicPath = new Path(this.getPosition(), new LinkedList<Orientation>(pathWhenScared));
            }
        }
        else {
            this.targetWhenNotScared = targetPos(2, refuged);
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
    public List<DiscreteCoordinates> getFieldOfViewCells() { return null; }

    public boolean wantsViewInteraction() {
        return true;
    }

    public void acceptInteraction(AreaInteractionVisitor v) {
        ((SuperPacmanInteractionVisitor) v).interactWith(this);
    }


    class PinkyHandler extends GhostHandler {

        public void interactWith(SuperPacmanPlayer.SuperPacmanPlayerHandler pacmanPlayer){
            playerInSight = true;
        }
    }
}