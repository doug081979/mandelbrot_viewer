package com.doug.ui;

import javax.swing.*;
import java.awt.*;

public class StatusBar extends JPanel {
    public static StatusBar instance;
    private JLabel xLabel;
    private JLabel yLabel;
    private JLabel xValue;
    private JLabel yValue;

    private StatusBar()
    {
        xLabel = new JLabel("X:");
        xValue = new JLabel();

        yLabel = new JLabel("Y:");
        yValue = new JLabel();

        this.setPreferredSize(new Dimension(1000, 30));
        this.setLayout(new FlowLayout(FlowLayout.RIGHT));

        this.add(xLabel);
        this.add(xValue);
        this.add(yLabel);
        this.add(yValue);
    }

    public static StatusBar getInstance()
    {
        if (instance == null)
        {
            instance = new StatusBar();
        }

        return instance;
    }

    public void setValues(double x, double y)
    {
        xValue.setText(Double.toString(x));
        yValue.setText(Double.toString(y));
    }
}
