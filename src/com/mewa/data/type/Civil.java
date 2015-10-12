package com.mewa.data.type;

import com.mewa.data.passengers.Passenger;

/**
 * Created by Mewa on 2015-10-10.
 */
public interface Civil {
    int getCapacity();
    int getNumberOfPassengers();
    public boolean board(Passenger passenger);
}
