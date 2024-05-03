package ai.synthesis.ComplexDSL.LS_Condition;

import java.util.List;
import java.util.Random;

import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.N;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.Type;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG_Condition.OpponentHasNumberOfUnits;
import ai.synthesis.ComplexDSL.LS_CFG.Node_LS;

public class OpponentHasNumberOfUnits_LS extends OpponentHasNumberOfUnits implements Node_LS {

	public OpponentHasNumberOfUnits_LS() {
		// TODO Auto-generated constructor stub
	}

	public OpponentHasNumberOfUnits_LS(Type type, N n) {
		super(type, n);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void sample(int budget) {
		// TODO Auto-generated method stub
		Type type = new Type();
		N n = new N();
		
		List<String> l1 = type.Rules();
		Random gerador = new Random();
		int g = gerador.nextInt(l1.size());
		type.setType(l1.get(g));
		this.setType(type);
		
		List<String> l2 = n.Rules();
		g = gerador.nextInt(l2.size());
		n.setN(l2.get(g));
		this.setN(n);
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
