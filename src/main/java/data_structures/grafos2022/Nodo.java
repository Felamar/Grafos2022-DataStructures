package data_structures.grafos2022;

import java.awt.geom.*;
import java.awt.*;

public class Nodo {
    private int Dato, x, y;
    private Ellipse2D node = null;
    private Point punto;
    
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDato() {
        return Dato;
    }

    public void setDato(int Dato) {
        this.Dato = Dato;
    }

    public Ellipse2D getNode() {
        if(node == null)
            node = new Ellipse2D.Double(x-20, y-20, 40, 40);
        return node;
    }

    public void setNode(Ellipse2D node) {
        this.node = node;
    }

    public Point getPunto() {
        this.punto = new Point(x,y);
        return punto;
    }

}
