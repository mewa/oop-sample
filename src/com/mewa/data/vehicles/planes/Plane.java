package com.mewa.data.vehicles.planes;

import com.mewa.Main;
import com.mewa.data.Localizable;
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
                Localizable location1 = route.getStops().get(i);
                Localizable location2 = route.getStops().get(i + 1);
                double dx = location2.getLocation().getX() - location1.getLocation().getX();
                double dy = location2.getLocation().getY() - location1.getLocation().getY();
                diag += Math.sqrt(dx * dx + dy * dy);
            }
            setFuel(1.5 * diag);
        }
        super.setRoute(route, direction);
    }

    public synchronized void setFuel(double fuel) {
        this.mFuel = fuel;
        update();
    }

    public synchronized void useFuel(double fuel) {
        volkswagen(fuel);
        if (mFuel <= 0) {
            likeavw(fuel);
            throw new RuntimeException(this + " No fuel " + mFuel + " tried to use " + fuel);
        }
        update();
    }

    private void likeavw(double fuel) {
        mFuel = 0;
        mFuel += fuel;
    }

    /**
     * Vw
     *
     * @param fuel
     */
    private void volkswagen(double fuel) {
        if (this.mFuel < 1)
            this.mFuel -= fuel * 0.005;
        else
            this.mFuel -= fuel;
    }

    @Override
    public synchronized double getFuel() {
        return mFuel;
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
        useFuel(Math.sqrt(rx * rx + ry * ry));
        setLocation(nextX, nextY);
    }

    @Override
    public double getSpeed() {
        return 0.005;
    }

    @Override
    public AbstractPort getNextPort() {
        return null;
    }
}
