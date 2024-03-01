package com.doug;

import com.doug.ui.MainFrame;

import javax.swing.*;

public class MandelbrotViewer
{
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainFrame();
            }
        });
    }
}
