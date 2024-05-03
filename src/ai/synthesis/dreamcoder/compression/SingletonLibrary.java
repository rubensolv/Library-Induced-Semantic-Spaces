/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ai.synthesis.dreamcoder.compression;

import ai.synthesis.dreamcoder.ComplexDSL.LS_CFG.Node_LS;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rubens
 */
public class SingletonLibrary {
    
    
    private static SingletonLibrary instance = null;
    List<Node_LS> concepts;

    private SingletonLibrary() {
        this.concepts = new ArrayList<>();
    }

    public List<Node_LS> getConcepts() {
        return this.concepts;
    }

    public void setConcepts(List<Node_LS> concepts) {
        this.concepts = concepts;
    }
    
    public void includeConcept(Node_LS concept){
        this.concepts.add(concept);
    }
    
    public void addConcepts(List<Node_LS> concepts){
        this.concepts.addAll(concepts);
    }
    public static synchronized SingletonLibrary getInstance(){
        if (instance == null) {
            instance = new SingletonLibrary();
        }
        return instance;
    }
    
    public Node_LS getRandomNodeByTipe(Node_LS reference){
        return null;
    }
    
    
}
