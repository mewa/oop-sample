package com.mewa.data.ports;

import com.mewa.Main;
import com.mewa.data.type.Military;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.ui.Drawable;
import com.mewa.ui.controllers.GUIMain;
import com.mewa.utils.i.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by Mewa on 2015-10-12.
 */
public class MilitaryNavalPort extends NavalPort {

    @Override
    public boolean receive(Vehicle vehicle) {
        if (vehicle instanceof Military) {
            return super.receive(vehicle);
        } else {
            Main.logger.log(Logger.VERBOSE, String.format("%s denied %s: only military vehicles are permitted", this, vehicle));
            return false;
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.color(0.3 * Color.TEAL.getRed(), Color.TEAL.getGreen(), 0.3 * Color.TEAL.getBlue(), 0.5));
        double inset = 0.05;
        gc.fillRect(getLocation().getX() * GUIMain.CELL_SIZE + inset * GUIMain.CELL_SIZE,
                getLocation().getY() * GUIMain.CELL_SIZE + inset * GUIMain.CELL_SIZE,
                (1 - 2 * inset) * GUIMain.CELL_SIZE,
                (1 - 2 * inset) * GUIMain.CELL_SIZE);
        super.draw(gc);
    }
}
