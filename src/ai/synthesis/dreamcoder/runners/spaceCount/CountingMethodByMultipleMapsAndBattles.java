/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ai.synthesis.dreamcoder.runners.spaceCount;

import ai.core.AI;
import static ai.synthesis.ComplexDSL.Tests.Teste3.getMap;
import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.C_LS;
import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.Empty_LS;
import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.FactoryLS;
import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.NoTerminal_LS;
import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.Node_LS;
import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.S_LS;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.AIs.Interpreter;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.ChildC;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.ChildS;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.Control;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.Factory;
import ai.synthesis.dreamcoder.ComplexDSL.Synthesis_Base.CFG.Node;
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
import java.util.Random;
import rts.GameState;
import rts.PhysicalGameState;
import rts.units.UnitTypeTable;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import rts.PlayerAction;

/**
 * Este teste contem uma biblioteca gigantesca (22k) inicial baseado nos
 * fragmentos do mapa 24x24. A solucao inicial eh gerada a partir de uma solucao
 * vazia.
 *
 * @author rubens
 */
public class CountingMethodByMultipleMapsAndBattles {

    protected static int NUMBER_OF_ITERATION = 10000;
    private static String uniqueID = UUID.randomUUID().toString();
    static Factory f = new FactoryLS();
    static HashMap<String, Node_LS> library = new HashMap<>();
    static Random rand = new Random(NUMBER_OF_ITERATION);
    static Float epsilon_0 = 0.0f;
    static Random r;
    static List<iInputsBottomUp> inputs = get_all_inputs();

    public static void main(String[] args) throws Exception {
        //settings 
        UnitTypeTable utt = new UnitTypeTable();
//        Node_LS str1 = get_random_solution(utt, System.getProperty("user.dir").concat("/logs3/"));
//        System.err.println(" -- "+str1.translate());
//        Node_LS str2 = get_random_solution(utt, System.getProperty("user.dir").concat("/logs3/"));
//        System.err.println(" -- "+str2.translate());
//        boolean equal = is_strategies_behavioral(utt, str1, str2);

        String path_map = getMap("1");
        System.out.println(path_map);
        PhysicalGameState pgs = PhysicalGameState.load(path_map, utt);
        GameState gs2 = new GameState(pgs, utt);
        int max = get_game_lenght(pgs);
        r = new Random();
        BasicElimEquvalent elimEquval = new BasicElimEquvalent();

        // get nodes and compose initial dictionary based on the last map
        List<Node> nodes = get_ast_as_node(utt, System.getProperty("user.dir").concat("/logs2/"));
        HashSet<Node_LS> fragst = new HashSet<>();
        fragst.addAll(get_all_frags_from_nodes(nodes));
        System.out.println("#Library size (pre shrink):" + library.size());
        library = shrink_frags(fragst, library);
        System.out.println("#Library size:" + library.size());
//        List<Node_LS> fragst_S = new ArrayList<>(library.values());
//        fragst_S = elimEquval.removeEquivalents(fragst_S, inputs);
//        library.clear();
//        library = shrink_frags(new HashSet<>(fragst_S), library);
//        System.out.println("#Library size:" + library.size());
        int count_full = 0;
        for (int t = 0; t < 10; t++) {
            //run an algorithm and collect the solution
            //Step 1: Gere X programas p aleatoriamente, exatamente como você faz para gerar o primeiro programa da busca da primeira iteração do 2L.
            Node_LS current_solution = get_random_solution(utt, System.getProperty("user.dir").concat("/logs3/")); // Inicializing the current empty solution                  
//            Node_LS current_solution = new S_LS(new Empty_LS());
//            for (int i = 0; i < 20; i++) {
//                Node_LS aux = (Node_LS) (current_solution.Clone(f));
//                List<Node_LS> list = new ArrayList<>();
//                for (int ii = 0; ii < 1; ii++) {
//
//                    aux.countNode(list);
//                    int custo = r.nextInt(9) + 1;
//                    int no = r.nextInt(list.size());
//
//                    list.get(no).mutation(0, custo, false);
//
//                }
//                current_solution = (Node_LS) aux.Clone(f);
//            }

            System.out.println(current_solution.translate());
            //Step 2 Para cada programa p gere sua vizinhança:
            ArrayList<Node_LS> neighbor_p = new ArrayList<>();
            //for (int i = 0; i < 1000; i++) {
            int N = 0;
            while (neighbor_p.size() < 1000) {
                Node_LS aux = (Node_LS) (current_solution.Clone(f));
                List<Node_LS> list = new ArrayList<>();
                //for (int ii = 0; ii < 1; ii++) {
                aux.countNode(list);
                int custo = r.nextInt(9) + 1;
                int no = r.nextInt(list.size());
                //System.out.println("Initial Solution " +  Control.save((Node) aux)) ;
                if (!library.isEmpty() && rand.nextFloat() >= epsilon_0) {
                    aux = prepare_mutation_by_library(aux, library);
                    aux = (Node_LS) (aux.Clone(f));
                    list = new ArrayList<>();
                    //epsilon_0_temp = epsilon_0_temp + epsilon_0;
                } else {
                    list.get(no).mutation(0, custo, false);

                }
                //if (!is_in_list(neighbor_p, aux)) {
                Node_LS to_eval = (Node_LS) (aux.Clone(f));
                //if (!elimEquval.findTargetEquivalents(current_solution, to_eval, inputs)) {
                //System.out.println("current_solution "+current_solution.translate());
                //System.out.println("to_eval "+to_eval.translate());
                Boolean result = is_strategies_behavioral(utt, current_solution, to_eval);
                if (result != null) {
                    if (!is_in_list(neighbor_p, to_eval)) {
                        neighbor_p.add(to_eval);
                        if (result) {
                            N++;
                            if (N % 100 == 0) {
                                System.out.println("N is current..." + N + "  in " + neighbor_p.size());
                            }
                        }
                    }
                }
                //}

                //System.out.println("Modified Solution " + Control.save((Node) aux));                    
            }
            //System.out.println("");
//            for (Node_LS node_LS : neighbor_p) {
//                System.out.println(node_LS.translate());
//            }
            //System.out.println("Neighbor size" + neighbor_p.size());
            //elimEquval.removeEquivalents(neighbor_p, inputs);

//            for (Node_LS node_LS : neighbor_p) {
//                System.out.println(" neighbor_p: "+ node_LS.translate());
//            }
//            
//            for (Node_LS node_LS : p) {
//                System.out.println("p: "+ node_LS.translate());
//            }
//            int repetition_count = 0;
//            int M = 0;
//            for (Node_LS node_LS : neighbor_p) {                
//                    //if (node_LS.toString().contains(node_LS1.toString())) {
//                    if (elimEquval.findTargetEquivalents(node_LS, p, inputs)) {
//                        M++;
//                    }                
//            }            
            System.out.println("-----> N:" + N + "  in " + neighbor_p.size());
            //System.out.println("X:"+ p.size()); 
        }
    }

    public static Boolean is_strategies_behavioral(UnitTypeTable utt, Node_LS str1, Node_LS str2) {
        String path_map = "";
        ArrayList<String> maps = new ArrayList<>();
        maps.add("1");
        maps.add("11");
        //maps.add("9");

        Node_LS node_enemy1 = get_random_solution(utt, System.getProperty("user.dir").concat("/logs3/")); // Inicializing the current empty solution                  
        AI enemy1 = new Interpreter(utt, node_enemy1);
        Node_LS node_enemy2 = (Node_LS) node_enemy1.Clone(f);
        AI enemy2 = new Interpreter(utt, node_enemy2);

        AI ai1 = new Interpreter(utt, str1);
        AI ai2 = new Interpreter(utt, str2);
        for (String map : maps) {
            path_map = getMap(map);
            //System.out.println(path_map);
            PhysicalGameState pgs1 = null;
            try {
                pgs1 = PhysicalGameState.load(path_map, utt);
            } catch (Exception ex) {
                Logger.getLogger(CountingMethodByMultipleMapsAndBattles.class.getName()).log(Level.SEVERE, null, ex);
            }
            PhysicalGameState pgs2 = null;
            try {
                pgs2 = PhysicalGameState.load(path_map, utt);
            } catch (Exception ex) {
                Logger.getLogger(CountingMethodByMultipleMapsAndBattles.class.getName()).log(Level.SEVERE, null, ex);
            }
            GameState gs1 = new GameState(pgs1, utt);
            GameState gs2 = new GameState(pgs2, utt);
            int MAXCYCLES = get_game_lenght(pgs1);

            boolean gameover = false;

            do {
                //game 1
                try {
                    PlayerAction g1pa1 = ai1.getAction(0, gs1);
                    PlayerAction g1pa2 = enemy1.getAction(1, gs1);

                    //game 2
                    PlayerAction g2pa1 = ai2.getAction(0, gs2);
                    PlayerAction g2pa2 = enemy2.getAction(1, gs2);

                    //perform comparation
                    if (!g1pa1.equalsFully(g2pa1)) {
                        //System.out.println("Diferent, stop the search!");
                        return false;
                    }

                    gs1.issueSafe(g1pa1);
                    gs1.issueSafe(g1pa2);

                    gs2.issueSafe(g2pa1);
                    gs2.issueSafe(g2pa2);

                    gameover = gs1.cycle();
                    gameover = gs2.cycle();
                } catch (Exception ex) {
                    return null;
                }

            } while (!gameover && (gs1.getTime() < MAXCYCLES));

        }

        return true;
    }

    private static Node_LS prepare_mutation_by_library(Node_LS current_solution, HashMap<String, Node_LS> library) {
        Node_LS aux = (Node_LS) (current_solution.Clone(f));
        List<Node_LS> list = new ArrayList<>();
        aux.countNode(list);
        int cost = rand.nextInt(9) + 1;
        int node_number = rand.nextInt(list.size());
        Node_LS cut_point = list.get(node_number);
        if (cut_point instanceof NoTerminal_LS) {
            Node_LS child = ((NoTerminal_LS) cut_point).sorteiaFilho(cost);
            ArrayList<Node_LS> mylist = new ArrayList(library.values());
            Collections.shuffle(mylist);
            for (Node_LS value : mylist) {
                //if (!this.used_frags.contains(value.translate())) {
                if (value.getClass().equals(child.getClass())) {
                    //String text_to_save = value.translate();
                    if (cut_point instanceof S_LS) {
                        ((S_LS) (cut_point)).setChild((ChildS) value.Clone(f));
                    } else if (cut_point instanceof C_LS) {
                        ((C_LS) cut_point).setChildC((ChildC) value.Clone(f));
                    } else {
                        //System.out.println(cut_point.getClass());
                    }
                    //this.used_frags.add(text_to_save);
                    return aux;
                } else if (rand.nextFloat() >= 0.5) {
                    return get_large_SLS(mylist);
                }
                //}
            }
        }
        return aux;
    }

    private static Node_LS get_large_SLS(ArrayList<Node_LS> mylist) {
        Node_LS aux = mylist.get(0);
        int len = aux.translate().length();
        for (Node_LS node_LS : mylist) {
            if (node_LS instanceof S_LS) {
                if (node_LS.translate().length() > len) {
                    aux = node_LS;
                    len = node_LS.translate().length();
                    if (len >= 50) {
                        return aux;
                    }
                }
            }
        }

        return aux;
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

    private static List<Node> get_ast_as_node(UnitTypeTable utt, String pathDSLs) {
        System.out.println("path ASts " + pathDSLs);
        String linha = "";
        List<Node> asts = new ArrayList<>();
        File folder = new File(pathDSLs);
        ArrayList<String> dicionaries = new ArrayList<>();
        for (String ast1 : folder.list()) {
            dicionaries.add(ast1);
        }
        Collections.shuffle(dicionaries);
        for (String ast : dicionaries) {
            if (ast.contains(".txt")) {
                System.out.println("Dictionary used " + ast);
                File file = new File(pathDSLs + "/" + ast);
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
                    return asts;
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

    private static Node_LS get_random_solution(UnitTypeTable utt, String pathDSLs) {
        //System.out.println("path strategies " + pathDSLs);
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
        Collections.shuffle(asts);
        return (Node_LS) asts.get(0);
    }

    private static boolean is_in_list(ArrayList<Node_LS> neighbor_p, Node_LS aux) {
        for (Node_LS node_LS : neighbor_p) {
            if (node_LS.translate().equals(aux.translate())) {
                return true;
            }
        }
        return false;
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
}
