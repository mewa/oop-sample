package com.mewa.data.vehicles.ships;

import com.mewa.data.ports.AbstractPort;
import com.mewa.data.ports.HasPort;
import com.mewa.data.ports.MilitaryAirport;
import com.mewa.data.type.Military;
import com.mewa.data.weapons.Weapon;

/**
 * Created by Mewa on 2015-10-10.
 */
public class AircraftCarrier extends Ship implements Military, HasPort {
    private AbstractPort airport = new MilitaryAirport();

    @Override
    public AbstractPort getPort() {
        return airport;
    }

    @Override
    public Weapon getWeapon() {
        return null;
    }
}
