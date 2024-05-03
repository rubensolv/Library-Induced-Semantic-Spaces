package ai.competition.Bezjak;

import ai.core.AI;
import ai.core.AIWithComputationBudget;
import ai.core.ParameterSpecification;
import rts.*;
import rts.units.Unit;
import rts.units.UnitType;
import rts.units.UnitTypeTable;
import util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Main class for the bot, necessary for the contest
 */
public class Predator extends AIWithComputationBudget {

    UnitTypeTable m_utt;

    //This is the default constructor that microRTS will call:

    public Predator(UnitTypeTable utt) {

        super(-1, -1);

        m_utt = utt;

    }

    // This will be called by microRTS when it wants to create new instances of this bot (e.g., to play multiple games).

    public AI clone() {

        return new Predator(m_utt);

    }


    // This will be called once at the beginning of each new game:

    public void reset() {

    }

    // Called by microRTS at each game cycle.

    // Returns the action the bot wants to execute.

    public PlayerAction getAction(int player, GameState gs) {
        try {

            if (!gs.canExecuteAnyAction(player)) return new PlayerAction();
            PhysicalGameState pgs = gs.getPhysicalGameState();
            PlayerActionGenerator pag = new PlayerActionGenerator(gs, player);
            PlayerAction pa = new PlayerAction();
            ResourceUsage r = gs.getResourceUsage();
            pa.setResourceUsage(r);

            // Gets all Choices from Generator
            List<Pair<Unit, List<UnitAction>>> unitChoices = pag.getChoices();
            Unit selectedUnit = null;

            for (Pair<Unit, List<UnitAction>> selectedUnitWithActions : unitChoices) {
                selectedUnit = selectedUnitWithActions.m_a;
                List<UnitAction> actionsOfSelectedUnit = selectedUnitWithActions.m_b;
                UnitType type = selectedUnit.getType();
                UnitAction unitAction = null;

                switch (type.name) {
                    case BaseUnit.UNIT_BASE:
                        Base base = new Base(selectedUnit, actionsOfSelectedUnit, pgs, player);
                        unitAction = base.getNextUnitAction();
                        break;
                    case BaseUnit.UNIT_BARRACK:
                        Barracks barracks = new Barracks(selectedUnit, actionsOfSelectedUnit, pgs, player);
                        unitAction = barracks.getNextUnitAction();
                        break;
                    case BaseUnit.UNIT_WORKER:
                        Worker worker = new Worker(selectedUnit, actionsOfSelectedUnit, pgs, player);
                        unitAction = worker.getNextUnitAction();
                        break;
                    case BaseUnit.UNIT_LIGHT:
                        Light light = new Light(selectedUnit, actionsOfSelectedUnit, pgs, player);
                        unitAction = light.getNextUnitAction();
                        break;
                    case BaseUnit.UNIT_HEAVY:
                        Heavy heavy = new Heavy(selectedUnit, actionsOfSelectedUnit, pgs, player);
                        unitAction = heavy.getNextUnitAction();
                        break;
                    case BaseUnit.UNIT_RANGED:
                        Ranged ranged = new Ranged(selectedUnit, actionsOfSelectedUnit, pgs, player);
                        unitAction = ranged.getNextUnitAction();
                        break;
                }
                ResourceUsage r2 = unitAction.resourceUsage(selectedUnit, pgs);

                if (pa.getResourceUsage().consistentWith(r2, gs)) {

                    pa.getResourceUsage().merge(r2);
                    pa.addUnitAction(selectedUnit, unitAction);
                    //break;
                }
            }


            return pa;
        } catch (Exception e) {
            return new PlayerAction();
        }
    }

    public List<ParameterSpecification> getParameters() {

        return new ArrayList<>();

    }

}