package ai.synthesis.dreamcoder.ComplexDSL.LS_CFG;

import java.util.List;

import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.Empty;
import java.util.ArrayList;

public class Empty_LS extends Empty implements Node_LS {

    public Empty_LS() {
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
    public void mutation(int node_atual, int budget, boolean describe) {
        // TODO Auto-generated method stub

    }

}
