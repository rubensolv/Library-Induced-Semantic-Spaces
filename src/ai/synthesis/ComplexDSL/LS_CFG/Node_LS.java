package ai.synthesis.ComplexDSL.LS_CFG;

import java.util.List;

import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.Node;

public interface Node_LS extends Node {
	void sample(int budget);
	void countNode(List<Node_LS> list);
	void mutation(int node_atual,int budget,boolean descreve);
	
}
