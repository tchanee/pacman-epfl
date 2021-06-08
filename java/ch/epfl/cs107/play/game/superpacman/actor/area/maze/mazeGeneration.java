package ch.epfl.cs107.play.game.superpacman.actor.area.maze;

import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;

import java.util.ArrayList;

public class mazeGeneration {

    int size;
    int numBonus;
    int numCherries;
    int numPinky;
    int numInky;
    int numBlinky;
    int[][] maze;

    public mazeGeneration(int size, int numBonus, int numCherries, int numBlinky, int numInky, int numPinky) {
        this.size = size;
        this.numBonus = numBonus;
        this.numPinky = numPinky;
        this.numInky = numInky;
        this.numBlinky = numBlinky;
        this.numCherries = numCherries;
        this.maze = new int[size][size];
    }

    public static void printArray(int[][] arr) {
        String s = "";
        for (int i = 0; i < arr.length; ++i) {
            for (int j = 0; j < arr[0].length; ++j) {
                s += " " + arr[i][j] + " ";
            }
            System.out.println(s);
            s = "";
        }
    }

    public void fillMaze(int startx, int starty) {
        maze[startx][starty] = 1;
        ArrayList<DiscreteCoordinates> wallList = new ArrayList<>();
        DiscreteCoordinates tempPair1 = new DiscreteCoordinates(startx + 1, starty);
        DiscreteCoordinates tempPair2 = new DiscreteCoordinates(startx, starty + 1);
        wallList.add(tempPair1);
        wallList.add(tempPair2);

        while (wallList.size() != 0) {
            int randWall = RandomGenerator.getInstance().nextInt(wallList.size());

            int condition = wallNeighborhoodvalue(
                    getWallNeighborhood(size, size, wallList.get(randWall).x, wallList.get(randWall).y));
            if (condition < 3) {
                maze[wallList.get(randWall).x][wallList.get(randWall).y] = 1;
                if ((wallList.get(randWall).x + 1 < size - 1) && (wallList.get(randWall).y < size - 1)) {
                    if (maze[wallList.get(randWall).x + 1][wallList.get(randWall).y] == 0) {
                        DiscreteCoordinates tempPair = new DiscreteCoordinates(wallList.get(randWall).x + 1,
                                wallList.get(randWall).y);
                        wallList.add(tempPair);
                    }
                }
                if ((wallList.get(randWall).x < size - 1) && (wallList.get(randWall).y - 1 > 0)) {
                    if (maze[wallList.get(randWall).x][wallList.get(randWall).y - 1] == 0) {
                        DiscreteCoordinates tempPair = new DiscreteCoordinates(wallList.get(randWall).x,
                                wallList.get(randWall).y - 1);
                        wallList.add(tempPair);
                    }
                }
                if ((wallList.get(randWall).x - 1 > 0) && (wallList.get(randWall).y < size - 1)) {
                    if (maze[wallList.get(randWall).x - 1][wallList.get(randWall).y] == 0) {
                        DiscreteCoordinates tempPair = new DiscreteCoordinates(wallList.get(randWall).x - 1,
                                wallList.get(randWall).y);
                        wallList.add(tempPair);
                    }
                }
                if ((wallList.get(randWall).x < size - 1) && (wallList.get(randWall).y + 1 < size - 1)) {
                    if (maze[wallList.get(randWall).x][wallList.get(randWall).y + 1] == 0) {
                        DiscreteCoordinates tempPair = new DiscreteCoordinates(wallList.get(randWall).x,
                                wallList.get(randWall).y + 1);
                        wallList.add(tempPair);
                    }
                }
            }
            for (int j = 0; j < wallList.size(); ++j) {
                if (wallList.get(j).x == wallList.get(randWall).x && wallList.get(j).y == wallList.get(randWall).y) {
                    wallList.remove(wallList.get(j));
                    break;
                }
            }
        }
    }

    public int[][] getWallNeighborhood(int width, int height, int i, int j) {

        int[][] wallNeighborhood = new int[3][3];
        int xx = 0;
        int yy = 0;

        if (i == width - 1 && j == height - 1) {
            yy = 2;
            xx = 1;
            for (int y = j; y > j - 2; y--) {
                for (int x = i; x > i - 2; x--) {
                    if (maze[x][y] == 1) {
                        wallNeighborhood[xx][yy] = 1;
                    }
                    xx++;
                }
                xx = 0;
                yy--;
            }
        }

        else if (i == width - 1 && j == 0) {
            xx = 1;
            for (int y = j; y < j + 2; y++) {
                for (int x = i; x > i - 2; x--) {
                    if (maze[x][y] == 1) {
                        wallNeighborhood[xx][yy] = 1;
                    }
                    xx++;
                }
                xx = 0;
                yy++;
            }
        }

        else if (j == height - 1 && i == 0) {
            xx = 1;
            yy = 2;
            for (int y = j; y > j - 2; y--) {
                for (int x = i; x < i + 2; x++) {
                    if (maze[x][y] == 1) {
                        wallNeighborhood[xx][yy] = 1;
                    }
                    xx--;
                }
                xx = 2;
                yy--;
            }
        }

        else if (i == width - 1 && j >= 1) {
            // wall is on the right
            yy = 2;
            xx = 1;
            for (int y = j - 1; y < j + 2; y++) {
                for (int x = i; x > i - 2; x--) {
                    if (maze[x][y] == 1) {
                        wallNeighborhood[xx][yy] = 1;
                    }
                    xx--;
                }
                xx = 1;
                yy--;
            }
        }

        else if (j == height - 1 && i >= 1) {
            yy = 1;
            for (int y = j; y > j - 2; y--) {
                for (int x = i - 1; x < i + 2; x++) {
                    if (maze[x][y] == 1) {
                        wallNeighborhood[xx][2 - yy] = 1;
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
                    if (maze[x][y] == 1) {
                        wallNeighborhood[xx][yy] = 1;
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
                    if (maze[x][y] == 1) {
                        wallNeighborhood[2 - xx][yy] = 1;
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
                    if (maze[x][y] == 1) {
                        wallNeighborhood[2 - xx][2 - yy] = 1;
                    }

                    xx--;
                }
                xx = 2;
                yy++;
            }
        }

        else {
            for (int y = j - 1; y < j + 2; ++y) {
                for (int x = i - 1; x < i + 2; ++x) {
                    if (maze[x][y] == 1) {
                        wallNeighborhood[xx][yy] = 1;
                    }

                    xx++;
                }
                xx = 0;
                yy++;
            }
        }
        return wallNeighborhood;
    }

    public int wallNeighborhoodvalue(int[][] neigh) {
        int temp = 0;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                temp += neigh[i][j];
            }

        }
        return temp;
    }
}
