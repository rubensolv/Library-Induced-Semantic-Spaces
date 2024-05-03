package ai.competition.Bezjak;

import rts.PhysicalGameState;
import rts.UnitAction;
import rts.units.Unit;

import java.util.Iterator;
import java.util.List;

/**
 * Base class to determine action for the base units
 */
public class Base extends BaseUnit {
    /**
     * @param unit
     * @param actions
     * @param pgs
     * @param player
     */
    public Base(Unit unit, List<UnitAction> actions, PhysicalGameState pgs, int player) {
        super(unit, actions, pgs, player);
    }

    /**
     * @return the next UnitAction the unit should execute
     */
    public UnitAction getNextUnitAction() {
        Iterator iter = this.actions.iterator();
        UnitAction action = null;
        int currentAmountOfWorkers = this.getAmountOfUnits(UNIT_WORKER);
        int currentAmountOfWorkersInRessourceZone = this.getAmountOfWorkersInRessourceZone();

        if (MAXIMUM_WORKERS > currentAmountOfWorkers) {
            if (MAXIMUM_WORKES_IN_RESSOURCE_ZONE > currentAmountOfWorkersInRessourceZone) {
                action = (UnitAction) iter.next();
                if (this.player == 0) {
                    do {
                        if (action.getType() == UnitAction.TYPE_PRODUCE && action.getDirection() == UnitAction.DIRECTION_LEFT) {
                            return action;
                        }
                        action = (UnitAction) iter.next();
                    } while (iter.hasNext());
                }
                return action;
            } else {
                while (iter.hasNext()) {
                    action = (UnitAction) iter.next();
                    if (!this.getProduceOutsideOfResourceZone().isEmpty()) {
                        return this.getProduceOutsideOfResourceZone().get(0);
                    }

                }
            }
        } else {
            while (iter.hasNext()) {
                action = (UnitAction) iter.next();
                if (action.getType() == 0) {
                    return action;
                }
            }
        }
        return action;
    }

    /**
     * @return the ammount of workers outside the ressource zone
     */
    public int getAmountOfWorkersInRessourceZone() {
        List<Unit> allUnitsInGame = this.pgs.getUnits();
        Iterator iter = allUnitsInGame.iterator();
        int counter = 0;
        Unit unit;
        while (iter.hasNext()) {
            unit = (Unit) iter.next();
            Worker w = new Worker(unit, this.actions, this.pgs, this.player);
            if (unit.getPlayer() == this.player && unit.getType().name == UNIT_WORKER && w.isUnitInRessurceZone()) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * @return produce Worker unit action (outside the ressource zone) or null
     */
    public List<UnitAction> getProduceOutsideOfResourceZone() {

        return (this.actions.stream()
                .filter(action -> (this.player == 0) ?
                        (action.getType() == UnitAction.TYPE_PRODUCE && (action.getDirection() == UnitAction.DIRECTION_DOWN || action.getDirection() == UnitAction.DIRECTION_RIGHT)) :
                        (action.getType() == UnitAction.TYPE_PRODUCE && (action.getDirection() == UnitAction.DIRECTION_UP || action.getDirection() == UnitAction.DIRECTION_LEFT)))
                .toList());
    }

}
