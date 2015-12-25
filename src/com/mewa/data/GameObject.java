package com.mewa.data;

import com.mewa.ui.controllers.GUIMain;

/**
 * Created by Mewa on 2015-12-19.
 */
public abstract class GameObject {
    /**
     * dispatches click logic coming for a click coming from a GUIMain object
     * @param guiMain object
     */
    public abstract void onClick(GUIMain guiMain);
}
