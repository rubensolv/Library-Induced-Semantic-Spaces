package ai.synthesis.ComplexDSL.IAs2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.Factory;
import ai.synthesis.ComplexDSL.LS_CFG.FactoryLS;
import ai.synthesis.ComplexDSL.LS_CFG.Node_LS;
import rts.GameState;

public class SA implements Search2 {	
	int tempo_limite;
	double T0_inicial;
	double alpha_inicial;
	double beta_inicial;
	double T0;
	double alpha;
	double beta;

	Factory f;
	Random r;
	
	public SA(int tempo,double T0, double alpha, double beta) {
		// TODO Auto-generated constructor stub
		System.out.println("Busca SA");		
		this.tempo_limite=tempo;
		
		this.T0_inicial = T0;
		this.alpha_inicial= alpha;
		this.beta_inicial = beta;
		f = new FactoryLS();
		r =new Random();
	}

	
	
	
	@Override
	public Node_LS run(GameState gs, int max, Node_LS best,Avaliador ava) throws Exception {
		// TODO Auto-generated method stub
		this.resert();
		double v = ava.Avalia(gs, max, best);
		long Tini = System.currentTimeMillis();
		long tempo = System.currentTimeMillis()-Tini;
	
		int cont=0;
		Node_LS local= (Node_LS) best.Clone(f);
		double v_local= v;
		while( (tempo*1.0)/1000.0 <tempo_limite && !ava.criterioParada(v)) {
			double T = this.T0/(1+cont*this.alpha);
			Node_LS melhor_vizinho = null ;
			double v_vizinho = -111111;
			for(int i= 0;i<2;i++) {
				
				Node_LS aux = (Node_LS) (local.Clone(f));
				List<Node_LS> list =new ArrayList<>();
				for(int ii=0;ii<1;ii++) {
					
					aux.countNode(list);
					int custo = r.nextInt(9)+1;
					int no = r.nextInt(list.size());
			
					list.get(no).mutation(0, custo, false);
					
				}
				double v2 = ava.Avalia(gs, max, aux);
				//System.out.println(v2.m_b+" "+aux.translate());
				//this.coac.Avalia(gs, max, aux);
			
				if(v_vizinho<v2) {
					melhor_vizinho = (Node_LS) aux.Clone(f);
					v_vizinho=v2;	
				}
				
				tempo = System.currentTimeMillis()-Tini;
				if((tempo*1.0)/1000.0 >tempo_limite || ava.criterioParada(v_vizinho))break;
		
			}

			if(accept(v_local,v_vizinho,T)) {
				
				local=(Node_LS) melhor_vizinho.Clone(f);
				v_local=v_vizinho;
				
			}
			//System.out.println(v_vizinho.m_b+"   t2\t"+melhor_vizinho.translate());
			tempo = System.currentTimeMillis()-Tini;
			
			
			if(v<v_vizinho) {
				System.out.println("melhorou "+v+"<"+v_vizinho);
				best = (Node_LS) melhor_vizinho.Clone(f);
				v_local=v_vizinho;
				v= v_vizinho;		
			
			}
			
			cont++;
			
		}
		return best;
	}

	private boolean accept(double v, double v_vizinho, double t) {
		// TODO Auto-generated method stub
		double aux2 = Math.exp(this.beta*((v_vizinho - v)/t));
		
		aux2 = Math.min(1,aux2);
		if(r.nextFloat()<aux2)return true;
		
		
		return false;
	}


	@Override
	public void resert() {
		// TODO Auto-generated method stub
		this.T0 = this.T0_inicial;
		this.alpha = this.alpha_inicial;
		this.beta = this.beta_inicial;
		
	}

}
