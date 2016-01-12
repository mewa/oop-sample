package com.mewa.data.type;

import com.mewa.data.passengers.Passenger;

/**
 * Created by Mewa on 2016-01-11.
 */
public interface CivilVehicle extends Civil{
    /**
     * zwraca pojemność pojazdu
     * @return pojemność pojazdu
     */
    int getCapacity();

    /**
     * zapakuj delikwenta do paki (pojazdu)
     * @param passenger
     * @return czy udało się zapakować
     */
    public boolean board(Passenger passenger);

    /**
     * czysci pojazd z pasazerow
     */
    void clearPassengers();
}
