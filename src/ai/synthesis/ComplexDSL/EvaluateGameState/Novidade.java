package ai.synthesis.ComplexDSL.EvaluateGameState;

import java.util.List;

public interface Novidade {
	int compareTo(Novidade n);
    public int hashCode();
    Novidade Clone();
    void mutacao();
    double semelhaca(Novidade n);
    int[] convertList();
}
