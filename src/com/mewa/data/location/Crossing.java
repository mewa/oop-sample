package com.mewa.data.location;

import com.mewa.Main;
import com.mewa.data.GameObject;
import com.mewa.data.type.Airborne;
import com.mewa.data.type.Naval;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.ui.Drawable;
import com.mewa.ui.controllers.GUIMain;
import com.mewa.utils.i.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by Mewa on 2015-12-15.
 */
public class Crossing extends GameObject implements Drawable {

    public static final double RADIUS = 1;
    private static final Color BG_COLOR = new Color(0xD4 / 0xFF, 0x6A / 0xFF, 0x6A / 0xFF, 0.1);
    private final Semaphore mWaterLock = new Semaphore(1);
    private final Semaphore mAirLock = new Semaphore(1);

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(BG_COLOR);
        gc.fillOval((getLocation().getX() - RADIUS) * GUIMain.CELL_SIZE, (getLocation().getY() - RADIUS) * GUIMain.CELL_SIZE, 2 * RADIUS * GUIMain.CELL_SIZE, 2 * RADIUS * GUIMain.CELL_SIZE);
        gc.setStroke(Color.BROWN);
        gc.setLineWidth(2);
        gc.strokeText("X", (getLocation().getX() - 0.12) * GUIMain.CELL_SIZE, (getLocation().getY() + 0.12) * GUIMain.CELL_SIZE);
    }

    List<Vehicle> vehicles = Collections.synchronizedList(new ArrayList<Vehicle>());

    /**
     * Sprawdza czy pojazd moze wjechac na skrzyzowanie
     * @param vehicle
     */
    public void accept(final Vehicle vehicle) {
        try {
            if (vehicle instanceof Airborne) {
                mAirLock.acquire();
                new Thread(this + "-lock-" + vehicle) {
                    @Override
                    public void run() {
                        boolean locked = true;
                        while (locked) {
                            try {
                                sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            locked = World.getInstance().collides(Crossing.this, vehicle, RADIUS);
                        }
                        mAirLock.release();
                        Main.logger.log(Logger.VERBOSE, this + " unlocked");
                    }
                }.start();
            } else if (vehicle instanceof Naval) {
                mWaterLock.acquire();
                new Thread(this + "-lock-" + vehicle) {
                    @Override
                    public void run() {
                        boolean locked = true;
                        while (locked) {
                            try {
                                sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            locked = World.getInstance().collides(Crossing.this, vehicle, RADIUS);
                        }
                        mWaterLock.release();
                        Main.logger.log(Logger.VERBOSE, this + " unlocked");
                    }
                }.start();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        mAirLock.drainPermits();
        mWaterLock.drainPermits();
        mWaterLock.release();
        mAirLock.release();
    }

    @Override
    public void onClick(GUIMain guiMain) {

    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + String.format("%06x", hashCode());
    }
}
