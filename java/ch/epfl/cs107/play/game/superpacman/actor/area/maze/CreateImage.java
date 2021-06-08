package ch.epfl.cs107.play.game.superpacman.actor.area.maze;

import ch.epfl.cs107.play.game.areagame.io.ResourcePath;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;

public class CreateImage {

    int height;
    int width;
    public BufferedImage bufferedImage;
    public Graphics graphics;

    public static void drawMaze(int size, int bonus, int cherries, int blinky, int inky, int pinky) {
        try {
            mazeGeneration matz = new mazeGeneration(size, bonus, cherries, blinky, inky, pinky);
            matz.fillMaze(1, 1);
            CreateImage test = new CreateImage(matz.size, matz.size);
            test.mazeFiller(matz);
            String s = new File("").getAbsolutePath();
            String stemp = "";
            for (int i = 0; i < s.length(); ++i) {
                if (s.charAt(i) == '/' && s.charAt(i + 1) == 'j' && s.charAt(i + 2) == 'a') {
                    break;
                }
                stemp += s.charAt(i);
            }
            ImageIO.write(test.bufferedImage, "png", new File(stemp + "/res/images/behaviors/superpacman/leveln.png"));
        } catch (IOException e) {
            ;
        }

    }

    public int getHeight() {
        return height;

    }

    public int getWidth() {
        return width;
    }

    public CreateImage(int height, int width) {
        this.height = height;
        this.width = width;
        this.bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        graphics = bufferedImage.getGraphics();
    }

    public void imageSetter(int x, int y, Color color) {
        graphics.setColor(color);
        graphics.fillRect(x, y, 1, 1);
    }

    public void mazeFiller(mazeGeneration maze) {
        int counterBonus = maze.numBonus;
        int counterCherries = maze.numCherries;
        int counterInky = maze.numInky;
        int counterPinky = maze.numPinky;
        int counterBlinky = maze.numBlinky;

        for (int i = 0; i < maze.maze.length; ++i) {
            for (int j = 0; j < maze.maze[0].length; ++j) {

                if (i == maze.maze.length / 2 && j < 2 && false) { // to change and adapt the rest
                    imageSetter(i, j, Color.GRAY);
                    imageSetter(i - 1, j, Color.GRAY);
                } else {
                    double distance = Math.sqrt((i - 1) * (i - 1) + (j - 1) * (j - 1));
                    if (maze.maze[i][j] == 1) {
                        imageSetter(i, j, Color.WHITE);
                        if (counterBonus > 0) {
                            if (i > 0 && i < maze.maze.length - 1 && j > 0 && j < maze.maze.length - 1
                                    && distance > 2) {
                                if (maze.maze[i + 1][j] + maze.maze[i - 1][j] + maze.maze[i][j - 1]
                                        + maze.maze[i][j + 1] == 1) {
                                    imageSetter(i, j, new Color(4, 141, 253));
                                    --counterBonus;
                                }
                            }
                        } else if (counterCherries > 0) {
                            if (i > 0 && i < maze.maze.length - 1 && j > 0 && j < maze.maze.length - 1
                                    && distance > 2) {
                                if (maze.maze[i + 1][j] + maze.maze[i - 1][j] + maze.maze[i][j - 1]
                                        + maze.maze[i][j + 1] == 1) {
                                    imageSetter(i, j, new Color(255, 112, 112));
                                    --counterCherries;
                                }
                            }
                        } else if (counterBlinky > 0) {
                            if (i > 0 && i < maze.maze.length - 1 && j > 0 && j < maze.maze.length - 1
                                    && distance > 2) {
                                if (maze.maze[i + 1][j] + maze.maze[i - 1][j] + maze.maze[i][j - 1]
                                        + maze.maze[i][j + 1] == 1) {
                                    imageSetter(i, j, new Color(255, 0, 0));
                                    --counterBlinky;
                                }
                            }
                        } else if (counterInky > 0) {
                            if (i > 0 && i < maze.maze.length - 1 && j > 0 && j < maze.maze.length - 1
                                    && distance > 2) {
                                if (maze.maze[i + 1][j] + maze.maze[i - 1][j] + maze.maze[i][j - 1]
                                        + maze.maze[i][j + 1] == 1) {
                                    imageSetter(i, j, new Color(0, 204, 255));
                                    --counterInky;
                                }
                            }
                        } else if (counterPinky > 0) {
                            if (i > 0 && i < maze.maze.length - 1 && j > 0 && j < maze.maze.length - 1
                                    && distance > 2) {
                                if (maze.maze[i + 1][j] + maze.maze[i - 1][j] + maze.maze[i][j - 1]
                                        + maze.maze[i][j + 1] == 1) {
                                    imageSetter(i, j, new Color(253, 152, 203));
                                    --counterPinky;
                                }
                            }
                        }
                    } else {
                        imageSetter(i, j, Color.BLACK);
                    }
                }
            }
        }
    }
}
