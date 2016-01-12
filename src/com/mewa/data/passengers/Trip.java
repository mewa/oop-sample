package com.mewa.data.passengers;

import com.mewa.data.location.Route;
import com.mewa.data.ports.AbstractPort;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Mewa on 2015-10-12.
 */
public class Trip implements Serializable {

    private final Passenger mPassenger;

    public enum Type {
        LEISURE,
        WORK
    }

    private int mPosition = 0;
    private final Type mType;
    private final AbstractPort mHome;
    private final List<Pair<Route, Integer>> mRoutes = new ArrayList<Pair<Route, Integer>>() {
        @Override
        public boolean contains(Object o) {
            Pair<Route, Integer> entry = (Pair<Route, Integer>) o;
            for (int i = 0; i < size(); i++) {
                Pair<Route, Integer> item = get(i);
                if (item.getKey() == entry.getKey() && item.getValue() == entry.getValue()) {
                    return true;
                }
            }
            return super.contains(o);
        }
    };

    public Trip(AbstractPort home, Type type, Passenger passenger) {
        mPassenger = passenger;
        mHome = home;
        mType = type;
        add(mHome.getRandomRoute());
        Pair<Route, Integer> route;
        do {
            route = mRoutes.get(mRoutes.size() - 1);
            Pair<Route, Integer> randomRoute;
            randomRoute = route.getKey().getDestination(route.getValue()).getRandomRoute();
            add(randomRoute);
        } while (mRoutes.get(mRoutes.size() - 1).getKey().getDestination(mRoutes.get(mRoutes.size() - 1).getValue()) != mHome);
    }

    private void add(Pair<Route, Integer> route) {
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
    public synchronized Pair<Route, Integer> getNextRoute() {
        return mRoutes.get(mPosition);
    }

    /**
     * ruszaj w trasę
     */
    public synchronized void advance() {
        mPassenger.setLocation(
                mRoutes.get(mPosition).getKey().getDestination(
                        mRoutes.get(mPosition++).getValue()
                )
        );
        if (isFinished()) {
            mPassenger.onTripFinished();
        }
    }

    /**
     * @return czy koneic podrozy
     */
    public boolean isFinished() {
        return mPosition >= mRoutes.size();
    }
}
