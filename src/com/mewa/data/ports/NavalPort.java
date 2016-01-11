package com.mewa.data.ports;

import com.mewa.Main;
import com.mewa.data.type.Naval;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.ui.controllers.GUIMain;
import com.mewa.utils.i.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by Mewa on 2015-10-12.
 */
public class NavalPort extends AbstractPort {
    @Override
    public void receive(Vehicle vehicle) {
        if (vehicle instanceof Naval) {
            super.receive(vehicle);
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.TEAL);
        double inset = 0.2;
        gc.fillRect(getLocation().getX() * GUIMain.CELL_SIZE + inset * GUIMain.CELL_SIZE,
                getLocation().getY() * GUIMain.CELL_SIZE + inset * GUIMain.CELL_SIZE,
                (1 - 2 * inset) * GUIMain.CELL_SIZE,
                (1 - 2 * inset) * GUIMain.CELL_SIZE);
        super.draw(gc);
    }
}
