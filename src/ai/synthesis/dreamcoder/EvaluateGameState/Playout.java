package ai.synthesis.dreamcoder.EvaluateGameState;

import ai.core.AI;
import java.util.List;
import rts.GameState;
import rts.units.UnitTypeTable;
import util.Pair;

public interface Playout {

    Pair<Double, BehavioralFeature> run(GameState gs, UnitTypeTable utt, int player, int max_cycle, AI ai1, AI ai2,
            boolean show_screen) throws Exception;
    List<Pair<Double, BehavioralFeature>> runForBoth(GameState gs, UnitTypeTable utt, int player, int max_cycle, AI ai1, AI ai2,
            boolean show_screen) throws Exception;
}
