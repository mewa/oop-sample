package com.mewa.data.passengers;

import com.mewa.Main;
import com.mewa.data.Localizable;
import com.mewa.data.location.Location;
import com.mewa.data.ports.AbstractPort;
import com.mewa.data.ports.CivilAirport;
import com.mewa.data.type.Civil;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.utils.i.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

/**
 * Created by Mewa on 2015-10-10.
 */
public class Passenger {
    private static List<String> kNames = new ArrayList<String>() {{
        add("Arya");
        add("Stannis");
        add("Cersei");
        add("Joffrey");
        add("Tyrion");
        add("Eddard");
        add("Sansa");
        add("Renley");
        add("Lysa");
        add("Petyr");
        add("John");
        add("Tommen");
    }};

    private static List<String> kSurnames = new ArrayList<String>() {{
        add("Stark");
        add("Lannister");
        add("Baratheon");
        add("Baelish");
        add("Snow");
        add("Sand");
        add("Targaryen");
        add("Bolton");
    }};

    private String mFirstName;
    private String mLastName;
    private String mId;
    private int mAge = (int) ((0.1 * Math.random()) * 30 + Math.random() * 80);
    private AbstractPort mHomeLocation;
    private Trip mTrip;
    private Localizable mLocation;
    private boolean isSleeping = false;

    public Passenger(AbstractPort home) {
        mFirstName = kNames.get((int) (Math.random() * (kNames.size()) - 1));
        mLastName = kSurnames.get((int) (Math.random() * (kSurnames.size()) - 1));
        setHomeLocation(home);
        mId = randomId();
        mLocation = mHomeLocation;
        new Thread(this.toString()) {
            @Override
            public void run() {
                while (zyj()) {
                    pij();
                }
            }
        }.start();
    }

    private void pij() {
        //Main.logger.log(Logger.VERBOSE, "\tbo pije");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void spij() {
        long time = 1000;
        if (mTrip != null) {
            if (mTrip.getType() == Trip.Type.LEISURE) {
                time = (long) (500 + Math.random() * 2000);
            } else if (mTrip.getType() == Trip.Type.WORK) {
                time = (long) (2000 + Math.random() * 500);
            }
            try {
                mTrip = null;
                isSleeping = true;
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isSleeping = false;
            mTrip = new Trip(getHomeLocation(), Math.random() > 0.5 ? Trip.Type.LEISURE : Trip.Type.WORK);
        }
    }

    private boolean zyj() {
        //Main.logger.log(Logger.VERBOSE, this + " zyje ");
        if (mLocation == mHomeLocation) {
            if (mTrip.isFinished())
                spij();
        }
        return true;
    }

    public boolean isSleeping() {
        return isSleeping;
    }

    private String randomId() {
        if (mId == null) {
            IntStream ints = new Random().ints(11, 0, 9);
            mId = "";
            ints.iterator().forEachRemaining(new IntConsumer() {
                @Override
                public void accept(int value) {
                    mId += value;
                }
            });
        }
        return mId;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public synchronized void setFirstName(String firstName) {
        this.mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public synchronized void setLastName(String lastName) {
        this.mLastName = lastName;
    }

    public String getId() {
        return mId;
    }

    public synchronized void setId(String id) {
        this.mId = id;
    }

    public int getAge() {
        return mAge;
    }

    public synchronized void setAge(int age) {
        this.mAge = age;
    }

    public AbstractPort getHomeLocation() {
        return mHomeLocation;
    }

    public void setHomeLocation(AbstractPort homeLocation) {
        this.mHomeLocation = homeLocation;
        mTrip = new Trip(getHomeLocation(), Math.random() > 0.5 ? Trip.Type.LEISURE : Trip.Type.WORK);
    }

    public Trip getTrip() {
        return mTrip;
    }

    @Override
    public String toString() {
        return String.format("%s %s, lat %d, PIESEŁ[%s], zamieszkały w %s", getFirstName(), getLastName(), getAge(), getId(), getHomeLocation());
    }

    public synchronized void setLocation(AbstractPort location) {
        this.mLocation = location;
    }
}
