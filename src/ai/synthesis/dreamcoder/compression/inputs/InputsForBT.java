/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.dreamcoder.compression.inputs;

import rts.GameState;

/**
 *
 * @author rubens
 */
public class InputsForBT extends AbstractInput{

    public InputsForBT() {
    }    
    
    public InputsForBT(GameState game) {
        super.setGame(game);
    }
    
    
    
    @Override
    public String get_path_input() {
        return super.getPath();
    }        
}
