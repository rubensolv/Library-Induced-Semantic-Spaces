package ai.synthesis.ComplexDSL.Synthesis_Base.CFG;

import java.util.List;

public interface AlmostTerminal {
	List<String> Rules();
	public String getName();
	public String getValue();
	String translate();
	AlmostTerminal Clone(Factory f);
	boolean equals(AlmostTerminal at);
	List<AlmostTerminal> AllCombinations(Factory f); 
}
