package com.mewa.data.location;

import com.mewa.data.type.Airborne;
import com.mewa.data.type.Naval;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.ui.controllers.GUIMain;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.concurrent.Semaphore;

/**
 * Created by Mewa on 2015-12-15.
 */
public class Crossing extends Location {

    private final Semaphore mWaterLock = new Semaphore(1);
    private final Semaphore mAirLock = new Semaphore(1);

    public Crossing(int x, int y) {
        setX(x);
        setY(y);
    }

    @Override
    public void onVehicleArriving(Vehicle vehicle) {
        if (vehicle instanceof Airborne) {
            try {
                mAirLock.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (vehicle instanceof Naval) {
            try {
                mWaterLock.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        super.onVehicleArriving(vehicle);
    }

    @Override
    public void onVehicleLeaving(Vehicle vehicle) {
        if (vehicle instanceof Airborne) {
            mAirLock.release();
        } else if (vehicle instanceof Naval) {
            mWaterLock.release();
        }
        super.onVehicleLeaving(vehicle);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(Color.BROWN);
        gc.setLineWidth(2);
        gc.strokeText("X", (getX() + 0.4) * GUIMain.CELL_SIZE, (getY() + 0.6) * GUIMain.CELL_SIZE);
    }
}
