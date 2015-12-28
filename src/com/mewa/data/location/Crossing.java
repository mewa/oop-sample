package com.mewa.data.location;

import com.mewa.data.GameObject;
import com.mewa.data.type.Airborne;
import com.mewa.data.type.Naval;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.ui.Drawable;
import com.mewa.ui.controllers.GUIMain;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.concurrent.Semaphore;

/**
 * Created by Mewa on 2015-12-15.
 */
public class Crossing extends GameObject implements Drawable {

    private final Semaphore mWaterLock = new Semaphore(1);
    private final Semaphore mAirLock = new Semaphore(1);

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(Color.BROWN);
        gc.setLineWidth(2);
        gc.strokeText("X", (getLocation().getX() + 0.4) * GUIMain.CELL_SIZE, (getLocation().getY() + 0.6) * GUIMain.CELL_SIZE);
    }

    @Override
    public void onClick(GUIMain guiMain) {

    }
}
