package com.mewa.data.location;

import com.mewa.Main;
import com.mewa.data.Localizable;
import com.mewa.data.type.Airborne;
import com.mewa.data.type.Naval;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.data.vehicles.planes.Plane;
import com.mewa.ui.Drawable;
import com.mewa.utils.i.Logger;
import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by Mewa on 2015-10-10.
 */
public class Location implements Comparable<Location>, Drawable, Localizable, Serializable {
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

    /**
     * zwraca najblizsza lokacje z przekazanej kolekcji
     * @param locations
     * @return
     */
    public Localizable closest(Collection<Localizable> locations) {
        Iterator<Localizable> it = locations.iterator();
        Localizable closestLocation = it.next();
        double minDistance = closestLocation.getLocation().distanceTo(this);
        while (it.hasNext()) {
            Localizable currentLocation = it.next();
            double distance = currentLocation.getLocation().distanceTo(this);
            if (distance < minDistance) {
                closestLocation = currentLocation;
                minDistance = distance;
            }
        }
        return closestLocation;
    }

    /**
     *
     * @param location
     * @return dystans do lokacji
     */
    public double distanceTo(Location location) {
        return Math.pow(Math.pow(getX() - location.getX(), 2) + Math.pow(getY() - location.getY(), 2), 0.5);
    }

    @Override
    public void draw(GraphicsContext gc) {

    }

    @Override
    public Location getLocation() {
        return this;
    }

    @Override
    public void setLocation(double x, double y) {
        setX(x);
        setY(y);
    }

    @Override
    public void setLocation(Location location) {
        setLocation(location.getX(), location.getY());
    }
}
