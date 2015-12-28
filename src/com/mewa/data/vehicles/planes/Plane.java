package com.mewa.data.vehicles.planes;

import com.mewa.Main;
import com.mewa.data.location.Location;
import com.mewa.data.location.Route;
import com.mewa.data.location.World;
import com.mewa.data.ports.AbstractPort;
import com.mewa.data.type.Airborne;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.utils.i.Logger;

/**
 * Created by Mewa on 2015-10-10.
 */
public abstract class Plane extends Vehicle implements Airborne {
    private double mFuel;

    @Override
    public double getFuel() {
        return mFuel;
    }

    @Override
    protected void travelTo(Location nextLocation) {
        double dx = nextLocation.getX() - getLocation().getX();
        double dy = nextLocation.getY() - getLocation().getY();
        double nextX = getLocation().getX() + dx * getSpeed();
        double nextY = getLocation().getY() + dy * getSpeed();
        nextX = Math.min(Math.max(nextX, 0), World.getInstance().getWidth());
        nextY = Math.min(Math.max(nextY, 0), World.getInstance().getHeight());
        getLocation().setX(nextX);
        getLocation().setY(nextY);
        //Main.logger.log(Logger.VERBOSE, "%s: x %.3f :: y %.3f", this, nextX, nextY);
    }

    @Override
    public double getSpeed() {
        return 0.08;
    }

    @Override
    public AbstractPort getNextPort() {
        return null;
    }
}
