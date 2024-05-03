package ai.synthesis.ComplexDSL.LS_Condition;

import java.util.List;
import java.util.Random;

import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.Type;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG_Condition.is_Type;
import ai.synthesis.ComplexDSL.LS_CFG.Node_LS;

public class Is_Type_LS extends is_Type implements Node_LS {

	public Is_Type_LS() {
		// TODO Auto-generated constructor stub
	}

	public Is_Type_LS(Type type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void sample(int budget) {
		// TODO Auto-generated method stub
		Type type = new Type();
		
		
		List<String> l1 = type.Rules();
		Random gerador = new Random();
		int g = gerador.nextInt(l1.size());
		type.setType(l1.get(g));
		this.setType(type);
		
		
	}

	@Override
	public void countNode(List<Node_LS> list) {
		// TODO Auto-generated method stub
	
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
