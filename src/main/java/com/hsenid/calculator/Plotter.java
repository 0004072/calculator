package com.hsenid.calculator;

/**
 * Created by hsenid on 12/7/16.
 */

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class Plotter extends JPanel {

    private static final Stroke GRAPH_STROKE = new BasicStroke(0.9f);
    private int width = 1100;
    private int heigth = 900;
    private int padding = 25;
    private Color sineColor = new Color(44, 102, 230, 180);
    private Color cosColor = new Color(125, 23, 230, 180);
    private Color pointColor = new Color(100, 100, 100, 180);
    private Color gridColor = new Color(200, 200, 200, 200);
    private int pointWidth = 4;
    private int numberYDivisions = 10;

    private static void createAndShowGui() {
        Plotter plotter = new Plotter();
        plotter.setPreferredSize(new Dimension(1100, 900));
        JFrame frame = new JFrame("Plotter");
        frame.getContentPane().add(plotter);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void load() {
        createAndShowGui();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //Sine for an example
        List<Point> graphPoints = new ArrayList<>();

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int intervalX = (getWidth() - (2 * padding)) / 8;
        int intervalY = (getHeight() - (2 * padding)) / 8;

        // draw white background
        g2.setColor(Color.WHITE);
        g2.fillRect(padding, padding, getWidth() - (2 * padding), getHeight() - 2 * padding);
        g2.setColor(Color.BLACK);

        // create hatch marks and grid lines for y axis.
        for (int i = 0; i < 9; i++) {
            int x0 = getWidth() / 2 - pointWidth;
            int x1 = x0 + 2 * pointWidth;
            int y = 1 + padding + intervalY * i;
            g2.drawLine(x0, y, x1, y);
            /*int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;
            int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
            int y1 = y0;
            if (scores.size() > 0) {
                g2.setColor(gridColor);
                g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
                g2.setColor(Color.BLACK);
                String yLabel = ((int) ((getMinScore() + (getMaxScore() - getMinScore()) * ((i * 1.0) / numberYDivisions)) * 100)) / 100.0 + "";
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }
            g2.drawLine(x0, y0, x1, y1);*/
        }

        for (int i = 0; i < 9; i++) {
            int y0 = getHeight() / 2 - pointWidth;
            int y1 = y0 + 2 * pointWidth;
            int x = 1 + padding + intervalX * i;
            g2.drawLine(x, y0, x, y1);
            /*if (scores.size() > 1) {
                int x0 = i * (getWidth() - padding * 2 - labelPadding) / (scores.size() - 1) + padding + labelPadding;
                int x1 = x0;
                int y0 = getHeight() - padding - labelPadding;
                int y1 = y0 - pointWidth;
                if ((i % ((int) ((scores.size() / 20.0)) + 1)) == 0) {
                    g2.setColor(gridColor);
                    g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
                    g2.setColor(Color.BLACK);
                    String xLabel = i + "";
                    FontMetrics metrics = g2.getFontMetrics();
                    int labelWidth = metrics.stringWidth(xLabel);
                    g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
                }
                g2.drawLine(x0, y0, x1, y1);
            }*/
        }

        // create x and y axes
        g2.drawLine(getWidth() / 2, padding, getWidth() / 2, getHeight() - padding);
        g2.drawLine(padding, getHeight() / 2, getWidth() - padding, getHeight() / 2);

        Stroke oldStroke = g2.getStroke();
        g2.setColor(sineColor);
        g2.setStroke(GRAPH_STROKE);

        //Sine function
        for (int x = -360; x <= 360; x++) {
            double sinX = Math.sine(Math.toRadians(x));
            int x1 = centerX + (x * intervalX / 180);
            int y1 = centerY - (int) (sinX * intervalY);
            graphPoints.add(new Point(x1, y1));
        }

        for (int i = 0; i < graphPoints.size() - 1; i++) {
            int x1 = graphPoints.get(i).x;
            int y1 = graphPoints.get(i).y;
            int x2 = graphPoints.get(i + 1).x;
            int y2 = graphPoints.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }

        g2.setColor(cosColor);
        graphPoints.clear();
        //Cosine function
        for (int x = -360; x <= 360; x++) {
            double cosX = 1 / Math.secant(Math.toRadians(x));
            int x1 = centerX + (x * intervalX / 180);
            int y1 = centerY - (int) (cosX * intervalY);
            graphPoints.add(new Point(x1, y1));
        }

        for (int i = 0; i < graphPoints.size() - 1; i++) {
            int x1 = graphPoints.get(i).x;
            int y1 = graphPoints.get(i).y;
            int x2 = graphPoints.get(i + 1).x;
            int y2 = graphPoints.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }

        g2.setStroke(oldStroke);
        g2.setColor(pointColor);
        /*for (int i = 0; i < graphPoints.size(); i++) {
            int x = graphPoints.get(i).x - pointWidth / 2;
            int y = graphPoints.get(i).y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(x, y, ovalW, ovalH);
        }*/
    }
}
