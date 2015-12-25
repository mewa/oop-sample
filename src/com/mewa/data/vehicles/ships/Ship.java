package com.mewa.data.vehicles.ships;

import com.mewa.data.location.Location;
import com.mewa.data.type.Naval;
import com.mewa.data.vehicles.Vehicle;

/**
 * Created by Mewa on 2015-10-10.
 */
public abstract class Ship extends Vehicle implements Naval {
    private double mMaxVelocity;

    @Override
    public double getMaxVelocity() {
        return mMaxVelocity;
    }

    public void setMaxVelocity(double maxVelocity) {
        if (maxVelocity < 0.0) {
            throw new IllegalArgumentException("Must be positive");
        }
        this.mMaxVelocity = maxVelocity;
    }

    @Override
    protected void travelTo(Location nextLocation) {

    }
}
