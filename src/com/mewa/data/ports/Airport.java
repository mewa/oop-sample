package com.mewa.data.ports;

import com.mewa.Main;
import com.mewa.data.type.Airborne;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.ui.Drawable;
import com.mewa.ui.controllers.GUIMain;
import com.mewa.utils.i.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by Mewa on 2015-10-12.
 */
public class Airport extends AbstractPort {
    @Override
    public boolean receive(Vehicle vehicle) {
        if (vehicle instanceof Airborne) {
            return super.receive(vehicle);
        } else {
            Main.logger.log(Logger.VERBOSE, String.format("%s denied %s: only aircrafts are permitted", this, vehicle));
            return false;
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.DEEPSKYBLUE);
        double inset = 0.2;
        gc.fillRect(getLocation().getX() * GUIMain.CELL_SIZE + inset * GUIMain.CELL_SIZE,
                getLocation().getY() * GUIMain.CELL_SIZE + inset * GUIMain.CELL_SIZE,
                (1 - 2 * inset) * GUIMain.CELL_SIZE,
                (1 - 2 * inset) * GUIMain.CELL_SIZE);
    }
}
