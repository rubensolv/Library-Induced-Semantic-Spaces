package ai.synthesis.dreamcoder.EvaluateGameState;

public interface IFeatureFactory {

    Feature create(BehavioralFeature cd);

    Feature create();

    Feature create(String novS);
}
