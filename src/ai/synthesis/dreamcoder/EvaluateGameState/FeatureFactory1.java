package ai.synthesis.dreamcoder.EvaluateGameState;


public class FeatureFactory1 implements IFeatureFactory {

    public FeatureFactory1() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public Feature create(BehavioralFeature cd) {
        // TODO Auto-generated method stub
        return new Feature(cd);
    }

    @Override
    public Feature create() {
        // TODO Auto-generated method stub
        return new Feature();
    }

    @Override
    public Feature create(String novS) {
        // TODO Auto-generated method stub
        String dados[] = novS.split(" ");
        int w = Integer.parseInt(dados[0].substring(0, dados[0].length() - 1));
        int l = Integer.parseInt(dados[1].substring(0, dados[1].length() - 1));;
        int r = Integer.parseInt(dados[2].substring(0, dados[2].length() - 1));;
        int h = Integer.parseInt(dados[3].substring(0, dados[3].length() - 1));;
        int ba = Integer.parseInt(dados[4].substring(0, dados[4].length() - 2));;
        int br = Integer.parseInt(dados[5].substring(0, dados[5].length() - 2));;
        int re = Integer.parseInt(dados[6].substring(0, dados[6].length() - 2));;;
        return this.create(new BehavioralFeature(w, l, r, h, ba, br, re));

    }

}
