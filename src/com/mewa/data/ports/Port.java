package com.mewa.data.ports;

import com.mewa.Main;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.data.vehicles.ships.Ship;
import com.mewa.utils.i.Logger;

/**
 * Created by Mewa on 2015-10-10.
 */
public class Port extends AbstractPort {
    @Override
    public void receive(Vehicle vehicle) {
        if (vehicle instanceof Ship) {
            Main.logger.log(Logger.VERBOSE, vehicle + " arrived @ " + this);
            return;
        }
        super.receive(vehicle);
    }

    @Override
    public void depart(Vehicle vehicle) {
        super.depart(vehicle);
    }
}
