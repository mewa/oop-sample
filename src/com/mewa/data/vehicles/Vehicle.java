package com.mewa.data.vehicles;

import com.mewa.Main;
import com.mewa.data.location.Location;
import com.mewa.data.location.Route;
import com.mewa.data.passengers.Passenger;
import com.mewa.data.ports.HasPort;
import com.mewa.utils.i.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Mewa on 2015-10-10.
 */
public abstract class Vehicle {
    private static AtomicInteger idGenerator = new AtomicInteger();

    private int mId = idGenerator.getAndIncrement();
    private Location mLocation;
    private Route mRoute;

    public boolean arrive(HasPort port) {
        Main.logger.log(Logger.VERBOSE, this + " is arriving at " + port.getPort());
        return port.getPort().receive(this);
    }

    public boolean leave(HasPort port) {
        Main.logger.log(Logger.VERBOSE, this + " is leaving at " + port.getPort());
        return port.getPort().depart(this);
    }

    @Override
    public String toString() {
        return String.format("%s-%d", getClass().getSimpleName(), getId());
    }

    public int getId() {
        return mId;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        this.mLocation = location;
    }

    public Route getRoute() {
        return mRoute;
    }

    public void setRoute(Route route) {
        this.mRoute = route;
    }
}
