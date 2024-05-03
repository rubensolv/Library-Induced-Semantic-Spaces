/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ai.synthesis.dreamcoder.runners;

import ai.core.AI;
import static ai.synthesis.ComplexDSL.Tests.Teste3.getMap;
import ai.synthesis.dreamcoder.ComplexDSL.IAs.HC;
import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.Empty_LS;
import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.FactoryLS;
import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.Node_LS;
import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.S_LS;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.AIs.Interpreter;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.Control;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.Factory;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.Node;
import ai.synthesis.dreamcoder.EvaluateGameState.BehavioralFeature;
import ai.synthesis.dreamcoder.EvaluateGameState.Playout;
import ai.synthesis.dreamcoder.EvaluateGameState.SimplePlayout;
import ai.synthesis.dreamcoder.basicAlgorithms.DefaultEvaluation;
import ai.synthesis.dreamcoder.basicAlgorithms.WrapperAlgorithm;
import ai.synthesis.dreamcoder.compression.BasicElimEquvalent;
import ai.synthesis.dreamcoder.compression.iRemoveEquivalent;
import ai.synthesis.dreamcoder.compression.inputs.InputsForBT;
import ai.synthesis.dreamcoder.compression.inputs.iInputsBottomUp;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import rts.GameState;
import rts.PhysicalGameState;
import rts.units.UnitTypeTable;
import util.Pair;

/**
 *
 * @author rubens
 */
public class MainLoopDreamCoder {

    protected static int NUMBER_OF_ITERATION = 10;
    static Factory f = new FactoryLS();
    static HashMap<String, Node_LS> library = new HashMap<>();

    public static void main(String[] args) throws Exception {
        //settings 
        UnitTypeTable utt = new UnitTypeTable();
        String path_map = getMap("0");
        PhysicalGameState pgs = PhysicalGameState.load(path_map, utt);
        GameState gs2 = new GameState(pgs, utt);

        //test para quebrar fragmentos
        List<Node> nodes = get_ast_as_node(utt, System.getProperty("user.dir").concat("/logs2/"));

        //run an algorithm and collect the solution
        Node_LS current_solution = new S_LS(new Empty_LS()); // Inicializing the current empty solution            

        for (int i = 0; i < NUMBER_OF_ITERATION; i++) {
            //default solution initialization
            DefaultEvaluation evaluator = new DefaultEvaluation(1000);
            evaluator.resetListandSetCurrentSolution(current_solution);

            WrapperAlgorithm runner = new WrapperAlgorithm(new HC(200), evaluator);
            current_solution = runner.run(gs2, 6000, Float.valueOf(0.2f), library);

            List<Node_LS> solutions = evaluator.getAllIndividuos();
            Node_LS enemy = evaluator.getIndividuo();
            Pair<Double, BehavioralFeature> features = collect_features(enemy, current_solution, utt, pgs, gs2);
            //let's reduce the codes by behavior (observacao) in this moment
            List<Node_LS> reduced_solution = perform_reduction(solutions, path_map);

            //let's break it in pieces (?)
            HashSet<Node_LS> frags = get_all_frags_from_nodes_LS(reduced_solution);

            // apply reduction proccess
            //Key= string definition, value= object 
            library = shink_frags(frags, MainLoopDreamCoder.library);

        }
    }

    private static List<iInputsBottomUp> get_all_inputs() {
        List<iInputsBottomUp> inputs = new ArrayList<>();
        String path = System.getProperty("user.dir").concat("/inputs_synthesis/");
        File folder = new File(path);
        for (String f : folder.list()) {
            iInputsBottomUp inp = new InputsForBT();
            inp.setPath(path.concat(f));
            inputs.add(inp);
        }

        return inputs;
    }

    private static List<Node_LS> perform_reduction(List<Node_LS> solutions, String path_map) {
        iRemoveEquivalent elimEquval = new BasicElimEquvalent();
        List<iInputsBottomUp> inputs = get_all_inputs();
        return elimEquval.removeEquivalents(solutions, inputs);
    }

    private static List<Node> get_ast_as_node(UnitTypeTable utt, String pathDSLs) {
        System.out.println("path ASts " + pathDSLs);
        String linha = "";
        List<Node> asts = new ArrayList<>();
        File folder = new File(pathDSLs);
        for (String ast : folder.list()) {
            if (ast.contains(".txt")) {
                File file = new File(pathDSLs + "/" + ast);
                Node pre = null;
                try {
                    FileReader arq = new FileReader(file);
                    java.io.BufferedReader learArq = new BufferedReader(arq);
                    linha = learArq.readLine();
                    while (linha != null) {
                        pre = Control.load(linha, f);
                        asts.add(pre);
                        linha = learArq.readLine();
                    }
                    arq.close();
                } catch (Exception e) {
                    System.err.printf("Erro na leitura da linha de IA");
                    System.out.println(e.toString());
                }
            }
        }

        return asts;
    }

    private static HashSet<Node_LS> get_all_frags_from_nodes(List<Node> nodes) {
        HashSet<Node_LS> ret = new HashSet<>();
        for (Node node : nodes) {
            Node_LS n = (Node_LS) node;
            HashSet<Node_LS> ns = new HashSet<>(n.getAllNodes());
            ret.addAll(ns);
        }
        return ret;
    }

    private static HashSet<Node_LS> get_all_frags_from_nodes_LS(List<Node_LS> nodes) {
        HashSet<Node_LS> ret = new HashSet<>();
        for (Node_LS node : nodes) {
            HashSet<Node_LS> ns = new HashSet<>(node.getAllNodes());
            ret.addAll(ns);
        }
        return ret;
    }

    private static HashMap<String, Node_LS> shink_frags(HashSet<Node_LS> frags, HashMap<String, Node_LS> library) {

        for (Node_LS frag : frags) {
            String def = frag.translate();
            if (!library.containsKey(def)) {
                library.put(def, frag);
            }
        }

        return library;
    }

    private static Pair<Double, BehavioralFeature> collect_features(Node_LS enemy, Node_LS current_solution, UnitTypeTable utt, PhysicalGameState pgs, GameState gs2) throws Exception {
        Playout play = new SimplePlayout();
        AI ai1 = new Interpreter(utt, enemy);
        AI ai2 = new Interpreter(utt, current_solution);
        return play.run(gs2, utt, 0, 6000, ai1, ai2, false);
    }

}
