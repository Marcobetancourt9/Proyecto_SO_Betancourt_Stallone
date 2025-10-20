/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI.Classes;

import AuxClass.Cola;
import AuxClass.List;
import AuxClass.Nodo;
import MainClasses.CPU;
import MainClasses.PCB;
import MainClasses.Proceso;
import MainClasses.ProcesoCPUBOUND;
import MainClasses.RegistrosControlEstado;
import MainPackage.App;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontFormatException;
import java.awt.Panel;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author marco
 */
public class Simulator extends javax.swing.JFrame {

    /**
     * Creates new form Simulator
     */
    private final App app = App.getInstance();

    private AtomicInteger cycleDurationParameter;
    private int processorsQuantity;
    private DefaultListModel[] modelosCPU;
    private int relojGlobalSimulator;
    private boolean flagLabel;

    private String currentAlgorithm;

    private Thread simulationThread;

    public Simulator(AtomicInteger cycleDurationParameter, int processorsQuantity, String currentAlgorithm) {

        initComponents();

        this.setResizable(false);
        this.setSize(1100, 605);
        this.setLocationRelativeTo(null);

        this.processorsQuantity = processorsQuantity;
        this.cycleDurationParameter = cycleDurationParameter;
        // OJITOSSS

        this.relojGlobalSimulator = app.relojGlobal;

        this.flagLabel = false;

        this.currentAlgorithm = currentAlgorithm;
        // CREACION DE PROCESADORES
        this.modelosCPU = createProcessors();

        // SET UP DE ALGORITMO EN APP Y EN SIMULADOR (GUI)
        // setAlgorithm(currentAlgorithm);
        app.getPlanificador().setNombreAlgoritmo(currentAlgorithm);

        // SET UP DEL VALOR DEL SPINNER EN LA SIMULACION
        updateSpinner();
        // INICIO DE SIMULACION
        startSimulation();

    }
    
    /*

    private void setAlgorithm(String currentAlgorithm) {
        for (int i = 0; i < currentAlgorithmComboBOX.getItemCount(); i++) {
            if (currentAlgorithmComboBOX.getItemAt(i).equals(currentAlgorithm)) {
                currentAlgorithmComboBOX.setSelectedItem(currentAlgorithm);
            }
        }
    }
*/
    private void startSimulation() {
        simulationThread = new Thread(() -> {
            while (true) {

                try {
                    SwingUtilities.invokeLater(() -> {

                        if (flagLabel) {
                            updatecycleDurationLabel();
                        }
                        ejecutarProcesos();

                        actualizarInterfaz(); // Refresca la UI
                        createJScrollPaneOnReady(app.getPlanificador().getColaListos());
                        createJScrollPaneOnBlocked(app.getPlanificador().getColaBloqueados());

                        //app.getPlanificador().setNombreAlgoritmo(currentAlgorithmComboBOX.getModel().getSelectedItem().toString());
                    });
                    Thread.sleep(this.cycleDurationParameter.get()); // Actualiza cada x que definio el usuario

                    // POSIBLE LUGAR PARA EL RELOJ GLOBAL, MODIFICAR SLEEP PARA QUE SEA LA MISMA QUE LA DURACION DEL CICLO.
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
        simulationThread.start();
    }

    private void ejecutarProcesos() {
        try {
            CPU currentCPU0 = app.getCpu1();
            CPU currentCPU1 = app.getCpu2();
            CPU currentCPU2 = app.getCpu3();

            for (int i = 0; i < modelosCPU.length; i++) {
                if (currentCPU1 == null || currentCPU1.getActualProceso() == null) {
                    modelosCPU[i].clear();

                    // Posible Ejecucion del SO
                    modelosCPU[i].addElement("CPU " + (i + 1) + " Vacío");
                    continue;
                }

                if (i == 0) {
                    Proceso procesoActual = currentCPU0.getActualProceso();
                    //procesoActual.setCiclosDuracion(new AtomicInteger(Integer.parseInt(this.cycleDurationParameter)));

                    int marValue = procesoActual.getCant_instrucciones() - procesoActual.getTiempoRestante();
                    procesoActual.getPCB_proceso().getAmbienteEjecucion().setMAR(marValue);
                    procesoActual.getPCB_proceso().getAmbienteEjecucion().setPc(marValue + 1);

                    modelosCPU[i].clear();
                    modelosCPU[i].addElement("Proceso: " + procesoActual.getNombreProceso());
                    modelosCPU[i].addElement("Instrucciones Restantes: " + procesoActual.getTiempoRestante());
                    modelosCPU[i].addElement("PC: " + procesoActual.getPCB_proceso().getAmbienteEjecucion().getPc());
                    modelosCPU[i].addElement("MAR: " + procesoActual.getPCB_proceso().getAmbienteEjecucion().getMAR());

                } else if (i == 1) {
                    Proceso procesoActual = currentCPU1.getActualProceso();
                    //procesoActual.setCiclosDuracion(new AtomicInteger(Integer.parseInt(this.cycleDurationParameter)));
                    int marValue = procesoActual.getCant_instrucciones() - procesoActual.getTiempoRestante();
                    procesoActual.getPCB_proceso().getAmbienteEjecucion().setMAR(marValue);
                    procesoActual.getPCB_proceso().getAmbienteEjecucion().setPc(marValue + 1);

                    modelosCPU[i].clear();
                    modelosCPU[i].addElement("Proceso: " + procesoActual.getNombreProceso());
                    modelosCPU[i].addElement("Instrucciones Restantes: " + procesoActual.getTiempoRestante());
                    modelosCPU[i].addElement("PC: " + procesoActual.getPCB_proceso().getAmbienteEjecucion().getPc());
                    modelosCPU[i].addElement("MAR: " + procesoActual.getPCB_proceso().getAmbienteEjecucion().getMAR());
                } else {
                    Proceso procesoActual = currentCPU2.getActualProceso();
                    //procesoActual.setCiclosDuracion(new AtomicInteger(Integer.parseInt(this.cycleDurationParameter)));
                    int marValue = procesoActual.getCant_instrucciones() - procesoActual.getTiempoRestante();
                    procesoActual.getPCB_proceso().getAmbienteEjecucion().setMAR(marValue);
                    procesoActual.getPCB_proceso().getAmbienteEjecucion().setPc(marValue + 1);

                    modelosCPU[i].clear();
                    modelosCPU[i].addElement("Proceso: " + procesoActual.getNombreProceso());
                    modelosCPU[i].addElement("Instrucciones Restantes: " + procesoActual.getTiempoRestante());
                    modelosCPU[i].addElement("PC: " + procesoActual.getPCB_proceso().getAmbienteEjecucion().getPc());
                    modelosCPU[i].addElement("MAR: " + procesoActual.getPCB_proceso().getAmbienteEjecucion().getMAR());
                }
            }
        } catch (NullPointerException e) {
            System.err.println("Error: currentCPU1 es nulo.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            // Aquí puedes manejar el error de una forma más específica si lo necesitas.
        }
    }

    private void actualizarInterfaz() {

        jPanel4.revalidate();
        jPanel4.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        startSimulation = new javax.swing.JButton();
        cycleDurationSpinner = new javax.swing.JSpinner();
        currentAlgorithmComboBOX = new javax.swing.JComboBox<>();
        primaryPanelCPU = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        cycleDurationLabel = new javax.swing.JLabel();
        Salir = new javax.swing.JButton();
        CreateProcess = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        startSimulation.setText("Start");
        startSimulation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startSimulationActionPerformed(evt);
            }
        });
        jPanel1.add(startSimulation, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 160, 80, 30));

        cycleDurationSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, 10, 1));
        cycleDurationSpinner.setToolTipText("");
        cycleDurationSpinner.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cycleDurationSpinner.setFocusable(false);
        cycleDurationSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                cycleDurationSpinnerStateChanged(evt);
            }
        });
        cycleDurationSpinner.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cycleDurationSpinnerKeyPressed(evt);
            }
        });
        jPanel1.add(cycleDurationSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 110, 80, 30));

        currentAlgorithmComboBOX.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "FCFS", "SPN", "RR", "SRT", "HRRN" }));
        currentAlgorithmComboBOX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                currentAlgorithmComboBOXActionPerformed(evt);
            }
        });
        jPanel1.add(currentAlgorithmComboBOX, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 70, 130, -1));

        primaryPanelCPU.setBackground(new java.awt.Color(56, 12, 36));
        primaryPanelCPU.setLayout(new javax.swing.BoxLayout(primaryPanelCPU, javax.swing.BoxLayout.LINE_AXIS));
        jPanel1.add(primaryPanelCPU, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 60, 550, 170));

        jPanel3.setBackground(new java.awt.Color(56, 12, 36));
        jPanel3.setForeground(new java.awt.Color(75, 0, 130));
        jPanel3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));
        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 260, 750, 120));

        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.LINE_AXIS));
        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 260, 750, 120));

        jPanel5.setBackground(new java.awt.Color(56, 12, 36));
        jPanel5.setForeground(new java.awt.Color(75, 0, 130));
        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.LINE_AXIS));
        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 420, 750, 110));
        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 430, 750, 100));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 215, 0));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Cola Bloqueados ");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 380, 160, 30));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 140, 0));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("CPU-3");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 20, 80, 40));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 140, 0));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("CPU-1");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 20, 80, 40));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 140, 0));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("CPU-2");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 20, 80, 40));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 215, 0));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Cola de Listos");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 240, 130, 20));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Assets/solid-background-color.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 0, 890, 580));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cycleDurationLabel.setForeground(new java.awt.Color(255, 215, 0));
        cycleDurationLabel.setText("Ciclos de reloj:");
        jPanel2.add(cycleDurationLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 490, 140, 30));

        Salir.setText("Salir");
        Salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SalirActionPerformed(evt);
            }
        });
        jPanel2.add(Salir, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 430, 110, 40));

        CreateProcess.setText("Interfaz de Creación");
        CreateProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreateProcessActionPerformed(evt);
            }
        });
        jPanel2.add(CreateProcess, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 230, 140, 40));

        jButton4.setText("Simulador");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 280, 110, 40));

        jButton5.setText("Estadistica");
        jButton5.setMaximumSize(new java.awt.Dimension(72, 23));
        jButton5.setMinimumSize(new java.awt.Dimension(72, 23));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 330, 110, 40));

        jButton3.setText("Guardar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 380, 110, 40));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Assets/background2.png"))); // NOI18N
        jLabel3.setText("jLabel3");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1110, 640));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 570));

        jButton1.setText("jButton1");
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 160, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void createJScrollPaneOnReady(Cola<Proceso> colaListos) {
        jPanel4.removeAll();
        jPanel4.setLayout(new BoxLayout(jPanel4, BoxLayout.X_AXIS));

        Nodo<Proceso> current = colaListos.getHead();

        while (current != null) {
            DefaultListModel<Object> modeloListos = new DefaultListModel<>();
            JList<Object> newJList = new JList<>(modeloListos);

            Proceso process = current.gettInfo();
            modeloListos.addElement("Proceso: " + process.getNombreProceso());
            modeloListos.addElement("Instrucciones: " + process.getCant_instrucciones());
            modeloListos.addElement("PC: " + process.getPCB_proceso().getAmbienteEjecucion().getPc());
            modeloListos.addElement("MAR: " + process.getPCB_proceso().getAmbienteEjecucion().getMAR());
            modeloListos.addElement("PSW: " + process.getPCB_proceso().getAmbienteEjecucion().getPsw());

            JScrollPane scrollPane = new JScrollPane(newJList);
            scrollPane.setPreferredSize(new Dimension(150, 100));

            jPanel4.add(scrollPane);
            jPanel4.add(Box.createRigidArea(new Dimension(10, 0)));

            current = current.getpNext();
        }

        jPanel4.revalidate();
        jPanel4.repaint();

        jPanel3.removeAll();
        jPanel3.setLayout(new BorderLayout());
        jPanel3.add(new JScrollPane(jPanel4), BorderLayout.CENTER);
        jPanel3.revalidate();
        jPanel3.repaint();
    }

    private void createJScrollPaneOnBlocked(Cola<Proceso> colaBloqueados) {
        jPanel7.removeAll();
        jPanel7.setLayout(new BoxLayout(jPanel7, BoxLayout.X_AXIS));

        Nodo<Proceso> current = colaBloqueados.getHead();

        while (current != null) {
            DefaultListModel<Object> modeloListos = new DefaultListModel<>();
            JList<Object> newJList = new JList<>(modeloListos);

            Proceso process = current.gettInfo();
            modeloListos.addElement("Proceso: " + process.getNombreProceso());
            modeloListos.addElement("Instrucciones: " + process.getCant_instrucciones());
            modeloListos.addElement("PC: " + process.getPCB_proceso().getAmbienteEjecucion().getPc());
            modeloListos.addElement("MAR: " + process.getPCB_proceso().getAmbienteEjecucion().getMAR());
            modeloListos.addElement("PSW: " + process.getPCB_proceso().getAmbienteEjecucion().getPsw());

            JScrollPane scrollPane = new JScrollPane(newJList);
            scrollPane.setPreferredSize(new Dimension(150, 100));

            jPanel7.add(scrollPane);
            jPanel7.add(Box.createRigidArea(new Dimension(10, 0)));

            current = current.getpNext();
        }

        jPanel7.revalidate();
        jPanel7.repaint();

        jPanel5.removeAll();
        jPanel5.setLayout(new BorderLayout());
        jPanel5.add(new JScrollPane(jPanel7), BorderLayout.CENTER);
        jPanel5.revalidate();
        jPanel5.repaint();
    }

    private DefaultListModel[] createProcessors() {
        int gap = 10;
        int processors = this.processorsQuantity;

        // Forma interactiva de mostrar el tercer cpu
//        auxPanelCPU.setVisible(processors != 2);
        jLabel7.setVisible(processors != 2);

        DefaultListModel<String>[] modelosCPU = new DefaultListModel[processors];
        JList[] cpuLists = new JList[processors];
        JScrollPane[] scrollPanes = new JScrollPane[processors];
        Dimension dimensionScrollPane = new Dimension(30, 20);

        for (int i = 0; i < processors; i++) {
            modelosCPU[i] = new DefaultListModel<>();
            cpuLists[i] = new JList(modelosCPU[i]);
            scrollPanes[i] = new JScrollPane(cpuLists[i]);
            scrollPanes[i].setPreferredSize(dimensionScrollPane);
            addProcessorToPanel(scrollPanes[i], i, processors, gap);
        }

        primaryPanelCPU.repaint();
        primaryPanelCPU.revalidate();

        return modelosCPU;
    }

    private void addProcessorToPanel(JScrollPane scrollPane, int index, int totalProcessors, int gap) {
        if (totalProcessors == 2 || index < 2) {
            primaryPanelCPU.add(scrollPane);
            primaryPanelCPU.add(Box.createRigidArea(new Dimension(gap, 0)));
        } else {
            primaryPanelCPU.add(scrollPane);
            primaryPanelCPU.add(Box.createRigidArea(new Dimension(gap, 0)));
        }
    }

    private void updatecycleDurationLabel() {
        this.relojGlobalSimulator++;
        String relojActualString = String.valueOf(this.relojGlobalSimulator);
        cycleDurationLabel.setText("Ciclos de reloj: " + relojActualString);
        app.relojGlobal = this.relojGlobalSimulator;
    }

    private void updateSpinner() {
        this.cycleDurationSpinner.setValue((int) (this.cycleDurationParameter.get() / 1000));

    }


    private void SalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_SalirActionPerformed


    private void CreateProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreateProcessActionPerformed
        try {
            ProcessMaker processMakerWindow = new ProcessMaker();
            this.setVisible(false);
            processMakerWindow.setVisible(true);
        } catch (FontFormatException | IOException ex) {
            Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_CreateProcessActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        Estadisticas estadistica = new Estadisticas();
        estadistica.setVisible(true);

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        app.getGuardadoGson().GuardadoGson();
    }//GEN-LAST:event_jButton3ActionPerformed


    private void currentAlgorithmComboBOXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_currentAlgorithmComboBOXActionPerformed

        // Cambiar el nombre del algoritmo:
        try {
            //Esto hay que modificarlo para que modifique los CPUs que estén activos, ya sean 2 o 3. Quizá pasarle una lista de CPUs como atributo a la clase.
            this.currentAlgorithm = currentAlgorithmComboBOX.getModel().getSelectedItem().toString();
            app.getPlanificador().setNombreAlgoritmo(currentAlgorithm);
            app.flagCambio = true;

        } catch (Exception e) {
    }


    }//GEN-LAST:event_currentAlgorithmComboBOXActionPerformed

    private void cycleDurationSpinnerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cycleDurationSpinnerKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cycleDurationSpinnerKeyPressed

    private void cycleDurationSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_cycleDurationSpinnerStateChanged
        this.cycleDurationParameter.set((int) (this.cycleDurationSpinner.getValue()) * 1000);

        app.duracionCicloInstruccion = (this.cycleDurationParameter);
    }//GEN-LAST:event_cycleDurationSpinnerStateChanged

    private void startSimulationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startSimulationActionPerformed
        app.startSimulator(this.processorsQuantity);
        this.flagLabel = true;
    }//GEN-LAST:event_startSimulationActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CreateProcess;
    private javax.swing.JButton Salir;
    private javax.swing.JComboBox<String> currentAlgorithmComboBOX;
    private javax.swing.JLabel cycleDurationLabel;
    private javax.swing.JSpinner cycleDurationSpinner;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel primaryPanelCPU;
    private javax.swing.JButton startSimulation;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the relojGlobal
     */
}
