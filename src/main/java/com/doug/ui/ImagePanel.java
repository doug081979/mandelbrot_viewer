package com.doug.ui;

import com.doug.mandelbrot.Constants;
import com.doug.mandelbrot.Mandelbrot;
import com.doug.xml.Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    public static ImagePanel instance;
    private boolean drawingRect = false;
    private double rectXStart;
    private double rectXEnd;
    private double rectYStart;
    private double rectYEnd;
    private BufferedImage currentImage;
    private boolean zoomed = false;

    public static ImagePanel getInstance()
    {
        if (instance == null)
        {
            instance = new ImagePanel();
        }

        return instance;
    }

    private ImagePanel() {
        this.setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        createListeners();
        currentImage = Mandelbrot.getInstance().calculate();
    }

    public void setParameters(double xStart, double xEnd, double yStart, double yEnd)
    {
        Mandelbrot.getInstance().setXStart(xStart);
        Mandelbrot.getInstance().setXEnd(xEnd);
        Mandelbrot.getInstance().setYStart(yStart);
        Mandelbrot.getInstance().setYEnd(yEnd);
        zoomed = true;
        currentImage = Mandelbrot.getInstance().calculate();
        repaint();
    }

    public Project saveProject()
    {
        Project project = new Project();
        project.setXStart(Mandelbrot.getInstance().getXStart());
        project.setXEnd(Mandelbrot.getInstance().getXEnd());
        project.setYStart(Mandelbrot.getInstance().getYStart());
        project.setYEnd(Mandelbrot.getInstance().getYEnd());
        project.setMaxIter(Mandelbrot.getInstance().getMaxIter().intValue());
        project.setZoomed(zoomed);
        return project;
    }

    public void loadProject(Project project)
    {
        Mandelbrot.getInstance().setXStart(project.getXStart());
        Mandelbrot.getInstance().setXEnd(project.getXEnd());
        Mandelbrot.getInstance().setYStart(project.getYStart());
        Mandelbrot.getInstance().setYEnd(project.getYEnd());
        Mandelbrot.getInstance().setMaxIter((float)project.getMaxIter());
        zoomed = project.isZoomed();
        currentImage = Mandelbrot.getInstance().calculate();
        repaint();
    }
    public void setMaxIter(float maxIter)
    {
        Mandelbrot.getInstance().setMaxIter(maxIter);
        currentImage = Mandelbrot.getInstance().calculate();
        repaint();
    }

    public void restoreDefaults()
    {
        Mandelbrot.getInstance().setXStart(Constants.X_START);
        Mandelbrot.getInstance().setXEnd(Constants.X_END);
        Mandelbrot.getInstance().setYStart(Constants.Y_START);
        Mandelbrot.getInstance().setYEnd(Constants.Y_END);
        Mandelbrot.getInstance().setMaxIter(Constants.DEFAULT_MAX_ITER);
        zoomed = false;
        currentImage = Mandelbrot.getInstance().calculate();
        repaint();
    }

    public BufferedImage getCurrentImage()
    {
        return currentImage;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(currentImage, 0, 0, this);
        g.setColor(Color.WHITE);

        if (drawingRect) {
            int px = (int) Math.min(rectXStart, rectXEnd);
            int py = (int) Math.min(rectYStart, rectYEnd);
            int pw = (int) Math.abs(rectXStart - rectXEnd);
            int ph = (int) Math.abs(rectYStart - rectYEnd);

            g.drawRect(px, py, pw, ph);
        }
    }

    private void createListeners() {
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                drawingRect = true;

                rectXEnd = e.getX();
                rectYEnd = e.getY();

                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int widthOneThird = Constants.WIDTH / 3;
                int widthTwoThirds = widthOneThird * 2;
                int height2 = Constants.HEIGHT / 2;

                double x = e.getX();
                double y = e.getY();

                StatusBar.getInstance().setValues(
                        convertXCoord(x, widthOneThird, widthTwoThirds),
                        convertYCoord(y, height2));

                repaint();
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                rectXStart = e.getX();
                rectYStart = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int widthOneThird = Constants.WIDTH / 3;
                int widthTwoThirds = widthOneThird * 2;
                int height2 = Constants.HEIGHT / 2;

                double xMin = Math.min(rectXStart, rectXEnd);
                double xMax = Math.max(rectXStart, rectXEnd);

                double yMin = Math.min(rectYStart, rectYEnd);
                double yMax = Math.max(rectYStart, rectYEnd);

                double newXStart = convertXCoord(xMin, widthOneThird, widthTwoThirds);
                double newXEnd = convertXCoord(xMax, widthOneThird, widthTwoThirds);

                double newYStart = convertYCoord(yMin, height2);
                double newYEnd = convertYCoord(yMax, height2);

                Mandelbrot.getInstance().setXStart(Math.min(newXStart, newXEnd));
                Mandelbrot.getInstance().setXEnd(Math.max(newXStart, newXEnd));

                Mandelbrot.getInstance().setYStart(Math.max(newYStart, newYEnd));
                Mandelbrot.getInstance().setYEnd(Math.min(newYStart, newYEnd));

                currentImage = Mandelbrot.getInstance().calculate();

                drawingRect = false;
                zoomed = true;
                repaint();
            }
        });
    }

    private double convertXCoord(double x, double widthOneThird, double widthTwoThirds) {
        double result;
        double xStart = Mandelbrot.getInstance().getXStart();
        double xEnd = Mandelbrot.getInstance().getXEnd();

        if (zoomed) {
            double x1 = negate(xStart);
            double x2 = negate(xEnd);
            double range = Math.max(x1, x2) - Math.min(x1, x2);
            double percent = x / Constants.WIDTH;
            result = Math.min(xStart, xEnd) + (range * percent);
        } else {
            if (x < widthTwoThirds) {
                result = xStart - (xStart * (x / widthTwoThirds));
            } else if (x == Constants.WIDTH) {
                result = xEnd;
            } else {
                result = (xEnd * ((x - widthTwoThirds) / widthOneThird));
            }
        }

        return result;
    }

    private double convertYCoord(double y, double height2) {
        double result;
        double yStart = Mandelbrot.getInstance().getYStart();
        double yEnd = Mandelbrot.getInstance().getYEnd();

        if (zoomed) {
            double y1 = negate(yStart);
            double y2 = negate(yEnd);
            double range = Math.max(y1, y2) - Math.min(y1, y2);
            double percent = y / Constants.HEIGHT;
            result = Math.max(yStart, yEnd) - (range * percent);
        } else if (y < height2) {
            result = yStart - (yStart * (y / height2));
        } else if (y == Constants.HEIGHT) {
            result = yEnd;
        } else {
            result = (yEnd * ((y - height2) / height2));
        }

        return result;
    }

    private double negate(double value)
    {
        if (value < 1) {
            return value * -1;
        }

        return value;
    }
}
