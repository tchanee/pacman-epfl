package ch.epfl.cs107.play.game.rpg;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.superpacman.actor.area.Leveln;
import ch.epfl.cs107.play.game.superpacman.actor.area.StartScreen;
import ch.epfl.cs107.play.game.superpacman.actor.area.SuperPacmanArea;
import ch.epfl.cs107.play.game.superpacman.actor.area.SuperPacmanBehavior;
import ch.epfl.cs107.play.game.superpacman.actor.area.maze.CreateImage;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;


/**
 * Roll Play Game is a concept of Game derived for AreaGame. It introduce the notion of Player
 * When initializing the player is added to the current area
 */
public abstract class RPG extends AreaGame {

    /// The player is a concept of RPG games
    private Player player;

    /**
     * Init the player for the current area and insert it
     * @param player (Player): controlled player of this RPG, not null
     * @throws NullPointerException if the player is null
     */
    protected void initPlayer(Player player, boolean normallevel){

        if(player == null){
            throw new NullPointerException("player is null when adding it to area");
        }
        this.player = player;

        getCurrentArea().registerActor(this.player);
        if(normallevel) {
            getCurrentArea().setViewCandidate(this.player);
        }
    }

    protected Player getPlayer(){
        return player;
    }

    /// RPG implements Playable

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        return super.begin(window, fileSystem);
    }

    @Override
    public void update(float deltaTime) {

        // Implementation of the teleport
        if(player != null && player.getRegulartest()){
            player.leaveArea();
            Area area = setCurrentArea("superpacman/Level0", false);
            player.enterArea(area, new DiscreteCoordinates(10,1));
        }
        else if(player != null && player.getSpeedruntest()){
            player.leaveArea();
            Area area = setCurrentArea("superpacman/Leveln", false);
            player.enterArea(area, new DiscreteCoordinates(1,18));
        }
        else if(player != null && player.getMainmenu()){
            player.leaveArea();
            Area area = setCurrentArea("superpacman/StartScreen", false);
            player.enterArea(area, new DiscreteCoordinates(1,1));
        }
        else if(player != null && player.getEaten()){
            player.leaveArea();
            Area area = setCurrentArea("superpacman/GameOver", false);
            player.enterArea(area, new DiscreteCoordinates(1,1));
        }
        else if(player != null && player.getWon()){
            player.leaveArea();
            player.setWon(false);
            Area area = setCurrentArea("superpacman/Congrats", false);
            player.enterArea(area, new DiscreteCoordinates(1,1));
        }
        else if(player != null && player.isPassingADoor()){
            Door door = player.passedDoor();
            player.leaveArea();
            Area area = setCurrentArea(door.getDestination(), false);
            player.enterArea(area, door.getOtherSideCoordinates());
        }

        super.update(deltaTime);
    }

    @Override
    public void end() {
    }
}
