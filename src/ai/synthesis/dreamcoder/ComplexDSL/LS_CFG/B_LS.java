package ai.synthesis.dreamcoder.ComplexDSL.LS_CFG;

import ai.synthesis.dreamcoder.ComplexDSL.LS_Condition.*;
import java.util.List;
import java.util.Random;

import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.B;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.ChildB;
import java.util.ArrayList;

public class B_LS extends B implements Node_LS, NoTerminal_LS {

    public B_LS() {
        // TODO Auto-generated constructor stub
    }

    public B_LS(ChildB childB) {
        super(childB);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Node_LS sorteiaFilho(int budget) {
        // TODO Auto-generated method stub
        Random gerador = new Random();

        int g = gerador.nextInt(14);

        if (g == 0) {
            return new CanAttack_LS();
        }
        if (g == 1) {
            return new CanHarvest_LS();
        }
        if (g == 2) {
            return new HasLessNumberOfUnit_LS();
        }
        if (g == 3) {
            return new HasNumberOfUnits_LS();
        }
        if (g == 4) {
            return new HasNumberOfWorkersHarvesting_LS();
        }
        if (g == 5) {
            return new HasUnitInOpponentRange_LS();
        }
        if (g == 6) {
            return new HasUnitThatKillsInOneAttack_LS();
        }
        if (g == 7) {
            return new HasUnitWithinDistanceFromOpponent_LS();
        }
        if (g == 8) {
            return new HaveQtdUnitsAttacking_LS();
        }
        if (g == 9) {
            return new Is_Builder_LS();
        }
        if (g == 10) {
            return new Is_Type_LS();
        }
        if (g == 11) {
            return new OpponentHasNumberOfUnits_LS();
        }
        if (g == 12) {
            return new OpponentHasUnitInPlayerRange_LS();
        }
        if (g == 13) {
            return new OpponentHasUnitThatKillsUnitInOneAttack_LS();
        }
        return null;
    }

    @Override
    public void sample(int budget) {
        // TODO Auto-generated method stub
        Node_LS child = this.sorteiaFilho(budget);
        child.sample(budget);
        this.setChildB((ChildB) child);
    }

    @Override
    public void countNode(List<Node_LS> list) {
        // TODO Auto-generated method stub
        Node_LS n2 = (Node_LS) this.getChildB();
        list.add(this);
        n2.countNode(list);
    }

    @Override
    public List<Node_LS> getAllNodes() {
        List<Node_LS> ret = new ArrayList<>();
        ret.add(this);
        Node_LS n2 = (Node_LS) this.getChildB();
        ret.add(n2);
        ret.addAll(n2.getAllNodes());        
        return ret;
    }

    @Override
    public void mutation(int node_atual, int budget, boolean desc) {
        // TODO Auto-generated method stub

        if (desc) {
            System.out.println("Mutacao \t " + this.getName());
            System.out.println("Anterior \t" + this.translate());
        }
        this.sample(budget);

        if (desc) {
            System.out.println("Atual \t" + this.translate());
        }
    }

}
