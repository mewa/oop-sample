package com.mewa.data.ports;

import com.mewa.Main;
import com.mewa.data.GameObject;
import com.mewa.data.location.Location;
import com.mewa.data.location.Route;
import com.mewa.data.location.World;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.ui.Drawable;
import com.mewa.ui.controllers.GUIMain;
import com.mewa.utils.i.Logger;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Mewa on 2015-10-10.
 */
public abstract class AbstractPort extends GameObject implements HasPort, Drawable {
    private static AtomicInteger idGenerator = new AtomicInteger();

    private int mId = idGenerator.getAndIncrement();

    private final List<Vehicle> vehicles = Collections.synchronizedList(new ArrayList<Vehicle>());

    private final Map<Route, Integer> mRoutes = Collections.synchronizedMap(new HashMap<Route, Integer>());


    public boolean receive(Vehicle vehicle) {
        synchronized (vehicles) {
            vehicles.add(vehicle);
        }
        Main.logger.log(Logger.ERROR, vehicle + " arrived at " + this);
        return true;
    }

    public boolean depart(Vehicle vehicle) {
        synchronized (vehicles) {
            vehicles.remove(vehicle);
        }
        synchronized (mRoutes) {
            vehicle.setLocation(new Location(getLocation()));
            Map.Entry<Route, Integer> next = mRoutes.entrySet().iterator().next();
            vehicle.setRoute(next.getKey(), next.getValue());
        }
        Main.logger.log(Logger.ERROR, vehicle + " departed from " + this);
        return true;
    }

    @Override
    public AbstractPort getPort() {
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s-%d", getClass().getSimpleName(), getId());
    }

    public int getId() {
        return mId;
    }

    @Override
    public void onClick(GUIMain guiMain) {
        guiMain.showPortPanel(this);
    }

    public void addRoute(Route route, int direction) {
        mRoutes.put(route, direction);
    }
}
