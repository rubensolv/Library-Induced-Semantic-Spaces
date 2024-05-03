package ai.synthesis.dreamcoder.basicAlgorithms;

import ai.synthesis.dreamcoder.ComplexDSL.IAs.*;
import java.util.ArrayList;
import java.util.List;

import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.AIs.Interpreter;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.Control;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.Factory;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.Node;
import ai.synthesis.dreamcoder.ComplexDSL.EvaluateGameState.SimplePlayout;
import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.Empty_LS;
import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.FactoryLS;
import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.Node_LS;
import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.S_LS;
import ai.core.AI;
import rts.GameState;
import rts.units.UnitTypeTable;

public class DefaultEvaluation implements Avaliador {

    int budget;
    List<Node_LS> js;
    int n;
    SimplePlayout playout;
    Factory f;
    long tempo_ini = System.currentTimeMillis();
    double best;

    public DefaultEvaluation(int n) {
        // TODO Auto-generated constructor stub
        System.out.println("N = " + n);
        this.playout = new SimplePlayout();
        this.f = new FactoryLS();
        this.n = n;
        this.js = new ArrayList();
        this.best = 0.5;
        this.js.add(new S_LS(new Empty_LS()));
        System.out.println("Camp\t0.0" + "\t" + 0 + "\t"
                + Control.save((Node) js.get(0)));
        this.budget = 0;
    }
    
    public DefaultEvaluation(int n, Node_LS initial) {
        // TODO Auto-generated constructor stub
        System.out.println("N = " + n);
        this.playout = new SimplePlayout();
        this.f = new FactoryLS();
        this.n = n;
        this.js = new ArrayList();
        this.best = 0.5;
        this.js.add(initial);
        System.out.println("Camp\t0.0" + "\t" + 0 + "\t"
                + Control.save((Node) js.get(0)));
        this.budget = 0;
    }

    /**
     * @author Rubens
     * Clean the current list of solutions and set an initial solution
     * @param solution any current node
     */
    public void resetListandSetCurrentSolution(Node_LS solution) {
        this.js = new ArrayList();
        this.js.add(solution);
    }

    public List<Node_LS> getAllIndividuos() {
        return js;
    }
    
    
        
    @Override
    public double Avalia(GameState gs, int max, Node_LS j) throws Exception {
        // TODO Auto-generated method stub
        UnitTypeTable utt = new UnitTypeTable();
        AI ai = new Interpreter(utt, j);
        double r = 0;

        for (Node_LS adv : this.js) {
            AI ai2 = new Interpreter(utt, adv);
            double r0 = playout.run(gs, utt, 0, max, ai, ai2, false).m_a;
            double r1 = playout.run(gs, utt, 1, max, ai, ai2, false).m_a;
            //if (r0 + r1 >= 0) {
            this.budget += 1;
            //}
            r += r0 + r1;
            long paraou = System.currentTimeMillis() - this.tempo_ini;
            if (this.budget % 1000 == 0) {
                System.out.println("Camp\t" + ((paraou * 1.0) / 1000.0) + "\t" + this.budget + "\t"
                        + Control.save((Node) this.getBest()));
            }

        }

        return r / 2;
    }

    @Override
    public void update(GameState gs, int max, Node_LS j) throws Exception {
        // TODO Auto-generated method stub
        long paraou = System.currentTimeMillis() - this.tempo_ini;

        Node_LS camp = (Node_LS) j.Clone(f);
        double r = Avalia(gs, max, camp);
        System.out.println("resultado " + ((paraou * 1.0) / 1000.0) + "     " + r + ">" + this.best + " individuos = " + js.size());
        if (r > this.best) {
            if (js.size() >= this.n) {
                js.remove(0);
            }
            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
            System.out.println("atulizou " + r + ">" + this.best + " individuos = " + js.size());

            camp.clear(null, f);
            js.add((Node_LS) camp);

            this.best = Avalia(gs, max, camp);

        }
        System.out.println("Camp\t" + ((paraou * 1.0) / 1000.0) + "\t" + this.budget + "\t"
                + Control.save((Node) js.get(js.size() - 1)));
    }

    @Override
    public Node_LS getIndividuo() {
        // TODO Auto-generated method stub
        return (Node_LS) js.get(js.size() - 1).Clone(f);
    }

    @Override
    public boolean criterioParada(double d) {
        // TODO Auto-generated method stub
        return d > this.js.size() - 0.1;
    }

    @Override
    public Node_LS getBest() {
        // TODO Auto-generated method stub
        return (Node_LS) js.get(js.size() - 1).Clone(f);
    }

    @Override
    public int getBudget() {
        return this.budget;
    }

    @Override
    public int getTotalIndividuos() {
        return this.js.size();
    }
    
    

}
