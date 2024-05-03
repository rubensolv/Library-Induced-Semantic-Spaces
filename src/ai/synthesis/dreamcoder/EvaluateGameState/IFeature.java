package ai.synthesis.dreamcoder.EvaluateGameState;

public interface IFeature {

    int compareTo(IFeature n);

    @Override
    public int hashCode();

    IFeature clone();

    void mutation();

    double similarity(IFeature n);

    int[] convertList();
}
