package data_structures.grafos2022;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.UIManager;

public class GrafosOto22 extends JFrame {
    // Declaración
    private double costMatrix[][];
    private File fileSelected;
    private String formating;
    private String printing;
    private ArrayList<Nodo> nodesArray;
    private ArrayList<Arista> aristasArray;
    private ArrayList<double[][]> floydArray;
    private PintaGrafo panelGraph;
    private AtomicBoolean directedAtBool, modifiedAtomBool;
    private JTextField nodeOriTField, nodeDestTField, pesoTField;
    private JLabel aristasLabel, nodo1Label, nodo2Label, pesoLabel;
    private JButton createAristaB, ereaseNodeB;
    private JTextArea areaTArea;
    private JScrollPane areaScrollPane;
    private JMenuBar menuBar;
    private JMenu fileMenu, graphMenu, diagramMenu;
    private JMenuItem newItem, saveItem, saveAsItem, openItem, closeItem, dirItem, nondirItem, matrixItem, dijkstraItem,
            floydItem, warshallItem;

    GrafosOto22() {
        setSize(1500, 950);
        setLocation(50, 50);
        setSize(1300,700);
        setLocation(10,10);
        setTitle("Grafos Otoño 2022");
        initComponentes();
    }

    private void initComponentes() {
        // InitComp
        {
            setLayout(null);
            fileSelected = null;
            formating = "%5d";
            nodesArray = new ArrayList<Nodo>();
            aristasArray = new ArrayList<Arista>();
            floydArray = new ArrayList<double[][]>();
            directedAtBool = new AtomicBoolean(true);
            modifiedAtomBool = new AtomicBoolean(false);
            panelGraph = new PintaGrafo(nodesArray, aristasArray, directedAtBool);

            menuBar = new JMenuBar();
            fileMenu = new JMenu("Archivo");
            newItem = new JMenuItem("Nuevo grafo");
            saveItem = new JMenuItem("Guardar");
            saveAsItem = new JMenuItem("Guardar como...");
            openItem = new JMenuItem("Abrir");
            closeItem = new JMenuItem("Cerrar");
            graphMenu = new JMenu("Opciones");
            dirItem = new JMenuItem("Grafo dirigido");
            nondirItem = new JMenuItem("Grafo no dirigido");
            diagramMenu = new JMenu("Generar...");
            matrixItem = new JMenuItem("Matriz");
            dijkstraItem = new JMenuItem("Dijkstra");
            floydItem = new JMenuItem("Floyd");
            warshallItem = new JMenuItem("Warshall");
            aristasLabel = new JLabel("ARISTAS");
            nodo1Label = new JLabel("Origen (nodo1Label)");
            nodo2Label = new JLabel("Destino (nodo2Label)");
            pesoLabel = new JLabel("Peso");

            nodeOriTField = new JTextField();
            nodeDestTField = new JTextField();
            pesoTField = new JTextField();
            createAristaB = new JButton("Crear Arista");
            ereaseNodeB = new JButton("Borrar Nodo");

            areaTArea = new JTextArea();
            areaTArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
            areaTArea.setBackground(new Color(246, 246, 246));
            areaTArea.setEditable(false);
            areaScrollPane = new JScrollPane(areaTArea);

            fileMenu.add(newItem);
            fileMenu.add(openItem);
            fileMenu.add(saveItem);
            fileMenu.add(saveAsItem);
            fileMenu.addSeparator();
            fileMenu.add(closeItem);
            menuBar.add(fileMenu);
            graphMenu.add(dirItem);
            graphMenu.add(nondirItem);
            menuBar.add(graphMenu);
            diagramMenu.add(matrixItem);
            diagramMenu.addSeparator();
            diagramMenu.add(dijkstraItem);
            diagramMenu.add(floydItem);
            diagramMenu.add(warshallItem);
            menuBar.add(diagramMenu);
            menuBar.setBounds(0, 0, 1500, 25);
            add(menuBar);
            panelGraph.setBounds(270, 25, 1215, 900);
            panelGraph.setBounds(270, 25, 1015, 650);
            add(panelGraph);
            aristasLabel.setBounds(20, 25, 150, 25);
            add(aristasLabel);
            nodo1Label.setBounds(20, 50, 70, 25);
            add(nodo1Label);
            nodeOriTField.setBounds(20, 75, 70, 25);
            add(nodeOriTField);
            nodo2Label.setBounds(100, 50, 70, 25);
            add(nodo2Label);
            nodeDestTField.setBounds(100, 75, 70, 25);
            add(nodeDestTField);
            pesoLabel.setBounds(180, 50, 70, 25);
            add(pesoLabel);
            pesoTField.setBounds(180, 75, 70, 25);
            add(pesoTField);
            createAristaB.setBounds(20, 110, 110, 25);
            add(createAristaB);
            ereaseNodeB.setBounds(140, 110, 110, 25);
            ereaseNodeB.setToolTipText("Borrar el nodo Origen (nodo1Label)");
            add(ereaseNodeB);
            areaScrollPane.setBounds(1, 150, 270, 760);
            areaScrollPane.setBounds(1, 150, 270, 510);
            add(areaScrollPane);
        }

        // Functions
        {
            createAristaB.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        if (Integer.parseInt(pesoTField.getText()) <= 0) {
                            JOptionPane.showMessageDialog(null, "El pesoTField debe ser mayor a 0", "WARNING", JOptionPane.WARNING_MESSAGE);
                            pesoTField.setText("");
                            pesoTField.requestFocus();
                            return;
                        }
                        if (nodesArray.size() < Integer.parseInt(nodeOriTField.getText()) || Integer.parseInt(nodeOriTField.getText()) < 1) {
                            JOptionPane.showMessageDialog(null, "El nodo origen debe existir", "WARNING", JOptionPane.WARNING_MESSAGE);
                            nodeOriTField.setText("");
                            nodeOriTField.requestFocus();
                            return;
                        }
                        if (nodesArray.size() < Integer.parseInt(nodeDestTField.getText()) || Integer.parseInt(nodeDestTField.getText()) < 1) {
                            JOptionPane.showMessageDialog(null, "El nodo destino debe existir", "WARNING", JOptionPane.WARNING_MESSAGE);
                            nodeDestTField.setText("");
                            nodeDestTField.requestFocus();
                            return;
                        }
                        if (Integer.parseInt(nodeOriTField.getText()) == Integer.parseInt(nodeDestTField.getText())) {
                            JOptionPane.showMessageDialog(null, "Los nodesArray origen y destino deben ser distintos", "WARNING", JOptionPane.WARNING_MESSAGE);
                            nodeDestTField.setText("");
                            nodeDestTField.requestFocus();
                            return;
                        }
                        Arista aristaTemp = new Arista();
                        aristaTemp.setOrigen(Integer.parseInt(nodeOriTField.getText()));
                        aristaTemp.setDestino(Integer.parseInt(nodeDestTField.getText()));
                        aristaTemp.setPeso(Integer.parseInt(pesoTField.getText()));
                        for (Arista aristaFor : aristasArray) {
                            if (aristaFor.getOrigen() == aristaTemp.getOrigen() && aristaFor.getDestino() == aristaTemp.getDestino()) {
                                String[] buttonsJOPTemp = { "Sí", "No" };
                                int selectedOptionTemp = JOptionPane.showOptionDialog(null, "Este camino ya existe\n ¿Quieres cambiar el valor del peso?", "WARNING", JOptionPane.WARNING_MESSAGE, 0, null, buttonsJOPTemp, buttonsJOPTemp[1]);
                                if (selectedOptionTemp == JOptionPane.YES_OPTION) {
                                    aristaFor.setPeso(Integer.parseInt(pesoTField.getText()));
                                    panelGraph.repaint();
                                    modifiedAtomBool.set(true);
                                    resetTextFields();
                                    return;
                                } else {
                                    resetTextFields();
                                    return;
                                }
                            }
                        }
                        aristasArray.add(aristaTemp);
                        panelGraph.repaint();
                        modifiedAtomBool.set(true);
                        resetTextFields();
                    } catch (NumberFormatException ex) {
                        resetTextFields();
                        JOptionPane.showMessageDialog(null, "Sólo Inserte Números Enteros de \n0 a 2^(31)-1", "WARNING", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });

            ereaseNodeB.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(nodesArray.isEmpty()){
                        JOptionPane.showMessageDialog(null, "No hay nodos existentes", "WARNING", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    try {
                        String buttonsJOPTemp[] = { "Sí", "No" };
                        int nodeValueTemp = Integer.parseInt(nodeOriTField.getText());
                        if (nodesArray.size() < nodeValueTemp || nodeValueTemp < 1) {
                            JOptionPane.showMessageDialog(null, "El nodo origen debe existir", "WARNING", JOptionPane.WARNING_MESSAGE);
                            resetTextFields();
                            return;
                        }
                        int selectedOptionTemp = JOptionPane.showOptionDialog(null, "¿Desea eliminar el nodo " + nodeValueTemp + '?', "Grafos Otoño 2022", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, buttonsJOPTemp, buttonsJOPTemp[1]);
                        if (selectedOptionTemp != JOptionPane.YES_OPTION) 
                            return;
                        for (int i = 0; i < aristasArray.size(); i++)
                            if (aristasArray.get(i).getOrigen() == nodeValueTemp || aristasArray.get(i).getDestino() == nodeValueTemp) {
                                aristasArray.remove(i);
                                i--;
                            }
                        nodesArray.remove(nodeValueTemp - 1);
                        for (Arista aristaFor : aristasArray) {
                            if (aristaFor.getOrigen() > nodeValueTemp)
                                aristaFor.setOrigen(aristaFor.getOrigen() - 1);
                            if (aristaFor.getDestino() > nodeValueTemp)
                                aristaFor.setDestino((aristaFor.getDestino() - 1));
                        }
                        for (int i = nodeValueTemp - 1; i < nodesArray.size(); i++)
                            nodesArray.get(i).setDato(nodesArray.get(i).getDato() - 1);
                        panelGraph.repaint();
                        modifiedAtomBool.set(true);
                        resetTextFields();
                    } catch (NumberFormatException ex) {
                        resetTextFields();
                        JOptionPane.showMessageDialog(null, "Sólo Inserte Números Enteros de \n0 a 2^(31)-1", "WARNING", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });

            matrixItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (nodesArray.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No hay nodos existentes", "WARNING", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    costMatrixInit();
                    areaTArea.setText("Matriz de costo:\n ");
                    for (Nodo nodeFor : nodesArray) {
                        printing = String.format(formating, nodeFor.getDato());
                        areaTArea.append(printing);
                    }
                    areaTArea.append("\n");
                    for (int i = 0; i < nodesArray.size(); i++) {
                        areaTArea.append("" + (i + 1));
                        for (int j = 0; j < nodesArray.size(); j++) {
                            if (costMatrix[i][j] == Double.POSITIVE_INFINITY) {
                                formating = "%4s";
                                printing = String.format(formating, '\u221e');
                                formating = "%5d";
                            } else
                                printing = String.format(formating, (long) costMatrix[i][j]);
                            areaTArea.append(printing);
                        }
                        areaTArea.append("\n");
                    }
                    areaTArea.append("\n");
                }
            });

            dijkstraItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (nodesArray.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No hay nodos existentes", "WARNING", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    costMatrixInit();
                    ArrayList<Integer> sumVertex = new ArrayList<Integer>();
                    ArrayList<Integer> totalVertex = new ArrayList<Integer>();
                    ArrayList<Double> dijkstraArray = new ArrayList<Double>();
                    int indexOfMinD;
                    sumVertex.add(0);
                    for (int i = 0; i < nodesArray.size(); i++)
                        totalVertex.add(i);
                    for (int i = 0; i < nodesArray.size(); i++)
                        dijkstraArray.add(costMatrix[0][i]);
                    totalVertex.removeAll(sumVertex);
                    for (int i = 0; i < nodesArray.size() - 1; i++) {
                        Double nodeValueTemp;
                        indexOfMinD = Collections.min(totalVertex);
                        nodeValueTemp = dijkstraArray.get(indexOfMinD);
                        for (int j = 0; j < totalVertex.size(); j++) {
                            if (nodeValueTemp > dijkstraArray.get(totalVertex.get(j))) {
                                nodeValueTemp = dijkstraArray.get(totalVertex.get(j));
                                indexOfMinD = totalVertex.get(j);
                            }
                        }
                        sumVertex.add(indexOfMinD);
                        totalVertex.removeAll(sumVertex);
                        for (int j = 0; j < totalVertex.size(); j++) 
                            if (dijkstraArray.get(totalVertex.get(j)) > dijkstraArray.get(indexOfMinD) + costMatrix[indexOfMinD][totalVertex.get(j)]) 
                                dijkstraArray.set(totalVertex.get(j), (dijkstraArray.get(indexOfMinD) + costMatrix[indexOfMinD][totalVertex.get(j)]));
                            
                    }
                    dijkstraArray.remove(0);
                    areaTArea.append("DIJKSTRA:\n");
                    areaTArea.append("" + dijkstraArray + "\n\n");
                }
            });

            floydItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (nodesArray.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No hay nodos existentes", "WARNING", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    costMatrixInit();
                    floydArray.clear();

                    // for (int i = 0; i < nodesArray.size(); i++)
                    //     floydArray.add(new double[nodesArray.size()][nodesArray.size()]);
                        
                    for (int i = 0; i < nodesArray.size(); i++){
                        floydArray.add(new double[nodesArray.size()][nodesArray.size()]);
                    for (int j = 0; j < nodesArray.size(); j++)
                    for (int k = 0; k < nodesArray.size(); k++)
                        floydArray.get(i)[j][k] = costMatrix[j][k];
                    }

                    for (int k = 0; k < nodesArray.size(); k++){
                    for (int j = 0; j < nodesArray.size(); j++)
                    for (int i = 0; i < nodesArray.size(); i++)
                        if (floydArray.get(k)[i][k] + floydArray.get(k)[k][j] < floydArray.get(k)[i][j])
                            for(int h = k; h < nodesArray.size(); h++)
                            floydArray.get(h)[i][j] = floydArray.get(h)[i][k] + floydArray.get(h)[k][j];
                    }
                    
                    areaTArea.setText("Floyd: \n");
                    for (int aux = 0; aux < floydArray.size(); aux++) {
                        areaTArea.append("Step " + (aux + 1) + ": \n ");
                        for (Nodo nodeFor : nodesArray) {
                            printing = String.format(formating, nodeFor.getDato());
                            areaTArea.append(printing);
                        }
                        areaTArea.append("\n");
                        for (int i = 0; i < nodesArray.size(); i++) {
                            areaTArea.append("" + (i + 1));
                            for (int j = 0; j < nodesArray.size(); j++) {
                                if (floydArray.get(aux)[i][j] == Double.POSITIVE_INFINITY) {
                                    formating = "%4s";
                                    printing = String.format(formating, '\u221e');
                                    formating = "%5d";
                                } else
                                    printing = String.format(formating, (long) floydArray.get(aux)[i][j]);
                                areaTArea.append(printing);
                            }
                            areaTArea.append("\n");
                        }
                        areaTArea.append("\n");
                    }
                }
            });

            warshallItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (nodesArray.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No hay nodos existentes", "WARNING", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    costMatrixInit();
                    boolean warshallTemp[][] = new boolean[nodesArray.size()][nodesArray.size()];

                    for (int i = 0; i < nodesArray.size(); i++)
                    for (int j = 0; j < nodesArray.size(); j++)
                        warshallTemp[i][j] = costMatrix[i][j] > 0 && costMatrix[i][j] != Double.POSITIVE_INFINITY;

                    for (int k = 0; k < nodesArray.size(); k++)
                    for (int i = 0; i < nodesArray.size(); i++)
                    for (int j = 0; j < nodesArray.size(); j++)
                        warshallTemp[i][j] = warshallTemp[i][j] || (warshallTemp[i][k] && warshallTemp[k][j]);

                    areaTArea.append("\nWarshall: \n ");

                    for (Nodo nodeFor : nodesArray) {
                        printing = String.format(formating, nodeFor.getDato());
                        areaTArea.append(printing);
                    }
                    areaTArea.append("\n");
                    for (int i = 0; i < nodesArray.size(); i++) {
                        areaTArea.append("" + (i + 1));
                        for (int j = 0; j < nodesArray.size(); j++) {
                            printing = String.format(formating, warshallTemp[i][j] ? 1 : 0);
                            areaTArea.append(printing);
                        }
                        areaTArea.append("\n");
                    }
                    areaTArea.append("\n");
                }
            });
            dirItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    directedAtBool.set(true);
                    dijkstraItem.setEnabled(true);
                    floydItem.setEnabled(true);
                    warshallItem.setEnabled(true);
                    panelGraph.repaint();
                }
            });

            nondirItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (!nodesArray.isEmpty()) {
                        outerloop:
                        for (Arista aristaFor : aristasArray) {
                        for (int i = aristasArray.indexOf(aristaFor) + 1; i < aristasArray.size(); i++)
                            if (aristaFor.getOrigen() == aristasArray.get(i).getDestino() && aristaFor.getDestino() == aristasArray.get(i).getOrigen()) {
                                int selectedOptionTemp = JOptionPane.showConfirmDialog(null, "Esta acción eliminará al menos un camino \n¿Desea continuar?", "Grafos Otoño 2022", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                                if (selectedOptionTemp != JOptionPane.YES_OPTION)
                                    return;
                                modifiedAtomBool.set(true);
                                break outerloop;
                            }
                        }

                        for (int first = 0; first < aristasArray.size(); first++) 
                        for (int i = first + 1; i < aristasArray.size(); i++)
                            if (aristasArray.get(first).getOrigen() == aristasArray.get(i).getDestino() && aristasArray.get(first).getDestino() == aristasArray.get(i).getOrigen()) 
                                aristasArray.remove(i);
                        
                    }
                    directedAtBool.set(false);
                    dijkstraItem.setEnabled(false);
                    floydItem.setEnabled(false);
                    warshallItem.setEnabled(false);
                    modifiedAtomBool.set(true);
                    panelGraph.repaint();
                }
            });

            newItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (!nodesArray.isEmpty() && (modifiedAtomBool.get() || panelGraph.isModified())){
                        int selectedOptionTemp = JOptionPane.showConfirmDialog(null, "¿Guardar progreso?", "Grafos Otoño 2022", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                        if (selectedOptionTemp == JOptionPane.CANCEL_OPTION)
                            return;
                        if (selectedOptionTemp == JOptionPane.YES_OPTION){
                            saveItem.doClick();
                            if (modifiedAtomBool.get() || panelGraph.isModified())
                                return;
                        }
                    }
                    aristasArray.clear();
                    nodesArray.clear();
                    panelGraph.repaint();
                    fileSelected = null;
                    modifiedAtomBool.set(false);
                    panelGraph.setModified(false);
                    areaTArea.setText("");
                }
            });

            saveItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (nodesArray.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No hay nodos existentes", "WARNING", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if (fileSelected == null) {
                        saveAsItem.doClick();
                        return;
                    }
                    try {
                        FileWriter fileWriterTemp = new FileWriter(fileSelected);
                        for (Nodo nodeFor : nodesArray)
                            fileWriterTemp.append("n " + nodeFor.getX() + " " + nodeFor.getY() + " " + nodeFor.getDato() + '\n');
                        for (Arista aristaFor : aristasArray)
                            fileWriterTemp.append("a " + aristaFor.getOrigen() + " " + aristaFor.getDestino() + " " + aristaFor.getPeso() + '\n');

                        fileWriterTemp.flush();
                        fileWriterTemp.close();
                        modifiedAtomBool.set(false);
                        panelGraph.setModified(false);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            });

            saveAsItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (nodesArray.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No hay nodos existentes", "WARNING", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    JFileChooser fChooser = new JFileChooser() {
                        @Override
                        public void approveSelection() {
                            File fileTemp = getSelectedFile();
                            if (fileTemp.exists() && getDialogType() == SAVE_DIALOG) {
                                int result = JOptionPane.showConfirmDialog(this, "¿Sobre escribir archivo existente?",
                                        "Existing tempFile", JOptionPane.YES_NO_CANCEL_OPTION);
                                switch (result) {
                                    case JOptionPane.YES_OPTION:
                                        super.approveSelection();
                                        return;
                                    case JOptionPane.NO_OPTION:
                                        return;
                                    case JOptionPane.CLOSED_OPTION:
                                        return;
                                    case JOptionPane.CANCEL_OPTION:
                                        return;
                                }
                            }
                            super.approveSelection();
                        }
                    };
                    fChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    fChooser.setFileFilter(new FileNameExtensionFilter("TEXT FILES (.txt)", "txt"));
                    if (fChooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION)
                        return;

                    try {
                        File tempFile = fChooser.getSelectedFile();
                        FileWriter fileWriterTemp = new FileWriter(tempFile);
                        if (!tempFile.getName().endsWith(".txt"))
                            tempFile = new File(tempFile.getAbsolutePath() + ".txt");
                        fileSelected = tempFile;
                        for (Nodo nodeFor : nodesArray)
                            fileWriterTemp.append("n " + nodeFor.getX() + " " + nodeFor.getY() + " " + nodeFor.getDato() + '\n');
                        for (Arista aristaFor : aristasArray)
                            fileWriterTemp.append("a " + aristaFor.getOrigen() + " " + aristaFor.getDestino() + " " + aristaFor.getPeso() + '\n');
                        
                        fileWriterTemp.flush();
                        fileWriterTemp.close();
                        modifiedAtomBool.set(false);
                        panelGraph.setModified(false);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            });

            openItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (!nodesArray.isEmpty() && (modifiedAtomBool.get() || panelGraph.isModified())) {
                        int selectedOptionTemp = JOptionPane.showConfirmDialog(null, "¿Guardar progreso?", "Grafos Otoño 2022", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                        if (selectedOptionTemp == JOptionPane.CANCEL_OPTION)
                            return;
                        if (selectedOptionTemp == JOptionPane.YES_OPTION)
                            saveItem.doClick();
                    }
                    JFileChooser fChooser = new JFileChooser();
                    fChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    fChooser.setFileFilter(new FileNameExtensionFilter("TEXT FILES", "txt"));
                    if (fChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
                        return;
                    File tempFile = fChooser.getSelectedFile();
                    try {
                        fileSelected = tempFile;
                        nodesArray.clear();
                        aristasArray.clear();
                        panelGraph.repaint();
                        Scanner fReader = new Scanner(tempFile);
                        while (fReader.hasNext()) {
                            if (fReader.next().equals("n")) {
                                Nodo nodeTemp = new Nodo();
                                nodeTemp.setX(fReader.nextInt());
                                nodeTemp.setY(fReader.nextInt());
                                nodeTemp.setDato(fReader.nextInt());
                                nodesArray.add(nodeTemp);
                            }
                        }
                        fReader.close();
                        fReader = new Scanner(tempFile);
                        while (fReader.hasNext()) {
                            if (fReader.next().equals("a")) {
                                Arista ar = new Arista();
                                ar.setOrigen(fReader.nextInt());
                                ar.setDestino(fReader.nextInt());
                                ar.setPeso(fReader.nextInt());
                                aristasArray.add(ar);
                            }
                        }
                        fReader.close();
                        if (nodesArray.isEmpty()) {
                            JOptionPane.showMessageDialog(panelGraph, "No se encontraron nodos en este archivo", "Grafos Otoño 2022", JOptionPane.WARNING_MESSAGE);
                            fileSelected = null;
                        }
                        panelGraph.repaint();
                        modifiedAtomBool.set(false);
                        panelGraph.setModified(false);
                        areaTArea.setText("");
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            });

            closeItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (!nodesArray.isEmpty() && (modifiedAtomBool.get() || panelGraph.isModified())) {
                        int rValue = JOptionPane.showConfirmDialog(null, "¿Guardar progreso?", "Grafos Otoño 2022", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                        if (rValue == JOptionPane.CANCEL_OPTION)
                            return;
                        if (rValue == JOptionPane.YES_OPTION)
                            saveItem.doClick();
                    }
                    dispose();
                    System.exit(0);
                }
            });

            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent ev) {
                    closeItem.doClick();
                }
            });
        }
    }

    public void costMatrixInit() {
        costMatrix = new double[nodesArray.size()][nodesArray.size()];

        for (int i = 0; i < nodesArray.size(); i++)
            for (int j = 0; j < nodesArray.size(); j++) 
                costMatrix[i][j] = Double.POSITIVE_INFINITY;
        for (int i = 0; i < nodesArray.size(); i++)
            costMatrix[i][i] = 0;

        if(aristasArray.isEmpty())
            return;
        
        if(directedAtBool.get()){
            for (int i = 0; i < nodesArray.size(); i++)
            for (int j = 0; j < nodesArray.size(); j++) 
            for (Arista aristaFor : aristasArray) 
                if (aristaFor.getOrigen() == nodesArray.get(i).getDato() && aristaFor.getDestino() == nodesArray.get(j).getDato()) {
                    costMatrix[i][j] = (double) aristaFor.getPeso();
                    break;
                }
            for (int i = 0; i < nodesArray.size(); i++)
                costMatrix[i][i] = 0;
            return;
        }

        for (int i = 0; i < nodesArray.size(); i++)
        for (int j = 0; j < i; j++) 
        for (Arista aristaFor : aristasArray) 
            if ((aristaFor.getOrigen() == nodesArray.get(i).getDato() && aristaFor.getDestino() == nodesArray.get(j).getDato()) || (aristaFor.getOrigen() == nodesArray.get(j).getDato() && aristaFor.getDestino() == nodesArray.get(i).getDato())) {
                costMatrix[i][j] = (double) aristaFor.getPeso();
                costMatrix[j][i] = (double) aristaFor.getPeso();
                break;
            }
        for (int i = 0; i < nodesArray.size(); i++)
            costMatrix[i][i] = 0;

    }

    public void resetTextFields(){
        nodeOriTField.setText("");
        nodeDestTField.setText("");
        pesoTField.setText("");
        nodeOriTField.requestFocus();
        return;
    }

    public static void main(String[] args) throws Exception {
        GrafosOto22 fr = new GrafosOto22();
        fr.setVisible(true);
        fr.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
}