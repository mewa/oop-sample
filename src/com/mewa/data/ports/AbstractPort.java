package com.mewa.data.ports;

import com.mewa.Main;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.utils.i.Logger;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Mewa on 2015-10-10.
 */
public abstract class AbstractPort implements HasPort {
    private static AtomicInteger idGenerator = new AtomicInteger();

    private int mId = idGenerator.getAndIncrement();

    public boolean receive(Vehicle vehicle) {
        Main.logger.log(Logger.ERROR, vehicle + " arrived at " + this);
        return true;
    }

    public boolean depart(Vehicle vehicle) {
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

}
