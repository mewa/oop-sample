package com.mewa.data.location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mewa on 2015-10-10.
 */
public class Route {
    private final List<Location> locations = Collections.synchronizedList(new ArrayList<Location>());

    public List<Location> getLocations() {
        return locations;
    }
}
