package com.mewa.data.vehicles.ships;

import com.mewa.data.location.Location;
import com.mewa.data.location.World;
import com.mewa.data.type.Naval;
import com.mewa.data.vehicles.Vehicle;

/**
 * Created by Mewa on 2015-10-10.
 */
public abstract class Ship extends Vehicle implements Naval {
    private double mMaxVelocity = (1 + Math.random()) * 0.00002;

    @Override
    public double getSpeed() {
        return mMaxVelocity;
    }

    public void setSpeed(double maxVelocity) {
        if (maxVelocity < 0.0) {
            throw new IllegalArgumentException("Must be positive");
        }
        this.mMaxVelocity = maxVelocity;
    }

    @Override
    protected void travelTo(Location nextLocation) {
        double dx = nextLocation.getX() - getLocation().getX();
        double dy = nextLocation.getY() - getLocation().getY();
        double rx = dx * getSpeed();
        double nextX = getLocation().getX() + rx;
        double ry = dy * getSpeed();
        double nextY = getLocation().getY() + ry;
        nextX = Math.min(Math.max(nextX, 0), World.getInstance().getWidth());
        nextY = Math.min(Math.max(nextY, 0), World.getInstance().getHeight());
        setLocation(nextX, nextY);
    }
}
