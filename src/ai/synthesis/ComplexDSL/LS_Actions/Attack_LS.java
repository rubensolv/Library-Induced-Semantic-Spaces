package ai.synthesis.ComplexDSL.LS_Actions;

import java.util.List;
import java.util.Random;

import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.OpponentPolicy;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG_Actions.Attack;
import ai.synthesis.ComplexDSL.LS_CFG.ChildC_LS;
import ai.synthesis.ComplexDSL.LS_CFG.Node_LS;

public class Attack_LS extends Attack implements Node_LS,ChildC_LS {

	public Attack_LS() {
		// TODO Auto-generated constructor stub
	}

	public Attack_LS(OpponentPolicy oP) {
		super(oP);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void sample(int budget) {
		// TODO Auto-generated method stub
		OpponentPolicy op = new OpponentPolicy();
		List<String> l = op.Rules();
	
		Random gerador = new Random();
		int g = gerador.nextInt(l.size());
		
		op.setOpponentPolicy(l.get(g));
		this.setOP(op);
		
		
		
	}

	@Override
	public void countNode(List<Node_LS> list) {
		// TODO Auto-generated method stub
		list.add(this);
	}

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
