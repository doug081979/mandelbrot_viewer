package com.doug.mandelbrot;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Mandelbrot {
    public static Mandelbrot instance;
    private final int width;
    private final  int height;
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
        int width2 = width / 2;
        int height2 = height / 2;

        ArrayList<MandelbrotWorker> workers = new ArrayList<>();

        workers.add(new MandelbrotWorker(0, width2, 0, height2,
                xStart, xEnd, yStart, yEnd, maxIter, width, height, image));
        workers.add(new MandelbrotWorker(width2 + 1, width, 0, height2,
                xStart, xEnd, yStart, yEnd, maxIter, width, height, image));
        workers.add(new MandelbrotWorker(0, width2, height2 + 1, height,
                xStart, xEnd, yStart, yEnd, maxIter, width, height, image));
        workers.add(new MandelbrotWorker(width2 + 1, width, height2 + 1, height,
                xStart, xEnd, yStart, yEnd, maxIter, width, height, image));

        try (ExecutorService es = Executors.newCachedThreadPool()) {
            for (MandelbrotWorker worker : workers) {
                es.execute(worker);
            }
            es.shutdown();
            es.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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
}
