package com.mewa.data.vehicles.ships;

import com.mewa.data.passengers.Passenger;
import com.mewa.data.type.CivilVehicle;
import com.mewa.ui.controllers.GUIMain;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mewa on 2015-10-10.
 */
public class CruiseShip extends Ship implements CivilVehicle {
    private int mCapacity;

    private final List<Passenger> mPassengers = Collections.synchronizedList(new ArrayList<Passenger>());

    public CruiseShip(int capacity) {
        mCapacity = capacity;
    }

    @Override
    public int getCapacity() {
        return mCapacity;
    }

    @Override
    public int getNumberOfPassengers() {
        return mPassengers.size();
    }

    @Override
    public Collection<Passenger> getPassengers() {
        return mPassengers;
    }

    @Override
    public synchronized boolean board(Passenger passenger) {
        if (canBoard()) {
            mPassengers.add(passenger);
            return true;
        }
        return false;
    }

    @Override
    public void clearPassengers() {
        synchronized (mPassengers) {
            mPassengers.clear();
        }
        update();
    }

    public boolean canBoard() {
        return mPassengers.size() < getCapacity();
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.DARKSEAGREEN);
        gc.setFont(Font.font("sans-serif", FontWeight.BOLD, 14));
        gc.fillText(
                "CS" + getId(),
                getLocation().getX() * GUIMain.CELL_SIZE,
                getLocation().getY() * GUIMain.CELL_SIZE,
                GUIMain.CELL_SIZE
        );
    }
}
