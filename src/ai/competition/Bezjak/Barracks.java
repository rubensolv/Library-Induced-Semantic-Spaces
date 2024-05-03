package ai.competition.Bezjak;

import rts.PhysicalGameState;
import rts.ResourceUsage;
import rts.UnitAction;
import rts.units.Unit;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Barracks class to determine action for the barrack units
 */
public class Barracks extends BaseUnit {
    /**
     * @param unit
     * @param actions
     * @param pgs
     * @param player
     */
    public Barracks(Unit unit, List<UnitAction> actions, PhysicalGameState pgs, int player) {
        super(unit, actions, pgs, player);
        int needsAdditionalUnit = (this.pgs.getWidth() % 2 == 0) ? -1 : 0;
        MAX_AMOUNT_OF_HEAVY_UNITS = this.pgs.getWidth() / 4 + needsAdditionalUnit;
    }


    /**
     * @return the next UnitAction the unit should execute
     */
    public UnitAction getNextUnitAction() {
        Iterator<UnitAction> iter = this.actions.iterator();
        UnitAction action = null;
        String unitProductionTarget = getNextUnitToProduce();
        while (iter.hasNext()) {
            action = iter.next();
            if (action.getType() == UnitAction.TYPE_PRODUCE) {
                String unitType = action.getUnitType().name;
                if (unitType.equals(unitProductionTarget)) {
                    return action;
                }
            }
        }
        if (action == null) {
            return new UnitAction(UnitAction.TYPE_NONE);
        }
        ResourceUsage barrackUsage = action.resourceUsage(unit, pgs);
        if (barrackUsage.getResourcesUsed(player) > 0) {
            return new UnitAction(UnitAction.TYPE_NONE);
        } else {
            return action;
        }
    }

    /**
     * @return name of the Unit that the barrack should produce next
     */
    public String getNextUnitToProduce() {
        Random random = new Random();
        int randomInt = random.nextInt(6);

        if (getAmountOfUnits(UNIT_HEAVY) < MAX_AMOUNT_OF_HEAVY_UNITS) {
            if (randomInt < 3) {
                return UNIT_LIGHT;
            } else if (randomInt == 5) {
                return UNIT_RANGED;
            } else {
                return UNIT_HEAVY;
            }
        } else {
            if (randomInt < 3) {
                return UNIT_LIGHT;
            } else {
                return UNIT_RANGED;
            }
        }
    }

}
