package ai.synthesis.ComplexDSL.Synthesis_Base.CFG;


import java.util.List;

import ai.synthesis.ComplexDSL.Synthesis_Base.AIs.Interpreter;
import rts.GameState;
import rts.units.Unit;

public interface Node {
	String translate();
	void interpret(GameState gs,int player, Unit u, Interpreter automato) throws Exception;
	
	boolean isComplete();
	
	String getName();
	String translateIndentation(int tap);
	Node Clone(Factory f);
	boolean equals(Node n);
	void resert();
	boolean clear(Node father,Factory f);
	void load(List<String> list,Factory f);
	void salve(List<String> list);
}
