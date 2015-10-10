package com.mewa.data.vehicles.ships;

import com.mewa.data.ports.AbstractPort;
import com.mewa.data.ports.Airport;
import com.mewa.data.ports.HasPort;

/**
 * Created by Mewa on 2015-10-10.
 */
public class AircraftCarrier extends Ship implements HasPort {
    private AbstractPort airport = new Airport();

    @Override
    public AbstractPort getPort() {
        return airport;
    }
}
