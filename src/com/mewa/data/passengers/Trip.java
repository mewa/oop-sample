package com.mewa.data.passengers;

import com.mewa.data.location.Route;

/**
 * Created by Mewa on 2015-10-12.
 */
public class Trip {
    public enum Type {
        LEISURE,
        WORK
    }
    
    private final Type mType;
    private final Route mRoute;

    Trip(Route route, Type type) {
        mRoute = route;
        mType = type;
    }

    public Route getRoute() {
        return mRoute;
    }

    public Type getType() {
        return mType;
    }
}
