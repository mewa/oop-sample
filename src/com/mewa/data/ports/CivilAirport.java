package com.mewa.data.ports;

import com.mewa.data.passengers.Passenger;
import com.mewa.data.type.Civil;
import com.mewa.data.type.CivilVehicle;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.data.vehicles.planes.PassengerPlane;
import com.mewa.ui.controllers.GUIMain;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.*;

/**
 * Created by Mewa on 2015-10-12.
 */
public class CivilAirport extends Airport implements Civil {
    private final List<Passenger> mPassengers = Collections.synchronizedList(new ArrayList<Passenger>());

    public CivilAirport() {
        this((int) (Math.random() * 10 + 1));
    }

    public CivilAirport(int capacity) {
        super(capacity);
    }

    @Override
    public boolean depart(Vehicle vehicle) {
        boolean wasInPort = super.depart(vehicle);
        if (vehicle instanceof CivilVehicle) {
            PassengerPlane passengerPlane = (PassengerPlane) vehicle;
            while (passengerPlane.getNumberOfPassengers() < passengerPlane.getCapacity() || Math.random() > 0.9) {
                synchronized (mPassengers) {
                    for (ListIterator<Passenger> it = mPassengers.listIterator(); it.hasNext(); ) {
                        Passenger passenger = it.next();
                        if (!passenger.isSleeping() && passenger.getTrip() != null &&
                                passenger.getTrip().getNextRoute().getKey().getDestination(passenger.getTrip().getNextRoute().getValue())
                                        == vehicle.getRoute().getDestination(vehicle.getDirection())) {
                            if (passengerPlane.board(passenger)) {
                                it.remove();
                            } else {
                                break;
                            }
                        }
                    }
                }
                if (passengerPlane.canBoard() && mPassengers.size() < 3 * getCapacity() && Math.random() > 0.5) {
                    addPassenger(new Passenger(this));
                } else {
                    break;
                }
            }
        }
        return wasInPort;
    }

    @Override
    public void receive(Vehicle vehicle) {
        super.receive(vehicle);
        if (vehicle instanceof CivilVehicle) {
            CivilVehicle civilVehicle = (CivilVehicle) vehicle;
            addPassengers(civilVehicle.getPassengers());
            civilVehicle.clearPassengers();
        }
    }

    private void addPassengers(Collection<Passenger> passengers) {
        for (Passenger passenger : passengers) {
            addPassenger(passenger);
        }
    }

    private void addPassenger(Passenger passenger) {
        synchronized (mPassengers) {
            mPassengers.add(passenger);
            synchronized (passenger) {
                passenger.setLocation(this);
                passenger.getTrip().advance();
            }
        }
        update();
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.color(Color.DEEPSKYBLUE.getRed(), Color.DEEPSKYBLUE.getGreen(), Color.DEEPSKYBLUE.getBlue(), 0.3));
        double inset = 0.05;
        gc.fillRect(getLocation().getX() * GUIMain.CELL_SIZE + inset * GUIMain.CELL_SIZE,
                getLocation().getY() * GUIMain.CELL_SIZE + inset * GUIMain.CELL_SIZE,
                (1 - 2 * inset) * GUIMain.CELL_SIZE,
                (1 - 2 * inset) * GUIMain.CELL_SIZE);
        super.draw(gc);
    }

    @Override
    public int getNumberOfPassengers() {
        return mPassengers.size();
    }

    @Override
    public Collection<Passenger> getPassengers() {
        return mPassengers;
    }

}
