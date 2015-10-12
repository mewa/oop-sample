package com.mewa;

import com.mewa.data.location.World;
import com.mewa.data.ports.*;
import com.mewa.data.vehicles.planes.PassengerPlane;
import com.mewa.data.vehicles.ships.AircraftCarrier;
import com.mewa.utils.NanoClock;
import com.mewa.utils.SerialClock;
import com.mewa.utils.StandardOutput;
import com.mewa.utils.i.Logger;
import com.mewa.utils.loggers.DebugLogger;

public class Main {

    public static Logger logger = new DebugLogger(new StandardOutput(), new SerialClock());

    public static void main(String[] args) {
        logger.setLogLevel(Logger.VERBOSE);

        World.getInstance();
    }
}
