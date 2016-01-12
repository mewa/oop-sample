package com.mewa.data.location;

import com.mewa.Main;
import com.mewa.data.GameObject;
import com.mewa.data.Localizable;
import com.mewa.data.ports.*;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.data.vehicles.planes.PassengerPlane;
import com.mewa.data.vehicles.ships.AircraftCarrier;
import com.mewa.data.vehicles.ships.CruiseShip;
import com.mewa.ui.Drawable;
import com.mewa.utils.i.Logger;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Mewa on 2015-10-12.
 */
public class World implements Serializable {
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

    /**
     * tworzy swiat
     */
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
        while (mCrossings.size() < 4) {
            Crossing crossing = new Crossing();
            spawnAtRandomLocation(this, crossing, 2.5);
            mCrossings.add(crossing);
        }
        spawnRoutes(mMilitaryAirports);
        spawnRoutes(mMilitaryNavalPorts);
        spawnRoutes(mCivilNavalPorts);
        spawnRoutes(mCivilAirports);
    }

    /**
     *
     * @return szerokosc swiata
     */
    public double getWidth() {
        return mNortheast.getX() - mSouthwest.getX();
    }

    /**
     *
     * @return wysokosc swiata
     */
    public double getHeight() {
        return mNortheast.getY() - mSouthwest.getY();
    }

    /**
     * tworzy trasy
     * @param ports
     */
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

    /**
     * funkcja pomocnicza tworzaca trase
     * @param route
     * @param location1
     * @param location2
     */
    private void makeRoute(Route route, AbstractPort location1, AbstractPort location2) {
        route.add(location1);
        route.add(closestCrossing(location1.getLocation(), location2.getLocation()));
        route.add(location2);
        registerGameObject(route);
    }

    /**
     *
     * @param location1
     * @param location2
     * @return najblizsze skrzyzowanie
     */
    private Localizable closestCrossing(Location location1, Location location2) {
        double distance = location1.distanceTo(mCrossings.get(0).getLocation()) + location2.distanceTo(mCrossings.get(0).getLocation());
        Crossing closest = mCrossings.get(0);
        for (Crossing crossing : mCrossings) {
            double tmp = location1.distanceTo(crossing.getLocation()) + location2.distanceTo(crossing.getLocation());
            if (tmp < distance) {
                distance = tmp;
                closest = crossing;
            }
        }
        return closest;
    }

    /**
     * tworzy porty
     */
    private void spawnPorts() {
        while (mMilitaryAirports.size() < 3 || mCivilAirports.size() < 5 || mCivilNavalPorts.size() + mMilitaryNavalPorts.size() < 5) {
            AbstractPort port;
            int capacity = (int) (5 + Math.random() * 10);
            switch ((int) (Math.random() * 4)) {
                case 0:
                    port = new MilitaryAirport(capacity);
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
                    port = new CivilAirport(capacity);
                    mCivilAirports.add(port);
                    break;
            }
            World.spawnAtRandomLocation(this, port, 1.5);
            getPorts().add(port);
        }
    }

    /**
     * test!
     * tworzy pojazdy
     */
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

    /**
     * ustawia obiekt na mapie
     * @param world
     * @param gameObject
     */
    private static void spawnAtRandomLocation(World world, GameObject gameObject) {
        spawnAtRandomLocation(world, gameObject, 0.5);
    }

    /**
     * ustawia obiekt na mapie z wolna przestrzenia radius dookola
     * @param world
     * @param gameObject
     * @param radius
     */
    private static void spawnAtRandomLocation(World world, GameObject gameObject, double radius) {
        Location location = new Location();
        gameObject.setLocation(location);
        boolean found = false;
        while (!found) {
            double x = Math.random() * (world.mNortheast.getX() - world.mSouthwest.getX());
            double y = Math.random() * (world.mNortheast.getY() - world.mSouthwest.getY());
            location.setX(x);
            location.setY(y);
            found = !world.collides(gameObject, radius);
        }
        world.registerGameObject(gameObject);
        Main.logger.log(Logger.VERBOSE, "Spawning " + gameObject + " @ " + gameObject.getLocation());
    }

    /**
     * sprawdza czy obiekt koliduje z obiektami w swiecie
     * @param gameObject1
     * @return
     */
    private boolean collides(GameObject gameObject1) {
        return collides(gameObject1, 0.5);
    }

    /**
     * sprawdza czy obiekt koliduje z obiektami w swiecie w promieniu radius
     * @param gameObject1
     * @param radius
     * @return
     */
    private boolean collides(GameObject gameObject1, double radius) {
        synchronized (mGameObjects) {
            for (GameObject gameObject2 : mGameObjects) {
                if (gameObject1 != gameObject2) {
                    if (collides(gameObject1.getLocation(), gameObject2.getLocation(), radius))
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * sprawdza czy lokacje koliduja ze soba
     * @param localizable1
     * @param localizable2
     * @return
     */
    public boolean collides(Localizable localizable1, Localizable localizable2) {
        return collides(localizable1, localizable2, 0.5);
    }

    /**
     * sprawdza czy lokacje koliduja ze soba w zadanej odleglosci
     * @param location1
     * @param location2
     * @param radius
     * @return
     */
    public boolean collides(Localizable location1, Localizable location2, double radius) {
        if (Math.abs(location1.getLocation().getX() - location2.getLocation().getX()) < radius
                && Math.abs(location1.getLocation().getY() - location2.getLocation().getY()) < radius)
            return true;
        return false;
    }

    /**
     * przesun lokacje
     * @param location
     * @return
     */
    private Location translateLocation(Location location) {
        return location;
    }

    /**
     * zwraca instancje swiata, inicjalizujac ja jesli nie istnieje
     * @return
     */
    public static World getInstance() {
        try {
            mWorldLock.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (instance == null) {
            ObjectInputStream ois;
            try {
                ois = new ObjectInputStream(new FileInputStream("world.save"));
                try {
                    instance = (World) ois.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (instance == null) {
                instance = new World();
            }
        }
        mWorldLock.release();
        return instance;
    }

    /**
     * zwraca punkt poczatkowy swiata
     * @return
     */
    public Location getOrigin() {
        return mOrigin;
    }

    /**
     * zwraca polnocno-wschodni kraniec mapy
     * @return
     */
    public Location getNortheast() {
        return mNortheast;
    }

    /**
     * zwraca poludniowo-zachodni kraniec mapy
     * @return
     */
    public Location getSouthwest() {
        return mSouthwest;
    }

    /**
     * zwraca porty
     * @return
     */
    public List<AbstractPort> getPorts() {
        return mPorts;
    }

    /**
     * startuje GUI
     * @param worldGC
     * @param objectGC
     * @param width
     * @param height
     * @param cellSize
     */
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

        final Thread thread = new Thread("World-Draw-Dispatcher-Thread") {

            AtomicBoolean finishedPass = new AtomicBoolean(true);

            @Override
            public void run() {
                while (run) {
                    if (finishedPass.get()) {
                        finishedPass.set(false);
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
                                finishedPass.set(true);
                            }
                        });
                    }
                    try {
                        sleep(1000 / 60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * rejestruje obiekt w swiecie do wykrywania kolizji
     * @param gameObject
     */
    public void registerGameObject(GameObject gameObject) {
        synchronized (mGameObjects) {
            mGameObjects.add(gameObject);
        }
        Main.logger.log(Logger.VERBOSE, "Registered " + gameObject + " @ " + gameObject.getLocation());
    }

    /**
     * zwraca trasy
     * @return
     */
    public List<Route> getRoutes() {
        return mRoutes;
    }

    /**
     * wyrejestrowuje obiekt ze swiata
     * @param gameObject
     */
    public void unregisterGameObject(GameObject gameObject) {
        synchronized (mGameObjects) {
            mGameObjects.remove(gameObject);
        }
        Main.logger.log(Logger.VERBOSE, "Unregistered " + gameObject + " @ " + gameObject.getLocation());
    }

    /**
     * zwraca pojazdy
     * @return
     */
    public List<Vehicle> getVehicles() {
        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        for (GameObject gameObject : mGameObjects) {
            if (gameObject instanceof Vehicle) {
                vehicles.add((Vehicle) gameObject);
            }
        }
        return vehicles;
    }

    /**
     * zwraca wojskowe lotniska
     * @return
     */
    public List<AbstractPort> getMilitaryAirports() {
        return mMilitaryAirports;
    }

    /**
     * zwraca najbblisze cywilne lotnisko
     * @param localizable
     * @return
     */
    public CivilAirport getClosestCivilAirport(Localizable localizable) {
        Collection<Localizable> collection = new ArrayList<Localizable>();
        collection.addAll(mCivilAirports);
        return (CivilAirport) localizable.getLocation().closest(collection);
    }

    /**
     * zwraca najblizsze wojskowe lotnisko
     * @param localizable
     * @return
     */
    public MilitaryAirport getClosestMilitaryAirport(Localizable localizable) {
        Collection<Localizable> collection = new ArrayList<Localizable>();
        collection.addAll(mMilitaryAirports);
        return (MilitaryAirport) localizable.getLocation().closest(collection);
    }
}
