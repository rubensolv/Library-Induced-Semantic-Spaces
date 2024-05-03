/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ai.synthesis.dreamcoder.DbEvaluationDataset.runner;

import ai.core.AI;
import static ai.synthesis.ComplexDSL.Tests.Teste3.getMap;
import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.FactoryLS;
import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.Node_LS;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.AIs.Interpreter;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.Control;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.Factory;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.Node;
import ai.synthesis.dreamcoder.DbEvaluationDataset.Battles;
import ai.synthesis.dreamcoder.DbEvaluationDataset.Behaviorfeature;
import ai.synthesis.dreamcoder.DbEvaluationDataset.Controllers.BattlesJpaController;
import ai.synthesis.dreamcoder.DbEvaluationDataset.Controllers.BehaviorfeatureJpaController;
import ai.synthesis.dreamcoder.DbEvaluationDataset.Controllers.StrategiesJpaController;
import ai.synthesis.dreamcoder.DbEvaluationDataset.Strategies;
import ai.synthesis.dreamcoder.EvaluateGameState.BehavioralFeature;
import ai.synthesis.dreamcoder.EvaluateGameState.DoubleFeaturePlayout;
import ai.synthesis.dreamcoder.EvaluateGameState.Playout;
import ai.synthesis.dreamcoder.EvaluateGameState.SimplePlayout;
import java.util.List;
import java.util.Random;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import rts.GameState;
import rts.PhysicalGameState;
import rts.units.UnitTypeTable;
import util.Pair;

/**
 *
 * @author rubens
 */
public class ExecuteBattleRandomly {

    static int max = 6000;
    static Random generator = new Random();

    static Factory f = new FactoryLS();

    static EntityManagerFactory factory = Persistence.createEntityManagerFactory("MicroRTSPU");
    static StrategiesJpaController jpaStrategies = new StrategiesJpaController(factory);
    static BattlesJpaController jpaBattle = new BattlesJpaController(factory);
    static BehaviorfeatureJpaController jpaBehave = new BehaviorfeatureJpaController(factory);

    public static void main(String args[]) throws Exception {
        List<Strategies> strategies = jpaStrategies.getIdsForMap("8x8");
        int size = strategies.size();
        for (int i = 0; i < 1000; i++) {
            runBattle(args[0], strategies, size);
            //runBattle("0", strategies, size);
        }

    }

    private static void runBattle(String map, List<Strategies> strategies,int size) throws Exception {
        //settings 
        UnitTypeTable utt = new UnitTypeTable();
        String path_map = getMap(map);
        PhysicalGameState pgs = PhysicalGameState.load(path_map, utt);
        GameState gs2 = new GameState(pgs, utt);

        
        Strategies player1 = strategies.get(generator.nextInt(size));
        Strategies player2 = strategies.get(generator.nextInt(size));
//        System.out.println("p1" + player1);
//        System.out.println("p2" + player2);
        if (!jpaBattle.checkIfBattleExistByPlayersID(player1, player2)) {
            //Battle begins
            Battles battle = new Battles();
            battle.setPlayer1Id(player1);
            battle.setPlayer2Id(player2);
            battle.setMap(path_map);
            jpaBattle.create(battle);

            //Convert to nodes
            Node a1 = Control.load(player1.getStrategy(), f);
            Node a2 = Control.load(player2.getStrategy(), f);
            //run the evaluation
            List<Pair<Double, BehavioralFeature>> features = collect_features(a1, a2, utt, pgs, gs2);
            Pair<Double, BehavioralFeature> rp1 = features.get(0);
            Behaviorfeature bh1 = new Behaviorfeature(rp1.m_b);
            jpaBehave.create(bh1);
            Pair<Double, BehavioralFeature> rp2 = features.get(1);
            Behaviorfeature bh2 = new Behaviorfeature(rp2.m_b);
            jpaBehave.create(bh2);
            //set winner
            if (rp1.m_a == 1.0) {
                battle.setWinner(1);
            } else if (rp1.m_a == -1.0) {
                battle.setWinner(2);
            } else {
                battle.setWinner(-1);
            }
            battle.setP1featuresId(bh1);
            battle.setP2featuresId(bh2);
            jpaBattle.edit(battle);
        }
    }

    private static List<Pair<Double, BehavioralFeature>> collect_features(Node enemy, Node current_solution, UnitTypeTable utt, PhysicalGameState pgs, GameState gs2) throws Exception {
        Playout play = new DoubleFeaturePlayout();
        AI ai1 = new Interpreter(utt, enemy);
        AI ai2 = new Interpreter(utt, current_solution);
        return play.runForBoth(gs2, utt, 0, max, ai1, ai2, false);
    }

    public static String getMap(String s) {
        if (s.equals("0")) {
            max = 3000;
            return "maps/8x8/basesWorkers8x8A.xml";
        }
        if (s.equals("1")) {
            return "maps/NoWhereToRun9x8.xml";
        }
        if (s.equals("2")) {
            max = 4000;
            return "maps/16x16/basesWorkers16x16A.xml";
        }
        if (s.equals("3")) {
            return "maps/24x24/basesWorkers24x24A.xml";
        }
        if (s.equals("4")) {
            return "maps/DoubleGame24x24.xml";
        }
        if (s.equals("5")) {
            return "maps/32x32/basesWorkers32x32A.xml";
        }
        if (s.equals("6")) {
            max = 8000;
            return "maps/BroodWar/(4)BloodBath.scmB.xml";
        }
        if (s.equals("7")) {
            return "maps/BWDistantResources32x32.xml";
        }
        if (s.equals("8")) {
            max = 8000;
            return "maps/GardenOfWar64x64.xml";
        }
        if (s.equals("9")) {
            return "maps/chambers32x32.xml";
        }
        if (s.equals("10")) {
            max = 12000;
            return "maps/BroodWar/(3)Aztec.scxA.xml";
        }

        return null;
    }

}
