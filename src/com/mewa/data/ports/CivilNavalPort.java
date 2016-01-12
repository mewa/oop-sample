package com.mewa.data.ports;

import com.mewa.data.passengers.Passenger;
import com.mewa.data.type.Civil;
import com.mewa.data.type.CivilVehicle;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.data.vehicles.planes.PassengerPlane;
import com.mewa.data.vehicles.ships.CruiseShip;
import com.mewa.ui.controllers.GUIMain;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.*;

/**
 * Created by Mewa on 2015-10-12.
 */
public class CivilNavalPort extends NavalPort implements Civil {
    private final List<Passenger> mPassengers = Collections.synchronizedList(new ArrayList<Passenger>());

    @Override
    public void receive(Vehicle vehicle) {
        super.receive(vehicle);
        if (vehicle instanceof CivilVehicle) {
            CivilVehicle civilVehicle = (CivilVehicle) vehicle;
            for (Passenger passenger : civilVehicle.getPassengers()) {
                passenger.getTrip().advance();
            }
            addPassengers(civilVehicle.getPassengers());
            civilVehicle.clearPassengers();
        }
    }

    @Override
    public boolean depart(Vehicle vehicle) {
        boolean wasInPort = super.depart(vehicle);
        if (vehicle instanceof CivilVehicle) {
            CruiseShip cruiseShip = (CruiseShip) vehicle;
            while (cruiseShip.getNumberOfPassengers() < cruiseShip.getCapacity() || Math.random() > 0.9) {
                synchronized (mPassengers) {
                    for (ListIterator<Passenger> it = mPassengers.listIterator(); it.hasNext(); ) {
                        Passenger passenger = it.next();
                        if (!passenger.isSleeping() && passenger.getTrip() != null && !passenger.getTrip().isFinished() &&
                                passenger.getTrip().getNextRoute().getKey().getDestination(passenger.getTrip().getNextRoute().getValue())
                                        == vehicle.getRoute().getDestination(vehicle.getDirection())) {
                            if (cruiseShip.board(passenger)) {
                                it.remove();
                            } else {
                                break;
                            }
                        }
                    }
                }
                if (cruiseShip.canBoard() && mPassengers.size() < 15 + Math.random() * 5 && Math.random() > 0.5) {
                    addPassenger(new Passenger(this));
                } else {
                    break;
                }
            }
        }
        return wasInPort;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.color(Color.TEAL.getRed(), Color.TEAL.getGreen(), Color.TEAL.getBlue(), 0.3));
        double inset = 0.05;
        gc.fillRect(getLocation().getX() * GUIMain.CELL_SIZE + inset * GUIMain.CELL_SIZE,
                getLocation().getY() * GUIMain.CELL_SIZE + inset * GUIMain.CELL_SIZE,
                (1 - 2 * inset) * GUIMain.CELL_SIZE,
                (1 - 2 * inset) * GUIMain.CELL_SIZE);
        super.draw(gc);
    }

    /**
     * dodaje pasazerow na lotnisko
     *
     * @param passengers
     */
    private void addPassengers(Collection<Passenger> passengers) {
        for (Passenger passenger : passengers) {
            addPassenger(passenger);
        }
    }

    /**
     * dodaje pasazera na lotnisko
     *
     * @param passenger
     */
    private void addPassenger(Passenger passenger) {
        synchronized (mPassengers) {
            mPassengers.add(passenger);
        }
        update();
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
