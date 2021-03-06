package com.mewa.data.vehicles.planes;

import com.mewa.data.passengers.Passenger;
import com.mewa.data.type.CivilVehicle;
import com.mewa.ui.controllers.GUIMain;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Mewa on 2015-10-10.
 */
public class PassengerPlane extends Plane implements CivilVehicle, Serializable {
    private int mCapacity;

    private final List<Passenger> mPassengers = Collections.synchronizedList(new ArrayList<Passenger>());

    public PassengerPlane(int capacity) {
        super();
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
            synchronized (mPassengers) {
                mPassengers.add(passenger);
            }
            update();
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
        gc.setFill(Color.DEEPSKYBLUE);
        gc.setFont(Font.font("sans-serif", FontWeight.BOLD, 14));
        gc.fillText(
                "PP" + getId(),
                getLocation().getX() * GUIMain.CELL_SIZE,
                getLocation().getY() * GUIMain.CELL_SIZE,
                GUIMain.CELL_SIZE
        );
    }
}
