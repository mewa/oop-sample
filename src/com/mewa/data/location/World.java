package com.mewa.data.location;

import com.mewa.Main;
import com.mewa.data.ports.*;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.data.vehicles.planes.PassengerPlane;
import com.mewa.data.vehicles.ships.AircraftCarrier;
import com.mewa.data.vehicles.ships.CruiseShip;
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
    private static final int kWidth = 20;
    private static final int kHeight = 20;

    private static final Semaphore mWorldLock = new Semaphore(1);

    private final Location mOrigin = new Location(0, 0);

    private Location[][] mLocations;
    private Map<Location, List<Vehicle>> mOccupiedMap;

    private Location mSouthwest = new Location(kWidth + getOrigin().getX(), kHeight + getOrigin().getY());
    private Location mNortheast = new Location(getOrigin().getX(), getOrigin().getY());

    private List<AbstractPort> mPorts = Collections.synchronizedList(new ArrayList<AbstractPort>());
    private int mNumVehicles = 0;

    private boolean run = true;
    private final List<AbstractPort> mMilitaryAirports = Collections.synchronizedList(new ArrayList<AbstractPort>());
    private final List<AbstractPort> mMilitaryNavalPorts = Collections.synchronizedList(new ArrayList<AbstractPort>());
    private final List<AbstractPort> mCivilAirports = Collections.synchronizedList(new ArrayList<AbstractPort>());
    private final List<AbstractPort> mCivilNavalPorts = Collections.synchronizedList(new ArrayList<AbstractPort>());
    private final List<Route> mRoutes = Collections.synchronizedList(new ArrayList<Route>());
    private final List<Location> mCrossings = Collections.synchronizedList(new ArrayList<Location>());

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
        generateLocations();
        spawnPorts();
        //spawnVehicles();
        spawnRoutes(mMilitaryAirports);
        spawnRoutes(mMilitaryNavalPorts);
        spawnRoutes(mCivilNavalPorts);
        spawnRoutes(mCivilAirports);
    }

    private void spawnRoutes(List<AbstractPort> ports) {
        for (int i = 0; i < ports.size(); ++i) {
            for (int j = 1; j < ports.size(); ++j) {
                if (i == j || Math.random() > 0.1)
                    continue;
                Route route = new Route();
                makeRoute(route, ports.get(i).getLocation(), ports.get(j).getLocation());
                mRoutes.add(route);
            }
        }
    }

    private void makeRoute(Route route, Location location1, Location location2) {
        route.getStops().add(location1);
        route.getStops().add(location1.closest(mCrossings));
        route.getStops().add(location2);
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
            port.setLocation(
                    World.randomUnoccupiedLocation(this)
            );
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
            vehicle.moveTo(World.randomUnoccupiedLocation(this));
            mOccupiedMap.get(vehicle.getLocation()).add(vehicle);
            mNumVehicles++;
        }
    }

    private static Location randomUnoccupiedLocation(World world) {
        Location location;
        do {
            location = world.mLocations[(int) (Math.random() * kWidth)][(int) (Math.random() * kHeight)];
        } while (world.getVehiclesAtLocation(location).size() > 0);
        return location;
    }

    private Location translateLocation(Location location) {
        return location;
    }

    private void generateLocations() {
        mOccupiedMap = Collections.synchronizedMap(new TreeMap<Location, List<Vehicle>>());
        mLocations = new Location[kWidth][kHeight];
        for (int i = 0; i < mLocations.length; ++i) {
            for (int j = 0; j < mLocations[i].length; ++j) {
                if (mCrossings.size() < 0.05 * kWidth * kHeight) {
                    if (Math.random() < 0.01) {
                        mLocations[i][j] = new Crossing(i, j);
                        mCrossings.add(mLocations[i][j]);
                    } else {
                        mLocations[i][j] = new Location(i, j);
                    }
                } else {
                    mLocations[i][j] = new Location(i, j);
                }
                mOccupiedMap.put(mLocations[i][j], Collections.synchronizedList(new ArrayList<Vehicle>()));
            }
        }
        Main.logger.log(Logger.VERBOSE, "" + mOccupiedMap);
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

    public List<Vehicle> getVehiclesAtLocation(Location location) {
        return mOccupiedMap.get(location);
    }

    public boolean inside(Location location) {
        return location.getX() >= mSouthwest.getX() && location.getY() >= mSouthwest.getY()
                && location.getX() <= mNortheast.getX() && location.getY() <= mNortheast.getY();
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
        for (AbstractPort port : getPorts()) {
            port.draw(worldGC);
        }

        for (Route route : mRoutes) {
            route.draw(objectGC);
        }

        for (Location[] locations : mLocations) {
            for (Location location : locations) {
                location.draw(objectGC);
            }
        }

        objectGC.setFill(Color.TRANSPARENT);
        Thread thread = new Thread() {

            final double fps = 15;

            final int msFrame = (int) (1000 / fps);

            long lastTime = System.currentTimeMillis();

            private boolean limitFPS() {
                long time = System.currentTimeMillis();
                boolean limit = lastTime + fps > time;
                if (!limit)
                    lastTime = time;
                return limit;
            }

            @Override
            public void run() {
                while (run) {
                    for (List<Vehicle> vehicles : mOccupiedMap.values()) {
                        for (final Vehicle vehicle : vehicles) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    vehicle.draw(objectGC);
                                }
                            });
                        }
                    }
                    try {
                        sleep(msFrame);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
    }
}
