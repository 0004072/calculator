package com.hsenid.calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Plotter: Populates & manipulates the plotter window.
 * Created by 000407 on 12/9/2016.
 */
public class Plotter extends JFrame {
    private List<Point> graphPoints;

    public void reloadPlane(CartesianPlane cp){
        getContentPane().removeAll();
        getContentPane().add(cp);
        revalidate();
        repaint();
    }

    public Plotter(String title) throws HeadlessException {
        super(title);
        graphPoints = new ArrayList<>();

        CartesianPlane cp = new CartesianPlane(graphPoints);
        cp.setPreferredSize(new Dimension(1100, 700));

        JMenuBar menubar = new JMenuBar();
        JMenu menu;
        JMenuItem itmSave, itmChangeFunction, itmClearGraph;

        //File
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);

        //File - Save
        itmSave = new JMenuItem("Save...");
        itmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        menu.add(itmSave);

        menubar.add(menu);

        //Function
        menu = new JMenu("Function");
        menu.setMnemonic(KeyEvent.VK_F);

        //Function - Change Function
        itmChangeFunction = new JMenuItem("Change function");
        itmChangeFunction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));

        menu.add(itmChangeFunction);

        menubar.add(menu);

        getContentPane().add(cp);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        //Actions
        itmChangeFunction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graphPoints.clear();
                try {
                    String function = JOptionPane.showInputDialog("<html>Enter the function needs to be plotted. Note the following when the function is entered.<ul><li>Use 'x' to represent the variable</li></ul>", "");
                    for(int x = -300; x <= 300; x++){
                        int y = (int) CalculatorUI.eval(function.replaceAll("x", String.valueOf(x)));
                        graphPoints.add(new Point(x, y));
                    }
                    CartesianPlane cp = new CartesianPlane(graphPoints);
                    cp.setPreferredSize(new Dimension(1100, 700));
                    reloadPlane(cp);
                }
                catch (NullPointerException ne) {
                    ne.printStackTrace();
                }
                catch (RuntimeException re){
                    JOptionPane.showMessageDialog(null, re.getMessage());
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        setJMenuBar(menubar);
    }
}
