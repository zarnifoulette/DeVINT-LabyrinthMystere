package dvt.labyrinth.model.player;

import static dvt.labyrinth.tools.ConstantesLabyrinth.*;

import dvt.labyrinth.tools.ConstantesLabyrinth;
import dvt.labyrinth.tools.Position;
import dvt.labyrinth.model.essential.Tray;
import dvt.labyrinth.model.essential.Pawn;
import javafx.geometry.Pos;

import java.util.Queue;

/**
 * Created by user on 21/03/2016.
 */
public class IAMedium extends AdvancedIAs{
    private int counter =0;

    public IAMedium(Pawn pawn, Position pos, Tray tray){
        super(pawn,pos,tray);
    }

    /**
     * The IA moves and positions walls in front of the other player
     * @param tray
     * @param directions the direction we want to move to
     * @param position the position of the other player
     * @return if the IA has performed the action successfully or not
     */
    @Override
    public boolean moveAndWall(Tray tray, DIRECTIONS directions, Position position){
        counter++;
        if(position.getX()<NBRE_CASES-CASE_LENGTH && counter%6==0) {
            tray.getTile(new Position(position.getX(), position.getY() - 1)).positionWall();
            tray.getTile(new Position(position.getX() + 1, position.getY() - 1)).positionWall();
            tray.getTile(new Position(position.getX() + 2, position.getY() - 1)).positionWall();
            return true;
        }
        else if (counter%6==0) {
            tray.getTile(new Position(position.getX(),position.getY() - 1)).positionWall();
            tray.getTile(new Position(position.getX() - 1, position.getY() - 1)).positionWall();
            tray.getTile(new Position(position.getX() - 2, position.getY() - 1)).positionWall();
            return true;
        }
        return move(tray, directions);
    }

    /**
     * The IA moves
     * @param tray
     * @param directions the direction we want to move to
     * @return
     */
    @Override
    public boolean move(Tray tray, ConstantesLabyrinth.DIRECTIONS directions){
        decision.add(ConstantesLabyrinth.DIRECTIONS.BACK);
        if ((!decision.isEmpty())) {
            if (canMove(tray,decision.peek())){
                updatePlayerPos(tray,convertDirectionToPosition(decision.peek()));
                decision.poll();
                return true;
            }

            else {
                decision.clear();
                strategyIA(tray);
                move(tray,null);
                return true;
            }
        }
        else {
            strategyIA(tray);
            move(tray,null);
            return true;
        }
    }


}
