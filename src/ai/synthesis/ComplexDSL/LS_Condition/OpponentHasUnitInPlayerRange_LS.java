package ai.synthesis.ComplexDSL.LS_Condition;

import java.util.List;

import ai.synthesis.ComplexDSL.Synthesis_Base.CFG_Condition.OpponentHasUnitInPlayerRange;
import ai.synthesis.ComplexDSL.LS_CFG.Node_LS;

public class OpponentHasUnitInPlayerRange_LS extends OpponentHasUnitInPlayerRange implements Node_LS {

	public OpponentHasUnitInPlayerRange_LS() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void sample(int budget) {
		// TODO Auto-generated method stub

	}

	@Override
	public void countNode(List<Node_LS> list) {
		// TODO Auto-generated method stub
		list.add(this);
	}
	
	@Override
	public void mutation(int node_atual, int budget,boolean desc) {
		// TODO Auto-generated method stub
		
		if(desc) {
			System.out.println("Mutacao \t "+this.getName());
			System.out.println("Anterior \t"+this.translate());
		}
		this.sample(budget);
		
		if(desc) {
			System.out.println("Atual \t"+this.translate());
		}
	}
	
}
