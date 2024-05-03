package ai.synthesis.dreamcoder.ComplexDSL.LS_CFG;

import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.NoTerminal;



public interface NoTerminal_LS extends NoTerminal {
	Node_LS sorteiaFilho(int budget);
}
