package com.mewa.data.vehicles.ships;

import com.mewa.data.type.Civil;

/**
 * Created by Mewa on 2015-10-10.
 */
public class CruiseShip extends Ship implements Civil {
    private int mCapacity;

    @Override
    public int getCapacity() {
        return mCapacity;
    }

    @Override
    public int getNumberOfPassengers() {
        return 0;
    }
}
