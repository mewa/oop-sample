package com.mewa.data.vehicles.planes;

import com.mewa.data.type.Civil;

/**
 * Created by Mewa on 2015-10-10.
 */
public class PassengerPlane extends Plane implements Civil {
    private int mCapacity;
    private int mNumPassengers;

    public PassengerPlane(int capacity) {
        mCapacity = capacity;
    }

    @Override
    public int getCapacity() {
        return mCapacity;
    }

    @Override
    public int getNumberOfPassengers() {
        return mNumPassengers;
    }
}
