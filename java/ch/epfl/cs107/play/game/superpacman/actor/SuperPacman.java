package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.rpg.RPG;
import ch.epfl.cs107.play.game.superpacman.actor.area.*;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public class SuperPacman extends RPG {

    public String getTitle() {
        return "Super Pac-Man";
    }

    private DiscreteCoordinates PLAYER_SPAWN_POSITIONS[] = {new DiscreteCoordinates(1, 1), new DiscreteCoordinates(10, 1),
            new DiscreteCoordinates(15, 6), new DiscreteCoordinates(15, 29) , new DiscreteCoordinates(1,18),new DiscreteCoordinates(1, 1),new DiscreteCoordinates(1, 1)};

    private SuperPacmanPlayer player;

    private final String[] areas = { "superpacman/StartScreen", "superpacman/Level0", "superpacman/Level1", "superpacman/Level2", "superpacman/Leveln","superpacman/GameOver","superpacman/Congrats" };

    private final DiscreteCoordinates[] startingPositions = { new DiscreteCoordinates(2, 10),
            new DiscreteCoordinates(5, 15) };

    private int areaIndex;

    /**
     * Adds all the areas
     */

    private void createAreas() {


        addArea(new Level0());
        addArea(new Level1());
        addArea(new Level2());
        addArea(new Leveln());
        addArea(new GameOver());
        addArea(new StartScreen());
        addArea(new Congrats());

    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {

        if (super.begin(window, fileSystem)) {

            createAreas();
            areaIndex = 0;
            Area area = setCurrentArea(areas[areaIndex], true);
            /**
            When you have ghosts it takes some time to load things, it's probably the squareroot; but to view
             **/
            //If you want to start the player at a different area make sure to put normallevel = true
            if(areaIndex == 0) {
                player = new SuperPacmanPlayer(getCurrentArea(), PLAYER_SPAWN_POSITIONS[areaIndex]);
                ((SuperPacmanArea) area).playerCoordinatesSetter(PLAYER_SPAWN_POSITIONS[areaIndex]);
                ((SuperPacmanArea) area).setPlayer(player);
                initPlayer(player,false);
            }
            return true;

        }
        return false;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    public void end() {
    }

}
