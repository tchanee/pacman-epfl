package ch.epfl.cs107.play.game.superpacman.actor.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.AreaGraph;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.actor.*;
import ch.epfl.cs107.play.game.tutos.area.Tuto2Behavior;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SuperPacmanBehavior extends AreaBehavior {

    private int numDiamonds;
    private boolean reseted = false;
    private ArrayList<Ghost> ghosts = new ArrayList<>();
    private AreaGraph areaGraph = new AreaGraph();

    public enum SuperPacmanCellType {

        NONE(0, false), // Never used as real content
        WALL(-16777216, false), // black
        FREE_WITH_DIAMOND(-1, true), // white
        FREE_WITH_BLINKY(-65536, true), // red
        FREE_WITH_PINKY(-157237, true), // pink
        FREE_WITH_INKY(-16724737, true), // cyan
        FREE_WITH_CHERRY(-36752, true), // light red
        FREE_WITH_BONUS(-16478723, true), // light blue
        FREE_EMPTY(-6118750, true); // sort of grey

        final int type;
        final boolean isWalkable;

        SuperPacmanCellType(int type, boolean isWalkable) {
            this.type = type;
            this.isWalkable = isWalkable;
        }

        public static SuperPacmanCellType toType(int type) {
            for (SuperPacmanCellType ict : SuperPacmanCellType.values()) {
                if (ict.type == type)
                    return ict;
            }
            // When you add a new color, you can print the int value here before assign it
            // to a type
            return NONE;
        }
    }

    public AreaGraph getAreaGraph() {
        return this.areaGraph;
    }

    public SuperPacmanBehavior(Window window, String name) {
        super(window, name);
        int height = getHeight();
        int width = getWidth();
        this.numDiamonds = 0;
        boolean tempLeftEdge = false;
        boolean tempRightEdge = false;
        boolean tempUpEdge = false;
        boolean tempDownEdge = false;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                SuperPacmanCellType type = SuperPacmanCellType.toType(getRGB(height - 1 - y, x));
                setCell(x, y, new SuperPacmanBehavior.SuperPacmanCell(x, y, type));
                if (type != SuperPacmanCellType.WALL) {
                    if (x > 0) {
                        SuperPacmanCellType left = SuperPacmanCellType.toType(getRGB(height - 1 - y, x - 1));
                        if (left != SuperPacmanCellType.WALL) {
                            tempLeftEdge = true;
                        }
                    }
                    if (x < width - 1) {
                        SuperPacmanCellType right = SuperPacmanCellType.toType(getRGB(height - 1 - y, x + 1));
                        if (right != SuperPacmanCellType.WALL) {
                            tempRightEdge = true;

                        }
                    }
                    if (y > 0) {
                        SuperPacmanCellType down = SuperPacmanCellType.toType(getRGB(height - y, x));
                        if (down != SuperPacmanCellType.WALL) {
                            tempDownEdge = true;

                        }
                    }
                    if (y < height - 1) {
                        SuperPacmanCellType up = SuperPacmanCellType.toType(getRGB(height - y - 1 - 1, x));
                        if (up != SuperPacmanCellType.WALL) {
                            tempUpEdge = true;

                        }
                    }
                    areaGraph.addNode(new DiscreteCoordinates(x, y), tempLeftEdge, tempUpEdge, tempRightEdge,
                            tempDownEdge);
                    tempDownEdge = false;
                    tempLeftEdge = false;
                    tempUpEdge = false;
                    tempRightEdge = false;
                }

            }
        }
    }

    public void registerActors(Area area) {
        for (int y = 0; y < area.getHeight(); y++) {
            for (int x = 0; x < area.getWidth(); x++) {
                SuperPacmanCellType type = SuperPacmanCellType.toType(getRGB(area.getHeight() - 1 - y, x));
                if (type == SuperPacmanCellType.WALL) {
                    Wall wall = new Wall(area, new DiscreteCoordinates(x, y), getWallNeighborhood(area, x, y));
                    if (area.canEnterAreaCells(wall,Collections.singletonList(new DiscreteCoordinates(x,y)))) {
                        area.registerActor(wall);
                    }
                } else if (type == SuperPacmanCellType.FREE_WITH_BONUS) {
                    Bonus bonus = new Bonus(area, Orientation.UP, new DiscreteCoordinates(x, y));
                    if (area.canEnterAreaCells(bonus,Collections.singletonList(new DiscreteCoordinates(x,y)))) {
                        area.registerActor(bonus);
                    }
                } else if (type == SuperPacmanCellType.FREE_WITH_CHERRY) {
                    Cherry cherry = new Cherry(area, Orientation.UP, new DiscreteCoordinates(x, y));
                    if (area.canEnterAreaCells(cherry,Collections.singletonList(new DiscreteCoordinates(x,y)))) {
                        area.registerActor(cherry);
                    }
                } else if (type == SuperPacmanCellType.FREE_WITH_DIAMOND) {
                    Diamond diamond = new Diamond(area, Orientation.UP, new DiscreteCoordinates(x, y));
                    if (area.canEnterAreaCells(diamond,Collections.singletonList(new DiscreteCoordinates(x,y)))) {
                        area.registerActor(diamond);
                    }
                    this.numDiamonds++;
                } else if (type == SuperPacmanCellType.FREE_WITH_INKY) {
                    Inky inky = new Inky(area, Orientation.UP, new DiscreteCoordinates(x, y), "superpacman/ghost.inky");
                    if (area.canEnterAreaCells(inky,Collections.singletonList(new DiscreteCoordinates(x,y)))) {
                        area.registerActor(inky);
                    }
                    ghosts.add(inky);
                } else if (type == SuperPacmanCellType.FREE_WITH_BLINKY) {
                    Blinky blinky = new Blinky(area, Orientation.UP, new DiscreteCoordinates(x, y),
                            "superpacman/ghost.blinky");
                    if (area.canEnterAreaCells(blinky,Collections.singletonList(new DiscreteCoordinates(x,y)))) {
                        area.registerActor(blinky);
                    }
                    ghosts.add(blinky);
                } else if (type == SuperPacmanCellType.FREE_WITH_PINKY) {
                    Pinky pinky = new Pinky(area, Orientation.UP, new DiscreteCoordinates(x, y),
                            "superpacman/ghost.pinky");
                    if (area.canEnterAreaCells(pinky,Collections.singletonList(new DiscreteCoordinates(x,y)))) {
                        area.registerActor(pinky);
                    }
                    ghosts.add(pinky);
                }
            }
        }
    }

    public boolean[][] getWallNeighborhood(Area area, int i, int j) {

        boolean wallNeighborhood[][] = new boolean[3][3];
        int xx = 0;
        int yy = 0;

        if (i == area.getWidth() - 1 && j == area.getHeight() - 1) {
            yy = 2;
            xx = 1;
            for (int y = j; y > j - 2; y--) {
                for (int x = i; x > i - 2; x--) {
                    SuperPacmanCellType type = SuperPacmanCellType.toType(getRGB(area.getHeight() - 1 - y, x));
                    if (type == SuperPacmanCellType.WALL) {
                        wallNeighborhood[xx][yy] = true;
                    }
                    xx++;
                }
                xx = 0;
                yy--;
            }
        }

        else if (i == area.getWidth() - 1 && j == 0) {
            xx = 1;
            for (int y = j; y < j + 2; y++) {
                for (int x = i; x > i - 2; x--) {
                    SuperPacmanCellType type = SuperPacmanCellType.toType(getRGB(area.getHeight() - 1 - y, x));
                    if (type == SuperPacmanCellType.WALL) {
                        wallNeighborhood[xx][yy] = true;
                    }
                    xx++;
                }
                xx = 0;
                yy++;
            }
        }

        else if (j == area.getHeight() - 1 && i == 0) {
            xx = 1;
            yy = 2;
            for (int y = j; y > j - 2; y--) {
                for (int x = i; x < i + 2; x++) {
                    SuperPacmanCellType type = SuperPacmanCellType.toType(getRGB(area.getHeight() - 1 - y, x));
                    if (type == SuperPacmanCellType.WALL) {
                        wallNeighborhood[xx][yy] = true;
                    }
                    xx--;
                }
                xx = 2;
                yy--;
            }
        }

        else if (i == area.getWidth() - 1 && j >= 1) {
            // wall is on the right
            yy = 2;
            xx = 1;
            for (int y = j - 1; y < j + 2; y++) {
                for (int x = i; x > i - 2; x--) {
                    SuperPacmanCellType type = SuperPacmanCellType.toType(getRGB(area.getHeight() - 1 - y, x));
                    if (type == SuperPacmanCellType.WALL) {
                        wallNeighborhood[xx][yy] = true;
                    }
                    xx--;
                }
                xx = 1;
                yy--;
            }
        }

        else if (j == area.getHeight() - 1 && i >= 1) {
            yy = 1;
            for (int y = j; y > j - 2; y--) {
                for (int x = i - 1; x < i + 2; x++) {
                    SuperPacmanCellType type = SuperPacmanCellType.toType(getRGB(area.getHeight() - 1 - y, x));
                    if (type == SuperPacmanCellType.WALL) {
                        wallNeighborhood[xx][2 - yy] = true;
                    }
                    xx++;
                }
                xx = 0;
                yy--;
            }
        }

        else if (i == 0 && j == 0) {
            xx = 1;
            for (int y = j; y < j + 2; y++) {
                for (int x = i; x < i + 2; x++) {
                    SuperPacmanCellType type = SuperPacmanCellType.toType(getRGB(area.getHeight() - 1 - y, x));
                    if (type == SuperPacmanCellType.WALL) {
                        wallNeighborhood[xx][yy] = true;
                    }
                    xx--;
                }
                xx = 2;
                yy++;
            }
        }

        else if (i == 0 && j >= 1) {
            xx = 1;
            yy = 2;
            for (int y = j - 1; y < j + 2; y++) {
                for (int x = i; x < i + 2; x++) {
                    SuperPacmanCellType type = SuperPacmanCellType.toType(getRGB(area.getHeight() - 1 - y, x));
                    if (type == SuperPacmanCellType.WALL) {
                        wallNeighborhood[2 - xx][yy] = true;
                    }

                    xx--;
                }
                xx = 1;
                yy--;
            }
        }

        else if (j == 0 && i >= 1) {
            xx = 2;
            yy = 1;
            for (int y = j; y < j + 2; y++) {
                for (int x = i - 1; x < i + 2; x++) {
                    SuperPacmanCellType type = SuperPacmanCellType.toType(getRGB(area.getHeight() - 1 - y, x));
                    if (type == SuperPacmanCellType.WALL) {
                        wallNeighborhood[2 - xx][2 - yy] = true;
                    }

                    xx--;
                }
                xx = 2;
                yy++;
            }
        }

        else {
            yy = 2;
            for (int y = j - 1; y < j + 2; y++) {
                for (int x = i - 1; x < i + 2; x++) {
                    SuperPacmanCellType type = SuperPacmanCellType.toType(getRGB(area.getHeight() - 1 - y, x));
                    if (type == SuperPacmanCellType.WALL) {
                        wallNeighborhood[xx][yy] = true;
                    }

                    xx++;
                }
                xx = 0;
                yy--;
            }
        }

        return wallNeighborhood;
    }

    public int getNumDiamonds() {
        return numDiamonds;
    }

    public SuperPacmanCellType type(int x, int y) {
        return SuperPacmanCellType.toType(getRGB(getHeight() - 1 - y, x - 1));
    }

    public ArrayList<Ghost> getGhosts() {
        return ghosts;
    }


    public class SuperPacmanCell extends AreaBehavior.Cell {

        private final SuperPacmanCellType type;

        public SuperPacmanCell(int x, int y, SuperPacmanCellType type) {
            super(x, y);
            this.type = type;
        }

        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }

        @Override
        protected boolean canEnter(Interactable entity) {
            return !hasNonTraversableContent();
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
        }

    }
}