package ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG_Condition;

import java.util.ArrayList;
import java.util.List;

import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.AIs.Interpreter;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.ChildB;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.Factory;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.Node;
import rts.GameState;
import rts.units.Unit;

public class CanHarvest implements ChildB {

	boolean value;
	
	public CanHarvest() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String translate() {
		// TODO Auto-generated method stub
		return "u.canHarvest()";
	}

	@Override
	public void interpret(GameState gs, int player, Unit u, Interpreter automato) throws Exception {
		// TODO Auto-generated method stub
		this.value = u.getType().canHarvest;

	}

	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "CanHarvest";
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
		return f.build_CanHarvest();
	}

	@Override
	public boolean equals(Node n) {
		// TODO Auto-generated method stub
		if (!(n instanceof CanHarvest)) return false;
		
		return true;
	}

	@Override
	public List<ChildB> AllCombinations(Factory f) {
		// TODO Auto-generated method stub
		CanHarvest ch = (CanHarvest) f.build_CanHarvest();
		List<ChildB> l = new ArrayList<>();
		l.add(ch);
		return l;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean clear(Node father, Factory f) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void load(List<String> list,Factory f ) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(List<String> list) {
		// TODO Auto-generated method stub
		list.add(this.getName());
	}
	
}
