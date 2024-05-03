package ai.synthesis.ComplexDSL.Tests;


import ai.synthesis.ComplexDSL.CS.CS;
import ai.synthesis.ComplexDSL.CS.CS_Default;
import ai.synthesis.ComplexDSL.CS.CS_Orig_booster;
import ai.synthesis.ComplexDSL.CS.CS_Origin;
import ai.synthesis.ComplexDSL.DO.DO;
import ai.synthesis.ComplexDSL.IAs2.Algoritmo1;
import ai.synthesis.ComplexDSL.IAs2.AvaliadorPadrao;
import ai.synthesis.ComplexDSL.IAs2.HC;
import ai.synthesis.ComplexDSL.IAs2.SA;
import rts.GameState;
import rts.PhysicalGameState;
import rts.units.UnitTypeTable;

public class Teste2 {
	static int max=6000;
	public Teste2() {
		// TODO Auto-generated constructor stub
		
	}

	public static String getMap(String s) {
		if(s.equals("0")) return "maps/8x8/basesWorkers8x8A.xml";
		if(s.equals("1")) return "maps/NoWhereToRun9x8.xml";
		if(s.equals("2")) return "maps/16x16/basesWorkers16x16A.xml";
		if(s.equals("3")) return "maps/24x24/basesWorkers24x24A.xml";
		if(s.equals("4")) return "maps/DoubleGame24x24.xml";
		if(s.equals("5")) return "maps/32x32/basesWorkers32x32A.xml";
	    if(s.equals("6")) { max =15000;return "maps/BroodWar/(4)BloodBath.scmB.xml";}
	   return null;
	}
    
	
	public static void main(String[] args) throws Exception {
//            System.out.println(args[0]);
//            System.out.println(args[1]);
		// TODO Auto-generated method stub
		UnitTypeTable utt = new UnitTypeTable();
		String path_map = getMap(args[0]);
//		System.out.println(path_map);
		PhysicalGameState pgs = PhysicalGameState.load(path_map, utt);
		GameState gs2 = new GameState(pgs, utt);
                String setting = args[1];
                //String setting = "4";
		

		System.out.println(path_map+" novo mapas teste budget"
				+ "");
		//AvaliaCoac coac = new AvaliaCoac();
                
		Algoritmo1 se =null;
		/*
		if(args[1].equals("0"))se= new Algoritmo1(new SA(coac,1500,1000,0.9,15),1);
		if(args[1].equals("1"))se= new Algoritmo1(new SA(coac,1500,1000,0.9,15),3);
		if(args[1].equals("2"))se= new Algoritmo1(new SA(coac,1500,1000,0.9,15),1000);
		if(args[1].equals("3"))se= new Algoritmo1(new SA2(coac,1500,1000,0.9,15),1);
		if(args[1].equals("4"))se= new Algoritmo1(new SA2(coac,1500,1000,0.9,15),3);
		if(args[1].equals("5"))se= new Algoritmo1(new SA2(coac,1500,1000,0.9,15),1000);
		if(args[1].equals("6"))se= new Algoritmo1(new HC(coac,1500),1);
		if(args[1].equals("7"))se= new Algoritmo1(new HC(coac,1500),3);
		if(args[1].equals("8"))se= new Algoritmo1(new HC(coac,1500),1000);
		*/
		if(setting.equals("0"))se= new Algoritmo1( new SA(2000,2000,0.9,0.5), new AvaliadorPadrao(1) ); //SA_1
		if(setting.equals("1"))se= new Algoritmo1( new SA(2000,2000,0.9,0.5), new AvaliadorPadrao(1000) ); //SA_inf
                if(setting.equals("2")) se= new Algoritmo1( new SA(2000,2000,0.9,0.5), new DO() );
                if(setting.equals("3"))se= new Algoritmo1( new HC(2000), new AvaliadorPadrao(1) ); //SA_1
		if(setting.equals("4"))se= new Algoritmo1( new HC(2000), new AvaliadorPadrao(1000) ); //SA_inf
                if(setting.equals("5")) se= new Algoritmo1( new HC(2000), new DO() );
		if(args[1].equals("6")) se= new Algoritmo1(new SA(2000,2000,0.9,0.5),new CS_Origin());
		if(args[1].equals("7")) se= new Algoritmo1(new HC(2000),new CS_Origin());	
                if(args[1].equals("8")) se= new Algoritmo1(new SA(2000,2000,0.9,0.5),new CS_Orig_booster());
		if(args[1].equals("9")) se= new Algoritmo1(new HC(2000),new CS_Orig_booster());	
                if(args[1].equals("10")) se= new Algoritmo1(new SA(2000,2000,0.9,0.5),new CS_Default());
		if(args[1].equals("11")) se= new Algoritmo1(new HC(2000),new CS_Default());	
		//if(args[1].equals("4")) se= new Algoritmo1(new SA(2000,2000,0.9,0.5),new DO());
		//if(args[1].equals("5")) se= new Algoritmo1(new SA(2000,2000,0.9,0.5),new CS());	
		//se= new Algoritmo1(new SA(2000,2000,0.9,0.5),new CS());
		
		se.run(gs2, max);
	}

}
