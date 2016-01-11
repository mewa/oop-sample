package com.mewa.data.location;

import com.mewa.Main;
import com.mewa.data.GameObject;
import com.mewa.data.Localizable;
import com.mewa.data.ports.AbstractPort;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.ui.Drawable;
import com.mewa.ui.controllers.GUIMain;
import com.mewa.utils.i.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Mewa on 2015-10-10.
 */
public class Route extends GameObject implements Drawable {
    private final List<Localizable> locations = Collections.synchronizedList(new ArrayList<Localizable>());
    private final AbstractPort origin;
    private final AbstractPort destination;

    private final List<Vehicle> normalDirection = Collections.synchronizedList(new ArrayList<Vehicle>());
    private final List<Vehicle> reverseDirection = Collections.synchronizedList(new ArrayList<Vehicle>());
    private final Map<Vehicle, Integer> nextStopsMap = Collections.synchronizedMap(new HashMap<Vehicle, Integer>());

    public Route(AbstractPort port, AbstractPort port2) {
        super();
        origin = port;
        destination = port2;
    }

    public List<Localizable> getStops() {
        return locations;
    }

    public AbstractPort getOrigin(int direction) {
        if (direction > 0)
            return origin;
        else
            return destination;
    }

    public AbstractPort getDestination(int direction) {
        if (direction > 0)
            return destination;
        else
            return origin;
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (true) return;
        gc.setStroke(Color.RED);
        gc.setLineWidth(1.5);
        Main.logger.log(Logger.VERBOSE, "draw " + this + " " + locations + " on " + gc);
        gc.moveTo((locations.get(0).getLocation().getX() + 0.25 + Math.random() / 2) * GUIMain.CELL_SIZE, (locations.get(0).getLocation().getY() + 0.25 + Math.random() / 2) * GUIMain.CELL_SIZE);
        for (int i = 0; i < locations.size() - 1; ++i) {
            gc.lineTo((locations.get(i + 1).getLocation().getX() + 0.25 + Math.random() / 2) * GUIMain.CELL_SIZE, (locations.get(i + 1).getLocation().getY() + 0.25 + Math.random() / 2) * GUIMain.CELL_SIZE);
        }
        gc.stroke();
    }

    @Override
    public void onClick(GUIMain guiMain) {
        guiMain.showRoutePanel(this);
    }

    @Override
    public String toString() {
        return "Route " + getOrigin(1) + "-" + getDestination(1);
    }

    public void addVehicle(int direction, Vehicle vehicle) {
        synchronized (nextStopsMap) {
            if (direction == 1) {
                synchronized (normalDirection) {
                    nextStopsMap.put(vehicle, 1);
                    normalDirection.add(vehicle);
                }
            } else if (direction == -1) {
                synchronized (reverseDirection) {
                    nextStopsMap.put(vehicle, locations.size() - 1);
                    reverseDirection.add(vehicle);
                }
            }
        }
    }

    public boolean collidesWithNext(Vehicle vehicle, int direction) {
        if (direction == 1) {
            synchronized (normalDirection) {
                for (int i = normalDirection.size() - 1; i > 0; --i) {
                    if (normalDirection.get(i) == vehicle) {
                        return World.getInstance().collides(vehicle.getLocation(), normalDirection.get(i - 1).getLocation());
                    }
                }
            }
        } else if (direction == -1) {
            synchronized (reverseDirection) {
                for (int i = reverseDirection.size() - 1; i > 0; --i) {
                    if (reverseDirection.get(i) == vehicle) {
                        return World.getInstance().collides(vehicle.getLocation(), reverseDirection.get(i - 1).getLocation());
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

    public Localizable getNextStop(Vehicle vehicle) {
        synchronized (nextStopsMap) {
            boolean contains = nextStopsMap.containsKey(vehicle);
            int i;
            if (contains) {
                i = nextStopsMap.get(vehicle);
            } else {
                throw new NullPointerException("doesn't contain vehicle " + vehicle);
            }
            return i >= 0 && i < locations.size() ? locations.get(i) : null;
        }
    }

    public boolean incStop(Vehicle vehicle, int direction) {
        int pos;
        synchronized (nextStopsMap) {
            pos = nextStopsMap.get(vehicle);
            if (direction > 0) {
                nextStopsMap.put(vehicle, ++pos);
                return pos >= locations.size();
            } else {
                nextStopsMap.put(vehicle, --pos);
                return pos < 0;
            }
        }
    }


    public void add(Localizable localizable) {
        synchronized (locations) {
            locations.add(localizable);
        }
    }
}
