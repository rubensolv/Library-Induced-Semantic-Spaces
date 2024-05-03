package ai.synthesis.ComplexDSL.LS_CFG;

import java.util.List;
import java.util.Random;

import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.ChildS;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.S;

public class S_LS extends S implements Node_LS, NoTerminal_LS {

	public S_LS() {
		// TODO Auto-generated constructor stub
		super();
	}

	public S_LS(ChildS child) {
		super(child);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Node_LS sorteiaFilho(int budget) {
		// TODO Auto-generated method stub
		int op=0;
		if(budget>=1)op=1;
		if(budget>=2)op=3;
		if(budget>=3)op=4;
		if(budget>=4)op=5;
		
		
		if(op==0)return  new Empty_LS();
		
		Random gerador = new Random();
		
		int g = gerador.nextInt(op);
		if(g==0) return new C_LS();
		if(g==1) return new S_S_LS();
		if(g==2) return new For_S_LS();
		if(g==3) return new If_B_then_S_LS();
		if(g==4) return new If_B_then_S_else_S_LS();

		return  new Empty_LS();
		
		
	}

	@Override
	public void sample(int budget) {
		// TODO Auto-generated method stub
		Node_LS child = this.sorteiaFilho(budget);
		child.sample(budget );
		this.setChild((ChildS) child);
		
	}

	public void sample(int budget,Node_LS n) {
		// TODO Auto-generated method stub
		Node_LS child = new S_S_LS();
		child.sample(budget );
		
		if(child instanceof S_S_LS) {
			Random r = new Random();
			if(r.nextFloat()>0.5)((S_S_LS)child).getRightS().setChild((ChildS) n);
			else ((S_S_LS)child).getLeftS().setChild((ChildS) n);
		//	System.out.println(n.translate() +" aki "+" "+child.translate());
		}
		if(child instanceof If_B_then_S_else_S_LS) {
			Random r = new Random();
			if(r.nextFloat()>0.5)((If_B_then_S_else_S_LS)child).getElse_S().setChild((ChildS) n);
			else ((If_B_then_S_else_S_LS)child).getThen_S().setChild((ChildS) n);
		}
		if(child instanceof If_B_then_S_LS) {
			((If_B_then_S_LS)child).getS().setChild((ChildS)n);
		}
		if(child instanceof For_S_LS) {
			 ((For_S_LS)child).getChild().setChild((ChildS)n);
		}
		this.setChild((ChildS) child);
		
	}
	
	@Override
	public void countNode(List<Node_LS> list) {
		// TODO Auto-generated method stub
		Node_LS n = (Node_LS)this.getChild();
		list.add(this);
		n.countNode(list);
	}

	@Override
	public void mutation(int node_atual, int budget,boolean descreve) {
		// TODO Auto-generated method stub
		
			if(descreve) {
				System.out.println("Mutacao \t S");
				System.out.println("Anterior \t"+this.translate());
			}
		
			Node_LS n = (Node_LS)this.getChild();
			Random r = new Random();
			if(r.nextFloat()>0.8)this.sample(budget);
			else this.sample(budget,n);
			if(descreve)System.out.println("Atual \t"+this.translate());
		
		
	}



}
