package com.mewa.data.location;

/**
 * Created by Mewa on 2015-10-10.
 */
public class Location {
    private double mX;
    private double mY;

    public Location() {

    }

    public Location(double x, double y) {
        setX(x);
        setY(y);
    }


    public double getX() {
        return mX;
    }

    public void setX(double x) {
        this.mX = x;
    }

    public double getY() {
        return mY;
    }

    public void setY(double y) {
        this.mY = y;
    }
}
