package ai.synthesis.ComplexDSL.LS_Actions;

import java.util.List;
import java.util.Random;

import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.Direction;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.N;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.Type;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG_Actions.Train;
import ai.synthesis.ComplexDSL.LS_CFG.ChildC_LS;
import ai.synthesis.ComplexDSL.LS_CFG.Node_LS;

public class Train_LS extends Train implements Node_LS,ChildC_LS {

	public Train_LS() {
		// TODO Auto-generated constructor stub
	}

	public Train_LS(Type type, Direction direc, N n) {
		super(type, direc,n);
		// TODO Auto-generated constructor stub
	}

	public void sample(int budget,int ggg) {
		// TODO Auto-generated method stub
		Type type = new Type();
		Direction direc = new Direction();
		N n = new N();
		Random gerador = new Random();
		
		
		if(ggg==0) {
			List<String> l1 = type.Rules();
			int g = gerador.nextInt(l1.size());
			type.setType(l1.get(g));
			this.setType(type);
		}
		if(ggg==1) {
			List<String> l2 = direc.Rules();
			int g = gerador.nextInt(l2.size());
			direc.setDirection(l2.get(g));
			this.setDirec(direc);
		}
		
		if(ggg==2) {
			List<String> l3 = n.Rules();
			int g = gerador.nextInt(l3.size());
			n.setN(l3.get(g));
			this.setN(n);
		}
	}
	
	@Override
	public void sample(int budget) {
		// TODO Auto-generated method stub
		Type type = new Type();
		Direction direc = new Direction();
		N n = new N();
		Random gerador = new Random();
		int ggg = gerador.nextInt(3);
		
		if(ggg==0||this.getType().getType()==null) {
			List<String> l1 = type.Rules();
			int g = gerador.nextInt(l1.size());
			type.setType(l1.get(g));
			this.setType(type);
		}
		if(ggg==1||this.getDirec().getDirection()==null) {
			List<String> l2 = direc.Rules();
			int g = gerador.nextInt(l2.size());
			direc.setDirection(l2.get(g));
			this.setDirec(direc);
		}
		
		if(ggg==2||this.getN().getN()==null) {
			List<String> l3 = n.Rules();
			int g = gerador.nextInt(l3.size());
			n.setN(l3.get(g));
			this.setN(n);
		}
	}

	@Override
	public void countNode(List<Node_LS> list) {
		// TODO Auto-generated method stub
		list.add(this);
	}
	@Override
	public void mutation(int node_atual, int budget,boolean desc) {
		// TODO Auto-generated method stub
		
		if(desc) {
			System.out.println("Mutacao \t "+this.getName());
			System.out.println("Anterior \t"+this.translate());
		}
		this.sample(budget);
		
		if(desc) {
			System.out.println("Atual \t"+this.translate());
		}
	}
}
