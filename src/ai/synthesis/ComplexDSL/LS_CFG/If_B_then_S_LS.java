package ai.synthesis.ComplexDSL.LS_CFG;

import java.util.List;

import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.B;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.If_B_then_S;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.S;

public class If_B_then_S_LS extends If_B_then_S implements Node_LS {

	public If_B_then_S_LS() {
		// TODO Auto-generated constructor stub
		super();
	}

	public If_B_then_S_LS(B b, S s) {
		super(b, s);
		// TODO Auto-generated constructor stub
	}

	

	@Override
	public void sample(int budget) {
		// TODO Auto-generated method stub
		B_LS b = new B_LS();
		b.sample(1);
		this.setB(b);
		S_LS s1 = new S_LS();
		s1.sample(budget-2);
		this.setS(s1);
		
	}

	@Override
	public void countNode(List<Node_LS> list) {
		// TODO Auto-generated method stub
		Node_LS n1 = (Node_LS)this.getB();
		Node_LS n2 = (Node_LS)this.getS();
		list.add(this);
		
		n1.countNode(list);
		n2.countNode(list);
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
