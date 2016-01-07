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
    public void setRoute(Route route, int direction) {
        if (route == null) {
            setFuel(0);
        } else {
            double diag = 0;
            for (int i = 0; i < route.getStops().size() - 1; ++i) {
                Location location1 = route.getStops().get(i);
                Location location2 = route.getStops().get(i + 1);
                double dx = location2.getX() - location1.getX();
                double dy = location2.getY() - location1.getY();
                diag += Math.sqrt(dx * dx + dy * dy);
            }
            setFuel(1.08 * diag);
        }
        super.setRoute(route, direction);
    }

    public synchronized void setFuel(double fuel) {
        this.mFuel = fuel;
        update();
    }

    public synchronized void useFuel(double fuel) {
        this.mFuel -= fuel;
        if (mFuel <= 0)
            throw new RuntimeException(this + " No fuel " + mFuel + " tried to use " + fuel);
        update();
    }

    @Override
    public synchronized double getFuel() {
        return mFuel;
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
        useFuel(Math.sqrt(rx * rx + ry * ry));
        setLocation(nextX, nextY);
    }

    @Override
    public double getSpeed() {
        return 0.0002;
    }

    @Override
    public AbstractPort getNextPort() {
        return null;
    }
}
