package dvt.labyrinth.actions;

import dvt.labyrinth.game.Game;
import dvt.labyrinth.Tile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * An action : put a wall on a tile
 */
public class PutWall implements ActionListener {

    // The tile
    private Tile tile;
    // The game, where the tile is
    private Game lab;

    /**
     * Constructor
     *
     * @param lab
     *          The game itself
     * @param tile
     *          The tile
     */
    public PutWall(Game lab, Tile tile){
        this.tile = tile;
        this.lab = lab;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // We need to check if tile is a wall && disable the clic && is not already occupied
        if (tile.isAWall() && !lab.isSettingWall() && !tile.isOccupied()) {
            tile.putWall();
            lab.checkWall(tile.getPosition());
            //lab.unHighlightAll(); // If we've just blocked the player
        }
    }
}

