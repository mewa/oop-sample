package com.mewa.data.ports;

import com.mewa.Main;
import com.mewa.data.type.Civil;
import com.mewa.data.type.Military;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.utils.i.Logger;

/**
 * Created by Mewa on 2015-10-12.
 */
public class CivilNavalPort extends NavalPort {
    @Override
    public boolean receive(Vehicle vehicle) {
        if (vehicle instanceof Civil) {
            return super.receive(vehicle);
        } else {
            Main.logger.log(Logger.VERBOSE, String.format("%s denied %s: only civil vehicles are permitted", this, vehicle));
            return false;
        }
    }
}
