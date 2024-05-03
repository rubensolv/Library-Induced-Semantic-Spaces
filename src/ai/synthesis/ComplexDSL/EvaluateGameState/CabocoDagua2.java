package ai.synthesis.ComplexDSL.EvaluateGameState;

import java.util.List;


import rts.GameState;
import rts.PlayerAction;
import rts.UnitAction;
import rts.units.Unit;
import util.Pair;

public class CabocoDagua2 implements EvaluateGS {

	public int worker;
	int light;
	int ranged;
	int heavy;
	int base;
	int barrack;
	int saved_resource;
	
	
	
	public CabocoDagua2() {
		this.worker=0;
		this.light=0;
		this.ranged=0;
		this.heavy=0;
		this.base=0;
		this.barrack=0;
		this.saved_resource=0;
	}
	public CabocoDagua2(int w,int l,int r,int h, int ba, int br, int re) {
		this.worker=w;
		this.light=l;
		this.ranged=r;
		this.heavy=h;
		this.base=ba;
		this.barrack=br;
		this.saved_resource=re;
	}
	
	
	public CabocoDagua2(List<Integer> list) {
		this.worker=list.get(0);
		this.light=list.get(1);
		this.ranged=list.get(2);
		this.heavy=list.get(3);
		this.base=list.get(4);
		this.barrack=list.get(5);
		this.saved_resource=list.get(6);
		
	}
	
	
	
	public void imprimir() {
		System.out.println("Worker "+ this.worker);
		System.out.println("Light "+ this.light);
		System.out.println("Ranged "+ this.ranged);
		System.out.println("Heavy "+ this.heavy);
		System.out.println("Base "+ this.base);
		System.out.println("Barrack "+ this.barrack);
		System.out.println("Saved_resource "+ this.saved_resource);
	}

	
	public void evaluate(PlayerAction pa, int play) {
		// TODO Auto-generated method stub
		for(Pair<Unit,UnitAction> actions :pa.getActions()) {
			//System.out.println(actions.m_a.getType().name+" "+actions.m_b.getActionName());
			if(actions.m_b.getActionName().equals("return") ) {
				this.saved_resource++;
			}
			if(actions.m_b.getActionName().equals("produce") ) {
				if(actions.m_b.getUnitType().name.equals("Worker")) {
					this.worker++;
				}
				if(actions.m_b.getUnitType().name.equals("Light")) {
					this.light++;
				}
				if(actions.m_b.getUnitType().name.equals("Heavy")) {
					this.heavy++;
				}
				if(actions.m_b.getUnitType().name.equals("Base")) {
					this.base++;
				}
				if(actions.m_b.getUnitType().name.equals("Barracks")) {
					this.barrack++;
				}
				if(actions.m_b.getUnitType().name.equals("Ranged")) {
					this.ranged++;
				}
			}
		}
	}

	double diffType(int a,int b) {
		if(a==0&&b==0)return 0;
		
		double delta = Math.abs( a-b);
		return delta /Math.max(a,b);
	}
	
	
	@Override
	public void Resert() {
		// TODO Auto-generated method stub
		this.worker=0;
		this.light=0;
		this.ranged=0;
		this.heavy=0;
		this.base=0;
		this.barrack=0;
		this.saved_resource=0;

	}


	@Override
	public void evaluate(GameState gs, int play) {
		// TODO Auto-generated method stub
		
		
	}




	public int getWorker() {
		return worker;
	}




	public void setWorker(int worker) {
		this.worker = worker;
	}




	public int getLight() {
		return light;
	}




	public void setLight(int light) {
		this.light = light;
	}




	public int getRanged() {
		return ranged;
	}




	public void setRanged(int ranged) {
		this.ranged = ranged;
	}




	public int getHeavy() {
		return heavy;
	}




	public void setHeavy(int heavy) {
		this.heavy = heavy;
	}




	public int getBase() {
		return base;
	}




	public void setBase(int base) {
		this.base = base;
	}




	public int getBarrack() {
		return barrack;
	}




	public void setBarrack(int barrack) {
		this.barrack = barrack;
	}




	public int getSaved_resource() {
		return saved_resource;
	}




	public void setSaved_resource(int saved_resource) {
		this.saved_resource = saved_resource;
	}

	
	
}
