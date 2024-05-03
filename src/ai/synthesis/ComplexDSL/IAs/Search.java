package ai.synthesis.ComplexDSL.IAs;

import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.Node;
import rts.GameState;

public interface Search {
	Node run(GameState gs,int max_cicle,int lado) throws Exception;
}
