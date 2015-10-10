package com.mewa;

import com.mewa.data.ports.AbstractPort;
import com.mewa.data.ports.Airport;
import com.mewa.data.ports.Port;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.data.vehicles.planes.Plane;
import com.mewa.data.vehicles.ships.AircraftCarrier;
import com.mewa.data.vehicles.ships.Ship;
import com.mewa.utils.SerialClock;
import com.mewa.utils.StandardOutput;
import com.mewa.utils.i.Logger;
import com.mewa.utils.loggers.DebugLogger;

public class Main {

    public static Logger logger = new DebugLogger(new StandardOutput(), new SerialClock());

    public static void main(String[] args) {
        logger.setLogLevel(Logger.VERBOSE);

        AbstractPort port;
        port = new Airport();
        Vehicle vehicle;
        vehicle = new AircraftCarrier();
        vehicle.arrive(port);
        new Plane().arrive(new AircraftCarrier());
    }
}
