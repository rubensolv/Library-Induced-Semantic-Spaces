package ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG;

import java.util.List;

public interface NoTerminal {
		List<Node> rules(Factory f);
		Node getRule();
		void setRule(Node n);
		
}
