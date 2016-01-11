package com.mewa.data.type;

import com.mewa.data.passengers.Passenger;

import java.util.Collection;

/**
 * Created by Mewa on 2015-10-10.
 */
public interface Civil {
    /**
     *  @return liczbę pasażerów
     */
    int getNumberOfPassengers();

    /**
     *
     * @return kolekcję pasażerów
     */
    Collection<Passenger> getPassengers();
}
