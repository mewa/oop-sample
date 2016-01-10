package com.mewa.data.ports;

import com.mewa.Main;
import com.mewa.data.location.Location;
import com.mewa.data.location.World;
import com.mewa.data.type.Airborne;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.data.vehicles.planes.Plane;
import com.mewa.ui.Drawable;
import com.mewa.ui.controllers.GUIMain;
import com.mewa.utils.i.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Semaphore;

/**
 * Created by Mewa on 2015-10-12.
 */
public class Airport extends AbstractPort {

    private final int capacity;
    private final Semaphore capacitySemaphore;

    public Airport(int capacity) {
        this.capacity = capacity;
        this.capacitySemaphore = new Semaphore(capacity);
    }

    @Override
    public boolean receive(Vehicle vehicle) {
        try {
            capacitySemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return super.receive(vehicle);
    }

    @Override
    public boolean depart(Vehicle vehicle) {
        boolean wasInPort = super.depart(vehicle);
        if (wasInPort) {
            capacitySemaphore.release();
        }
        return wasInPort;
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

    public int getCapacity() {
        return capacity;
    }
}
