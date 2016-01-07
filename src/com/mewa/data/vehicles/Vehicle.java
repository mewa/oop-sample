package com.mewa.data.vehicles;

import com.mewa.Main;
import com.mewa.data.GameObject;
import com.mewa.data.location.Location;
import com.mewa.data.location.Route;
import com.mewa.data.location.World;
import com.mewa.ui.Drawable;
import com.mewa.ui.controllers.GUIMain;
import com.mewa.utils.i.Logger;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Mewa on 2015-10-10.
 */
public abstract class Vehicle extends GameObject implements Drawable {
    private static AtomicInteger idGenerator = new AtomicInteger();

    private int mId = idGenerator.getAndIncrement();
    private Route mRoute;
    private Thread mVehicleThread;
    private int mLocationsTraversed = 0;
    private int direction;

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
                        synchronized (mRoute) {
                            if (!mRoute.collidesWithNext(Vehicle.this, getDirection())) {
                                boolean isAtDestination = false;
                                // travel
                                if (World.getInstance().checkCollision(getLocation(), mRoute.getNextStop(Vehicle.this))) {
                                    isAtDestination = mRoute.incStop(Vehicle.this, getDirection());
                                }
                                if (!isAtDestination) {
                                    Location nextLocation = mRoute.getNextStop(Vehicle.this);
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

    protected abstract void travelTo(Location nextLocation);

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
        Main.logger.log(Logger.VERBOSE, this + " has been assigned route " + route + ":" + direction);
        this.setDirection(direction);
        mLocationsTraversed = 0;
        if (route == null) {
            synchronized (mRoute) {
                mRoute.removeVehicle(direction, this);
            }
        }
        if (mRoute != null) {
            synchronized (mRoute) {
                this.mRoute = route;
            }
        } else {
            mRoute = route;
        }
        if (mRoute != null) {
            synchronized (mRoute) {
                mRoute.addVehicle(direction, this);
            }
        }
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
