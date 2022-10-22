package data_structures.grafos2022;

import javax.swing.JFrame;
import java.util.*;
import java.util.ArrayList;

public class BPFWindow extends JFrame {
    private HashMap<Integer, List<Integer>> paths;
    private Set<Integer> visited;
    private ArrayList<Nodo> nodes;
    private ArrayList<Arista> aristArray;
    private PintaGrafo panel;

    BPFWindow(HashMap<Integer, List<Integer>> map){
        setSize(1000,700);
        setLocation(50,50);
        setTitle("Búsqueda en Profundidad");
        paths = map;
        visited = new HashSet<>();
        nodes = new ArrayList<Nodo>();
        aristArray = new ArrayList<Arista>();
        panel = new PintaGrafo(nodes, aristArray);
        panel.setBounds(getBounds());
        add(panel);
        for(int i = 1; i <= paths.size(); i++){
            Nodo nTemp = new Nodo();
            nTemp.setDato(i);
            nodes.add(nTemp);
        }
        bpfMethod(5);
        panel.repaint();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void bpfMethod(int v){
        List<Integer> nexTemp;
        visited.add(v);
        if(v == 1){
            nodes.get(v - 1).setX(250);
            nodes.get(v - 1).setY(50);
        }
        nexTemp = paths.get(v);

        for(int w = 0; w < nexTemp.size(); w++){
            if(!visited.contains(nexTemp.get(w))){
                nodes.get(nexTemp.get(w)-1).setX(500 / ((visited.size() + 1)*nexTemp.size()));
                nodes.get(nexTemp.get(w)-1).setY(700 * visited.size() / nodes.size() + 50);
                Arista aTemp = new Arista();
                aTemp.setOrigen(v);
                aTemp.setDestino(nexTemp.get(w));
                aTemp.setPeso(1);
                aristArray.add(aTemp);
                bpfMethod(nexTemp.get(w));
            }else{
                Arista aTemp = new Arista();
                aTemp.setOrigen(v);
                aTemp.setDestino(nexTemp.get(w));
                aTemp.setPeso(1);
                aristArray.add(aTemp);
            }
        }
    }
}
