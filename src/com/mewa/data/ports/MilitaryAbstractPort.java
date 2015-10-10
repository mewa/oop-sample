package com.mewa.data.ports;

import com.mewa.Main;
import com.mewa.data.type.Military;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.utils.i.Logger;

/**
 * Created by Mewa on 2015-10-10.
 */
public abstract class MilitaryAbstractPort<V extends Vehicle> extends AbstractPort {
    @Override
    public boolean receive(Vehicle vehicle) {
        if (vehicle instanceof Military) {
            Main.logger.log(Logger.VERBOSE, vehicle + " arrived @ " + this);
            return true;
        } else {
            Main.logger.log(Logger.VERBOSE, "Only military vehicles are permitted to enter military facilities");
            return super.receive(vehicle);
        }
    }

    @Override
    public boolean depart(Vehicle vehicle) {
        return super.depart(vehicle);
    }
}
