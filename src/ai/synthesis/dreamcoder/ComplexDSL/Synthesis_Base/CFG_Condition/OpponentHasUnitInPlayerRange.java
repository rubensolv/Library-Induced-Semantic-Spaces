package ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG_Condition;

import java.util.ArrayList;
import java.util.List;

import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.AIs.Interpreter;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.ChildB;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.Factory;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.Node;
import rts.GameState;
import rts.PhysicalGameState;
import rts.units.Unit;

public class OpponentHasUnitInPlayerRange implements ChildB {

	boolean value;
	public OpponentHasUnitInPlayerRange() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String translate() {
		// TODO Auto-generated method stub
		return "u.OpponentHasUnitInPlayerRange()";
	}

	@Override
	public void interpret(GameState gs, int player, Unit u, Interpreter automato) throws Exception {
		// TODO Auto-generated method stub
		PhysicalGameState pgs = gs.getPhysicalGameState();
	       
		   
		value = false;
    	if(u.getPlayer()==player) {
    	
			 for(Unit u2:pgs.getUnits()) {
				if (u2.getPlayer() >= 0 && u2.getPlayer() != player) {
		
		            int dx = u2.getX() - u.getX();
		            int dy = u2.getY() - u.getY();
		            double d = Math.sqrt(dx * dx + dy * dy);
		
		            //If satisfies, an action is applied to that unit. Units that not satisfies will be set with
		            // an action wait.
		            if ((d <= u.getAttackRange())) {
		
		                value = true;
		                return;
		            }
		        }
			 }
    	}

	}

	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "OpponentHasUnitInPlayerRange";
	}

	@Override
	public String translateIndentation(int tap) {
		// TODO Auto-generated method stub
		return this.translate();
	}

	@Override
	public boolean getValue() {
		// TODO Auto-generated method stub
		return value;
	}

	@Override
	public Node Clone(Factory f) {
		// TODO Auto-generated method stub
		return f.build_OpponentHasUnitInPlayerRange();
	}

	@Override
	public boolean equals(Node n) {
		// TODO Auto-generated method stub
		if (!(n instanceof OpponentHasUnitInPlayerRange)) return false;
		return true;
	}

	@Override
	public List<ChildB> AllCombinations(Factory f) {
		// TODO Auto-generated method stub
		OpponentHasUnitInPlayerRange aux = (OpponentHasUnitInPlayerRange) f.build_OpponentHasUnitInPlayerRange();
		List<ChildB> l = new ArrayList<>();
		l.add(aux);
		return l;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean clear(Node father,Factory f) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void load(List<String> list,Factory f) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(List<String> list) {
		// TODO Auto-generated method stub
		list.add(this.getName());
	}
	
}
