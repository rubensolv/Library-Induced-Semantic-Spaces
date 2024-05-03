package ai.synthesis.dreamcoder.ComplexDSL.IAs;


import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.Node_LS;
import java.util.HashMap;
import rts.GameState;

public interface Search {
	Node_LS run(GameState gs,int max,Node_LS j,Avaliador ava, Float epsilon_0, HashMap<String, Node_LS> library)throws Exception;
	void reset();
}
