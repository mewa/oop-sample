package com.mewa.data.ports;

import com.mewa.Main;
import com.mewa.data.type.Civil;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.utils.i.Logger;

/**
 * Created by Mewa on 2015-10-10.
 */
public abstract class CivilAbstractPort<V extends Vehicle> extends AbstractPort {
    @Override
    public boolean receive(Vehicle vehicle) {
        if (vehicle instanceof Civil) {
            Main.logger.log(Logger.VERBOSE, vehicle + " arrived @ " + this);
            return true;
        } else {
            Main.logger.log(Logger.VERBOSE, "Only civil vehicles are permitted to enter civil facilities");
            return super.receive(vehicle);
        }
    }

    @Override
    public boolean depart(Vehicle vehicle) {
        return super.depart(vehicle);
    }
}
