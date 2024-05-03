package ai.competition.Bezjak;

import rts.PhysicalGameState;
import rts.UnitAction;
import rts.units.Unit;

import java.util.Iterator;
import java.util.List;

/**
 * Worker class to determine action for the worker units
 */
public class Worker extends BaseUnit {
    /**
     * @param unit
     * @param actions
     * @param pgs
     * @param player
     */
    public Worker(Unit unit, List<UnitAction> actions, PhysicalGameState pgs, int player) {
        super(unit, actions, pgs, player);
    }

    /**
     * @return the next UnitAction the unit should execute
     */
    public UnitAction getNextUnitAction() {
        int[] positionOfBase = getUnitPosition(UNIT_BASE);
        int amountOfBarracks = this.getAmountOfUnits(UNIT_BARRACK);

        if (this.isUnitInRessurceZone()) {
            Unit closestRessource = this.getClosestRessource();
            if (!this.getHarvest().isEmpty()) {
                return this.getHarvest().get(0);
            } else if (!this.getReturn().isEmpty()) {
                return this.getReturn().get(0);
            } else if (this.isOnTheWayBackToBase()) {
                return moveToPosition(positionOfBase[0], positionOfBase[1]);
            } else {
                return moveToPosition(closestRessource.getX(), closestRessource.getY());
            }
        } else if (amountOfBarracks < this.MAX_AMOUNT_OF_BARRACK) {
            if (!(this.getProduce(UNIT_BARRACK).isEmpty())) {
                return this.getProduce(UNIT_BARRACK).get(0);
            }
        } else {

            int[] positionEnemy = getEnemyUnitPosition(UNIT_BASE);
            if (positionEnemy == null) {
                positionEnemy = getEnemyUnitPosition(UNIT_WORKER);
            }

            int enemyX = positionEnemy[0];
            int enemyY = positionEnemy[1];

            if (!this.getAttack().isEmpty()) {
                return this.getAttack().get(0);
            } else {
                return this.moveToPosition(enemyX, enemyY);
            }

        }

        if (!(this.waitAction().isEmpty())) {
            return this.waitAction().get(0);
        }

        return null;
    }
    
    /**
     * @return true if the unit is in a rectangle between base and resource
     */
    public boolean isUnitInRessurceZone() {
        int[] positionOfBase = this.getUnitPosition(UNIT_BASE);
        Unit closestResource = this.getClosestRessource();

        int XBorderLeft = (positionOfBase[0] > closestResource.getX()) ? closestResource.getX() : positionOfBase[0];
        int XBorderRight = (positionOfBase[0] < closestResource.getX()) ? closestResource.getX() : positionOfBase[0];
        int YBorderTop = (positionOfBase[1] > closestResource.getY()) ? closestResource.getY() : positionOfBase[1];
        int YBorderBottom = (positionOfBase[1] < closestResource.getY()) ? closestResource.getY() : positionOfBase[1];
        if (this.unit.getX() >= XBorderLeft && this.unit.getX() <= XBorderRight
                && this.unit.getY() >= YBorderTop && this.unit.getY() <= YBorderBottom) {
            return true;
        }
        // Just for one player so far !!!!!!!!!!!
        return false;
    }

    /**
     * @return the closest resource object
     */
    public Unit getClosestRessource() {
        int[] positionOfBase = this.getUnitPosition(UNIT_BASE);
        Unit unit = null;
        int diffX = this.pgs.getWidth();
        int diffY = this.pgs.getHeight();

        List<Unit> allUnits = this.pgs.getUnits();
        Iterator iter = allUnits.iterator();

        while (iter.hasNext()) {
            Unit u = (Unit) iter.next();
            if (u.getType().name == UNIT_RESOURCE) {
                int newX = positionOfBase[0] - u.getX();
                int newY = positionOfBase[1] - u.getY();
                if (newX < 0) {
                    newX *= -1;
                }
                if (newY < 0) {
                    newY *= -1;
                }
                if (unit == null) {
                    unit = u;
                    diffX = newX;
                    diffY = newY;
                } else {
                    if (newX <= diffX || newY <= diffY) {
                        unit = u;
                        diffX = newX;
                        diffY = newY;
                    }
                }
            }
        }

        return unit;
    }

    /**
     * @return harvest UnitAction or null
     */
    public List<UnitAction> getHarvest() {
        return (this.actions.stream()
                .filter(action -> (action.getType() == UnitAction.TYPE_HARVEST))
                .toList());
    }

    /**
     * @return return UnitAction or null
     */
    public List<UnitAction> getReturn() {
        return (this.actions.stream()
                .filter(action -> (action.getType() == UnitAction.TYPE_RETURN))
                .toList());
    }

    /**
     * @return true if the collected a ressource
     */
    public boolean isOnTheWayBackToBase() {
        return this.unit.getResources() == 1;
    }


}
