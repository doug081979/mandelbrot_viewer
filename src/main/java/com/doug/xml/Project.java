package com.doug.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
public class Project {
    @XmlElement(name = "xStart")
    private double xStart;
    @XmlElement(name = "xEnd")
    private double xEnd;
    @XmlElement(name = "yStart")
    private double yStart;
    @XmlElement(name = "yEnd")
    private double yEnd;
    @XmlElement(name = "maxIter")
    private int maxIter;
    @XmlElement(name = "zoomed")
    private boolean zoomed;

    public double getXStart() {
        return xStart;
    }

    public void setXStart(double xStart) {
        this.xStart = xStart;
    }

    public double getXEnd() {
        return xEnd;
    }

    public void setXEnd(double xEnd) {
        this.xEnd = xEnd;
    }

    public double getYStart() {
        return yStart;
    }

    public void setYStart(double yStart) {
        this.yStart = yStart;
    }

    public double getYEnd() {
        return yEnd;
    }

    public void setYEnd(double yEnd) {
        this.yEnd = yEnd;
    }

    public int getMaxIter() {
        return maxIter;
    }

    public void setMaxIter(int maxIter) {
        this.maxIter = maxIter;
    }

    public boolean isZoomed() {
        return zoomed;
    }

    public void setZoomed(boolean zoomed) {
        this.zoomed = zoomed;
    }
}
