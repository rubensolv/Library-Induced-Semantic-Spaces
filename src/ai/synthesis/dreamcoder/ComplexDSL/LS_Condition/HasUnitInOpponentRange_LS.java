package ai.synthesis.dreamcoder.ComplexDSL.LS_Condition;

import java.util.List;

import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG_Condition.HasUnitInOpponentRange;
import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.Node_LS;
import java.util.ArrayList;

public class HasUnitInOpponentRange_LS extends HasUnitInOpponentRange implements Node_LS {

    public HasUnitInOpponentRange_LS() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void sample(int budget) {
        // TODO Auto-generated method stub

    }

    @Override
    public void countNode(List<Node_LS> list) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<Node_LS> getAllNodes() {
        List<Node_LS> ret = new ArrayList<>();
        ret.add(this);
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
