package dvt.labyrinth.game;

import dvt.devint.Jeu;
import dvt.labyrinth.Position;
import dvt.labyrinth.Tile;
import dvt.labyrinth.Tray;
import dvt.labyrinth.actions.MovePlayerAction;

import dvt.labyrinth.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static dvt.labyrinth.ConstantesLabyrinth.*;

/**
 * The Labyrinth class.
 * It's the game itself.
 *
 * @author Arnaud, Thomas, Etienne & Adrian
 */
public class TwoPlayers extends Jeu {
    // The 'world' : Labyrinth
    private JPanel world;
    // The tray of the labyrinth
    private Tray tray;
    // The players
    private Player[] players;
    // The current player
    private Player currentPlayer;

    @Override
    public void init() {
        world = new JPanel();
        world.setBackground(getBackground());

        this.add(world);

        world.setLayout(new GridBagLayout());

        tray = new Tray();

        // Keep players
        addPlayers();

        showTray();

//        win();
    }

    @Override
    public void update() {
        if (currentPlayer.hasWon())
            win();

        checkMovePositions();
    }

    /**
     * Check where we can move, and highlight all the tiles (if we can move)
     */
    public void checkMovePositions() {
        int x = currentPlayer.getPosition().getX();
        int y = currentPlayer.getPosition().getY();

        Tile tile[][] = tray.getTray();
        if (currentPlayer.canMove(tray, DIRECTIONS.LEFT))
            tile[y][x - CASE_LENGTH].setHighlighted(new Arrow(RESSOURCES.ARROW_LEFT)); // Left
        if (currentPlayer.canMove(tray, DIRECTIONS.RIGHT))
            tile[y][x+CASE_LENGTH].setHighlighted(new Arrow(RESSOURCES.ARROW_RIGHT)); // Right
        if (currentPlayer.canMove(tray, DIRECTIONS.FRONT))
            tile[y-CASE_LENGTH][x].setHighlighted(new Arrow(RESSOURCES.ARROW_UP)); // Down
        if (currentPlayer.canMove(tray, DIRECTIONS.BACK))
            tile[y + CASE_LENGTH][x].setHighlighted(new Arrow(RESSOURCES.ARROW_DOWN)); // Up

        setTarget();

        addControlUp(KeyEvent.VK_DOWN, new MovePlayerAction(this, DIRECTIONS.BACK));
        addControlUp(KeyEvent.VK_UP, new MovePlayerAction(this, DIRECTIONS.FRONT));
        addControlUp(KeyEvent.VK_LEFT, new MovePlayerAction(this, DIRECTIONS.LEFT));
        addControlUp(KeyEvent.VK_RIGHT, new MovePlayerAction(this, DIRECTIONS.RIGHT));
    }

    public void setTarget() {
        Tile tile[] = tray.getTray()[currentPlayer.getWonY()];

        for (int x = 0; x < tile.length; x+=2) {
            if (!tile[x].isOccupied())
                tile[x].setHighlighted(new Target(), Color.WHITE);
        }
    }

    @Override
    public void render() {
        world.setBackground(getBackground());
        // TODO : changer la valeur de la couleur des borders quand fond noir
    }

    @Override
    public void reset() {
    }

    /**
     * Display the grid/tray
     */
    public void showTray() {
        Tile tile[][] = tray.getTray();

        for (int y = 0; y < tile.length; y++) {
            for (int x = 0; x < tile[y].length; x++) {

                if (tile[y][x].isAWall()) { // Walls
                    tile[y][x].getComponent().setSize(new Dimension(10,(y%2 == 1) ? 10 : 50));
                    tile[y][x].setListenerWall(this);
                }
                else // 'Moves' tiles
                    tile[y][x].getComponent().setPreferredSize(new Dimension(105, 50));

                world.add(tile[y][x].getComponent(), getGridBagConstraints(x,y));
            }
        }
    }

    /**
     * Display process (GridBagLayout)
     *
     * @param x
     *          The x coordinate (grid)
     * @param y
     *          The y coordinate (grid)
     *
     * @return the constraint (layout)
     */
    public GridBagConstraints getGridBagConstraints(int x, int y) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.CENTER;
        c.gridx = x;
        c.gridy = y;

        if (y % 2 == 1) // Wall
            c.gridwidth = (x % 2 == 0) ? 2 : 1; // Remove the small square
        else
            c.gridwidth = 1;

        c.gridheight = 1;

        c.weightx = 1.0;
        c.weighty = 1.0;

        c.fill = GridBagConstraints.BOTH;

        return c;
    }

    /**
     * Create players
     */
    public void addPlayers() {
        players = new Player[2];

        players[0] = new Player("Joueur 1", new Pawn(RESSOURCES.THEO), new Position(8,0), tray);
        players[1] = new Player("Joueur 2", new Pawn(RESSOURCES.GERARD), new Position(8, NBRE_CASES-1), tray);

        currentPlayer = players[0];
    }

    /**
     * Method to unlight all the tiles near the player
     */
    public void unHighlightAll() {
        for (int y = 0; y < tray.getTray().length; y++) {
            for (int x = 0; x < tray.getTray()[y].length; x++) {
                if (tray.getTray()[y][x].isHighlighted())
                    tray.getTray()[y][x].unHighlight();
            }
        }
    }

    /**
     * Move the player
     *
     * @param d a direction where to move
     */
    public void movePlayer(DIRECTIONS d) {
        unHighlightAll();
        if (currentPlayer.move(tray, d)) { // has moved
            if (currentPlayer.hasWon())
                return;

            currentPlayer = (currentPlayer == players[0]) ? players[1] : players[0];
        }
    }

    public void win() {
        this.dispose(); // Delete this view
        new WinGame(currentPlayer).loop(); // Add win view
    }
}
