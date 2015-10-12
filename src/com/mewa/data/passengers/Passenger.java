package com.mewa.data.passengers;

import com.mewa.data.location.Location;
import com.mewa.data.type.Civil;
import com.mewa.data.vehicles.Vehicle;

/**
 * Created by Mewa on 2015-10-10.
 */
public class Passenger {
    private String mFirstName;
    private String mLastName;
    private String mId;
    private int mAge;
    private Location mHomeLocation;

    public synchronized <T extends Vehicle & Civil> boolean enter(T vehicle) {
        return vehicle.board(this);
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

    public Location getHomeLocation() {
        return mHomeLocation;
    }

    public void setHomeLocation(Location homeLocation) {
        this.mHomeLocation = homeLocation;
    }
}
