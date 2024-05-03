/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.dreamcoder.compression;

import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.Node_LS;
import ai.synthesis.dreamcoder.compression.inputs.iInputsBottomUp;
import java.util.List;

/**
 *
 * @author rubens
 */
public interface iRemoveEquivalent {
    
    public List<Node_LS> removeEquivalents(List<Node_LS> pList, List<iInputsBottomUp> inputs);
}
