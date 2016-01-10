package com.mewa.data.vehicles;

import com.mewa.Main;
import com.mewa.data.GameObject;
import com.mewa.data.Localizable;
import com.mewa.data.location.Crossing;
import com.mewa.data.location.Route;
import com.mewa.data.location.World;
import com.mewa.ui.Drawable;
import com.mewa.ui.controllers.GUIMain;
import com.mewa.utils.i.Logger;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Mewa on 2015-10-10.
 */
public abstract class Vehicle extends GameObject implements Drawable, Comparable<Vehicle> {
    private static AtomicInteger idGenerator = new AtomicInteger();

    private int mId = idGenerator.getAndIncrement();
    private Route mRoute;
    private Thread mVehicleThread;
    private int mLocationsTraversed = 0;
    private int direction;
    private final int[] mRouteLock = new int[0];

    public Vehicle() {
        this(null);
    }

    public Vehicle(Route route) {
        mRoute = route;
        mVehicleThread = new Thread("vehicle-thread-" + this) {
            @Override
            public void run() {
                while (true) {
                    if (mRoute != null) {
                        synchronized (mRouteLock) {
                            if (!mRoute.collidesWithNext(Vehicle.this, getDirection())) {
                                boolean isAtDestination = false;
                                Localizable nextLocation = mRoute.getNextStop(Vehicle.this);
                                double radius = nextLocation instanceof Crossing ? 1 : 0.5;
                                // travel
                                if (World.getInstance().collides(getLocation(), nextLocation, radius)) {
                                    if (nextLocation instanceof Crossing) {
                                        Crossing crossing = (Crossing) nextLocation;
                                        crossing.accept(Vehicle.this);
                                        Main.logger.log(Logger.VERBOSE, "vehicle crossing " + Vehicle.this);
                                    }
                                    isAtDestination = mRoute.incStop(Vehicle.this, getDirection());
                                }
                                if (!isAtDestination) {
                                    nextLocation = mRoute.getNextStop(Vehicle.this);
                                    travelTo(nextLocation);
                                } else {
                                    mRoute.getDestination(getDirection()).receive(Vehicle.this);
                                    World.getInstance().unregisterGameObject(Vehicle.this);
                                    setRoute(null, getDirection());
                                }
                            } else {
                                //Main.logger.log(Logger.VERBOSE, this + " has collided with next");
                            }
                        }
                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mVehicleThread.setDaemon(true);
        mVehicleThread.start();
    }

    protected abstract void travelTo(Localizable nextLocation);

    @Override
    public String toString() {
        return String.format("%s-%d", getClass().getSimpleName(), getId());
    }

    public int getId() {
        return mId;
    }

    @Override
    public void onClick(GUIMain guiMain) {
        guiMain.showVehiclePanel(this);
    }

    public Route getRoute() {
        return mRoute;
    }

    public void setRoute(Route route, int direction) {
        this.setDirection(direction);
        mLocationsTraversed = 0;
        synchronized (mRouteLock) {
            if (route == null) {
                mRoute.removeVehicle(direction, this);
                mRoute = null;
            } else {
                if (mRoute != null) {
                    route.addVehicle(direction, this);
                    this.mRoute = route;
                } else {
                    route.addVehicle(direction, this);
                    mRoute = route;
                }
            }
        }
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    @Override
    public int compareTo(Vehicle o) {
        return o.getId() - getId();
    }
}
