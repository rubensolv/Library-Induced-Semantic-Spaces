package ai.synthesis.ComplexDSL.LS_CFG;

import java.util.List;
import java.util.Random;

import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.S;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.S_S;

public class S_S_LS extends S_S implements Node_LS {

	public S_S_LS() {
		// TODO Auto-generated constructor stub
	}

	public S_S_LS(S leftS, S rightS) {
		super(leftS, rightS);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void sample(int budget) {
		// TODO Auto-generated method stub
		
		
			S_LS s1 = new S_LS();
			s1.sample(budget/2);
			this.setLeftS(s1);
			S_LS s2 = new S_LS();
			s2.sample(budget/2);
			this.setRightS(s2);
		
	}

	@Override
	public void countNode(List<Node_LS> list) {
		// TODO Auto-generated method stub
		Node_LS n1 = (Node_LS)this.getLeftS();
		Node_LS n2 = (Node_LS)this.getRightS();
		list.add(this);
		 n1.countNode(list);
		 n2.countNode(list);
	}

	@Override
	public void mutation(int node_atual, int budget,boolean desc) {
		// TODO Auto-generated method stub
		
		if(desc) {
			System.out.println("Mutacao \t S_S");
			System.out.println("Anterior \t"+this.translate());
		}
		
			Random gerador = new Random();
			float g = gerador.nextFloat();
			if(g<0.90) {
				this.sample(budget);
			}else {
				S s1 = (S) this.getLeftS();
				S s2 = (S) this.getRightS();
				this.setRightS(s2);
				this.setLeftS(s1);
			}
		
			if(desc) {
				System.out.println("Atual \t"+this.translate());
			}
	}

}
