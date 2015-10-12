package com.mewa.data.vehicles.ships;

import com.mewa.data.type.Waterborne;
import com.mewa.data.vehicles.Vehicle;

/**
 * Created by Mewa on 2015-10-10.
 */
public class Ship extends Vehicle implements Waterborne {
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
}
