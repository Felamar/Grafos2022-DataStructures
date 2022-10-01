package data_structures.grafos2022;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


public class PintaGrafo extends JPanel{

    private ArrayList<Nodo> nodos;
    private ArrayList<Arista> aristas;
    private Line2D lineArista;
    private int conta, auxDragged, auxPopup;
    private boolean press = false, modified = false, creaArista = false;
    private AtomicBoolean dirigido;
    private Nodo lineNodo;
    private Polygon arrowHead, lineArrow;
    private JPopupMenu popupMenu;
    private JMenuItem borrar;

    public PintaGrafo(ArrayList<Nodo> nodo, ArrayList<Arista> arista, AtomicBoolean dirigido){
        setBackground(new Color(146, 222, 113));
        this.nodos=nodo;
        this.aristas = arista;
        this.dirigido = dirigido;
        lineNodo = null;
        lineArista = null;
        arrowHead = new Polygon(); 
            arrowHead.addPoint( 0, -20);
            arrowHead.addPoint( -7, -40);
            arrowHead.addPoint( 0, -35);
            arrowHead.addPoint( 7, -40);
        lineArrow = new Polygon(); 
            lineArrow.addPoint( 0, 0);
            lineArrow.addPoint( -7, -20);
            lineArrow.addPoint( 0, -15);
            lineArrow.addPoint( 7, -20);
        popupMenu = new JPopupMenu();
        borrar = new JMenuItem("Borrar");
        popupMenu.add(borrar);
        
        //ActionListeners
        
        addMouseMotionListener(new MouseMotionAdapter(){
            public void mouseMoved(MouseEvent e){
                if(!nodos.isEmpty()){
                    if(findNodo(e.getPoint()) != null)
                        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    else
                        setCursor(Cursor.getDefaultCursor());
                    if(creaArista){
                        lineArista.setLine(lineNodo.getPunto(), e.getPoint());
                        repaint();
                    }
                }
            }   

            public void mouseDragged(MouseEvent e){
                if(!SwingUtilities.isLeftMouseButton(e))
                    return;

                if(auxDragged != -1 && !nodos.isEmpty() && press){
                    nodos.get(auxDragged).setX(e.getX());
                    nodos.get(auxDragged).setY(e.getY());
                    nodos.get(auxDragged).setNode(new Ellipse2D.Double(e.getX()-20, e.getY()-20, 40, 40));
                    repaint();
                    modified = true;
                }
            }
        });

        addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                
                if(SwingUtilities.isRightMouseButton(e) && findNodo(e.getPoint()) != null){
                    creaArista = false;
                    lineArista = null;
                    repaint();
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    auxPopup = findNodo(e.getPoint()).getDato();
                }
                
                if(!SwingUtilities.isLeftMouseButton(e))
                    return;
                
                if(e.getClickCount() == 2 && !e.isConsumed()){
                    conta = nodos.size() + 1;
                    Nodo n = new Nodo();
                    n.setDato(conta);
                    n.setX(e.getX());
                    n.setY(e.getY());
                    n.setNode(new Ellipse2D.Double(n.getX()-20, n.getY()-20, 40, 40));
                    if(nodos.isEmpty()){
                        nodos.add(n);
                        repaint();
                        modified = true;
                        return;
                    }
                    if(findNodo(e.getPoint())==null){
                        nodos.add(n);
                        repaint();
                        modified = true;
                        return;
                    }
                }
                if(findNodo(e.getPoint()) != null && !creaArista){
                    lineNodo = findNodo(e.getPoint());
                    lineArista = new Line2D.Double(lineNodo.getX(), lineNodo.getY(), lineNodo.getX(), lineNodo.getY());
                    creaArista = true;
                    return;
                }
                if(findNodo(e.getPoint()) != null){
                    if(creaArista){
                        Arista a = new Arista();
                        int change = -1;
                        lineNodo = findNodo(e.getPoint());
                        lineArista.setLine(lineArista.getP1(), lineNodo.getPunto());
                        a.setOrigen(findNodo(lineArista.getP1()).getDato());
                        a.setDestino(lineNodo.getDato());               
                        repaint();
                        if(isDir()){
                            for(Arista x : aristas){
                                if(x.getOrigen() == a.getOrigen() && x.getDestino() == a.getDestino()){
                                    String[] buttons = { "Sí","No"};    
                                    int returnValue = JOptionPane.showOptionDialog(null, "Este camino ya existe\n ¿Quieres cambiar el valor del peso?", "WARNING",JOptionPane.WARNING_MESSAGE, 0, null, buttons, buttons[1]);
                                    if(returnValue == JOptionPane.YES_OPTION){
                                        change = aristas.indexOf(x);
                                        modified = true;
                                        break;
                                    }else{
                                        lineNodo = null;
                                        lineArista = null;
                                        creaArista = false;
                                        lineArrow.reset();
                                        lineArrow.addPoint( 0,0);
                                        lineArrow.addPoint( -7, -20);
                                        lineArrow.addPoint( 0, -15);
                                        lineArrow.addPoint( 7,-20);
                                        modified = false;
                                        repaint(); 
                                        return;
                                    }
                                }
                            }
                        }else{
                            for(Arista x : aristas){
                                if((x.getOrigen() == a.getOrigen() && x.getDestino() == a.getDestino()) || (x.getOrigen() == a.getDestino() && x.getDestino() == a.getOrigen())){
                                    String[] buttons = { "Sí","No"};    
                                    int returnValue = JOptionPane.showOptionDialog(null, "Este camino ya existe\n ¿Quieres cambiar el valor del peso?", "WARNING",JOptionPane.WARNING_MESSAGE, 0, null, buttons, buttons[1]);
                                    if(returnValue == JOptionPane.YES_OPTION){
                                        change = aristas.indexOf(x);
                                        modified = true;
                                        break;
                                    }else{
                                        lineNodo = null;
                                        lineArista = null;
                                        creaArista = false;
                                        lineArrow.reset();
                                        lineArrow.addPoint( 0,0);
                                        lineArrow.addPoint( -7, -20);
                                        lineArrow.addPoint( 0, -15);
                                        lineArrow.addPoint( 7,-20);
                                        modified = false;
                                        repaint(); 
                                        return;
                                    }
                                }
                            }
                        }
                        try {
                            do{
                                String s = JOptionPane.showInputDialog(null, "Introduzca el peso del camino:", "Grafos Otoño 2022", JOptionPane.PLAIN_MESSAGE);
                                if(s == null)
                                    break;
                                if(Integer.parseInt(s) <= 0){
                                    JOptionPane.showMessageDialog(null, "El peso debe ser mayor a 0", "WARNING", JOptionPane.WARNING_MESSAGE);
                                    continue;
                                }
                                if(change == -1){
                                    a.setPeso(Integer.parseInt(s));
                                    aristas.add(a);
                                    modified = true;
                                    break;
                                }else{
                                    aristas.get(change).setPeso(Integer.parseInt(s));
                                    modified = true;
                                    break;
                                }
                            }while(true);
                            lineNodo = null;
                            lineArista = null;
                            creaArista = false;
                            lineArrow.reset();
                            lineArrow.addPoint( 0,0);
                            lineArrow.addPoint( -7, -20);
                            lineArrow.addPoint( 0, -15);
                            lineArrow.addPoint( 7,-20);
                            repaint();   
                        }catch (NumberFormatException exception){
                            JOptionPane.showMessageDialog(null, "Sólo Inserte Números Enteros de \n0 a 2^(31)-1", "WARNING", JOptionPane.WARNING_MESSAGE);
                            lineNodo = null;
                            lineArista = null;
                            creaArista = false;
                            repaint();   
                        }
                                   
                    }
                }else{
                    lineNodo = null;
                    lineArista = null;
                    creaArista = false;
                    lineArrow.reset();
                    lineArrow.addPoint( 0,0);
                    lineArrow.addPoint( -7, -20);
                    lineArrow.addPoint( 0, -15);
                    lineArrow.addPoint( 7,-20);
                    repaint();
                    return;
                }
            }

            public void mousePressed(MouseEvent e1){
                press=true;
                if(!nodos.isEmpty())
                    if(findNodo(e1.getPoint()) != null){
                        auxDragged = findNodo(e1.getPoint()).getDato() - 1;
                    }else
                        auxDragged = -1;
            }

            public void mouseReleased(MouseEvent e2){
                press = false; 
            }

        });
    
        borrar.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

                String ObjButtons[] = {"Sí","No"};

                int PromptResult = JOptionPane.showOptionDialog(null,"¿Desea eliminar el nodo " + auxPopup + '?',"Grafos Otoño 2022",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,ObjButtons,ObjButtons[1]);

                if(PromptResult == JOptionPane.YES_OPTION){

                    for(int i = 0; i < aristas.size(); i++)
                        if(aristas.get(i).getOrigen() == auxPopup || aristas.get(i).getDestino() == auxPopup){
                            aristas.remove(i);
                            i--;
                        }

                    nodos.remove(auxPopup - 1);

                    for (Arista ar : aristas){
                        if(ar.getOrigen() > auxPopup)
                            ar.setOrigen(ar.getOrigen() - 1);
                        if(ar.getDestino() > auxPopup)
                            ar.setDestino((ar.getDestino() - 1)); 
                    }

                    for(int i = auxPopup - 1; i < nodos.size(); i++)
                        nodos.get(i).setDato(nodos.get(i).getDato() - 1);
                    
                    repaint(); 
                    modified = true;
                }

            }
        });
    
    }        
    
    public Nodo findNodo(Point2D p){
        for (Nodo n : nodos) {
            Ellipse2D r = (Ellipse2D) n.getNode();
            if (r.contains(p)) 
                return n;
        }
        return null;
    }

    public boolean isModified(){
        return modified;
    }

    public void setModified(boolean modified){
        this.modified = modified;
    }

    public boolean isDir(){
        return dirigido.get();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        AffineTransform tx = new AffineTransform();
        int dxArc , dyArc;

        g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        tx.setTransform(g2.getTransform());

        if(nodos.isEmpty())
            return;

        for(Arista a : aristas){
            double dx, dy, angle;
            a.setArista(new Line2D.Double(nodos.get(a.getOrigen() -1 ).getPunto(), nodos.get(a.getDestino() -1 ).getPunto()));
            dx = a.getArista().getX2() - a.getArista().getX1();
            dy = a.getArista().getY2() - a.getArista().getY1();
            angle = Math.atan2(dy, dx);
            g2.setColor(new Color(46, 145, 163));
            g2.draw((Line2D)a.getArista());

            if(a.getOrigen() == a.getDestino()){
                if(a.getArista().getX1() >= this.getWidth() / 2){
                    dxArc = -10;
                    angle = Math.PI;
                }else
                    dxArc = -80;

                if(a.getArista().getY1() >= this.getHeight() / 2)
                    dyArc = 0;
                else
                    dyArc = -40;
                g2.drawArc((int)a.getArista().getX2() + dxArc, (int)a.getArista().getY2() + dyArc, 90, 40, 0, 360 );
            }

            if(dirigido.get()){
                g2.translate(a.getArista().getX2(), a.getArista().getY2());
                g2.rotate((angle-Math.PI/2d));  
                g2.fill(arrowHead);
                g2.setTransform(tx);  
            }
        }

        for(Nodo n : nodos){
            g2.setColor(Color.WHITE);
            g2.fill((Ellipse2D)n.getNode());
            g2.setColor(new Color(48, 71, 37));
            g2.draw((Ellipse2D)n.getNode());
            String uwu = String.valueOf(n.getDato());
            g2.drawString(uwu, (int)n.getX() - g.getFontMetrics().stringWidth(uwu) / 2, (int)n.getY() + g.getFontMetrics().getHeight() / 4);            
        }
        
        if(creaArista){
            g2.setColor(new Color(46, 145, 163));
            g2.draw(lineArista);
            if(dirigido.get()){
                double dx, dy, angle;
                dx = lineArista.getX2() - lineArista.getX1();
                dy = lineArista.getY2() - lineArista.getY1();
                angle = Math.atan2(dy, dx);
                g2.translate(lineArista.getX2(), lineArista.getY2());
                g2.rotate(angle - Math.PI/2d);
                if(lineArista.getP1() != lineArista.getP2())
                    g2.fill(lineArrow);
                g2.setTransform(tx);
            }
        }
        g2.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        
        for(Arista a : aristas){
            double dx, dy;
            dx = a.getArista().getX2() - a.getArista().getX1();
            dy = a.getArista().getY2() - a.getArista().getY1();
            g2.setColor(new Color(78, 18, 163)); 
            if(!dirigido.get()){
                dx = dx * 9 / 4;
                dy = dy * 9 / 4;
            }
            if(dx == 0 && dy == 0){
                dy = 40 * 9 / 2;
                dx = g.getFontMetrics().stringWidth(String.valueOf(a.getPeso())) * 9 / 4;
                if(a.getArista().getY1() >= this.getHeight() / 2) 
                    dy = (-40 - g.getFontMetrics().getHeight() / 2) * 9 / 2 ;
            }
            g2.drawString(""+a.getPeso(), (int)(a.getArista().getX2() - dx * 2 / 9), (int)(a.getArista().getY2() - dy * 2 / 9)); 
        }
    } 

}