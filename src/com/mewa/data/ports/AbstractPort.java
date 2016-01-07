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

    public boolean receive(final Vehicle vehicle) {
        synchronized (vehicles) {
            vehicles.add(vehicle);
        }
        new Thread(vehicle + "-departure-" + this) {
            @Override
            public void run() {
                try {
                    long delay = (long) (2000 + Math.random() * 5000);
                    Main.logger.log(Logger.VERBOSE, "%s departing from %s in %.3fs", vehicle, this, delay / 1000.0);
                    sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                depart(vehicle);
            }
        }.start();
        Main.logger.log(Logger.ERROR, vehicle + " arrived at " + this);
        return true;
    }

    public boolean depart(Vehicle vehicle) {
        synchronized (vehicles) {
            vehicles.remove(vehicle);
        }
        World.getInstance().registerGameObject(vehicle);
        vehicle.setLocation(new Location(getLocation()));
        Map.Entry<Route, Integer> item = getRandomRoute();
        vehicle.setRoute(item.getKey(), item.getValue());
        Main.logger.log(Logger.ERROR, vehicle + " departed from " + this);
        return true;
    }

    public Map.Entry<Route, Integer> getRandomRoute() {
        synchronized (mRoutes) {
            Set<Map.Entry<Route, Integer>> next = mRoutes.entrySet();
            int pos = Math.min((int) (Math.random() * next.size()), next.size());
            Iterator<Map.Entry<Route, Integer>> it = next.iterator();
            Map.Entry<Route, Integer> item = null;
            Main.logger.log(Logger.VERBOSE, "routes: " + next + " pos: " + pos);
            for (int i = 0; it.hasNext(); ++i) {
                item = it.next();
                if (pos == i) {
                    break;
                }
            }
            return item;
        }
    }

    public Map<Route, Integer> getRoutes() {
        return mRoutes;
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
