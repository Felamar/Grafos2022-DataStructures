package data_structures.grafos2022;

import javax.swing.JFrame;
import java.awt.event.*;
import java.util.*;

public class dfsWindow extends JFrame {
    private HashMap<Integer, List<Integer>> paths;
    private Set<Integer> visited;
    private ArrayList<Nodo> nodes;
    private ArrayList<Integer> toVisit;
    private ArrayList<Arista> aristArray;
    private PintaGrafo panel;
    private int xc = 0, yc = 60;

    dfsWindow(HashMap<Integer, List<Integer>> map){
        setSize(1200,700);
        setLocation(50,50);
        setTitle("Búsqueda en Profundidad");
        paths = map;
        visited = new HashSet<>();
        toVisit = new ArrayList<>(paths.keySet());
        nodes = new ArrayList<Nodo>();
        aristArray = new ArrayList<Arista>();
        panel = new PintaGrafo(nodes, aristArray);
        panel.setBounds(getBounds());
        panel.setBPF(true);
        add(panel);

        for(int i = 1; i <= paths.size(); i++){
            Nodo nTemp = new Nodo();
            nTemp.setDato(i);
            nodes.add(nTemp);
        }
       
        while(!toVisit.isEmpty()){
            bpfMethod(toVisit.get(0));
            toVisit.removeAll(visited);
            xc += 300;
            yc = 50;
        }
        panel.repaint();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                dispose();
            }
        });
    }
    private void bpfMethod(int v){
        List<Integer> nexTemp;
        visited.add(v);
        if(v == toVisit.get(0)){
            nodes.get(v - 1).setX(150 + xc);
            nodes.get(v - 1).setY(yc);
            yc+=50;
        }
        nexTemp = paths.get(v);

        for(int w = 0; w < nexTemp.size(); w++){
            if(!visited.contains(nexTemp.get(w)) && nodes.get(nexTemp.get(w)-1).getX() == 0){
                nodes.get(nexTemp.get(w)-1).setX(300 * (w + 1) / (nexTemp.size() + 1) + xc);
                nodes.get(nexTemp.get(w)-1).setY(yc);   
            }
            Arista aTemp = new Arista();
            aTemp.setOrigen(v);
            aTemp.setDestino(nexTemp.get(w));
            aristArray.add(aTemp);
        }
        yc+=50;
        for (Integer w : nexTemp) 
            if(!visited.contains(w)) bpfMethod(w);
    }
}
