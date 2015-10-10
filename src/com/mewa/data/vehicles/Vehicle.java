package com.mewa.data.vehicles;

import com.mewa.Main;
import com.mewa.data.location.Location;
import com.mewa.data.ports.AbstractPort;
import com.mewa.data.ports.HasPort;
import com.mewa.utils.i.Logger;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Mewa on 2015-10-10.
 */
public abstract class Vehicle {
    private static AtomicInteger idGenerator = new AtomicInteger();

    private int mId = idGenerator.getAndIncrement();
    private Location mLocation;

    public void arrive(HasPort port) {
        Main.logger.log(Logger.VERBOSE, this + " is arriving to @ " + port.getPort());
        port.getPort().receive(this);
    }

    public void leave(HasPort port) {
        Main.logger.log(Logger.VERBOSE, this + " is leaving @ " + port.getPort());
        port.getPort().depart(this);
    }

    @Override
    public String toString() {
        return String.format("%s-%d", getClass().getSimpleName(), getId());
    }

    public int getId() {
        return mId;
    }
}
