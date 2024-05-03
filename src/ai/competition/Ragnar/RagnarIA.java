/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ai.competition.Ragnar;

import ai.core.AI;
import ai.core.AIWithComputationBudget;
import ai.core.ParameterSpecification;
import java.util.List;
import rts.GameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;

/**
 *
 * @author Quazi
 */
public class RagnarIA extends AIWithComputationBudget{
    
    AI ragnar = null;
    UnitTypeTable utt = null;
    public RagnarIA(UnitTypeTable utt) {
        super(-1, -1);
        this.utt = utt;
        this.ragnar = new Ragnar(utt);
    }

    @Override
    public void reset() {        
        ragnar.reset(utt);
    }

    @Override
    public PlayerAction getAction(int player, GameState gs) throws Exception {
        return ragnar.getAction(player, gs);
    }

    @Override
    public AI clone() {
        ragnar.reset(utt);
        return ragnar;
    }

    @Override
    public List<ParameterSpecification> getParameters() {
        return null;
    }

    @Override
    public String toString() {
        return "Ragnar";
    }
    
    
    
}
