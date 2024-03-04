package com.doug.mandelbrot;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.UUID;

public class MandelbrotWorker extends Thread {
    private final int xPixelStart;
    private int xPixelEnd;
    private final int yPixelStart;
    private int yPixelEnd;
    private final double xStart;
    private final double xEnd;
    private final double yStart;
    private final double yEnd;
    private final float maxIter;
    private final int width;
    private final int height;
    private final BufferedImage image;
    
    public MandelbrotWorker(int xPixelStart, int xPixelEnd, int yPixelStart, int yPixelEnd,
                            double xStart, double xEnd, double yStart, double yEnd, float maxIter,
                            int width, int height, BufferedImage image)
    {
        this.xPixelStart = xPixelStart;
        this.xPixelEnd = xPixelEnd;
        this.yPixelStart = yPixelStart;
        this.yPixelEnd = yPixelEnd;
        this.xStart = xStart;
        this.xEnd = xEnd;
        this.yStart = yStart;
        this.yEnd = yEnd;
        this.maxIter = maxIter;
        this.width = width;
        this.height = height;
        this.image = image;
        this.setName("Mandelbrot Worker: " + UUID.randomUUID());
        this.setDaemon(true);
    }
    
    @Override
    public void run()
    {
        if (xPixelEnd == width / 2)
        {
            xPixelEnd += 1;
        }

        if (yPixelEnd == height / 2)
        {
            yPixelEnd += 1;
        }

        for (int i = xPixelStart; i < xPixelEnd; i++) {
            Double x0 = xStart + ((double) i / (double) width) * (xEnd - xStart);

            for (int j = yPixelStart; j < yPixelEnd; j++) {
                Double y0 = yStart + ((double) j / (double) height) * (yEnd - yStart);

                float r = computeMandelbrot(x0, y0);

                Color color = computeColor(r);
                image.setRGB(i, j, color.getRGB());
            }
        }
    }

    private float computeMandelbrot(Double x0, Double y0) {
        float iter = 0;

        Double x = 0.0;
        Double y = 0.0;

        Double x2 = 0.0;
        Double y2 = 0.0;

        while ((x2 + y2 <= 4) && iter < maxIter) {
            y = (2 * x * y) + y0;
            x = (x2 - y2) + x0;
            x2 = x * x;
            y2 = y * y;
            iter++;
        }

        return iter;
    }

    private Color computeColor(float iter) {
        Color color;

        if (iter == maxIter) {
            color = Color.black;
        }  else {
            color = Color.getHSBColor(iter / maxIter, 1, 1);
        }

        return color;
    }
}
