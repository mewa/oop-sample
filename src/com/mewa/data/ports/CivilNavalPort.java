package com.mewa.data.ports;

import com.mewa.data.passengers.Passenger;
import com.mewa.data.type.Civil;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.ui.controllers.GUIMain;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mewa on 2015-10-12.
 */
public class CivilNavalPort extends NavalPort implements Civil {
    private final List<Passenger> mPassengers = Collections.synchronizedList(new ArrayList<Passenger>());

    @Override
    public void receive(Vehicle vehicle) {
        super.receive(vehicle);
    }

    @Override
    public boolean depart(Vehicle vehicle) {
        return super.depart(vehicle);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.color(Color.TEAL.getRed(), Color.TEAL.getGreen(), Color.TEAL.getBlue(), 0.3));
        double inset = 0.05;
        gc.fillRect(getLocation().getX() * GUIMain.CELL_SIZE + inset * GUIMain.CELL_SIZE,
                getLocation().getY() * GUIMain.CELL_SIZE + inset * GUIMain.CELL_SIZE,
                (1 - 2 * inset) * GUIMain.CELL_SIZE,
                (1 - 2 * inset) * GUIMain.CELL_SIZE);
        super.draw(gc);
    }


    @Override
    public int getNumberOfPassengers() {
        return 0;
    }

    @Override
    public Collection<Passenger> getPassengers() {
        return mPassengers;
    }

}
