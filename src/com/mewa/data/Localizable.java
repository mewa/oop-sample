package com.mewa.data;

import com.mewa.data.location.Location;

/**
 * Created by Mewa on 2016-01-09.
 */
public interface Localizable {
    /**
     * @return lokalizacja obiektu
     */
    public Location getLocation();

    /**
     * ustawia lokalizacje obiektu
     * @param x wspolrzedna x
     * @param y wspolrzedna y
     */
    public void setLocation(double x, double y);

    /**
     * ustawia lokalizacje obiektu
     * @param location
     */
    public void setLocation(Location location);
}
