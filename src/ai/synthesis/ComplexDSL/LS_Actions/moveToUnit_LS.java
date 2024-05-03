package ai.synthesis.ComplexDSL.LS_Actions;

import java.util.List;
import java.util.Random;

import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.OpponentPolicy;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.TargetPlayer;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG_Actions.moveToUnit;
import ai.synthesis.ComplexDSL.LS_CFG.ChildC_LS;
import ai.synthesis.ComplexDSL.LS_CFG.Node_LS;

public class moveToUnit_LS extends moveToUnit implements Node_LS,ChildC_LS {

	public moveToUnit_LS() {
		// TODO Auto-generated constructor stub
	}

	public moveToUnit_LS(TargetPlayer tagetplayer, OpponentPolicy oP) {
		super(tagetplayer, oP);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void sample(int budget) {
		// TODO Auto-generated method stub
		TargetPlayer tagetplayer = new TargetPlayer();
		OpponentPolicy oP = new OpponentPolicy();
		
		List<String> l1 = tagetplayer.Rules();
		Random gerador = new Random();
		int g = gerador.nextInt(l1.size());
		tagetplayer.setValue(l1.get(g));
		this.setTagetplayer(tagetplayer);
		
		List<String> l2 = oP.Rules();
		g = gerador.nextInt(l2.size());
		oP.setOpponentPolicy(l2.get(g));
		this.setOP(oP);
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
