/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ai.synthesis.dreamcoder.runners;

import ai.core.AI;
import static ai.synthesis.ComplexDSL.Tests.Teste3.getMap;
import ai.synthesis.dreamcoder.ComplexDSL.IAs.HC;
import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.C_LS;
import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.Empty_LS;
import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.FactoryLS;
import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.NoTerminal_LS;
import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.Node_LS;
import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.S_LS;
import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.S_S_LS;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.AIs.Interpreter;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.ChildC;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.ChildS;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.Control;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.Factory;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.Node;
import ai.synthesis.dreamcoder.basicAlgorithms.DO;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import rts.GameState;
import rts.PhysicalGameState;
import rts.units.UnitTypeTable;
import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Objects;
import java.util.UUID;
import rts.PlayerAction;

/**
 *
 * @author rubens
 */
public class BaselineOneNoModelGenStates {

    protected static int NUMBER_OF_ITERATION = 10000;
    private static String uniqueID = UUID.randomUUID().toString();
    static Factory f = new FactoryLS();
    static HashMap<String, Node_LS> library = new HashMap<>();
    static Random rand = new Random(NUMBER_OF_ITERATION);
    static Float epsilon_0 = 0.0f;
    static List<iInputsBottomUp> inputs = new ArrayList<>();
    static String folderInputs;

    public static void main(String[] args) throws Exception {
        //settings 
        folderInputs = "inputs_synthesis"+uniqueID.substring(0, 4);
        UnitTypeTable utt = new UnitTypeTable();
        String path_map = getMap("3");
        System.out.println(path_map+" mapa validacao baseline");
        PhysicalGameState pgs = PhysicalGameState.load(path_map, utt);
        GameState gs2 = new GameState(pgs, utt);
        int max = 6000;

        //test para quebrar fragmentos
        List<Node> nodes = get_ast_as_node(utt, System.getProperty("user.dir").concat("/logs2/"));

        //run an algorithm and collect the solution
        Node_LS current_solution = new S_LS(new Empty_LS()); // Inicializing the current empty solution      
        //IBR, FP, DO and 2L
        //DefaultEvaluation evaluator = new DefaultEvaluation(1); //IBR
        //DefaultEvaluation evaluator = new DefaultEvaluation(1000); //FP        
        //DO evaluator = new DO();
        LocalLearner evaluator = new LocalLearner();
        for (int i = 0; i < NUMBER_OF_ITERATION; i++) {
            clear_all_inputs();
            //default solution initialization            
            //evaluator.resetListandSetCurrentSolution(current_solution);            
            evaluator.recalcula(gs2, max, current_solution);

            WrapperAlgorithm runner = new WrapperAlgorithm(new HC(100), evaluator);
            current_solution = runner.run(gs2, max, 0.2f, library);

            List<Node_LS> solutions = evaluator.getAllIndividuos();

            //let's reduce the codes by behavior (observacao) in this moment
            generate_input_stats(current_solution, evaluator.getBest(), gs2.clone(), max, utt);
            inputs = get_all_inputs();
            List<Node_LS> reduced_solution = perform_reduction(solutions, path_map);

            //let's break it in pieces (?)            
            HashSet<Node_LS> frags = get_all_frags_from_nodes_LS(reduced_solution);
            frags.addAll(get_all_frags_from_nodes(nodes));
            //current_solution = (Node_LS) nodes.get(rand.nextInt(nodes.size()));
            double score = evaluator.Avalia(gs2, max, current_solution);
            //evaluator.update(gs2, max, current_solution);
            // apply reduction proccess
            //Key= string definition, value= object 
            library = shink_frags(frags, library);
            if (rand.nextFloat() >= epsilon_0) {
                System.out.println("Starting Collection from library");
                //run HC considering guidance model
                //1 - build up all the solutions
                List<Node_LS> to_eval = prepare_list(current_solution, library);
                while (to_eval.size() < 50) {
                    to_eval.addAll(prepare_list(current_solution, library));
                }
                System.out.println("Number of new asts:" + to_eval.size());
                //2 - eval the new strategies

                //3 - evaluate all the results\   
                int limit = 0;
                for (Node_LS ast_to_eval : to_eval) {
                    limit++;
                    double v2 = evaluator.Avalia(gs2, max, ast_to_eval);
                    if (v2 > score) {
                        evaluator.update(gs2, max, ast_to_eval);
                        System.out.println("Updated strategy");
                        System.out.println("From " + Control.save((Node) current_solution));
                        System.out.println("To " + Control.save((Node) ast_to_eval));
                        current_solution = ast_to_eval;
                        score = v2;
                    }
                    if (limit >= 10) {
                        //System.out.println("Stopped into 50");
                        break;
                    }
                }
                //keep running the algorithm

            }//else keep running the algorithm
        }
    }

    private static void clear_all_inputs() {
        String path = System.getProperty("user.dir").concat("/"+folderInputs+"/");
        File directory = new File(path);
        if (directory.exists()) {
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                if (!file.isDirectory()) {
                    file.delete();
                }
            }
        }

    }

    private static List<iInputsBottomUp> get_all_inputs() {
        List<iInputsBottomUp> inputs = new ArrayList<>();
        String path = System.getProperty("user.dir").concat("/"+folderInputs+"/");
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
        }
        for (String f : folder.list()) {
            iInputsBottomUp inp = new InputsForBT();
            inp.setPath(path.concat(f));
            inputs.add(inp);
        }

        return inputs;
    }

    private static List<Node_LS> perform_reduction(List<Node_LS> solutions, String path_map) {
        iRemoveEquivalent elimEquval = new BasicElimEquvalent();
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

    private static List<Node_LS> prepare_list_old(Node_LS current_solution, HashMap<String, Node_LS> library) {
        Node_LS aux = (Node_LS) (current_solution.Clone(f));
        List<Node_LS> list = new ArrayList<>();
        aux.countNode(list);
        int cost = rand.nextInt(9) + 1;
        int node_number = rand.nextInt(list.size());
        Node_LS cut_point = list.get(node_number);
//        while ( !(cut_point instanceof S_LS)) {            
//            node_number = rand.nextInt(list.size());
//            cut_point = list.get(node_number);
//        }

        S_LS s_cut_point = null;
        //build all the possible compositions
        list.get(node_number).mutation(0, cost, false);
        HashSet<Node_LS> all_combinations = new HashSet<>();
        for (Map.Entry<String, Node_LS> entry : library.entrySet()) {
//            System.out.println(aux.translate());
            String key = entry.getKey();
            Node_LS val = entry.getValue();

            Node_LS temp_node = (Node_LS) cut_point.Clone(f);
            s_cut_point = (S_LS) temp_node;
            if (val instanceof S_S_LS) {
                s_cut_point.setChild((S_S_LS) val);
//                System.out.println(aux.translate());
//                System.out.println(s_cut_point.translate());
                all_combinations.add(s_cut_point);
            } else {
                //s_cut_point.setChild((ChildS) val);
                //s_cut_point.setChild((ChildS)(Node)val);
            }

//            if (cut_point instanceof S_LS) {
//                s_cut_point = (S_LS) cut_point;            
//            }
        }

        return new ArrayList<Node_LS>(all_combinations);
    }

    private static List<Node_LS> prepare_list(Node_LS current_solution, HashMap<String, Node_LS> library) {
        HashSet<Node_LS> all_combinations = new HashSet<>();
        Node_LS aux = (Node_LS) (current_solution.Clone(f));
        List<Node_LS> list = new ArrayList<>();
        aux.countNode(list);
        int cost = rand.nextInt(9) + 1;
        int node_number = rand.nextInt(list.size());
        Node_LS cut_point = list.get(node_number);

        if (cut_point instanceof NoTerminal_LS) {
            Node_LS child = ((NoTerminal_LS) cut_point).sorteiaFilho(cost);
            for (Node_LS value : library.values()) {
                if (value.getClass().equals(child.getClass())) {
                    if (cut_point instanceof S_LS) {
                        ((S_LS) (cut_point)).setChild((ChildS) value.Clone(f));
                    } else if (cut_point instanceof C_LS) {
                        ((C_LS) cut_point).setChildC((ChildC) value.Clone(f));
                    } else {
                        System.out.println(cut_point.getClass());
                    }
                    all_combinations.add(aux);
                    aux = (Node_LS) (current_solution.Clone(f));
                    list.clear();
                    aux.countNode(list);
                    cut_point = list.get(node_number);
                }
            }
        }
        return new ArrayList<Node_LS>(all_combinations);
    }

    private static void prepare_csv_to_model(List<Node_LS> to_eval, Node_LS current_solution) {
        String filePath = System.getProperty("user.dir").concat("/input/" + uniqueID + ".csv");
        //String filePath = "/home/rubens/pythonProjects/pytorch_MicroRTS/input/onebatch24example.csv";
        String enemy = current_solution.translate();
        File file = new File(filePath);
        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);
            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);
            // adding header to csv
            String[] header = {"strategy", "enemy", "label"};
            writer.writeNext(header);
            List<String[]> data = new ArrayList<String[]>();
            for (Node_LS node_LS : to_eval) {
                // create a List which contains String array                
                data.add(new String[]{node_LS.translate(), enemy, ""});
            }
            writer.writeAll(data);
            // closing writer connection
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private static List<Double> get_results_from_model() {
        String s = null;
        List<Double> results = new ArrayList<>();
        try {

            // run the Unix "ps -ef" command
            // using the Runtime exec method:
            Process p = Runtime.getRuntime().exec("python3 modelEvaluateLSTM.py " + uniqueID);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            // read the output from the command
            //System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                //System.out.println(s);

                String value = s.substring(s.indexOf(".") - 1, s.indexOf("]"));
                //System.out.println("Value "+ value);
                results.add(Double.parseDouble(value));
            }

            // read any errors from the attempted command
//            System.out.println("Here is the standard error of the command (if any):\n");
//            while ((s = stdError.readLine()) != null) {
//                System.out.println(s);
//            }
            //System.exit(0);
        } catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            //System.exit(-1);
        }

        return results;

    }

    private static HashMap<Double, Node_LS> concat_score_ast(List<Node_LS> to_eval, List<Double> results) {
        System.out.println("-" + to_eval.size());
        System.out.println("-" + results.size());
        HashMap<Double, Node_LS> dic = new HashMap<>();
        for (int i = 0; i < (to_eval.size()) - 1; i++) {
            dic.put(results.get(i), to_eval.get(i));
        }

        return dic;
    }

    private static void clean_old_files() {
        File myObj = new File(System.getProperty("user.dir").concat("/input/" + uniqueID + ".csv"));
        myObj.delete();
        myObj = new File(System.getProperty("user.dir").concat("/input/san_" + uniqueID + ".csv"));
        myObj.delete();
    }
    
    private static void saveState(GameState gs) throws IOException, Exception {
        String uniqueID = UUID.randomUUID().toString();
        int cicle = gs.getTime();
        Writer writer = new FileWriter(System.getProperty("user.dir").concat("/"+folderInputs+"/")
                .concat(uniqueID+"_"+cicle+".txt"));
        gs.toJSON(writer);
        writer.flush();
        writer.close();
    }

    private static void generate_input_stats(Node_LS current_solution, Node_LS best, GameState gs, int MAXCYCLES, UnitTypeTable utt) throws Exception {
        System.out.println("---------AI's---------");
        AI ai1 = new Interpreter(utt, current_solution);
        AI ai2 = new Interpreter(utt, best);
        boolean gameover = false;
//        System.out.println("AI 1 = " + ai1.toString());
//        System.out.println("AI 2 = " + ai2.toString() + "\n");
        ai1.preGameAnalysis(gs, 1);
        ai2.preGameAnalysis(gs, 1);
        
        //settings for save
        File folder = new File(System.getProperty("user.dir").concat("/"+folderInputs+"/"));
        if(!folder.exists()){
            folder.mkdir();
        }
        
        do {
                if(gs.canExecuteAnyAction(0) || gs.canExecuteAnyAction(1)){
                    saveState(gs);
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

}
