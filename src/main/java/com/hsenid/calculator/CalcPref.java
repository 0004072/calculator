package com.hsenid.calculator;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hsenid on 12/15/16.
 */
public class CalcPref {
    private JPanel mainPanel;
    private JRadioButton fileRadioButton;
    private JRadioButton databaseRadioButton;

    public static void main(String[] args) {
        CalcPref obj = new CalcPref();
        obj.createGui();
    }

    private void createGui() {
        JFrame frame = new JFrame("Preferences");
        frame.setSize(new Dimension(300, 400));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }
}
