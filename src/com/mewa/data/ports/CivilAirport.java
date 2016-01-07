package com.mewa.data.ports;

import com.mewa.Main;
import com.mewa.data.location.World;
import com.mewa.data.type.Civil;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.data.vehicles.planes.Plane;
import com.mewa.ui.Drawable;
import com.mewa.ui.controllers.GUIMain;
import com.mewa.utils.i.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by Mewa on 2015-10-12.
 */
public class CivilAirport extends Airport {
    public CivilAirport() {
        super((int) (Math.random() * 10 + 1));
    }

    public <T extends Plane & Civil> void acceptArrival(T civilPlane) {
        receive(civilPlane);
    }

    public <T extends Plane & Civil> void acceptDeparture(T civilPlane) {
        depart(civilPlane);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.color(Color.DEEPSKYBLUE.getRed(), Color.DEEPSKYBLUE.getGreen(), Color.DEEPSKYBLUE.getBlue(), 0.3));
        double inset = 0.05;
        gc.fillRect(getLocation().getX() * GUIMain.CELL_SIZE + inset * GUIMain.CELL_SIZE,
                getLocation().getY() * GUIMain.CELL_SIZE + inset * GUIMain.CELL_SIZE,
                (1 - 2 * inset) * GUIMain.CELL_SIZE,
                (1 - 2 * inset) * GUIMain.CELL_SIZE);
        super.draw(gc);
    }
}
