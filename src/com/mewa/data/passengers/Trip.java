package com.mewa.data.passengers;

import com.mewa.data.location.Route;
import com.mewa.data.ports.AbstractPort;

import java.util.*;

/**
 * Created by Mewa on 2015-10-12.
 */
public class Trip {

    public enum Type {
        LEISURE,
        WORK
    }

    private int mPosition = 0;
    private final Type mType;
    private final AbstractPort mHome;
    private final List<Map.Entry<Route, Integer>> mRoutes = new ArrayList<Map.Entry<Route, Integer>>() {
        @Override
        public boolean contains(Object o) {
            Map.Entry<Route, Integer> entry = (Map.Entry<Route, Integer>) o;
            for (int i = 0; i < size(); i++) {
                Map.Entry<Route, Integer> item = get(i);
                if (item.getKey() == entry.getKey() && item.getValue() == entry.getValue()) {
                    return true;
                }
            }
            return super.contains(o);
        }
    };

    public Trip(AbstractPort home, Type type) {
        mHome = home;
        mType = type;
        add(mHome.getRandomRoute());
        Map.Entry<Route, Integer> route;
        do {
            route = mRoutes.get(mRoutes.size() - 1);
            Map.Entry<Route, Integer> randomRoute;
            randomRoute = route.getKey().getDestination(route.getValue()).getRandomRoute();
            add(randomRoute);
        } while (mRoutes.get(mRoutes.size() - 1).getKey().getDestination(route.getValue()) != mHome);
    }

    private void add(Map.Entry<Route, Integer> route) {
        mRoutes.add(route);
    }

    /**
     * zwraca lokację początkową dla tej trasy
     *
     * @return
     */
    public AbstractPort getHome() {
        return mHome;
    }

    /**
     * @return typ podróży Trip.Type
     */
    public Type getType() {
        return mType;
    }

    /**
     * @return zwraca następną trasę z jakiej ma się składać podróż
     */
    public Map.Entry<Route, Integer> getNextRoute() {
        return mRoutes.get(mPosition);
    }

    /**
     * ruszaj w trasę
     */
    public void advance() {
        ++mPosition;
    }

    /**
     *
     * @return czy koneic podrozy
     */
    public boolean isFinished() {
        return mPosition == mRoutes.size();
    }
}
