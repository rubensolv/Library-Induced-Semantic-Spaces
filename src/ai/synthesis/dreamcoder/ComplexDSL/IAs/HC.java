package ai.synthesis.dreamcoder.ComplexDSL.IAs;

import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.C_LS;
import java.util.Random;

import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.Factory;
import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.FactoryLS;
import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.NoTerminal_LS;
import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.Node_LS;
import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.S_LS;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.ChildC;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.ChildS;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import rts.GameState;

public class HC implements Search {

    int tempo_limite;

    Factory f;
    Random r;
    Random rand = new Random();
    HashSet<String> used_frags;

    public HC(int tempo) {
        // TODO Auto-generated constructor stub
        System.out.println("Busca HC");
        used_frags = new HashSet<>();
        this.tempo_limite = tempo;
        f = new FactoryLS();
        r = new Random();
    }

    @Override
    public Node_LS run(GameState gs, int max, Node_LS best, Avaliador ava, Float epsilon_0, HashMap<String, Node_LS> library) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("--- Starting Busca HC");
        this.reset();
        double v = ava.Avalia(gs, max, best);
        long Tini = System.currentTimeMillis();
        long tempo = System.currentTimeMillis() - Tini;

        while ((tempo * 1.0) / 1000.0 < tempo_limite && !ava.criterioParada(v)) {
            Float epsilon_0_temp = epsilon_0;
            Node_LS melhor_vizinho = (Node_LS) best.Clone(f);
            double v_vizinho = -111111;
            for (int i = 0; i < 1000; i++) {

                Node_LS aux = (Node_LS) (melhor_vizinho.Clone(f));
                String initial_solution = aux.translate();
                List<Node_LS> list = new ArrayList<>();
                //for (int ii = 0; ii < 1; ii++) {
                while (initial_solution.equals(aux.translate())) {
                    aux.countNode(list);
                    int custo = r.nextInt(9) + 1;
                    int no = r.nextInt(list.size());
                    //System.out.println("Initial Solution " + aux.translate());
                    if (!library.isEmpty() && rand.nextFloat() >= epsilon_0_temp) {
                        aux = prepare_mutation_by_library(aux, library);
                        aux = (Node_LS) (aux.Clone(f));                                        
                        list = new ArrayList<>();
                        //epsilon_0_temp = epsilon_0_temp + epsilon_0;
                    } else {                        
                        list.get(no).mutation(0, custo, false);                        
                    }                    

                    //System.out.println("Modified Solution " + aux.translate());                    
                }
                double v2 = ava.Avalia(gs, max, aux);

                if (v_vizinho < v2) {
                    melhor_vizinho = (Node_LS) aux.Clone(f);
                    v_vizinho = v2;
                }

                tempo = System.currentTimeMillis() - Tini;
                if ((tempo * 1.0) / 1000.0 > tempo_limite || ava.criterioParada(v_vizinho)) {
                    break;
                }

            }

            //System.out.println(v_vizinho.m_b+"   t2\t"+melhor_vizinho.translate());
            tempo = System.currentTimeMillis() - Tini;

            if (v < v_vizinho) {
                best = (Node_LS) melhor_vizinho.Clone(f);
                v = v_vizinho;

            }

        }
        return best;
    }

    @Override
    public void reset() {
        this.used_frags.clear();
    }

    private Node_LS prepare_mutation_by_library(Node_LS current_solution, HashMap<String, Node_LS> library) {
        Node_LS aux = (Node_LS) (current_solution.Clone(f));
        List<Node_LS> list = new ArrayList<>();
        aux.countNode(list);
        int cost = rand.nextInt(9) + 1;
        int node_number = rand.nextInt(list.size());
        Node_LS cut_point = list.get(node_number);
        if (cut_point instanceof NoTerminal_LS) {
            Node_LS child = ((NoTerminal_LS) cut_point).sorteiaFilho(cost);
            ArrayList<Node_LS> mylist = new ArrayList(library.values());
            Collections.shuffle(mylist);            
            for (Node_LS value : mylist) {
                //if (!this.used_frags.contains(value.translate())) {
                    if (value.getClass().equals(child.getClass())) {
                        //String text_to_save = value.translate();
                        if (cut_point instanceof S_LS) {
                            ((S_LS) (cut_point)).setChild((ChildS) value.Clone(f));
                        } else if (cut_point instanceof C_LS) {
                            ((C_LS) cut_point).setChildC((ChildC) value.Clone(f));
                        } else {
                            //System.out.println(cut_point.getClass());
                        }
                        //this.used_frags.add(text_to_save);
                        return aux;
                    }else if (rand.nextFloat() >= 0.5) {
                        return get_large_SLS(mylist);
                    }
                //}
            }
        }
        return aux;
    }

    private Node_LS get_large_SLS(ArrayList<Node_LS> mylist) {
        Node_LS aux = mylist.get(0);
        int len = aux.translate().length();
        for (Node_LS node_LS : mylist) {
            if (node_LS instanceof S_LS) {
                if (node_LS.translate().length() > len) {
                    aux = node_LS;
                    len = node_LS.translate().length();
                    if (len >= 50) {
                        return aux;
                    }
                }
            }
        }
        
        return aux;
    }

}
