package ai.synthesis.ComplexDSL.LS_CFG;

public class Cardapio {

	static public int custo(Node_LS n) {
		
		if(n instanceof For_S_LS)return 2;
		if(n instanceof If_B_then_S_else_S_LS)return 4;
		if(n instanceof If_B_then_S_LS)return 3;
		if(n instanceof S_S_LS)return 2;
		if(n instanceof C_LS)return 1;	
		if(n instanceof Empty_LS)return 0;
		return 10000;
	}
	
	public Cardapio() {
		// TODO Auto-generated constructor stub
	}

}
