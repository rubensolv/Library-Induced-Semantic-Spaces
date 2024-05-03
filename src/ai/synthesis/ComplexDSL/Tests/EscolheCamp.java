package ai.synthesis.ComplexDSL.Tests;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import util.Pair;

public class EscolheCamp {

	public static List<String> ler1(String map,String modo) throws FileNotFoundException{
		List<String> list = new ArrayList<>();
		for(int i =0;i<10;i++) {
			String entrada = "out_"+map+"_"+modo+"_"+i+".txt";
			Scanner in = new Scanner(new FileReader("Ava/"+entrada));
			String a1="";
			String a2="";
			while (in.hasNextLine()) {
			    String line = in.nextLine();
			    String dados[] = line.split("\t");
			    if(dados[0].equals("atualizou")) {
			    	a1=dados[3];
			    	a2=dados[4];
			    }
			}
			list.add(a1);
			list.add(a2);
		}
		return list;
	}
	

	public static List<String> ler0(String map,String modo) throws FileNotFoundException{
		List<String> list = new ArrayList<>();
		for(int i =0;i<10;i++) {
			String entrada = "out_"+map+"_"+modo+"_"+i+".txt";
			Scanner in = new Scanner(new FileReader("Ava/"+entrada));
			String a1="";
			String a2="";
			while (in.hasNextLine()) {
			    String line = in.nextLine();
			    String dados[] = line.split("\t");
			    if(dados[0].equals("Camp")) {
			    	a1=dados[2];
			    	a2=dados[3];
			    }
			}
			list.add(a1);
			list.add(a2);
		}
		return list;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub

		String map = "3";
		
		for(int i=0;i<5;i++) {
			Pair<String,String> resp= getMelhor(map,""+i);
			System.out.println("Camp"+"\t"+resp.m_a+"\t"+resp.m_b);
		}
		
		
	}

	private static Pair<String, String> getMelhor(String map, String modo) throws FileNotFoundException {
		// TODO Auto-generated method stub
		List<String> camps = null;
		if(modo.equals("1"))camps=ler1(map,modo);
		else camps=ler0(map,modo);
		int s1=-1;
		double pont=-1;
		for(int i=0;i<20;i++) {
			double r = ler(map,modo,""+i,"0");
			if(r>pont) {
				pont=r;
				s1=i;
			}
		}
		
		
		int s2=-1;
		double pont2=-1;
		for(int i=0;i<20;i++) {
			double r = ler(map,modo,""+i,"1");
			if(r>pont2) {
				pont2=r;
				s2=i;
			}
		}
		
		
		return new Pair<>(camps.get(s1),camps.get(s2));

	}

	private static double ler(String map, String modo, String rep, String lado) throws FileNotFoundException {
		// TODO Auto-generated method stub
		String entrada = "out_"+map+"_"+modo+"_"+rep+"_"+lado+".txt";
		Scanner in = new Scanner(new FileReader("Camp/"+entrada));
		double pont=0;
		while (in.hasNextLine()) {
		    String line = in.nextLine();
		    String dados[] = line.split(" ");
		    if(dados[0].equals("parcial")) {
		    	pont = Double.parseDouble(dados[1]);
		    }
		}
		
		return pont;
	}

}
