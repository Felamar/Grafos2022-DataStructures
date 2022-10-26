package data_structures.grafos2022;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.Set;
import java.util.List;
import java.util.Queue;
import java.lang.Math;

import javax.swing.filechooser.FileNameExtensionFilter;

public class GrafosOto22 extends JFrame {
    // Declaración
    private double costMatrix[][];
    private boolean directed;
    private File fileSelected;
    private String formatting;
    private String printing;
    private ArrayList<Nodo> nodesArray;
    private ArrayList<Arista> aristasArray;
    private ArrayList<double[][]> floydArray;
    private ArrayList<boolean[][]> warshallArray;
    public ArrayList<Integer> dfsArray;
    private HashMap<Integer, List<Integer>> paths;
    private PintaGrafo panelGraph;
    private AtomicBoolean modifiedAtomBool;
    private JTextField nodeOriTField, nodeDestTField, pesoTField;
    private JLabel aristasLabel, nodo1Label, nodo2Label, pesoLabel;
    private JButton createAristaB, eraseNodeB;
    private JTextArea areaTArea;
    private JScrollPane areaScrollPane;
    private JMenuBar menuBar;
    private JMenu fileMenu, graphMenu, diagramMenu;
    private JMenuItem newItem, saveItem, saveAsItem, openItem, closeItem, dirItem, nondirItem, matrixItem, dijkstraItem,
            floydItem, warshallItem, primItem, kruskalItem, dfsItem, bfsItem;

    GrafosOto22() {
        setSize(1300,700);
        setLocation(10,10);
        setTitle("Grafos Otoño 2022");
        initComponentes();
    }

    private void initComponentes() {
        // InitComp
        {
            setLayout(null);
            directed = true;
            fileSelected = null;
            formatting = "%4d";
            nodesArray = new ArrayList<Nodo>();
            aristasArray = new ArrayList<Arista>();
            floydArray = new ArrayList<double[][]>();
            warshallArray = new ArrayList<boolean[][]>();
            modifiedAtomBool = new AtomicBoolean(false);
            panelGraph = new PintaGrafo(nodesArray, aristasArray);

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
            primItem = new JMenuItem("Prim");
            primItem.setEnabled(false);
            kruskalItem = new JMenuItem("Kruskal");
            kruskalItem.setEnabled(false);
            dfsItem = new JMenuItem("DFS");
            bfsItem = new JMenuItem("BFS");

            aristasLabel = new JLabel("ARISTAS");
            nodo1Label = new JLabel("Origen (nodo1Label)");
            nodo2Label = new JLabel("Destino (nodo2Label)");
            pesoLabel = new JLabel("Peso");

            nodeOriTField = new JTextField();
            nodeDestTField = new JTextField();
            pesoTField = new JTextField();
            createAristaB = new JButton("Crear Arista");
            eraseNodeB = new JButton("Borrar Nodo");

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
            diagramMenu.add(dfsItem);
            diagramMenu.add(bfsItem);
            diagramMenu.addSeparator();
            diagramMenu.add(dijkstraItem);
            diagramMenu.add(floydItem);
            diagramMenu.add(warshallItem);
            diagramMenu.add(primItem);
            diagramMenu.add(kruskalItem);
            menuBar.add(diagramMenu);
            menuBar.setBounds(0, 0, 1500, 25);
            add(menuBar);
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
            eraseNodeB.setBounds(140, 110, 110, 25);
            eraseNodeB.setToolTipText("Borrar el nodo Origen (nodo1Label)");
            add(eraseNodeB);
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

            eraseNodeB.addActionListener(new ActionListener() {
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
                        printing = String.format(formatting, nodeFor.getDato());
                        areaTArea.append(printing);
                    }
                    areaTArea.append("\n");
                    for (int i = 0; i < nodesArray.size(); i++) {
                        areaTArea.append("" + (i + 1));
                        for (int j = 0; j < nodesArray.size(); j++) {
                            if (costMatrix[i][j] == Double.POSITIVE_INFINITY) {
                                formatting = "%3s";
                                printing = String.format(formatting, '\u221e');
                                formatting = "%4d";
                            } else
                                printing = String.format(formatting, (long) costMatrix[i][j]);
                            areaTArea.append(printing);
                        }
                        areaTArea.append("\n");
                    }
                    areaTArea.append("\n");
                }
            });
            
            dfsItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (nodesArray.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No hay nodos existentes", "WARNING", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    costMatrixInit();
                    paths = new HashMap<Integer, List<Integer>>();
                    dfsArray = new ArrayList<>();
                    boolean[] visited = new boolean[nodesArray.size()];
                    for (int i = 0; i < visited.length; i++) 
                        visited[i] = false;
                    for (int i = 0; i < visited.length; i++)
                        if(!visited[i])
                            dfsMethod(visited, i); 
                    areaTArea.setText("");
                    for (Integer v : paths.keySet()) 
                        areaTArea.append(v + "->" + paths.get(v) + "\n");
                    areaTArea.append(dfsArray + "\n");
                    // dfsWindow dfsFrame = new dfsWindow(paths);
                    // dfsFrame.setVisible(true);
                }
            });
            
            bfsItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (nodesArray.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No hay nodos existentes", "WARNING", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    costMatrixInit();
                    Queue<Integer> Q = new LinkedList<>();
                    ArrayList<Integer> bfs = new ArrayList<>();
                    boolean[] visited = new boolean[nodesArray.size()];
                    for (int i = 1; i < visited.length; i++)
                        visited[i] = false;
                    Q.add(0);
                    visited[0] = true;
                    while(!Q.isEmpty()){
                        bfs.add(Q.peek() + 1);
                        int v = Q.poll();
                        for(int w = 0; w < nodesArray.size(); w++)
                            if(costMatrix[v][w] != Double.POSITIVE_INFINITY && costMatrix[v][w] != 0){
                                if(!visited[w]){
                                    visited[w] = true;
                                    Q.add(w);
                    }}}
                    areaTArea.append(bfs + "\n");

                }
            });
            
            dijkstraItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (nodesArray.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No hay nodos existentes", "WARNING", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    costMatrixInit();
                    ArrayList<Integer> S = new ArrayList<Integer>();
                    ArrayList<Integer> V = new ArrayList<Integer>();
                    ArrayList<Double> dijkstra = new ArrayList<Double>();
                    ArrayList<Double> nodeValueTemp;
                    int index_m;
                    //Init
                    S.add(0);
                    for (int i = 0; i < nodesArray.size(); i++)
                        V.add(i);
                    for (int i = 0; i < nodesArray.size(); i++)
                        dijkstra.add(costMatrix[0][i]);
                    V.removeAll(S);
                    
                    for (int i = 0; i < nodesArray.size() - 1; i++) {
                        //Valores de D[i] de cada i en V
                        nodeValueTemp = new ArrayList<>();
                        for (int v : V) 
                            nodeValueTemp.add(dijkstra.get(v));
                        index_m = dijkstra.indexOf(Collections.min(nodeValueTemp));
                        S.add(index_m);
                        V.removeAll(S);
                        for (int v : V) 
                            dijkstra.set(v, (Math.min(dijkstra.get(v), dijkstra.get(index_m) + costMatrix[index_m][v])));
                    }
                    dijkstra.remove(0);
                    areaTArea.append("DIJKSTRA:\n");
                    areaTArea.append(dijkstra + "\n\n");
                }
            });

            floydItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (nodesArray.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No hay nodos existentes", "WARNING", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    costMatrixInit();
                    matrixItem.doClick();
                    floydArray.clear();
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
                    
                    areaTArea.append("Floyd: \n");
                    for (int aux = 0; aux < floydArray.size(); aux++) {
                        areaTArea.append("Step " + (aux + 1) + ": \n ");
                        for (Nodo nodeFor : nodesArray) {
                            printing = String.format(formatting, nodeFor.getDato());
                            areaTArea.append(printing);
                        }
                        areaTArea.append("\n");
                        for (int i = 0; i < nodesArray.size(); i++) {
                            areaTArea.append("" + (i + 1));
                            for (int j = 0; j < nodesArray.size(); j++) {
                                if (floydArray.get(aux)[i][j] == Double.POSITIVE_INFINITY) {
                                    formatting = "%3s";
                                    printing = String.format(formatting, '\u221e');
                                    formatting = "%4d";
                                } else
                                    printing = String.format(formatting, (long) floydArray.get(aux)[i][j]);
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
                    warshallArray = new ArrayList<>();
                    for (int i = 0; i < nodesArray.size(); i++){
                        warshallArray.add(new boolean[nodesArray.size()][nodesArray.size()]);
                    for (int j = 0; j < nodesArray.size(); j++)
                    for (int k = 0; k < nodesArray.size(); k++)
                        warshallArray.get(i)[j][k] = costMatrix[j][k] > 0 && costMatrix[j][k] != Double.POSITIVE_INFINITY;
                    }
                    for (int k = 0; k < nodesArray.size(); k++)
                    for (int i = 0; i < nodesArray.size(); i++)
                    for (int j = 0; j < nodesArray.size(); j++)
                        if(!warshallArray.get(k)[i][j])
                            for(int h = k; h < nodesArray.size(); h++)
                            warshallArray.get(h)[i][j] = warshallArray.get(h)[i][k] && warshallArray.get(h)[k][j];

                    areaTArea.append("\nWarshall: \n");
                    for(int warshInt = 0; warshInt < nodesArray.size(); warshInt++){
                        areaTArea.append("Step "+ (warshInt + 1) + ":\n ");
                        for (Nodo nodeFor : nodesArray) {
                            printing = String.format(formatting, nodeFor.getDato());
                            areaTArea.append(printing);
                        }
                        areaTArea.append("\n");
                        for (int i = 0; i < nodesArray.size(); i++) {
                            areaTArea.append("" + (i + 1));
                            for (int j = 0; j < nodesArray.size(); j++) {
                                printing = String.format(formatting, warshallArray.get(warshInt)[i][j] ? 1 : 0);
                                areaTArea.append(printing);
                            }
                            areaTArea.append("\n");
                        }
                    }
                    areaTArea.append("\n");
                }
            });

            primItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (nodesArray.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No hay nodos existentes", "WARNING", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    costMatrixInit();
                    ArrayList<Integer[]> toVisit = new ArrayList<Integer[]>();
                    ArrayList<Integer[]> st = new ArrayList<Integer[]>();
                    Set<Integer> V = new HashSet<>();
                    Set<Integer> U = new HashSet<>();
                    for(int i = 0; i < nodesArray.size(); i++) V.add(i);
                    U.add(0);
                    while(!V.isEmpty()){
                        V.removeAll(U);
                        toVisit.clear();
                        for(int u : U)
                        for(int v : V)
                            if(costMatrix[u][v] != Double.POSITIVE_INFINITY && costMatrix[u][v] != 0)
                                toVisit.add(new Integer[]{u, v, (int)costMatrix[u][v]});
                        if(toVisit.isEmpty()) break;
                        int v1 = -1, v2 = -1;
                        Double ch = Double.POSITIVE_INFINITY;
                        for(Integer[] q : toVisit)
                            if(ch > q[2]){
                                v1 = q[0];
                                v2 = q[1];
                                ch = q[2] + 0.0;
                            }
                        U.add(v2);
                        st.add(new Integer[]{v1, v2});           
                    }
                    areaTArea.append("\nPrim:\n"); 
                    ArrayList<Arista> primAristas = new ArrayList<>();
                    for(Integer[] branch : st){
                        areaTArea.append((branch[0]+1) + "--->" + (branch[1]+1) + "\n");
                        primAristas.add(new Arista(branch[0]+1, branch[1]+1));
                    }
                    JFrame primFrame = new JFrame("Prim");
                    PintaGrafo p = new PintaGrafo(nodesArray, primAristas);
                    p.setDirected(false);
                    p.setSearch(true);
                    primFrame.setVisible(true);
                    primFrame.setBounds(panelGraph.getBounds());
                    primFrame.add(p);
                    
                }   
            });
            
            kruskalItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (nodesArray.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No hay nodos existentes", "WARNING", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    costMatrixInit();
                    ArrayList<Integer[]> K = new ArrayList<>();
                    ArrayList<Integer[]> Q = new ArrayList<>();
                    Set<Set<Integer>> VS = new HashSet<>();
                    for(Arista arista : aristasArray)
                        K.add(new Integer[]{arista.getOrigen(), arista.getDestino(), arista.getPeso()});
                    for(Nodo nodo : nodesArray)
                        VS.add(new HashSet<>(Arrays.asList(nodo.getDato())));
                    while(VS.size() > 1){
                        int minWeight = Integer.MAX_VALUE, x = -1, y = -1, i = -1;
                        for(Integer[] k : K)
                            if(minWeight > k[2]){
                                x = k[0]; //5
                                y = k[1]; //6
                                minWeight = k[2];
                                i = K.indexOf(k);
                            }
                        K.remove(i);
                        Set<Integer> w1 = new HashSet<>();
                        w1.add(x);
                        Set<Integer> w2 = new HashSet<>();
                        w2.add(y);
                        for(Set<Integer> vs : VS)
                            if(vs.containsAll(w1)){
                                w1.addAll(vs);
                                break;
                            }
                        for(Set<Integer> vs : VS)
                            if(vs.containsAll(w2)){
                                w2.addAll(vs);
                                break;
                            }                       
                        if(w1.equals(w2))
                            continue;
                        VS.remove(w1);
                        VS.remove(w2);
                        w1.addAll(w2);
                        VS.add(w1);
                        Q.add(new Integer[]{x, y});
                    }
                    areaTArea.append("\nKruskal:\n"); 
                    ArrayList<Arista> kAristas = new ArrayList<>();
                    for(Integer[] q : Q){
                        areaTArea.append(q[0] + "--->" + q[1] + "\n");
                        kAristas.add(new Arista(q[0], q[1]));
                    }

                    JFrame kruskalFrame = new JFrame("Kruskal");
                    PintaGrafo p = new PintaGrafo(nodesArray, kAristas);
                    p.setDirected(false);
                    p.setSearch(true);
                    kruskalFrame.setVisible(true);
                    kruskalFrame.setBounds(panelGraph.getBounds());
                    kruskalFrame.add(p);
                }   
            });
            
            dirItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    directed = true;
                    panelGraph.setDirected(directed);
                    dfsItem.setEnabled(true);
                    bfsItem.setEnabled(true);
                    dijkstraItem.setEnabled(true);
                    floydItem.setEnabled(true);
                    warshallItem.setEnabled(true);
                    primItem.setEnabled(false);
                    kruskalItem.setEnabled(false);
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
                    directed = false;
                    panelGraph.setDirected(directed);
                    dfsItem.setEnabled(false);
                    bfsItem.setEnabled(false);
                    dijkstraItem.setEnabled(false);
                    floydItem.setEnabled(false);
                    warshallItem.setEnabled(false);
                    primItem.setEnabled(true);
                    kruskalItem.setEnabled(true);
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
                        if (!tempFile.getName().endsWith(".txt"))
                            tempFile = new File(tempFile.getAbsolutePath() + ".txt");
                        FileWriter fileWriterTemp = new FileWriter(tempFile);
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
        if(nodesArray.isEmpty()) return;
        costMatrix = new double[nodesArray.size()][nodesArray.size()];
        // Inicializar matriz de costos
        for (int i = 0; i < nodesArray.size(); i++)
            for (int j = 0; j < nodesArray.size(); j++) 
                costMatrix[i][j] = Double.POSITIVE_INFINITY;
        for (int i = 0; i < nodesArray.size(); i++)
            costMatrix[i][i] = 0;

        if(aristasArray.isEmpty())
            return;
        //Inicializar matriz de costos grafos dirigidos
        if(directed){
        for (Arista aristaFor : aristasArray)
            costMatrix[aristaFor.getOrigen() - 1][aristaFor.getDestino() - 1] = aristaFor.getPeso();
        }else
        //Inicializar matriz de costos grafos no dirigidos
        for (Arista aristaFor : aristasArray) {
            costMatrix[aristaFor.getOrigen() - 1][aristaFor.getDestino() - 1] = aristaFor.getPeso();
            costMatrix[aristaFor.getDestino() - 1][aristaFor.getOrigen() - 1] = aristaFor.getPeso();
        }
    }

    //Búsqueda en profundidad
    public void dfsMethod(boolean[] visited, int vertex){
        ArrayList<Integer> L = new ArrayList<Integer>();
        paths.put(vertex + 1, new ArrayList<Integer>());
        if(!visited[vertex])
            dfsArray.add(vertex+1);
        for(int i = 0; i < nodesArray.size(); i++)
            if(costMatrix[vertex][i] != Double.POSITIVE_INFINITY && vertex != i){
                L.add(i);
                paths.get(vertex + 1).add(i + 1);
            }
        visited[vertex] = true;
        for (Integer w : L) {
            if(!visited[w])
                dfsMethod(visited, w);           
        }
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