package ai.synthesis.ComplexDSL.LS_Actions;

import java.util.List;
import java.util.Random;

import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.N;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG_Actions.Harvest;
import ai.synthesis.ComplexDSL.LS_CFG.ChildC_LS;
import ai.synthesis.ComplexDSL.LS_CFG.Node_LS;

public class Harvest_LS extends Harvest implements Node_LS,ChildC_LS  {

	public Harvest_LS() {
		// TODO Auto-generated constructor stub
	}

	public Harvest_LS(N n) {
		// TODO Auto-generated constructor stub
		super(n);
	}
	
	@Override
	public void sample(int budget) {
		// TODO Auto-generated method stub
		N n = new N();
		Random gerador = new Random();
		List<String> l3 = n.Rules();
		int g = gerador.nextInt(l3.size());
		n.setN(l3.get(g));
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
