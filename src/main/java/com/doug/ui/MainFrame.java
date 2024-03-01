package com.doug.ui;

import com.doug.mandelbrot.Constants;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame()
    {
        JScrollPane scrollPane = new JScrollPane(ImagePanel.getInstance());

        this.add(new MenuBar(this), BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(StatusBar.getInstance(), BorderLayout.SOUTH);

        this.setDefaultCloseOperation(MainFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
    }
}
