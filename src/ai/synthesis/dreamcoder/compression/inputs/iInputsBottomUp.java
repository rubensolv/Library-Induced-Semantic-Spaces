/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.dreamcoder.compression.inputs;

import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.Node_LS;
import rts.GameState;
import rts.PlayerAction;

/**
 *
 * @author rubens
 */
public interface iInputsBottomUp {
    
    public String get_path_input();
    public PlayerAction get_action_from_input(Node_LS dsl);
    public void setPath(String path);
    public String getJsonInput();
    public void setJson(String json);
    public void setGame(GameState game);
    public GameState getGame();
}
