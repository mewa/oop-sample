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

    public void receive(Vehicle vehicle) {
        Main.logger.log(Logger.ERROR, this + " denied " + vehicle);
    }

    public void depart(Vehicle vehicle) {
        Main.logger.log(Logger.ERROR, this + " disallowed " + vehicle + "'s departure");
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
