package ai.synthesis.ComplexDSL.Synthesis_Base.CFG;

import java.util.List;

public interface ChildB extends Node {
	boolean getValue();
	public List<ChildB> AllCombinations(Factory f); 
}
