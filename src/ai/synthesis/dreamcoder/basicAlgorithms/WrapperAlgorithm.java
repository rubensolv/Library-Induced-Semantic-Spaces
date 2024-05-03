package ai.synthesis.dreamcoder.basicAlgorithms;

import ai.synthesis.dreamcoder.ComplexDSL.IAs.*;
import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.Node_LS;
import java.util.HashMap;
//import ai.synthesis.ComplexDSL.LS_CFG.Node_LS;
import rts.GameState;

public class WrapperAlgorithm {

    Search search_Algorithm;
    Avaliador evaluator;    

    public WrapperAlgorithm(Search sc, Avaliador ava) {
        // TODO Auto-generated constructor stub
        this.evaluator = ava;
        this.search_Algorithm = sc;

    }

    public Node_LS run(GameState gs, int max, Float epsilon_0, HashMap<String, Node_LS> library) throws Exception {
        
        while (evaluator.getBudget() <= 999999999) {            
            Node_LS j = evaluator.getIndividuo();

            Node_LS c0 = search_Algorithm.run(gs, max, j, evaluator, epsilon_0, library);

            double r0 = evaluator.Avalia(gs, max, c0);
            double r1 = evaluator.Avalia(gs, max, j);
            
            if (r0 > r1) {
                evaluator.update(gs, max, c0);
                return c0;
            } else {
                return j;
            }
            

        }
        return null;
    }

}
