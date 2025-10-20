/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package MainPackage;

import MainClasses.Planificador;
import AuxClass.*;
import MainClasses.*;
import java.awt.FontFormatException;
import java.io.IOException;
import javax.swing.SwingUtilities;

/**
 *
 * @author chela
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FontFormatException, IOException {
        
        App app = App.getInstance();
        
        app.start();
        app.start2();

    }

}

