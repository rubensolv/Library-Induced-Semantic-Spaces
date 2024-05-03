package ai.synthesis.ComplexDSL.IAs2;

import ai.synthesis.ComplexDSL.LS_CFG.Node_LS;
import rts.GameState;

public class Algoritmo1 {

	Search2 sc;
	Avaliador ava;
	long tempo_ini;


	
	
	
	public Algoritmo1(Search2 sc, Avaliador ava) {
		// TODO Auto-generated constructor stub
		this.ava=ava;
		this.sc = sc;
	
	}
	
	
	
	
	
	public void run(GameState gs,int max) throws Exception {
		
		
		while(ava.getBudget() <= 250000) {
			long paraou = System.currentTimeMillis()-this.tempo_ini;
			
			Node_LS j = ava.getIndividuo(); 
		
			
			Node_LS  c0 = sc.run(gs, max, j, ava);

			double r0 = ava.Avalia(gs, max, c0);
			double r1 = ava.Avalia(gs, max, j);
			
			if(r0>r1)ava.update(gs, max, c0);
			
		}
		
		
	}


	
		
		
}


	


