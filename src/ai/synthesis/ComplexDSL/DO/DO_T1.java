package ai.synthesis.ComplexDSL.DO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

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

public class DO_T1 implements Avaliador {

    int budget;
    List<Node_LS> individuos;
    List<List<Double>> score;
    List<AI> adv_atual;
    SimplePlayout playout;
    Factory f;
    long tempo_ini;

    public DO_T1() {
        // TODO Auto-generated constructor stub
        System.out.println("Double Oracle T1s");
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
        this.budget = 0;
    }

    @Override
    public int getBudget() {
        return this.budget;
    }

    @Override
    public double Avalia(GameState gs, int max, Node_LS j) throws Exception {
        // TODO Auto-generated method stub
        UnitTypeTable utt = new UnitTypeTable();            
        AI ai = new Interpreter(utt, j);
        double r = 0;

        for (AI ai2 : this.adv_atual) {

            double r0 = playout.run(gs, utt, 0, max, ai, ai2, false).m_a;
            double r1 = playout.run(gs, utt, 1, max, ai, ai2, false).m_a;
            if (r0 + r1 >= 0) {
                this.budget += 1;
            }
            r += r0 + r1;
            long paraou = System.currentTimeMillis() - this.tempo_ini;
            if (this.budget % 1000 == 0) {
                System.out.println("Camp\t" + ((paraou * 1.0) / 1000.0) + "\t" + this.budget + "\t"
                        + Control.save((Node) this.getBest()));
            }
        }
        j.clear(null, f); //talvez remover
        return r / 2;
    }

    @Override
    public void update(GameState gs, int max, Node_LS j) throws Exception {       
        UnitTypeTable utt = new UnitTypeTable();
        Node_LS camp = (Node_LS) j.Clone(f);
        AI ai = new Interpreter(utt, camp);
        List<Double> aux = new ArrayList();;
        for (int i = 0; i < this.individuos.size(); i++) {
            AI ai2 = new Interpreter(utt, this.individuos.get(i));
            Pair<Double, CabocoDagua2> aux0 = playout.run(gs, utt, 0, max, ai, ai2, false);
            Pair<Double, CabocoDagua2> aux1 = playout.run(gs, utt, 1, max, ai, ai2, false);
            double r = (aux0.m_a + aux1.m_a) / 2;
            this.score.get(i).add(1 - r);
            aux.add(r);
            this.budget += 2;
        }        
        this.individuos.add((Node_LS) camp);
        aux.add(0.5);
        this.score.add(aux);
        
        long paraou = System.currentTimeMillis() - this.tempo_ini;          
        System.out.println("Camp\t" + ((paraou * 1.0) / 1000.0) + "\t" + this.budget + "\t"
                + Control.save((Node) this.getBest()));
        this.selecionaADV();
    }

    private void selecionaADV() {
        this.adv_atual.clear();        
        UnitTypeTable utt = new UnitTypeTable();
        this.adv_atual.add(new Interpreter(utt, this.getBest()));
        if (this.individuos.size() == 1) {
            this.adv_atual.add(new Interpreter(utt, this.individuos.get(0)));
            System.out.println("Selected: 0");

        }
        int qtd_strategies = this.individuos.size();

        double[][] payoff = new double[qtd_strategies][qtd_strategies];

        for (int i = 0; i < qtd_strategies; i++) {
            for (int j = 0; j < qtd_strategies; j++) {
                payoff[i][j] = this.score.get(i).get(j);
            }
        }

        double[] prob = get_distribution(payoff);

        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        System.out.print("Selected: ");
        for (int i = 0; i < qtd_strategies; i++) {            
            if (prob[i] > 0.00f) {            
                this.adv_atual.add(new Interpreter(utt, this.individuos.get(i)));
                System.out.print(i + " ");
            }

        }
        System.out.println();

    }

    private double[] get_distribution(double[][] payoff) {
        double[] prob = MinMaxSolver.getRowPlayersStrategy(payoff);
        return prob;
    }

    @Override
    public Node_LS getIndividuo() {
        // TODO Auto-generated method stub
        return this.getBest();
    }

    @Override
    public Node_LS getBest() {
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
        return (Node_LS) this.individuos.get(index).Clone(f);
    }

    @Override
    public boolean criterioParada(double d) {
        // TODO Auto-generated method stub
        return d > this.adv_atual.size() - 0.1;
    }

    @Override
    public int getTotalIndividuos() {
        return this.individuos.size();
    }

}
