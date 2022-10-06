package data_structures.grafos2022;

import java.awt.geom.*;

public class Arista {

    private Line2D arista = null;
    private int peso, Origen, Destino;

    public Line2D getArista() {
        if(arista == null)
            arista = new Line2D.Double(0, 0, 0, 0);
        
        return arista;
    }
    public void setArista(Line2D arista) {
        this.arista = arista;
    }

    public int getPeso() {
        return peso;
    }
    public void setPeso(int peso) {
        this.peso = peso;
    }

    public int getOrigen() {
        return Origen;
    }
    public void setOrigen(int Origen) {
        this.Origen = Origen;
    }

    public int getDestino() {
        return Destino;
    }
    public void setDestino(int Destino) {
        this.Destino = Destino;
    }
    
}
