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

        ColaListos.revalidate();
        ColaListos.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelPrincipal = new javax.swing.JPanel();
        Cola_Suspendidos = new javax.swing.JPanel();
        startSimulation = new javax.swing.JButton();
        cycleDurationSpinner = new javax.swing.JSpinner();
        currentAlgorithmComboBOX = new javax.swing.JComboBox<>();
        primaryPanelCPU = new javax.swing.JPanel();
        CLinferior = new javax.swing.JPanel();
        ColaListos = new javax.swing.JPanel();
        ColaBloqueados = new javax.swing.JPanel();
        CSinferior = new javax.swing.JPanel();
        CBinferior = new javax.swing.JPanel();
        bloqueadoslabel = new javax.swing.JLabel();
        CPU3label = new javax.swing.JLabel();
        suspendidoslabel = new javax.swing.JLabel();
        CPU1label = new javax.swing.JLabel();
        CPU2label = new javax.swing.JLabel();
        listoslabel = new javax.swing.JLabel();
        fondo1 = new javax.swing.JPanel();
        cycleDurationLabel = new javax.swing.JLabel();
        Salir = new javax.swing.JButton();
        CreateProcess = new javax.swing.JButton();
        Simulador = new javax.swing.JButton();
        Estadistica = new javax.swing.JButton();
        Guardar = new javax.swing.JButton();
        Fondo2 = new javax.swing.JLabel();
        Fondo3 = new javax.swing.JLabel();
        Iniciar = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 800));
        setPreferredSize(new java.awt.Dimension(800, 800));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PanelPrincipal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Cola_Suspendidos.setBackground(new java.awt.Color(255, 255, 255));
        Cola_Suspendidos.setForeground(new java.awt.Color(75, 0, 130));
        Cola_Suspendidos.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Cola_Suspendidos.setLayout(new javax.swing.BoxLayout(Cola_Suspendidos, javax.swing.BoxLayout.LINE_AXIS));
        PanelPrincipal.add(Cola_Suspendidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 580, 750, 120));

        startSimulation.setText("Start");
        startSimulation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startSimulationActionPerformed(evt);
            }
        });
        PanelPrincipal.add(startSimulation, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 160, 80, 30));

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
        PanelPrincipal.add(cycleDurationSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 110, 80, 30));

        currentAlgorithmComboBOX.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "FCFS", "SPN", "RR", "SRT", "HRRN", "PRIORITY" }));
        currentAlgorithmComboBOX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                currentAlgorithmComboBOXActionPerformed(evt);
            }
        });
        PanelPrincipal.add(currentAlgorithmComboBOX, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 70, 130, -1));

        primaryPanelCPU.setBackground(new java.awt.Color(255, 255, 255));
        primaryPanelCPU.setLayout(new javax.swing.BoxLayout(primaryPanelCPU, javax.swing.BoxLayout.LINE_AXIS));
        PanelPrincipal.add(primaryPanelCPU, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 60, 550, 170));

        CLinferior.setBackground(new java.awt.Color(255, 255, 255));
        CLinferior.setForeground(new java.awt.Color(75, 0, 130));
        CLinferior.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        CLinferior.setLayout(new javax.swing.BoxLayout(CLinferior, javax.swing.BoxLayout.LINE_AXIS));
        PanelPrincipal.add(CLinferior, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 260, 750, 120));

        ColaListos.setLayout(new javax.swing.BoxLayout(ColaListos, javax.swing.BoxLayout.LINE_AXIS));
        PanelPrincipal.add(ColaListos, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 260, 750, 120));

        ColaBloqueados.setBackground(new java.awt.Color(255, 255, 255));
        ColaBloqueados.setForeground(new java.awt.Color(75, 0, 130));
        ColaBloqueados.setLayout(new javax.swing.BoxLayout(ColaBloqueados, javax.swing.BoxLayout.LINE_AXIS));
        PanelPrincipal.add(ColaBloqueados, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 420, 750, 110));
        PanelPrincipal.add(CSinferior, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 590, 750, 100));
        PanelPrincipal.add(CBinferior, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 430, 750, 100));

        bloqueadoslabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        bloqueadoslabel.setForeground(new java.awt.Color(255, 215, 0));
        bloqueadoslabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bloqueadoslabel.setText("Cola Bloqueados ");
        PanelPrincipal.add(bloqueadoslabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 380, 160, 30));

        CPU3label.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        CPU3label.setForeground(new java.awt.Color(255, 140, 0));
        CPU3label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CPU3label.setText("CPU-3");
        PanelPrincipal.add(CPU3label, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 20, 80, 40));

        suspendidoslabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        suspendidoslabel.setForeground(new java.awt.Color(255, 215, 0));
        suspendidoslabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        suspendidoslabel.setText("Cola Suspendidos");
        PanelPrincipal.add(suspendidoslabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 540, 160, 30));

        CPU1label.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        CPU1label.setForeground(new java.awt.Color(255, 140, 0));
        CPU1label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CPU1label.setText("CPU-1");
        PanelPrincipal.add(CPU1label, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 20, 80, 40));

        CPU2label.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        CPU2label.setForeground(new java.awt.Color(255, 140, 0));
        CPU2label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CPU2label.setText("CPU-2");
        PanelPrincipal.add(CPU2label, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 20, 80, 40));

        listoslabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        listoslabel.setForeground(new java.awt.Color(255, 215, 0));
        listoslabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        listoslabel.setText("Cola de Listos");
        PanelPrincipal.add(listoslabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 240, 130, 20));

        fondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cycleDurationLabel.setForeground(new java.awt.Color(255, 215, 0));
        cycleDurationLabel.setText("Ciclos de reloj:");
        fondo1.add(cycleDurationLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 490, 140, 30));

        Salir.setText("Salir");
        Salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SalirActionPerformed(evt);
            }
        });
        fondo1.add(Salir, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 430, 110, 40));

        CreateProcess.setText("Interfaz de Creación");
        CreateProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreateProcessActionPerformed(evt);
            }
        });
        fondo1.add(CreateProcess, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 230, 140, 40));

        Simulador.setText("Simulador");
        Simulador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SimuladorActionPerformed(evt);
            }
        });
        fondo1.add(Simulador, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 280, 110, 40));

        Estadistica.setText("Estadistica");
        Estadistica.setMaximumSize(new java.awt.Dimension(72, 23));
        Estadistica.setMinimumSize(new java.awt.Dimension(72, 23));
        Estadistica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EstadisticaActionPerformed(evt);
            }
        });
        fondo1.add(Estadistica, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 330, 110, 40));

        Guardar.setText("Guardar");
        Guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GuardarActionPerformed(evt);
            }
        });
        fondo1.add(Guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 380, 110, 40));

        Fondo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Assets/background2.png"))); // NOI18N
        Fondo2.setText("jLabel3");
        fondo1.add(Fondo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1110, 640));

        PanelPrincipal.add(fondo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 570));

        Fondo3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Assets/solid-background-color.png"))); // NOI18N
        Fondo3.setText("jLabel1");
        PanelPrincipal.add(Fondo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1120, 720));

        Iniciar.setText("jButton1");
        PanelPrincipal.add(Iniciar, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 160, -1, -1));

        getContentPane().add(PanelPrincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 720));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setForeground(new java.awt.Color(75, 0, 130));
        jPanel6.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.LINE_AXIS));
        getContentPane().add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 260, 750, 120));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void createJScrollPaneOnReady(Cola<Proceso> colaListos) {
        ColaListos.removeAll();
        ColaListos.setLayout(new BoxLayout(ColaListos, BoxLayout.X_AXIS));

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

            ColaListos.add(scrollPane);
            ColaListos.add(Box.createRigidArea(new Dimension(10, 0)));

            current = current.getpNext();
        }

        ColaListos.revalidate();
        ColaListos.repaint();

        CLinferior.removeAll();
        CLinferior.setLayout(new BorderLayout());
        CLinferior.add(new JScrollPane(ColaListos), BorderLayout.CENTER);
        CLinferior.revalidate();
        CLinferior.repaint();
    }

    private void createJScrollPaneOnBlocked(Cola<Proceso> colaBloqueados) {
        CBinferior.removeAll();
        CBinferior.setLayout(new BoxLayout(CBinferior, BoxLayout.X_AXIS));

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

            CBinferior.add(scrollPane);
            CBinferior.add(Box.createRigidArea(new Dimension(10, 0)));

            current = current.getpNext();
        }

        CBinferior.revalidate();
        CBinferior.repaint();

        ColaBloqueados.removeAll();
        ColaBloqueados.setLayout(new BorderLayout());
        ColaBloqueados.add(new JScrollPane(CBinferior), BorderLayout.CENTER);
        ColaBloqueados.revalidate();
        ColaBloqueados.repaint();
    }

    private DefaultListModel[] createProcessors() {
        int gap = 10;
        int processors = this.processorsQuantity;

        // Forma interactiva de mostrar el tercer cpu
//        auxPanelCPU.setVisible(processors != 2);
        CPU3label.setVisible(processors != 2);

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

    private void SimuladorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SimuladorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SimuladorActionPerformed

    private void EstadisticaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EstadisticaActionPerformed

        Estadisticas estadistica = new Estadisticas();
        estadistica.setVisible(true);

    }//GEN-LAST:event_EstadisticaActionPerformed

    private void GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GuardarActionPerformed
        app.getGuardadoGson().GuardadoGson();
    }//GEN-LAST:event_GuardarActionPerformed


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
    private javax.swing.JPanel CBinferior;
    private javax.swing.JPanel CLinferior;
    private javax.swing.JLabel CPU1label;
    private javax.swing.JLabel CPU2label;
    private javax.swing.JLabel CPU3label;
    private javax.swing.JPanel CSinferior;
    private javax.swing.JPanel ColaBloqueados;
    private javax.swing.JPanel ColaListos;
    private javax.swing.JPanel Cola_Suspendidos;
    private javax.swing.JButton CreateProcess;
    private javax.swing.JButton Estadistica;
    private javax.swing.JLabel Fondo2;
    private javax.swing.JLabel Fondo3;
    private javax.swing.JButton Guardar;
    private javax.swing.JButton Iniciar;
    private javax.swing.JPanel PanelPrincipal;
    private javax.swing.JButton Salir;
    private javax.swing.JButton Simulador;
    private javax.swing.JLabel bloqueadoslabel;
    private javax.swing.JComboBox<String> currentAlgorithmComboBOX;
    private javax.swing.JLabel cycleDurationLabel;
    private javax.swing.JSpinner cycleDurationSpinner;
    private javax.swing.JPanel fondo1;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel listoslabel;
    private javax.swing.JPanel primaryPanelCPU;
    private javax.swing.JButton startSimulation;
    private javax.swing.JLabel suspendidoslabel;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the relojGlobal
     */
}
