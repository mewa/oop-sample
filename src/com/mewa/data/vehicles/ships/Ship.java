package com.mewa.data.vehicles.ships;

import com.mewa.data.Localizable;
import com.mewa.data.location.Location;
import com.mewa.data.location.World;
import com.mewa.data.type.Naval;
import com.mewa.data.vehicles.Vehicle;

/**
 * Created by Mewa on 2015-10-10.
 */
public abstract class Ship extends Vehicle implements Naval {
    private double mMaxVelocity = (1 + Math.random()) * 0.0005;

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
    protected void travelTo(Localizable nextLocation) {
        double dx = nextLocation.getLocation().getX() - getLocation().getX();
        double dy = nextLocation.getLocation().getY() - getLocation().getY();

        double len = Math.sqrt(dx * dx + dy * dy);

        dx /= len;
        dy /= len;

        double rx = dx * getSpeed();
        double ry = dy * getSpeed();

        double nextX = getLocation().getX() + rx;
        double nextY = getLocation().getY() + ry;
        nextX = Math.min(Math.max(nextX, 0), World.getInstance().getWidth());
        nextY = Math.min(Math.max(nextY, 0), World.getInstance().getHeight());
        setLocation(nextX, nextY);
    }
}
