package ai.competition.Bezjak;

import rts.PhysicalGameState;
import rts.UnitAction;
import rts.units.Unit;

import java.util.Iterator;
import java.util.List;

/**
 * Light class to determin action for the light units
 */
public class Light extends BaseUnit {
    /**
     * @param unit
     * @param actions
     * @param pgs
     * @param player
     */
    public Light(Unit unit, List<UnitAction> actions, PhysicalGameState pgs, int player) {
        super(unit, actions, pgs, player);
    }

    /**
     * @return the next UnitAction the unit should execute
     */
    public UnitAction getNextUnitAction() {
        return attackEnemy();
    }

}

