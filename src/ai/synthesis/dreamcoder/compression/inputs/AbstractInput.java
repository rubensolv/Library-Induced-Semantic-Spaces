/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.dreamcoder.compression.inputs;

import ai.core.AI;
import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.Node_LS;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.AIs.Interpreter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import rts.GameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public abstract class AbstractInput implements iInputsBottomUp {

    private String path;
    private String json;
    private UnitTypeTable utt;
    private GameState game;

    public AbstractInput() {
        this.utt = new UnitTypeTable();
        this.json = null;

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        try {
            this.json = readFileAsJson(path);
        } catch (IOException ex) {
            Logger.getLogger(AbstractInput.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public PlayerAction get_action_from_input(Node_LS dsl) {
        PlayerAction pa = new PlayerAction();
        if (this.json != null) {
            game = GameState.fromJSON(this.json, utt);
        }        
        AI ai = buildCommandsIA(utt, dsl);
        ai.reset(utt);       
        if (game.canExecuteAnyAction(0)) {
            try {
                
                pa = ai.getAction(0, game);
                //System.out.println("----"+dsl.translate());
                //System.out.println(pa.toString());
            } catch (Exception ex) {
                //Logger.getLogger(AbstractInput.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                pa = ai.getAction(1, game);
//                System.out.println("----"+dsl.translate());
//                System.out.println(pa.toString());
//                System.out.println(game.toString());
            } catch (Exception ex) {
                //Logger.getLogger(AbstractInput.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return pa;
    }

    private AI buildCommandsIA(UnitTypeTable utt, Node_LS code) {
        return new Interpreter(utt, code);
    }

    private String readFileAsJson(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }

    public void setJson(String json) {
        this.json = json;
    }

    @Override
    public String getJsonInput() {
        return this.json;
    }

    @Override
    public GameState getGame() {
        return game;
    }

    @Override
    public void setGame(GameState game) {
        this.game = game;
    }

}
