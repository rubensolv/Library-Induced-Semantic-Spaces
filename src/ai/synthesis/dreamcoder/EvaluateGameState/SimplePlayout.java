package ai.synthesis.dreamcoder.EvaluateGameState;

import javax.swing.JFrame;

import ai.core.AI;
import gui.PhysicalGameStatePanel;
import java.util.List;
import rts.GameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;
import util.Pair;

public class SimplePlayout implements Playout {

    public SimplePlayout() {
        // TODO Auto-generated constructor stub

    }

    @Override
    public Pair<Double, BehavioralFeature> run(GameState gs, UnitTypeTable utt, int player, int max_cycle, AI ai1, AI ai2, boolean show_screen) throws Exception {
        // TODO Auto-generated method stub
        BehavioralFeature eval = new BehavioralFeature();

        ai1.reset(utt);
        ai2.reset(utt);
        GameState gs2 = gs.cloneChangingUTT(utt);
        boolean gameover = false;
        JFrame w = null;
        if (show_screen) {
            w = PhysicalGameStatePanel.newVisualizer(gs2, 640, 640, false, PhysicalGameStatePanel.COLORSCHEME_BLACK);
        }
        boolean itbroke = false;
        eval.evaluate(gs2, player);
        long aux_time;
        int cont_atraso = 0;

        do {

            PlayerAction pa1 = null;
            try {
                aux_time = System.currentTimeMillis();
                pa1 = ai1.getAction(player, gs2);
                long paraou = System.currentTimeMillis() - aux_time;

                if (paraou > 100) {
                    return new Pair<>(-1.0, new BehavioralFeature());
                }
                if (paraou > 50) {
                    System.out.println("demorou " + cont_atraso);
                    cont_atraso += 1;
                    if (cont_atraso > 10) {
                        return new Pair<>(-1.0, new BehavioralFeature());
                    }
                }
            } catch (Exception e) {
                itbroke = true;

                break;
            }

            PlayerAction pa2 = null;
            try {

                pa2 = ai2.getAction(1 - player, gs2);
            } catch (Exception e) {
                itbroke = true;

                break;
            }
            gs2.issueSafe(pa1);
            if (!itbroke) {
                gs2.issueSafe(pa2);
            }

            if (show_screen) {
                w.repaint();
                Thread.sleep(2);
            }

            gameover = gs2.cycle();
            if (eval instanceof BehavioralFeature) {
                ((BehavioralFeature) eval).evaluate(pa1, player);
            } else {
                eval.evaluate(gs2, player);
            }

        } while (!gameover && (gs2.getTime() <= max_cycle));

        double r = 0;
        if (itbroke) {
            return new Pair<>(-1.0, new BehavioralFeature());
        } else if (gs2.winner() == player) {
            r = 1;
        } else if (gs2.winner() == -1) {
            r = 0.5;
        }

        return new Pair<>(r, eval);

    }

    @Override
    public List<Pair<Double, BehavioralFeature>> runForBoth(GameState gs, UnitTypeTable utt, int player, int max_cycle, AI ai1, AI ai2, boolean show_screen) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
