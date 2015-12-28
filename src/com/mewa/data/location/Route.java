package com.mewa.data.location;

import com.mewa.Main;
import com.mewa.data.GameObject;
import com.mewa.data.ports.AbstractPort;
import com.mewa.data.ports.HasPort;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.ui.Drawable;
import com.mewa.ui.controllers.GUIMain;
import com.mewa.utils.i.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.*;

/**
 * Created by Mewa on 2015-10-10.
 */
public class Route extends GameObject implements Drawable {
    private final List<Location> locations = Collections.synchronizedList(new ArrayList<Location>());
    private final AbstractPort origin;
    private final AbstractPort destination;

    private final List<Vehicle> normalDirection = Collections.synchronizedList(new ArrayList<Vehicle>());
    private final List<Vehicle> reverseDirection = Collections.synchronizedList(new ArrayList<Vehicle>());

    public Route(AbstractPort port, AbstractPort port2) {
        super();
        origin = port;
        destination = port2;
    }

    public List<Location> getStops() {
        return locations;
    }

    public AbstractPort getOrigin() {
        return origin;
    }

    public AbstractPort getDestination() {
        return destination;
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (true) return;
        gc.setStroke(Color.RED);
        gc.setLineWidth(1.5);
        Main.logger.log(Logger.VERBOSE, "draw " + this + " " + locations + " on " + gc);
        gc.moveTo((locations.get(0).getX() + 0.25 + Math.random() / 2) * GUIMain.CELL_SIZE, (locations.get(0).getY() + 0.25 + Math.random() / 2) * GUIMain.CELL_SIZE);
        for (int i = 0; i < locations.size() - 1; ++i) {
            gc.lineTo((locations.get(i + 1).getX() + 0.25 + Math.random() / 2) * GUIMain.CELL_SIZE, (locations.get(i + 1).getY() + 0.25 + Math.random() / 2) * GUIMain.CELL_SIZE);
        }
        gc.stroke();
    }

    @Override
    public void onClick(GUIMain guiMain) {
        guiMain.showRoutePanel(this);
    }

    @Override
    public String toString() {
        return "Route " + getOrigin() + "-" + getDestination();
    }

    public void addVehicle(int direction, Vehicle vehicle) {
        if (direction == 1) {
            synchronized (normalDirection) {
                normalDirection.add(vehicle);
            }
        } else if (direction == -1) {
            synchronized (reverseDirection) {
                reverseDirection.add(vehicle);
            }
        }
    }

    public boolean collidesWithNext(Vehicle vehicle, int direction) {
        if (direction == 1) {
            synchronized (normalDirection) {
                for (int i = 0; i < normalDirection.size() - 1; ++i) {
                    if (normalDirection.get(i) == vehicle) {
                        return World.getInstance().checkCollision(vehicle.getLocation(), normalDirection.get(i + 1).getLocation());
                    }
                }
            }
        } else if (direction == -1) {
            synchronized (reverseDirection) {
                for (int i = reverseDirection.size() - 1; i-- > 1; ) {
                    if (reverseDirection.get(i) == vehicle) {
                        return World.getInstance().checkCollision(vehicle.getLocation(), reverseDirection.get(i - 1).getLocation());
                    }
                }
            }
        }
        return false;
    }

    public void removeVehicle(int direction, Vehicle vehicle) {
        if (direction == 1) {
            synchronized (normalDirection) {
                normalDirection.remove(vehicle);
            }
        } else if (direction == -1) {
            synchronized (reverseDirection) {
                reverseDirection.remove(vehicle);
            }
        }
    }
}
