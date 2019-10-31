package model;

import java.util.Objects;

/**
 * Describes an event in the database
 */
public class Locations {

    public Locations(Location[] data) {
        this.data = data;
    }

    Location[] data;

    public Location[] getData() {
        return data;
    }

    public void setData(Location[] data) {
        this.data = data;
    }
}
