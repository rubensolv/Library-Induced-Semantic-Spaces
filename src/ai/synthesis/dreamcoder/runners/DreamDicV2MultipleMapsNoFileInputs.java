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
import ai.synthesis.dreamcoder.basicAlgorithms.DefaultEvaluation;
import ai.synthesis.dreamcoder.basicAlgorithms.LocalLearner;
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
import java.util.Random;
import rts.GameState;
import rts.PhysicalGameState;
import rts.units.UnitTypeTable;
import java.util.UUID;
import rts.PlayerAction;

/**
 *
 * @author rubens
 */
public class DreamDicV2MultipleMapsNoFileInputs {

    protected static int NUMBER_OF_ITERATION = 400;
    private static String uniqueID = UUID.randomUUID().toString();
    static Factory f = new FactoryLS();
    static HashMap<String, Node_LS> library = new HashMap<>();
    static Random rand = new Random(NUMBER_OF_ITERATION);
    static Float epsilon_0 = 0.4f;
    static List<iInputsBottomUp> inputs = new ArrayList<>();
    static int max;

    public static void main(String[] args) throws Exception {
        //settings 
        UnitTypeTable utt = new UnitTypeTable();
        ArrayList<String> maps = new ArrayList<>();
        maps.add(getMap("0"));
//        maps.add(getMap("2"));
//        maps.add(getMap("3"));
//        maps.add(getMap("5"));
//        maps.add(getMap("9"));
        
        // get nodes and compose initial dictionary based on the last map
        List<Node> nodes = get_ast_as_node(utt, System.getProperty("user.dir").concat("/logs2/"));
        HashMap<String, Node> dic_by_scketch = compose_sketch_dic(nodes);
        //run an algorithm and collect the solution
        Node_LS current_solution = new S_LS(new Empty_LS()); // Inicializing the current empty solution              

        /*
        Comment the following lines in case the library is generated from scratch.
        Starting non-scratch inicialization
         */
        HashSet<Node_LS> fragst = new HashSet<>();
        fragst.addAll(get_all_frags_from_nodes(nodes));
        library = shrink_frags(fragst, library);
        System.out.println("#Library size:" + library.size());
        // finish non-scratch inicialization
        for (String path_map : maps) {
            //IBR, FP, DO and 2L
            DefaultEvaluation evaluator = new DefaultEvaluation(1); //IBR
            //DefaultEvaluation evaluator = new DefaultEvaluation(1000); //FP        
            //DO evaluator = new DO();
            //LocalLearner evaluator = new LocalLearner();
            System.out.println("Map for budget: " + path_map);
            PhysicalGameState pgs = PhysicalGameState.load(path_map, utt);
            max = get_game_lenght(pgs);
            GameState gs2 = new GameState(pgs, utt);

            for (int i = 0; i < NUMBER_OF_ITERATION; i++) {
                System.out.println("Iteration " + i);
                //default solution initialization            
                evaluator.resetListandSetCurrentSolution(current_solution);
                //evaluator.recalcula(gs2, max, current_solution);

                WrapperAlgorithm runner = new WrapperAlgorithm(new HC(200), evaluator);
                current_solution = runner.run(gs2, max, epsilon_0, library);

                List<Node_LS> solutions = evaluator.getAllIndividuos();

                //let's reduce the codes by behavior (observacao)
                generate_input_stats(current_solution, evaluator.getBest(), gs2.clone(), max, utt);
                List<Node_LS> reduced_solution = perform_reduction(solutions, path_map);

                //let's break it in pieces (?)            
                HashSet<Node_LS> frags = get_all_frags_from_nodes_LS(reduced_solution);
                //double score = evaluator.Avalia(gs2, max, current_solution);            
                // apply reduction proccess
                //Key= string definition, value= object 
                library = shrink_frags(frags, library);
                System.out.println("#Library size:" + library.size());
            }
        }
    }

    public static HashMap<String, Node> compose_sketch_dic(List<Node> nodes) {
        HashMap<String, Node> dic_by_scketch = new HashMap<>();
        for (Node node : nodes) {
            HashSet<Node_LS> frags = get_all_frags_from_node(node);
            for (Node_LS frag : frags) {
                if (!dic_by_scketch.containsKey(frag.getName())) {
                    dic_by_scketch.put(frag.getName(), node);
                }
            }
        }
        return dic_by_scketch;
    }

    private static List<Node_LS> perform_reduction(List<Node_LS> solutions, String path_map) {
        if (inputs.size() >= 1) {
            iRemoveEquivalent elimEquval = new BasicElimEquvalent();
            return elimEquval.removeEquivalents(solutions, inputs);
        }
        return solutions;
    }

    private static List<Node> get_ast_as_node(UnitTypeTable utt, String pathDSLs) {
        System.out.println("path ASts " + pathDSLs);
        String linha = "";
        List<Node> asts = new ArrayList<>();
        File folder = new File(pathDSLs);
        for (String ast : folder.list()) {
            if (ast.contains(".txt")) {
                File file = new File(pathDSLs + "/" + ast);
                System.out.println("Original dic " + file);
                Node pre = null;
                try {
                    FileReader arq = new FileReader(file);
                    java.io.BufferedReader learArq = new BufferedReader(arq);
                    linha = learArq.readLine();

                    while (linha != null) {
                        try {
                            pre = Control.load(linha, f);
                            asts.add(pre);
                            linha = learArq.readLine();
                        } catch (Exception e) {
                            System.err.printf("Erro na leitura da linha de IA" + linha);
                            linha = learArq.readLine();
                        }

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

    private static HashSet<Node_LS> get_all_frags_from_node(Node node) {
        HashSet<Node_LS> ret = new HashSet<>();
        Node_LS n = (Node_LS) node;
        HashSet<Node_LS> ns = new HashSet<>(n.getAllNodes());
        ret.addAll(ns);
        return ret;
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

    /**
     * Shrink by similarity based on the string python like translation
     *
     * @param frags
     * @param library
     * @return
     */
    private static HashMap<String, Node_LS> shrink_frags(HashSet<Node_LS> frags, HashMap<String, Node_LS> library) {
        for (Node_LS frag : frags) {
            String def = frag.translate();
            if (!library.containsKey(def)) {
                library.put(def, frag);
            }
        }
        return library;
    }

    private static void generate_input_stats(Node_LS current_solution, Node_LS best, GameState gs, int MAXCYCLES, UnitTypeTable utt) throws Exception {
        System.out.println("---------AI's---------");
        inputs.clear();
        AI ai1 = new Interpreter(utt, current_solution);
        AI ai2 = new Interpreter(utt, best);
        boolean gameover = false;
//        System.out.println("AI 1 = " + ai1.toString());
//        System.out.println("AI 2 = " + ai2.toString() + "\n");
        ai1.preGameAnalysis(gs, 1);
        ai2.preGameAnalysis(gs, 1);

        do {
            if (gs.canExecuteAnyAction(0) || gs.canExecuteAnyAction(1)) {
                inputs.add(new InputsForBT(gs.clone()));
            }

            PlayerAction pa1 = ai1.getAction(0, gs);
            PlayerAction pa2 = ai2.getAction(1, gs);
            gs.issueSafe(pa1);
            gs.issueSafe(pa2);

            // simulate:
            gameover = gs.cycle();

        } while (!gameover && gs.getTime() < MAXCYCLES);

//        System.out.println("Winner " + Integer.toString(gs.winner()));
//        System.out.println("Game Over");
    }

    private static void check_if_inputs_folder_exist(String folderInputs) {
        String path = System.getProperty("user.dir").concat("/" + folderInputs + "/");
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    private static int get_game_lenght(PhysicalGameState pgs) {
        switch (pgs.getHeight()) {
            case 8:
                return 3000;
            case 16:
                return 4000;
                //MAXCYCLES = 1000;
            case 24:
                return 5000;
            case 32:
                return 6000;
            case 64:
                return 8000;
            default:
                return 12000;
        }        
    }

}
