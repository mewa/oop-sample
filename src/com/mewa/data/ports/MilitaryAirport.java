package com.mewa.data.ports;

import com.mewa.Main;
import com.mewa.data.type.Airborne;
import com.mewa.data.type.Military;
import com.mewa.data.type.Waterborne;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.data.vehicles.planes.Plane;
import com.mewa.data.vehicles.ships.Ship;
import com.mewa.data.weapons.Weapon;
import com.mewa.utils.i.Logger;

/**
 * Created by Mewa on 2015-10-10.
 */
public class MilitaryAirport extends MilitaryAbstractPort<Plane> {
    @Override
    public boolean receive(Vehicle vehicle) {
        if (vehicle instanceof Plane) {
            return super.receive(vehicle);
        } else {
            Main.logger.log(Logger.VERBOSE, vehicle + " cannot land @ " + this);
            return false;
        }
    }
}
