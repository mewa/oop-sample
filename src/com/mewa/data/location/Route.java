package com.mewa.data.location;

import com.mewa.data.ports.HasPort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mewa on 2015-10-10.
 */
public class Route {
    private final List<HasPort> locations = Collections.synchronizedList(new ArrayList<HasPort>());

    public List<HasPort> getStops() {
        return locations;
    }
}
