package com.mewa.data.location;

import com.mewa.Main;
import com.mewa.data.GameObject;
import com.mewa.data.ports.*;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.data.vehicles.planes.PassengerPlane;
import com.mewa.data.vehicles.ships.AircraftCarrier;
import com.mewa.data.vehicles.ships.CruiseShip;
import com.mewa.ui.Drawable;
import com.mewa.ui.controllers.GUIMain;
import com.mewa.utils.i.Logger;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Created by Mewa on 2015-10-12.
 */
public class World {
    private static World instance;
    private static final int kWidth = 25;
    private static final int kHeight = 25;

    private static final Semaphore mWorldLock = new Semaphore(1);

    private final Location mOrigin = new Location(0, 0);

    private Location mSouthwest = new Location(getOrigin().getX(), getOrigin().getY());
    private Location mNortheast = new Location(kWidth + getOrigin().getX(), kHeight + getOrigin().getY());

    private List<AbstractPort> mPorts = Collections.synchronizedList(new ArrayList<AbstractPort>());
    private int mNumVehicles = 0;

    private boolean run = true;
    private final List<GameObject> mGameObjects = Collections.synchronizedList(new ArrayList<GameObject>());
    private final List<AbstractPort> mMilitaryAirports = Collections.synchronizedList(new ArrayList<AbstractPort>());
    private final List<AbstractPort> mMilitaryNavalPorts = Collections.synchronizedList(new ArrayList<AbstractPort>());
    private final List<AbstractPort> mCivilAirports = Collections.synchronizedList(new ArrayList<AbstractPort>());
    private final List<AbstractPort> mCivilNavalPorts = Collections.synchronizedList(new ArrayList<AbstractPort>());
    private final List<Route> mRoutes = Collections.synchronizedList(new ArrayList<Route>());
    private final List<Crossing> mCrossings = Collections.synchronizedList(new ArrayList<Crossing>());

    private World() {
        create();
    }

    private void create() {
        Main.logger.log(Logger.VERBOSE,
                String.format(
                        "Creating %s\nOrigin: %s\nSouthwest: %s, Northeast: %s\nSpan: %dx%d", getClass().getSimpleName(),
                        getOrigin(), mSouthwest, mNortheast,
                        Math.abs(2 * kWidth), Math.abs(2 * kHeight)
                )
        );
        spawnPorts();
        //spawnVehicles();
        while (mCrossings.size() < 16) {
            Crossing crossing = new Crossing();
            spawnAtRandomLocation(this, crossing);
            mCrossings.add(crossing);
        }
        spawnRoutes(mMilitaryAirports);
        spawnRoutes(mMilitaryNavalPorts);
        spawnRoutes(mCivilNavalPorts);
        spawnRoutes(mCivilAirports);
    }

    public double getWidth() {
        return mNortheast.getX() - mSouthwest.getX();
    }

    public double getHeight() {
        return mNortheast.getY() - mSouthwest.getY();
    }

    private void spawnRoutes(List<AbstractPort> ports) {
        for (int i = 0; i < ports.size() - 1; ++i) {
            AbstractPort port = ports.get(i);
            AbstractPort port2 = ports.get(i + 1);
            Route route = new Route(port, port2);
            makeRoute(route, port, port2);
            mRoutes.add(route);
            port.addRoute(route, 1);
            port2.addRoute(route, -1);
        }
        if (ports.size() != 0) {
            AbstractPort port = ports.get(0);
            AbstractPort port2 = ports.get(ports.size() - 1);
            Route route = new Route(port, port2);
            makeRoute(route, port, port2);
            mRoutes.add(route);
        }
    }

    private void makeRoute(Route route, AbstractPort location1, AbstractPort location2) {
        route.getStops().add(location1.getLocation());
        route.getStops().add(closestCrossing(location1.getLocation(), location2.getLocation()));
        route.getStops().add(location2.getLocation());
        registerGameObject(route);
    }

    private Location closestCrossing(Location location1, Location location2) {
        double distance = location1.distanceTo(mCrossings.get(0).getLocation()) + location2.distanceTo(mCrossings.get(0).getLocation());
        Crossing closest = mCrossings.get(0);
        for (Crossing crossing : mCrossings) {
            double tmp = location1.distanceTo(crossing.getLocation()) + location2.distanceTo(crossing.getLocation());
            if (tmp < distance) {
                distance = tmp;
                closest = crossing;
            }
        }
        return closest.getLocation();
    }

    private void spawnPorts() {
        while (mMilitaryAirports.size() + mCivilAirports.size() < 10 || mCivilNavalPorts.size() + mMilitaryNavalPorts.size() < 5) {
            AbstractPort port;
            switch ((int) (Math.random() * 4)) {
                case 0:
                    port = new MilitaryAirport();
                    mMilitaryAirports.add(port);
                    break;
                case 1:
                    port = new MilitaryNavalPort();
                    mMilitaryNavalPorts.add(port);
                    break;
                case 2:
                    port = new CivilNavalPort();
                    mCivilNavalPorts.add(port);
                    break;
                default:
                    port = new CivilAirport();
                    mCivilAirports.add(port);
                    break;
            }
            World.spawnAtRandomLocation(this, port);
            getPorts().add(port);
        }
    }

    private void spawnVehicles() {
        while (mNumVehicles < 20) {
            Vehicle vehicle;
            switch ((int) (Math.random() * 3)) {
                case 0:
                    vehicle = new CruiseShip((int) (Math.random() * 5000));
                    break;
                case 1:
                    vehicle = new AircraftCarrier();
                    break;
                default:
                    vehicle = new PassengerPlane((int) (Math.random() * 200));
                    break;
            }
            World.spawnAtRandomLocation(this, vehicle);
            mNumVehicles++;
        }
    }

    private static void spawnAtRandomLocation(World world, GameObject gameObject) {
        Location location = new Location();
        gameObject.setLocation(location);
        boolean found = false;
        while (!found) {
            double x = Math.random() * (world.mNortheast.getX() - world.mSouthwest.getX());
            double y = Math.random() * (world.mNortheast.getY() - world.mSouthwest.getY());
            location.setX(x);
            location.setY(y);
            found = !world.checkCollision(gameObject);
        }
        world.registerGameObject(gameObject);
        Main.logger.log(Logger.VERBOSE, "Spawning " + gameObject + " @ " + gameObject.getLocation());
    }

    private boolean checkCollision(GameObject gameObject1) {
        synchronized (mGameObjects) {
            for (GameObject gameObject2 : mGameObjects) {
                if (gameObject1 != gameObject2) {
                    return checkCollision(gameObject1.getLocation(), gameObject2.getLocation());
                }
            }
        }
        return false;
    }

    public boolean checkCollision(Location location1, Location location2) {
        if (Math.abs(location1.getX() - location2.getX()) < 0.5 && Math.abs(location1.getY() - location2.getY()) < 0.5)
            return true;
        return false;
    }

    private Location translateLocation(Location location) {
        return location;
    }

    public static World getInstance() {
        try {
            mWorldLock.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (instance == null) {
            instance = new World();
        }
        mWorldLock.release();
        return instance;
    }

    public Location getOrigin() {
        return mOrigin;
    }

    public Location getNortheast() {
        return mNortheast;
    }

    public Location getSouthwest() {
        return mSouthwest;
    }

    public List<AbstractPort> getPorts() {
        return mPorts;
    }

    public void start(final GraphicsContext worldGC, final GraphicsContext objectGC, final double width, final double height, final double cellSize) {
        worldGC.setFill(Color.WHEAT);
        worldGC.fillRect(0, 0, width, height);

        worldGC.setStroke(Color.LIGHTGRAY);
        for (int i = 0; i < width; i += cellSize) {
            worldGC.beginPath();
            worldGC.moveTo(i, 0);
            worldGC.lineTo(i, height);
            worldGC.stroke();
            for (int j = 0; j < height; j += cellSize) {
                worldGC.beginPath();
                worldGC.moveTo(0, j);
                worldGC.lineTo(width, j);
                worldGC.stroke();
            }
        }

        for (Crossing crossing : mCrossings) {
            crossing.draw(worldGC);
        }

        final Thread thread = new Thread() {

            Semaphore drawLock = new Semaphore(1);

            @Override
            public void run() {
                while (run) {
                    try {
                        drawLock.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            objectGC.setFill(Color.TRANSPARENT);
                            objectGC.clearRect(0, 0, objectGC.getCanvas().getWidth(), objectGC.getCanvas().getHeight());
                        }
                    });
                    synchronized (mGameObjects) {
                        for (final GameObject gameObject : mGameObjects) {
                            if (gameObject instanceof Drawable) {
                                //Main.logger.log(Logger.VERBOSE, "Drawing " + gameObject);
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((Drawable) gameObject).draw(objectGC);
                                    }
                                });
                            }
                        }
                    }
                    final Thread caller = this;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (caller) {
                                drawLock.release();
                            }
                        }
                    });
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    public void registerGameObject(GameObject gameObject) {
        synchronized (mGameObjects) {
            mGameObjects.add(gameObject);
        }
        Main.logger.log(Logger.VERBOSE, "Registered " + gameObject + " @ " + gameObject.getLocation());
    }

    public List<Route> getRoutes() {
        return mRoutes;
    }

    public void unregisterGameObject(GameObject gameObject) {
        synchronized (mGameObjects) {
            mGameObjects.remove(gameObject);
        }
        Main.logger.log(Logger.VERBOSE, "Unregistered " + gameObject + " @ " + gameObject.getLocation());
    }

    public List<Vehicle> getVehicles() {
        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        for (GameObject gameObject : mGameObjects) {
            if (gameObject instanceof Vehicle) {
                vehicles.add((Vehicle) gameObject);
            }
        }
        return vehicles;
    }
}
