package com.mewa.data.location;

import com.mewa.Main;
import com.mewa.data.type.Airborne;
import com.mewa.data.type.Naval;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.data.vehicles.planes.Plane;
import com.mewa.ui.Drawable;
import com.mewa.utils.i.Logger;
import javafx.scene.canvas.GraphicsContext;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by Mewa on 2015-10-10.
 */
public class Location implements Comparable<Location>, Drawable {
    private double mX;
    private double mY;

    public Location() {

    }

    public Location(double x, double y) {
        setX(x);
        setY(y);
    }

    public Location(Location location) {
        Main.logger.log(Logger.VERBOSE, "location: " + location);
        setX(location.getX());
        setY((location.getY()));
    }


    public double getX() {
        return mX;
    }

    public void setX(double x) {
        this.mX = x;
    }

    public double getY() {
        return mY;
    }

    public void setY(double y) {
        this.mY = y;
    }

    @Override
    public String toString() {
        return String.format("[Location: %.2f x %.2f]", getX(), getY());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Location) {
            Location location = (Location) obj;
            return Math.abs(mX - location.getX()) < 0.001 && Math.abs(mY - location.getY()) < 0.001;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return (int) mX << 4 | (int) mY;
    }

    @Override
    public int compareTo(Location o) {
        int cmp = Double.compare(getX(), o.getX());
        if (cmp != 0)
            return cmp;
        return Double.compare(getY(), o.getY());
    }

    public Location closest(Collection<Location> locations) {
        Iterator<Location> it = locations.iterator();
        Location closestLocation = it.next();
        double minDistance = closestLocation.distanceTo(this);
        while (it.hasNext()) {
            Location currentLocation = it.next();
            double distance = currentLocation.distanceTo(this);
            if (distance < minDistance) {
                closestLocation = currentLocation;
            }
        }
        return closestLocation;
    }

    public double distanceTo(Location location) {
        return Math.pow(Math.pow(getX() - location.getX(), 2) + Math.pow(getY() - location.getY(), 2), 0.5);
    }

    public void onVehicleArriving(Vehicle vehicle) {

    }

    public void onVehicleLeaving(Vehicle vehicle) {

    }

    @Override
    public void draw(GraphicsContext gc) {

    }
}
