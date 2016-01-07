package com.mewa.data;

import com.mewa.data.location.Location;
import com.mewa.ui.controllers.GUIMain;

/**
 * Created by Mewa on 2015-12-19.
 */
public abstract class GameObject {
    private Location mLocation;
    private GameObjectUpdateListener listener;

    public void setLocation(Location location) {
        this.mLocation = location;
        update();
    }

    protected void update() {
        if (listener != null) {
            synchronized (listener) {
                listener.onGameObjectUpdated(this);
            }
        }
    }

    public void setLocation(double x, double y) {
        this.mLocation.setX(x);
        this.mLocation.setY(y);
        update();
    }

    public void setLocationUpdateListener(GameObjectUpdateListener gameObjectUpdateListener) {
        listener = gameObjectUpdateListener;
    }

    public Location getLocation() {
        return mLocation;
    }

    /**
     * dispatches click logic coming for a click coming from a GUIMain object
     *
     * @param guiMain object
     */
    public abstract void onClick(GUIMain guiMain);
}
