/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests.guiValidation;

import ai.abstraction.WorkerRush;
import javax.swing.JOptionPane;

/**
 *
 * @author rubens
 */
public class ControlerExhibition {

    public static void main(String args[]) throws Exception {
        int option = 1;

        ObjectBattle bat = new ObjectBattle();
        while (!bat.gameover) {
            bat.runForOneFrame();
            if (bat.gs.getTime()%100==0) {
                option = JOptionPane.showConfirmDialog(null, "Gostaria de pausar?");
            }
            if (option == 0) {
                bat.ai1 = new WorkerRush(bat.utt);
                option = 1;
            }

        }

    }

}
