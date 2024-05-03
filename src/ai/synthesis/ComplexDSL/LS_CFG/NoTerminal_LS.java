package ai.synthesis.ComplexDSL.LS_CFG;

import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.NoTerminal;

public interface NoTerminal_LS extends NoTerminal {
	Node_LS sorteiaFilho(int budget);
}
