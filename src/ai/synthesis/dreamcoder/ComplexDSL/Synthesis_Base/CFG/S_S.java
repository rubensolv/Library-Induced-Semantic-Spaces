package ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG;


import java.util.List;

import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.AIs.Interpreter;
import rts.GameState;
import rts.units.Unit;

public class S_S implements ChildS {

	S leftS;
	S rightS;
	
	public S_S() {
		// TODO Auto-generated constructor stub
		leftS= new S();
		rightS = new S();
	}

	
	
	
	public S_S(S leftS, S rightS) {
		super();
		this.leftS = leftS;
		this.rightS = rightS;
	}


	


	public S getLeftS() {
		return leftS;
	}




	public void setLeftS(S leftS) {
		this.leftS = leftS;
	}




	public S getRightS() {
		return rightS;
	}




	public void setRightS(S rightS) {
		this.rightS = rightS;
	}




	@Override
	public String translate() {
		// TODO Auto-generated method stub
		return this.leftS.translate()+" "+this.rightS.translate();
	}

	@Override
	public void interpret(GameState gs, int player, Unit u, Interpreter automato) throws Exception {
		// TODO Auto-generated method stub
		this.leftS.interpret(gs, player, u, automato);
		this.rightS.interpret(gs, player, u, automato);
	}

	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "S_S";
	}

	@Override
	public String translateIndentation(int tap) {
		// TODO Auto-generated method stub
		return this.leftS.translateIndentation(tap)+"\n"+this.rightS.translateIndentation(tap);
	}




	@Override
	public Node Clone(Factory f) {
		// TODO Auto-generated method stub
		return f.build_S_S((S)this.leftS.Clone(f),(S)this.rightS.Clone(f));
	}




	@Override
	public boolean equals(Node n) {
		// TODO Auto-generated method stub
		if(!(n instanceof S_S) )return false;
		S_S s_s = (S_S)n;
	
		return this.leftS.equals(s_s.leftS)  && this.rightS.equals(s_s.rightS) ;
	}




	@Override
	public void reset() {
		// TODO Auto-generated method stub
		if(this.leftS!=null)this.leftS.reset();
		if(this.rightS!=null)this.rightS.reset();
		
	}





	@Override
	public boolean clear(Node father,Factory f) {
		// TODO Auto-generated method stub
		boolean childwasuse1 = this.leftS.clear(this,f);
		boolean childwasuse2 = this.rightS.clear(this,f);
		if(!childwasuse1 && !childwasuse2) {				
			S aux = (S)father;
			aux.childS= (ChildS) f.build_Empty();
			return false;
		}
		
		if(!childwasuse1 ) {				
			S aux = (S)father;
			aux.childS=this.rightS.childS;
			return true;
		}
		if(!childwasuse2 ) {				
			S aux = (S)father;
			aux.childS=this.leftS.childS;
			return true;
		}
		
		return true; 
	}




	@Override
	public void load(List<String> list,Factory f) {
		// TODO Auto-generated method stub
		String s = list.get(0);
		list.remove(0);
		Node n = Control.aux_load(s, f);
		n.load(list, f);
		this.leftS = (S) n;
		String s1 = list.get(0);
		list.remove(0);
		Node n1 = Control.aux_load(s1, f);
		n1.load(list, f);
		this.rightS = (S) n1;
	}




	@Override
	public void save(List<String> list) {
		// TODO Auto-generated method stub
		list.add(this.getName());
		this.leftS.save(list);
		this.rightS.save(list);
	}
	
}
