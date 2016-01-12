package com.mewa.data.type;

import com.mewa.data.ports.AbstractPort;

/**
 * Created by Mewa on 2015-10-10.
 */
public interface Airborne {
    /**
     * zwraca ilosc paliwa
     * @return
     */
    double getFuel();

    /**
     * zwraca predkosc
     * @return
     */
    double getSpeed();
    AbstractPort getNextPort();
}
