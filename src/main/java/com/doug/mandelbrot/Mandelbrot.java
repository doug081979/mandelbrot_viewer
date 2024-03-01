package com.doug.mandelbrot;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Mandelbrot {
    public static Mandelbrot instance;
    private int width;
    private int height;
    private Double xStart = Constants.X_START;
    private Double xEnd = Constants.X_END;
    private Double yStart = Constants.Y_START;
    private Double yEnd = Constants.Y_END;
    private Float maxIter = Constants.DEFAULT_MAX_ITER;

    public static Mandelbrot getInstance()
    {
        if (instance == null)
        {
            instance = new Mandelbrot();
        }

        return instance;
    }

    private Mandelbrot() {
        this.width = Constants.WIDTH;
        this.height = Constants.HEIGHT;
    }

    public BufferedImage calculate() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < width; i++) {
            Double x0 = xStart + ((double) i / (double) width) * (xEnd - xStart);

            for (int j = 0; j < height; j++) {
                Double y0 = yStart + ((double) j / (double) height) * (yEnd - yStart);

                float r = computeMandelbrot(x0, y0);

                Color color = computeColor(r);
                image.setRGB(i, j, color.getRGB());
            }
        }

        return image;
    }

    public Float getMaxIter() {
        return maxIter;
    }

    public void setMaxIter(Float maxIter) {
        this.maxIter = maxIter;
    }

    public Double getXStart() {
        return xStart;
    }

    public void setXStart(Double xStart) {
        this.xStart = xStart;
    }

    public Double getXEnd() {
        return xEnd;
    }

    public void setXEnd(Double xEnd) {
        this.xEnd = xEnd;
    }

    public Double getYStart() {
        return yStart;
    }

    public void setYStart(Double yStart) {
        this.yStart = yStart;
    }

    public Double getYEnd() {
        return yEnd;
    }

    public void setYEnd(Double yEnd) {
        this.yEnd = yEnd;
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
