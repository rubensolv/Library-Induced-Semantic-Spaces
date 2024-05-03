package ai.competition.Bezjak;

import rts.PhysicalGameState;
import rts.UnitAction;
import rts.units.Unit;

import java.util.List;
import java.util.Random;

/**
 * Generall class to control the movements of the units (e.g. move to enemy, move up, etc.)
 */
public class MoveController {
    Unit unit;
    List<UnitAction> actions;
    PhysicalGameState pgs;
    int player;

    /**
     * @param unit
     * @param actions
     * @param pgs
     * @param player
     */
    public MoveController(Unit unit, List<UnitAction> actions, PhysicalGameState pgs, int player) {
        this.pgs = pgs;
        this.unit = unit;
        this.actions = actions;
        this.player = player;
    }

    /**
     * @return move up unit actions or null
     */
    public List<UnitAction> getMoveUp() {
        return this.actions.stream()
                .filter(action -> (action.getType() == UnitAction.TYPE_MOVE && action.getDirection() == UnitAction.DIRECTION_UP))
                .toList();
    }

    /**
     * @return move down unit actions or null
     */
    public List<UnitAction> getMoveDown() {
        return this.actions.stream()
                .filter(action -> (action.getType() == UnitAction.TYPE_MOVE && action.getDirection() == UnitAction.DIRECTION_DOWN))
                .toList();
    }

    /**
     * @return move right unit actions or null
     */
    public List<UnitAction> getMoveRight() {
        return this.actions.stream()
                .filter(action -> (action.getType() == UnitAction.TYPE_MOVE && action.getDirection() == UnitAction.DIRECTION_RIGHT))
                .toList();
    }

    /**
     * @return move left unit actions or null
     */
    public List<UnitAction> getMoveLeft() {
        return this.actions.stream()
                .filter(action -> (action.getType() == UnitAction.TYPE_MOVE && action.getDirection() == UnitAction.DIRECTION_LEFT))
                .toList();
    }

    /**
     * @return wait unit actions or null
     */
    public List<UnitAction> waitAction() {
        return (this.actions.stream()
                .filter(action -> (action.getType() == UnitAction.TYPE_NONE))
                .toList());

    }

    /**
     * moves to a position. If x and y movement is possible, the movement will be selected randomly
     *
     * @param x
     * @param y
     * @return move unit actions or null
     */
    public UnitAction moveToPosition(int x, int y) {
        Random rand = new Random();
        int takeX = rand.nextInt(2);
        if (takeX == 1) {
            if (this.unit.getX() < x && !this.getMoveRight().isEmpty()) {
                return this.getMoveRight().get(0);
            } else if (this.unit.getX() > x && !this.getMoveLeft().isEmpty()) {
                return this.getMoveLeft().get(0);
            }
        }

        if (this.unit.getY() < y && !this.getMoveDown().isEmpty()) {
            return this.getMoveDown().get(0);
        } else if (this.unit.getY() > y && !this.getMoveUp().isEmpty()) {
            return this.getMoveUp().get(0);
        } else if (this.unit.getX() < x && !this.getMoveRight().isEmpty()) {
            return this.getMoveRight().get(0);
        } else if (this.unit.getX() > x && !this.getMoveLeft().isEmpty()) {
            return this.getMoveLeft().get(0);
        } else {
            UnitAction ua = this.waitAction().get(0);
            return ua;
        }
    }

}
