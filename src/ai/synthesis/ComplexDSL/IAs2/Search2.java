package ai.synthesis.ComplexDSL.IAs2;


import ai.synthesis.ComplexDSL.LS_CFG.Node_LS;
import rts.GameState;

public interface Search2 {
	Node_LS run(GameState gs,int max,Node_LS j,Avaliador ava)throws Exception;
	void resert();
}
