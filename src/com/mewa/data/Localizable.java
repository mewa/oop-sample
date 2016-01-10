package com.mewa.data;

import com.mewa.data.location.Location;

/**
 * Created by Mewa on 2016-01-09.
 */
public interface Localizable {
    public Location getLocation();
    public void setLocation(double x, double y);
    public void setLocation(Location location);
}
