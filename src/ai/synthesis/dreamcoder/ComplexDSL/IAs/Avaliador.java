package ai.synthesis.dreamcoder.ComplexDSL.IAs;

import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.Node_LS;
import rts.GameState;

public interface Avaliador {	
	double Avalia(GameState gs,int max,Node_LS n) throws Exception;
	void update(GameState gs,int max,Node_LS n) throws Exception;
	Node_LS getIndividuo();
	Node_LS getBest();
	boolean criterioParada(double d);
        int getBudget();
        int getTotalIndividuos();
}
