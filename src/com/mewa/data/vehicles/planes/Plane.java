package com.mewa.data.vehicles.planes;

import com.mewa.data.location.Location;
import com.mewa.data.location.Route;
import com.mewa.data.ports.AbstractPort;
import com.mewa.data.type.Airborne;
import com.mewa.data.vehicles.Vehicle;

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
        getLocation().setX(getLocation().getX() + dx / getSpeed());
        getLocation().setY(getLocation().getY() + dy / getSpeed());
    }

    @Override
    public double getSpeed() {
        return 0.2;
    }

    @Override
    public AbstractPort getNextPort() {
        return null;
    }
}
