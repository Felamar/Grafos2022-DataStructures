package data_structures.grafos2022;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


public class PintaGrafo extends JPanel{

    private int auxData, auxDragged, nodePopUp, aristaPopUp = -1;
    private boolean press = false, isModified = false, creatingArista = false;
    private AtomicBoolean isDirected;
    private ArrayList<Nodo> nodesArray;
    private ArrayList<Arista> aristasArray;
    private Nodo lineNodo;
    private Line2D lineArista;
    private Polygon arrowHead, lineArrow;
    private JPopupMenu popupMenu;
    private JMenuItem ereaseItem;

    public PintaGrafo(ArrayList<Nodo> nodo, ArrayList<Arista> arista, AtomicBoolean isDirected){
        setBackground(new Color(146, 222, 113));
        this.nodesArray = nodo;
        this.aristasArray = arista;
        this.isDirected = isDirected;
        lineNodo = null;
        lineArista = null;
        arrowHead = new Polygon(); 
        arrowHead.addPoint( 0, -19);
        arrowHead.addPoint( -7, -39);
        arrowHead.addPoint( 0, -35);
        arrowHead.addPoint( 7, -39);
        lineArrow = new Polygon(); 
        lineArrow.addPoint( 0, 0);
        lineArrow.addPoint( -7, -20);
        lineArrow.addPoint( 0, -15);
        lineArrow.addPoint( 7, -20);
        popupMenu = new JPopupMenu();
        ereaseItem = new JMenuItem("Borrar");
        popupMenu.add(ereaseItem);
        
        //ActionListeners
        
        addMouseMotionListener(new MouseMotionAdapter(){
            public void mouseMoved(MouseEvent e){
                if(nodesArray.isEmpty())
                    return;
                if(findNodo(e.getPoint()) != null)
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                if(creatingArista){
                    lineArista.setLine(lineNodo.getPunto(), e.getPoint());
                    repaint();
                }
                if(findArista(e.getX(), e.getY()) != null)
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                if(findNodo(e.getPoint()) == null && findArista(e.getX(), e.getY()) == null)
                    setCursor(Cursor.getDefaultCursor());
            }
            public void mouseDragged(MouseEvent e){
                if(!SwingUtilities.isLeftMouseButton(e))
                    return;
                if(auxDragged != -1 && !nodesArray.isEmpty() && press){
                    nodesArray.get(auxDragged).setX(e.getX());
                    nodesArray.get(auxDragged).setY(e.getY());
                    nodesArray.get(auxDragged).setNode(new Ellipse2D.Double(e.getX() - 20, e.getY() - 20, 40, 40));
                    repaint();
                    isModified = true;
                }
            }
        });

        addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                if(SwingUtilities.isRightMouseButton(e) && findNodo(e.getPoint()) != null){
                    creatingArista = false;
                    lineArista = null;
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    nodePopUp = findNodo(e.getPoint()).getDato();
                    repaint();
                    return;
                }
                if(SwingUtilities.isRightMouseButton(e) && findArista(e.getX(), e.getY()) != null){
                    creatingArista = false;
                    lineArista = null;
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    aristaPopUp = aristasArray.indexOf(findArista(e.getX(), e.getY()));
                    repaint();
                    return;
                }
                if(!SwingUtilities.isLeftMouseButton(e))
                    return;
                if(e.getClickCount() == 2 && !e.isConsumed()){
                    Nodo nodeTemp = new Nodo();
                    auxData = nodesArray.size() + 1;
                    nodeTemp.setX(e.getX());
                    nodeTemp.setY(e.getY());
                    nodeTemp.setDato(auxData);
                    nodeTemp.setNode(new Ellipse2D.Double(nodeTemp.getX() - 20, nodeTemp.getY() - 20, 40, 40));
                    if(nodesArray.isEmpty()){
                        nodesArray.add(nodeTemp);
                        isModified = true;
                        repaint();
                        return;
                    }
                    if(findNodo(e.getPoint()) == null){
                        nodesArray.add(nodeTemp);
                        isModified = true;
                        repaint();
                        return;
                    }
                }
                if(findNodo(e.getPoint()) != null && !creatingArista){
                    lineNodo = findNodo(e.getPoint());
                    lineArista = new Line2D.Double(lineNodo.getX(), lineNodo.getY(), lineNodo.getX(), lineNodo.getY());
                    creatingArista = true;
                    return;
                }
                if(findNodo(e.getPoint()) == null && creatingArista){
                    creatingArista = false;
                    lineNodo = null;
                    lineArista = null;
                    repaint();
                    return;
                }
                if(findNodo(e.getPoint()) != null && creatingArista){
                    Arista aristaTemp = new Arista();
                    String[] buttonsTemp = { "Sí","No"}; 
                    int changePeso = -1;
                    if(findNodo(e.getPoint()) == findNodo(lineArista.getP1())){
                        lineArista = null;
                        lineNodo = null;
                        creatingArista = false;
                        repaint();   
                        return;
                    }
                    lineNodo = findNodo(e.getPoint());
                    lineArista.setLine(lineArista.getP1(), lineNodo.getPunto());
                    aristaTemp.setOrigen(findNodo(lineArista.getP1()).getDato());
                    aristaTemp.setDestino(lineNodo.getDato());               
                    repaint();
                    for(Arista aristaFor : aristasArray){
                        if((aristaFor.getOrigen() == aristaTemp.getOrigen() && aristaFor.getDestino() == aristaTemp.getDestino()) || (!isDirectedM() && aristaFor.getOrigen() == aristaTemp.getDestino() && aristaFor.getDestino() == aristaTemp.getOrigen())){
                            int returnValue = JOptionPane.showOptionDialog(null, "Este camino ya existe\n ¿Quieres cambiar el valor del peso?", "WARNING",JOptionPane.WARNING_MESSAGE, 0, null, buttonsTemp, buttonsTemp[1]);
                            if(returnValue == JOptionPane.YES_OPTION){
                                changePeso = aristasArray.indexOf(aristaFor);
                                isModified = true;
                                break;
                            }else{
                                creatingArista = false;
                                lineNodo = null;
                                lineArista = null;
                                repaint(); 
                                return;
                            }
                        }
                    }
                    try {
                        String s = JOptionPane.showInputDialog(null, "Introduzca el peso del camino:", "Grafos Otoño 2022", JOptionPane.PLAIN_MESSAGE);
                        if(s == null)
                            throw new NumberFormatException();
                        if(Integer.parseInt(s) <= 0)
                            throw new NumberFormatException();
                        if(changePeso == -1){
                            aristaTemp.setPeso(Integer.parseInt(s));
                            aristasArray.add(aristaTemp);
                            isModified = true;
                        }else{
                            aristasArray.get(changePeso).setPeso(Integer.parseInt(s));
                            isModified = true;
                        }
                        lineArista = null;
                        lineNodo = null;
                        creatingArista = false;
                        repaint();   
                    }catch (NumberFormatException exception){
                        JOptionPane.showMessageDialog(null, "Sólo Inserte Números Enteros de \n1 a 2^(31)-1", "WARNING", JOptionPane.WARNING_MESSAGE);
                        lineArista = null;
                        lineNodo = null;
                        creatingArista = false;
                        repaint();   
                    }
                }
            }
            public void mousePressed(MouseEvent e1){
                press=true;
                if(!nodesArray.isEmpty())
                    if(findNodo(e1.getPoint()) != null)
                        auxDragged = findNodo(e1.getPoint()).getDato() - 1;
                    else
                        auxDragged = -1;
            }
            public void mouseReleased(MouseEvent e2){
                press = false; 
            }

        });
    
        ereaseItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String ObjButtons[] = {"Sí","No"};
                int PromptResult = JOptionPane.showOptionDialog(null,"¿Desea eliminar el " + (aristaPopUp >= 0 ? "arista" : "nodo"), "Grafos Otoño 2022",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,ObjButtons,ObjButtons[1]);
                if(PromptResult != JOptionPane.YES_OPTION){
                    aristaPopUp = -1;
                    return;
                }
                if(aristaPopUp >= 0){
                    aristasArray.remove(aristaPopUp);
                    aristaPopUp = - 1;
                    repaint();
                    return;
                }
                for(int i = 0; i < aristasArray.size(); i++)
                    if(aristasArray.get(i).getOrigen() == nodePopUp || aristasArray.get(i).getDestino() == nodePopUp){
                        aristasArray.remove(i);
                        i--;
                    }
                nodesArray.remove(nodePopUp - 1);
                for (Arista ar : aristasArray){
                    if(ar.getOrigen() > nodePopUp)
                        ar.setOrigen(ar.getOrigen() - 1);
                    if(ar.getDestino() > nodePopUp)
                        ar.setDestino((ar.getDestino() - 1)); 
                }
                for(int i = nodePopUp - 1; i < nodesArray.size(); i++)
                    nodesArray.get(i).setDato(nodesArray.get(i).getDato() - 1);
                isModified = true;
                repaint(); 
            }
        });
    
    }        
    
    public Nodo findNodo(Point2D point){
        for (Nodo nodeFor : nodesArray) {
            Ellipse2D ellipseTemp = (Ellipse2D) nodeFor.getNode();
            if (ellipseTemp.contains(point)) 
                return nodeFor;
        }
        return null;
    }

    public Arista findArista(double x, double y){
        for (Arista aristaFor : aristasArray) {
            Line2D lineTemp = (Line2D) aristaFor.getArista();
            if(lineTemp.intersects(x - 2, y - 2, 4, 4))
                return aristaFor;
        }
        return null;
    }

    public boolean isModified(){
        return isModified;
    }

    public void setModified(boolean isModified){
        this.isModified = isModified;
    }

    public boolean isDirectedM(){
        return isDirected.get();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        int dxArc , dyArc;
        Graphics2D g2 = (Graphics2D)g;
        AffineTransform tx = new AffineTransform();
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh);
        g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        tx.setTransform(g2.getTransform());
        if(nodesArray.isEmpty())
            return;
        for(Arista aristaFor : aristasArray){
            double dx, dy, angle;
            aristaFor.setArista(new Line2D.Double(nodesArray.get(aristaFor.getOrigen() -1).getPunto(), nodesArray.get(aristaFor.getDestino() -1).getPunto()));
            dx = aristaFor.getArista().getX2() - aristaFor.getArista().getX1();
            dy = aristaFor.getArista().getY2() - aristaFor.getArista().getY1();
            angle = Math.atan2(dy, dx);
            g2.setColor(new Color(62, 169, 189));
            g2.setStroke(new BasicStroke(2));
            g2.draw((Line2D)aristaFor.getArista());
            g2.setStroke(new BasicStroke(1));
            if(aristaFor.getOrigen() == aristaFor.getDestino()){
                if(aristaFor.getArista().getX1() >= this.getWidth() / 2){
                    dxArc = -10;
                    angle = Math.PI;
                }else
                    dxArc = -80;

                if(aristaFor.getArista().getY1() >= this.getHeight() / 2)
                    dyArc = 0;
                else
                    dyArc = -40;
                g2.setStroke(new BasicStroke(2));
                g2.drawArc((int)aristaFor.getArista().getX2() + dxArc, (int)aristaFor.getArista().getY2() + dyArc, 90, 40, 0, 360 );
                g2.setStroke(new BasicStroke(1));
            }

            if(isDirected.get()){
                g2.setColor(new Color(58, 138, 153));
                g2.translate(aristaFor.getArista().getX2(), aristaFor.getArista().getY2());
                g2.rotate((angle-Math.PI/2d));  
                g2.fill(arrowHead);
                g2.setTransform(tx);  
            }
        }
        for(Nodo nodeFor : nodesArray){
            g2.setColor(Color.WHITE);
            g2.fill((Ellipse2D)nodeFor.getNode());
            g2.setColor(new Color(48, 71, 37));
            g2.draw((Ellipse2D)nodeFor.getNode());
            String uwu = String.valueOf(nodeFor.getDato());
            g2.drawString(uwu, (int)nodeFor.getX() - g.getFontMetrics().stringWidth(uwu) / 2, (int)nodeFor.getY() + g.getFontMetrics().getHeight() / 4);            
        }
        if(creatingArista){
            g2.setColor(new Color(62, 169, 189));
            g2.setStroke(new BasicStroke(2));
            g2.draw(lineArista);
            if(isDirected.get()){
                double dx, dy, angle;
                dx = lineArista.getX2() - lineArista.getX1();
                dy = lineArista.getY2() - lineArista.getY1();
                angle = Math.atan2(dy, dx);
                g2.translate(lineArista.getX2(), lineArista.getY2());
                g2.rotate(angle - Math.PI/2d);
                g2.setColor(new Color(58, 138, 153));
                if(lineArista.getP1() != lineArista.getP2())
                    g2.fill(lineArrow);
                g2.setTransform(tx);
            }
            g2.setStroke(new BasicStroke(1));
        }
        g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));
        for(Arista aristaFor : aristasArray){
            double dx, dy;
            dx = aristaFor.getArista().getX2() - aristaFor.getArista().getX1();
            dy = aristaFor.getArista().getY2() - aristaFor.getArista().getY1();
            g2.setColor(new Color(17, 105, 115)); 
            if(!isDirected.get()){
                dx = dx * 9 / 4;
                dy = dy * 9 / 4;
            }
            if(dx == 0 && dy == 0){
                dy = 40 * 9 / 2;
                dx = g.getFontMetrics().stringWidth(String.valueOf(aristaFor.getPeso())) * 9 / 4;
                if(aristaFor.getArista().getY1() >= this.getHeight() / 2) 
                    dy = (-40 - g.getFontMetrics().getHeight() / 2) * 9 / 2;
            }
            g2.drawString("" + aristaFor.getPeso(), (int)(aristaFor.getArista().getX2() - dx * 2 / 9), (int)(aristaFor.getArista().getY2() - dy * 7 / 27)); 
        }
    } 
}