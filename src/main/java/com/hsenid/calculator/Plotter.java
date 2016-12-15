package com.hsenid.calculator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
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
    private Logger logger;
    public Plotter(String title) throws HeadlessException {
        super(title);
        graphPoints = new ArrayList<>();
        logger = LogManager.getLogger(Plotter.class);

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
        itmChangeFunction.addActionListener(e -> {
            graphPoints.clear();
            try {
                String function = JOptionPane.showInputDialog("<html>Enter the function needs to be plotted. Note the following when the function is entered.<ul><li>Use 'x' to represent the variable</li></ul>", "");
                for (int x1 = -300; x1 <= 300; x1++) {
                    int y1 = (int) CalculatorUI.eval(function.replaceAll("x", String.valueOf(x1)));
                    graphPoints.add(new Point(x1, y1));
                }
                CartesianPlane cp1 = new CartesianPlane(graphPoints);
                cp1.setPreferredSize(new Dimension(1100, 700));
                reloadPlane(cp1);
            } catch (NullPointerException ne) {
                logger.error(ne);
            } catch (RuntimeException re) {
                JOptionPane.showMessageDialog(null, re.getMessage());
                logger.error(re);
            } catch (Exception ex) {
                logger.error(ex);
            }
        });

        setJMenuBar(menubar);
    }

    public void reloadPlane(CartesianPlane cp) {
        getContentPane().removeAll();
        getContentPane().add(cp);
        revalidate();
        repaint();
    }
}
