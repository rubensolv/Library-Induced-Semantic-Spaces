package ai.synthesis.ComplexDSL.Synthesis_Base.AIs;

import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.Node;
import rts.GameState;
import util.Pair;

public interface Search {
	public Pair<Node, Node> run(GameState gs,int max_cicle) throws Exception;
}
