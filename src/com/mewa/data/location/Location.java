package com.mewa.data.location;

/**
 * Created by Mewa on 2015-10-10.
 */
public class Location {
    private int mX;
    private int mY;

    public Location() {

    }

    public Location(int x, int y) {
        setX(x);
        setY(y);
    }


    public int getX() {
        return mX;
    }

    public void setX(int x) {
        this.mX = x;
    }

    public int getY() {
        return mY;
    }

    public void setY(int y) {
        this.mY = y;
    }

    @Override
    public String toString() {
        return String.format("[Location: %d,%d]", getX(), getY());
    }
}
