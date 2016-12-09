package com.hsenid.calculator;

/**
 * Populate the cartesian plane
 * Created by hsenid on 12/7/16.
 */

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CartesianPlane extends JPanel {
    private static final Stroke GRAPH_STROKE = new BasicStroke(0.9f);
    private int width = 1100;
    private int heigth = 700;
    private int padding = 25;
    private Color sineColor = new Color(44, 102, 230, 180);
    private Color cosColor = new Color(125, 23, 230, 180);
    private Color pointColor = new Color(100, 100, 100, 180);
    private Color gridColor = new Color(200, 200, 200, 200);
    private int pointWidth = 4;
    private int numberYDivisions = 10;
    private List<Point> graphPoints;

    public CartesianPlane(List<Point> gp) {
        graphPoints = gp;
    }

    @Override
    protected void paintComponent(Graphics g) {
        System.out.println(graphPoints.toString());
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int interval = 50;
        int plotAreaWidth = getWidth() - 2 * padding;
        int plotAreaHeight = getHeight() - 2 * padding;
        int numberOfYGlyphs = (plotAreaWidth / (interval * 2));
        int numberOfXGlyphs = plotAreaHeight / (interval * 2);

        // draw white background
        g2.setColor(Color.WHITE);
        g2.fillRect(padding, padding, getWidth() - (2 * padding), getHeight() - 2 * padding);
        g2.setColor(Color.BLACK);

        // create hatch marks and grid lines for y axis.
        for (int i = -(numberOfXGlyphs); i <= numberOfXGlyphs; i++) {
            int x0 = padding;
            int x1 = getWidth() - padding;
            int y = centerY + interval * i;

            g2.setColor(gridColor);
            g2.drawLine(x0, y, x1, y);

            g2.setColor(Color.BLACK);
            x0 = getWidth() / 2 - pointWidth;
            x1 = x0 + 2 * pointWidth;
            g2.drawLine(x0, y, x1, y);
            g2.drawString(String.valueOf(-i * interval), x1, y + g2.getFontMetrics().getHeight());
        }

        for (int i = -(numberOfYGlyphs); i <= numberOfYGlyphs; i++) {
            int y0 = padding;
            int y1 = getHeight() - padding;
            int x = centerX + interval * i;

            g2.setColor(gridColor);
            g2.drawLine(x, y0, x, y1);

            y0 = getHeight() / 2 - pointWidth;
            y1 = y0 + 2 * pointWidth;

            g2.setColor(Color.BLACK);
            g2.drawLine(x, y0, x, y1);
            if (i != 0)
                g2.drawString(String.valueOf(i * interval), x, y1 + g2.getFontMetrics().getHeight());
        }

        // create x and y axes
        g2.drawLine(getWidth() / 2, padding, getWidth() / 2, getHeight() - padding);
        g2.drawLine(padding, getHeight() / 2, getWidth() - padding, getHeight() / 2);

        g2.setColor(sineColor);
        g2.setStroke(GRAPH_STROKE);

        //Draw the graph
        for (int i = 0; i < graphPoints.size() - 1; i++) {
            int x1 = centerX + graphPoints.get(i).x;
            int y1 = centerY - graphPoints.get(i).y;
            int x2 = centerX + graphPoints.get(i + 1).x;
            int y2 = centerY - graphPoints.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }
    }
}
