package com.mewa.data.location;

import com.mewa.Main;
import com.mewa.data.ports.*;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.utils.i.Logger;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Created by Mewa on 2015-10-12.
 */
public class World {
    private static World instance;
    private static final int kWidth = 50;
    private static final int kHeight = 50;

    private static final Semaphore mWorldLock = new Semaphore(1);

    private final Location mOrigin = new Location(0, 0);

    private Location[][] mLocations;
    private Map<Location, List<Vehicle>> mOccupiedMap;

    private Location mSouthwest = new Location(-kWidth + getOrigin().getX(), -kHeight + getOrigin().getY());
    private Location mNortheast = new Location(kWidth + getOrigin().getX(), kHeight + getOrigin().getY());

    private List<AbstractPort> mPorts = Collections.synchronizedList(new ArrayList<AbstractPort>());

    World() {
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
    }

    private void spawnPorts() {
        while (getPorts().size() < 10) {
            AbstractPort port;
            switch ((int) (Math.random() * 4)) {
                case 0:
                    port = new MilitaryAirport();
                    break;
                case 1:
                    port = new MilitaryNavalPort();
                    break;
                case 2:
                    port = new CivilNavalPort();
                    break;
                default:
                    port = new CivilAirport();
                    break;
            }
            port.setLocation(
                    World.randomUnoccupiedLocation(this)
            );
            getPorts().add(port);
        }
    }

    private static Location randomUnoccupiedLocation(World world) {
        Location location;
        do {
            location = world.translateLocation(new Location(
                    (int) (Math.random() * kWidth),
                    (int) (Math.random() * kHeight)
            ));
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
                mLocations[i][j] = new Location(i, j);
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
}
