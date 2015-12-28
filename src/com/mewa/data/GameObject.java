package com.mewa.data;

import com.mewa.Main;
import com.mewa.data.location.Location;
import com.mewa.data.location.World;
import com.mewa.ui.controllers.GUIMain;
import com.mewa.utils.i.Logger;

/**
 * Created by Mewa on 2015-12-19.
 */
public abstract class GameObject {
    private Location mLocation;

    public void setLocation(Location location) {
        this.mLocation = location;
    }

    public Location getLocation() {
        return mLocation;
    }

    /**
     * dispatches click logic coming for a click coming from a GUIMain object
     * @param guiMain object
     */
    public abstract void onClick(GUIMain guiMain);
}
