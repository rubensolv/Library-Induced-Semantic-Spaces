package ai.competition.Bezjak;

import rts.PhysicalGameState;
import rts.UnitAction;
import rts.units.Unit;

import java.util.Iterator;
import java.util.List;

/**
 * Calss for share functionallity of the units (e.g amountOfenemyUnits, etc.)
 */
public class BaseUnit extends MoveController {
    public static final String UNIT_HEAVY = "Heavy";
    public static final String UNIT_LIGHT = "Light";
    public static final String UNIT_RANGED = "Ranged";
    public static final String UNIT_BASE = "Base";
    public static final String UNIT_BARRACK = "Barracks";
    public static final String UNIT_WORKER = "Worker";
    public static final String UNIT_RESOURCE = "Resource";
    int MAXIMUM_WORKERS = 5;
    int MAXIMUM_WORKES_IN_RESSOURCE_ZONE = 2;
    int MAX_AMOUNT_OF_BARRACK = 1;
    int MAX_AMOUNT_OF_HEAVY_UNITS;

    /**
     * @param unit
     * @param actions
     * @param pgs
     * @param player
     */
    public BaseUnit(Unit unit, List<UnitAction> actions, PhysicalGameState pgs, int player) {
        super(unit, actions, pgs, player);
    }

    /**
     * @return attack actions or null if there is no attack action
     */
    public List<UnitAction> getAttack() {
        return (this.actions.stream()
                .filter(action -> (action.getType() == UnitAction.TYPE_ATTACK_LOCATION))
                .toList());
    }

    /**
     * @param unitName - name of the Unit type that should be produced
     * @return produce actions or null if there are none
     */
    public List<UnitAction> getProduce(String unitName) {
        return this.actions.stream()
                .filter(action -> (action.getType() == UnitAction.TYPE_PRODUCE && action.getUnitType().name == unitName))
                .toList();
    }

    /**
     * @param unitName - name of the unit type
     * @return array with enemy unit position ([0: x, 1: y])
     */
    public int[] getEnemyUnitPosition(String unitName) {
        List<Unit> units = this.pgs.getUnits();
        Unit Base = null;
        Unit unit = null;
        Iterator iter = units.iterator();
        while (iter.hasNext()) {
            unit = (Unit) iter.next();
            if (unit.getType().name == unitName && unit.getPlayer() != this.player) {
                Base = unit;
                break;
            }
        }

        if (Base == null) {
            return null;
        }


        int[] position = new int[2];
        position[0] = Base.getX();
        position[1] = Base.getY();
        return position;
    }

    /**
     * @param unitName - name of the unit type
     * @return array with unit position of this bot ([0: x, 1: y])
     */
    public int[] getUnitPosition(String unitName) {
        List<Unit> units = this.pgs.getUnits();
        Unit Base = null;
        Unit unit = null;
        Iterator iter = units.iterator();
        while (iter.hasNext()) {
            unit = (Unit) iter.next();
            if (unit.getType().name == unitName && unit.getPlayer() == this.player) {
                Base = unit;
                break;
            }
        }

        if (Base == null) {
            return null;
        }


        int[] position = new int[2];
        position[0] = Base.getX();
        position[1] = Base.getY();
        return position;
    }

    /**
     * @param unitName - name of the unit type
     * @return amount of the unit type for this bot
     */
    public int getAmountOfUnits(String unitName) {
        List<Unit> allUnitsInGame = this.pgs.getUnits();
        Iterator iter = allUnitsInGame.iterator();
        int counter = 0;
        Unit unit;
        while (iter.hasNext()) {
            unit = (Unit) iter.next();
            if (unit.getPlayer() == this.player && unit.getType().name == unitName) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * Enemy will be attacked in a specific order. Units first will move to the enemy and then attack him.
     *
     * @return move, attack or wait unit action, depending on the state of the game.
     */
    public UnitAction attackEnemy() {
        int[] positionEnemy = getEnemyUnitPosition(UNIT_BASE);
        if (positionEnemy == null) {
            positionEnemy = getEnemyUnitPosition(UNIT_BARRACK);
        }
        if (positionEnemy == null) {
            positionEnemy = getEnemyUnitPosition(UNIT_BASE);
        }
        if (positionEnemy == null) {
            positionEnemy = getEnemyUnitPosition(UNIT_LIGHT);
        }
        if (positionEnemy == null) {
            positionEnemy = getEnemyUnitPosition(UNIT_HEAVY);
        }
        if (positionEnemy == null) {
            positionEnemy = getEnemyUnitPosition(UNIT_RANGED);
        }
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
}
