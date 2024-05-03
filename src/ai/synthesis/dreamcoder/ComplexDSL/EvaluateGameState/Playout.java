package ai.synthesis.dreamcoder.ComplexDSL.EvaluateGameState;

import ai.core.AI;
import rts.GameState;
import rts.units.UnitTypeTable;
import util.Pair;

public interface Playout {

	Pair<Double, StatisticalData> run(GameState gs, UnitTypeTable utt, int player, int max_cycle, AI ai1, AI ai2,
			boolean exibe) throws Exception;
}
