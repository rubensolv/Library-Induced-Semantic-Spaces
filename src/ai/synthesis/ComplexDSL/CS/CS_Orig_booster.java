package ai.synthesis.ComplexDSL.CS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ai.core.AI;
import ai.synthesis.ComplexDSL.EvaluateGameState.CabocoDagua2;
import ai.synthesis.ComplexDSL.EvaluateGameState.SimplePlayout;
import ai.synthesis.ComplexDSL.IAs2.Avaliador;
import ai.synthesis.ComplexDSL.LS_CFG.Empty_LS;
import ai.synthesis.ComplexDSL.LS_CFG.FactoryLS;
import ai.synthesis.ComplexDSL.LS_CFG.Node_LS;
import ai.synthesis.ComplexDSL.LS_CFG.S_LS;
import ai.synthesis.ComplexDSL.Synthesis_Base.AIs.Interpreter;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.Control;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.Factory;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.Node;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import rts.GameState;
import rts.units.UnitTypeTable;
import util.Pair;

public class CS_Orig_booster implements Avaliador {

    int budget = 0;
    List<Node_LS> individuos;
    List<List<Double>> score;
    Set<Integer> selecionados;
    List<AI> adv_atual;
    SimplePlayout playout;
    Factory f;
    long tempo_ini;
    private HashMap<List<Integer>, Integer> dict_data_points;
    List<Integer> neighbor_data_boolean = new ArrayList<>();

    public CS_Orig_booster() {
        // TODO Auto-generated constructor stub
        System.out.println("CS Booster");
        UnitTypeTable utt = new UnitTypeTable();
        tempo_ini = System.currentTimeMillis();
        this.playout = new SimplePlayout();
        this.f = new FactoryLS();
        this.individuos = new ArrayList();
        this.individuos.add(new S_LS(new Empty_LS()));
        System.out.println("Camp\t" + 0.0 + "\t" + 0 + "\t"
                + Control.save((Node) this.individuos.get(0)));

        this.adv_atual = new ArrayList();
        this.adv_atual.add(new Interpreter(utt, this.individuos.get(0)));

        this.score = new ArrayList();
        score.add(new ArrayList());
        score.get(0).add(0.5);
        this.selecionados = new HashSet<>();
        this.dict_data_points = new HashMap<>();

    }
    
    @Override
    public int getBudget() {
        return this.budget;
    }

    public Node_LS recalcula(GameState gs, int max, Node_LS n) throws Exception {
        UnitTypeTable utt = new UnitTypeTable();
        Node_LS camp = (Node_LS) n.Clone(f);        
        AI ai = new Interpreter(utt, camp);
        List<Double> aux = new ArrayList();
        for (int i = 0; i < this.individuos.size(); i++) {            
            AI ai2 = new Interpreter(utt, this.individuos.get(i));
            Pair<Double, CabocoDagua2> aux0 = playout.run(gs, utt, 0, max, ai, ai2, false);
            Pair<Double, CabocoDagua2> aux1 = playout.run(gs, utt, 1, max, ai, ai2, false);
            double r = (aux0.m_a + aux1.m_a) / 2;
            this.score.get(i).add(1 - r);
            aux.add(r);
        }
        camp.clear(null, f);
        this.individuos.add((Node_LS) camp);
        aux.add(0.5);
        this.score.add(aux);

        System.out.println();
        System.out.println("Tabela: ");
        for (int i = 0; i < score.size(); i++) {
            System.out.print("\t" + i);
        }
        System.out.println();
        for (int i = 0; i < score.size(); i++) {
            System.out.print(i);
            for (int j = 0; j < score.size(); j++) {
                System.out.print("\t" + score.get(i).get(j));
            }
            System.out.println();
        }

        long paraou = System.currentTimeMillis() - this.tempo_ini;
        System.out.println("Camp\t" + ((paraou * 1.0) / 1000.0) + "\t" + this.budget + "\t"
                + Control.save((Node) this.getBest()));
        return camp;
    }

    @Override
    public double Avalia(GameState gs, int max, Node_LS n) throws Exception {
        // TODO Auto-generated method stub
        UnitTypeTable utt = new UnitTypeTable();
        AI ai = new Interpreter(utt, n);
        double r = 0;
        double rt0 = playout.run(gs, utt, 0, max, ai, adv_atual.get(adv_atual.size() - 1), false).m_a;
        double rt1 = playout.run(gs, utt, 1, max, ai, adv_atual.get(adv_atual.size() - 1), false).m_a;
        double value_against_last = rt0 + rt1;
        neighbor_data_boolean.clear();
        for (int i = 0; i < this.adv_atual.size(); i++) {

            double r0 = playout.run(gs, utt, 0, max, ai, adv_atual.get(i), false).m_a;
            double r1 = playout.run(gs, utt, 1, max, ai, adv_atual.get(i), false).m_a;
            if (r0 + r1 >= 0) {
                this.budget += 1;
            }
            r += r0 + r1;

            if (value_against_last > 1 && (r0 + r) < 1) {
                neighbor_data_boolean.add(1);
            } else {
                neighbor_data_boolean.add(0);
            }

            long paraou = System.currentTimeMillis() - this.tempo_ini;
            if (this.budget % 1000 == 0) {
                System.out.println("Camp\t" + ((paraou * 1.0) / 1000.0) + "\t" + this.budget + "\t"
                        + Control.save((Node) this.getBest()));
            }

        }

        if (!this.dict_data_points.containsKey(neighbor_data_boolean)) {
            this.dict_data_points.put(neighbor_data_boolean, 1);
        } else {
            this.dict_data_points.put(neighbor_data_boolean, (this.dict_data_points.get(neighbor_data_boolean) + 1));
        }

        return r / 2;
    }

    @Override
    public void update(GameState gs, int max, Node_LS n) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("#################################");           
        n = this.recalcula(gs, max, n);
        Node best = this.getBest();        
//
//        int id_m = this.getIdBest();
//        this.adv_atual = new ArrayList();
//        UnitTypeTable utt = new UnitTypeTable();
//        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
//        System.out.print("Selecionado: ");
//        for (int o : this.selecionados) {
//            System.out.print(o + " ");
//            this.adv_atual.add(new Interpreter(utt, this.individuos.get(o)));
//        }
//        System.out.println(id_m);
//        this.adv_atual.add(new Interpreter(utt, this.individuos.get(id_m)));
//
//        this.selecionados = new HashSet<>();
        UnitTypeTable utt = new UnitTypeTable();
        System.out.println("Ai "+n.translate());
        AI ai = new Interpreter(utt, n);
        List<AI> strongers = get_stronger(gs, max, n, individuos);
        if (!strongers.isEmpty()) {
            System.out.println("Stronger opponent(s) found!");
            this.adv_atual.addAll(strongers);

        } else {
            System.out.println("CS normal interaction!");
            ArrayList<Integer> indexes_opponents = select_opponents_memory_friendly(this.adv_atual);
            List<AI> new_opponents = new ArrayList<>();

            for (int i = 0; i < indexes_opponents.size(); i++) {
                new_opponents.add(this.adv_atual.get(indexes_opponents.get(i)));
            }
            this.adv_atual = new_opponents;            
            this.adv_atual.add(ai);
        }
        System.out.println("Total of adversary "+this.individuos.size());
        System.out.println("Total of Opponents selected "+this.adv_atual.size());
        boolean add = true;
        for (AI ai1 : this.adv_atual) {
            Interpreter t = (Interpreter) ai1;
            System.out.println("\t"+t.getNode().translate());
            if (best.translate().equals(t.getNode().translate())) {
                add = false;
            }
        }
        if (add) {
            System.out.println("Add best in the group "+best.translate());
            this.adv_atual.add(new Interpreter(utt, best));
        }
        this.dict_data_points.clear();
//        if (!contains_strategy(individuos, n)) {
//            this.individuos.add((Node_LS) n);
//        }
        this.selecionados = new HashSet<>();
        System.out.println("#################################");

    }

    private List<AI> get_stronger(GameState gs, int max, Node_LS strategy, List<Node_LS> opponents) throws Exception {
        UnitTypeTable utt = new UnitTypeTable();
        AI ai = new Interpreter(utt, strategy);

        List<AI> meta = new ArrayList<>();
        for (Node_LS opponent : opponents) {
            AI ai2 = new Interpreter(utt, opponent);
            double r0 = playout.run(gs, utt, 0, max, ai2, ai, false).m_a;
            double r1 = playout.run(gs, utt, 1, max, ai2, ai, false).m_a;
            if ((r0 + r1) > 1.0) {
                meta.add(new Interpreter(utt,opponent));
            }
        }
        return meta;
    }

    private boolean contains_strategy(List<Node_LS> opponents, Node_LS strategy) {
        for (Node_LS opponent : opponents) {
            if (opponent.translate().equals(strategy.translate())) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<Integer> select_opponents_memory_friendly(List<AI> opponents) {
        ArrayList<Integer> indexes_opponents = new ArrayList<>();
        indexes_opponents.add(opponents.size() - 1);

        while (true) {
            ArrayList<Integer> opponent_scores = new ArrayList<>(Collections.nCopies(opponents.size(), 0));

            for (Map.Entry<List<Integer>, Integer> entry : dict_data_points.entrySet()) {
                for (int i = 0; i < entry.getKey().size(); i++) {
                    if (entry.getKey().get(i) == 1) {                        
                        opponent_scores.set(i,opponent_scores.get(i)+entry.getValue());
                    }
                }
            }

            if (Collections.max(opponent_scores) == 0) {
                return indexes_opponents;
            }
            int index_opponent_added = argmax(opponent_scores);
            indexes_opponents.add(index_opponent_added);

            HashMap<List<Integer>, Integer> new_data_points = new HashMap<>();
            for (Map.Entry<List<Integer>, Integer> entry : dict_data_points.entrySet()) {
                try {
                    if (entry.getKey().get(index_opponent_added) == 0) {
                        new_data_points.put(entry.getKey(), entry.getValue());
                    }
                } catch (Exception e) {
                    if (entry.getKey().get(index_opponent_added) == 0) {
                        new_data_points.put(entry.getKey(), entry.getValue());
                    }
                }

            }
            dict_data_points = new_data_points;
        }

    }

    public static int argmax(ArrayList<Integer> a) {
        int re = Integer.MIN_VALUE;
        int arg = -1;
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i) > re) {
                re = a.get(i);
                arg = i;
            }
        }
        return arg;
    }

    @Override
    public Node_LS getIndividuo() {
        // TODO Auto-generated method stub
        return this.getBest();
    }

    @Override
    public Node_LS getBest() {
        // TODO Auto-generated method stub
        int index = this.getIdBest();
        System.out.println("Melhor " + index);
        return (Node_LS) this.individuos.get(index).Clone(f);
    }

    @Override
    public boolean criterioParada(double d) {
        // TODO Auto-generated method stub
        return d > this.adv_atual.size() - 0.1;
    }

    public int getIdBest() {
        // TODO Auto-generated method stub
        double melhor = -1111111;
        int index = -1;
        for (int i = 0; i < this.individuos.size(); i++) {
            double cont = 0;
            for (int j = 0; j < this.individuos.size(); j++) {
                cont += score.get(i).get(j);
            }
            if (cont >= melhor) {
                melhor = cont;
                index = i;
            }
        }

        return index;
    }
    
    @Override
    public int getTotalIndividuos() {
        return this.individuos.size();
    }

}
