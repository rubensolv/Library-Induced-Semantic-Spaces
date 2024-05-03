package ai.synthesis.ComplexDSL.LS_CFG;

import ai.synthesis.ComplexDSL.LS_Actions.Train_LS;
import ai.synthesis.ComplexDSL.LS_Actions.Build_LS;
import ai.synthesis.ComplexDSL.LS_Actions.Idle_LS;
import ai.synthesis.ComplexDSL.LS_Actions.moveToUnit_LS;
import ai.synthesis.ComplexDSL.LS_Actions.MoveAway_LS;
import ai.synthesis.ComplexDSL.LS_Actions.Attack_LS;
import ai.synthesis.ComplexDSL.LS_Actions.Harvest_LS;
import java.util.List;
import java.util.Random;

import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.C;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.ChildC;

public class C_LS extends C implements Node_LS, NoTerminal_LS {

	public C_LS() {
		// TODO Auto-generated constructor stub
	}

	public C_LS(ChildC childC) {
		super(childC);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Node_LS sorteiaFilho(int budget) {
		// TODO Auto-generated method stub
		
		Random gerador = new Random();
		
		int g = gerador.nextInt(7);

		if(g==0) return new Attack_LS();
		if(g==1) return new Build_LS();
		if(g==2) return new Harvest_LS();
		if(g==3) return new Idle_LS();
		if(g==4) return new MoveAway_LS();
		if(g==5) return new moveToUnit_LS();
		if(g==6) return new Train_LS();
		
		
		return null;
	}

	@Override
	public void sample(int budget) {
		// TODO Auto-generated method stub
		Node_LS child = this.sorteiaFilho(budget);
		child.sample(budget );
		this.setChildC((ChildC)child);
	}

	@Override
	public void countNode(List<Node_LS> list) {
		// TODO Auto-generated method stub
		Node_LS n2 = (Node_LS)this.getChildC();
		list.add(this);
		 
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
