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
import rts.GameState;
import rts.units.UnitTypeTable;
import util.Pair;

public class CS implements Avaliador {

    int budget = 0;
    List<Node_LS> individuos;
    List<List<Double>> score;
    Set<Integer> selecionados;
    List<AI> adv_atual;
    SimplePlayout playout;
    Factory f;
    long tempo_ini;

    public CS() {
        // TODO Auto-generated constructor stub
        System.out.println("CS");
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

    }
    
    @Override
    public int getBudget() {
        return this.budget;
    }

    public void novosSelect() {

        UnitTypeTable utt = new UnitTypeTable();

        int mel = this.getIdBest();
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        System.out.println("Melhor: " + mel);
        this.adv_atual.add(new Interpreter(utt, this.individuos.get(mel)));
        System.out.print("Selecionado: ");
        for (int i = 0; i < score.size(); i++) {

            if (this.score.get(mel).get(i) < 0.5) {

                this.selecionados.add(i);

            }

        }

    }

    public void recalcula(GameState gs, int max, Node_LS n) throws Exception {
        UnitTypeTable utt = new UnitTypeTable();
        Node_LS camp = (Node_LS) n.Clone(f);
        AI ai = new Interpreter(utt, camp);
        List<Double> aux = new ArrayList();;
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

    }

    @Override
    public double Avalia(GameState gs, int max, Node_LS n) throws Exception {
        // TODO Auto-generated method stub
        UnitTypeTable utt = new UnitTypeTable();
        AI ai = new Interpreter(utt, n);
        double r = 0;
        List<Boolean> aux = new ArrayList<>();
        for (int i = 0; i < this.adv_atual.size(); i++) {

            double r0 = playout.run(gs, utt, 0, max, ai, adv_atual.get(i), false).m_a;
            double r1 = playout.run(gs, utt, 1, max, ai, adv_atual.get(i), false).m_a;
            if (r0 + r1 >= 0) {
                this.budget += 1;
            }
            r += r0 + r1;

            if (r0 + r1 > 1) {
                aux.add(true);
            } else {
                aux.add(false);
            }

            long paraou = System.currentTimeMillis() - this.tempo_ini;
            if (this.budget % 1000 == 0) {
                System.out.println("Camp\t" + ((paraou * 1.0) / 1000.0) + "\t" + this.budget + "\t"
                        + Control.save((Node) this.getBest()));
            }

        }
        if (aux.get(aux.size() - 1)) {
            for (int i = 0; i < aux.size() - 1; i++) {
                if (aux.get(i) == false) {
                    this.selecionados.add(i);
                }
            }
        }

        return r / 2;
    }

    @Override
    public void update(GameState gs, int max, Node_LS n) throws Exception {
        // TODO Auto-generated method stub

        this.recalcula(gs, max, n);

        int id_m = this.getIdBest();
        this.adv_atual = new ArrayList();
        UnitTypeTable utt = new UnitTypeTable();
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        System.out.print("Selecionado: ");
        for (int o : this.selecionados) {
            System.out.print(o + " ");
            this.adv_atual.add(new Interpreter(utt, this.individuos.get(o)));
        }
        System.out.println(id_m);
        this.adv_atual.add(new Interpreter(utt, this.individuos.get(id_m)));

        this.selecionados = new HashSet<>();;

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
