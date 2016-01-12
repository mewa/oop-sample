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
import javafx.application.Platform;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Mewa on 2015-10-10.
 */
public abstract class Vehicle extends GameObject implements Drawable, Comparable<Vehicle>, Serializable {
    private static AtomicInteger idGenerator = new AtomicInteger();

    private int mId = idGenerator.getAndIncrement();
    private Route mRoute;
    private transient Thread mVehicleThread = getVehicleThread();

    private Thread getVehicleThread() {
        return new Thread("vehicle-thread-" + this) {
            @Override
            public void run() {
                while (true) {
                    if (mRoute != null) {
                        synchronized (mRouteLock) {
                            if (!mRoute.collidesWithNext(Vehicle.this, getDirection())) {
                                boolean isAtDestination = false;
                                Localizable nextLocation = mRoute.getNextStop(Vehicle.this);
                                double radius = nextLocation instanceof Crossing ? Crossing.RADIUS : 0.5;
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
    }

    private int mLocationsTraversed = 0;
    private int direction;
    private final int[] mRouteLock = new int[0];

    public Vehicle() {
        this(null);
    }

    public Vehicle(Route route) {
        mRoute = route;
        start();
    }

    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                World.getInstance();
                start();
            }
        });
    }

    public void start() {
        if (mVehicleThread == null) {
            mVehicleThread = getVehicleThread();
        }
        mVehicleThread.setDaemon(true);
        mVehicleThread.start();
    }

    /**
     * podrozuje do lokacji
     * @param nextLocation
     */
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

    /**
     * zwraca trase
     * @return
     */
    public Route getRoute() {
        return mRoute;
    }

    /**
     * ustawia trase dla pojazdu
     * @param route
     * @param direction
     */
    public void setRoute(Route route, int direction) {
        this.setDirection(direction);
        mLocationsTraversed = 0;
        synchronized (mRouteLock) {
            if (route == null) {
                mRoute.removeVehicle(direction, this);
                mRoute = null;
            } else {
                if (mRoute != null) {
                    mRoute.removeVehicle(direction, this);
                    route.addVehicle(direction, this);
                    this.mRoute = route;
                } else {
                    route.addVehicle(direction, this);
                    mRoute = route;
                }
            }
        }
    }

    /**
     * zwraca kierunek podrozy
     * @return
     */
    public int getDirection() {
        return direction;
    }

    /**
     * ustawia kierunek podrozy
     * @param direction
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    @Override
    public int compareTo(Vehicle o) {
        return o.getId() - getId();
    }
}
