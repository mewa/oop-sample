package com.mewa.data.vehicles.planes;

import com.mewa.data.ports.AbstractPort;
import com.mewa.data.type.Airborne;
import com.mewa.data.vehicles.Vehicle;

/**
 * Created by Mewa on 2015-10-10.
 */
public abstract class Plane extends Vehicle implements Airborne {
    private double mFuel;

    @Override
    public double getFuel() {
        return mFuel;
    }

    @Override
    public AbstractPort getNextPort() {
        return null;
    }
}
