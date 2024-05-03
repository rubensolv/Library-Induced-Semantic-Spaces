package ai.synthesis.ComplexDSL.Synthesis_Base.CFG;

import java.util.List;

public interface ChildC extends Node {
	public List<ChildC> AllCombinations(Factory f); 
}
