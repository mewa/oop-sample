package com.mewa.data;

import com.mewa.data.location.Location;
import com.mewa.ui.controllers.GUIMain;

import java.io.Serializable;

/**
 * Created by Mewa on 2015-12-19.
 */
public abstract class GameObject implements Localizable, Serializable {
    private Location mLocation;
    private transient GameObjectUpdateListener listener;

    public void setLocation(Location location) {
        this.mLocation = location;
        update();
    }

    /**
     * przekaz aktualizcje obiektu
     */
    protected void update() {
        if (listener != null) {
            synchronized (listener) {
                listener.onGameObjectUpdated(this);
            }
        }
    }

    /**
     * ustaw lokalizacje obiektu
     * @param x wspolrzedna x
     * @param y wspolrzedna y
     */
    public void setLocation(double x, double y) {
        this.mLocation.setX(x);
        this.mLocation.setY(y);
        update();
    }

    /**
     * ustaw listenera
     * @param gameObjectUpdateListener
     */
    public void setGameObjectListener(GameObjectUpdateListener gameObjectUpdateListener) {
        listener = gameObjectUpdateListener;
    }

    @Override
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
