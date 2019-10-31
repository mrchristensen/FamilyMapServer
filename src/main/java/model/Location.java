package model;

import java.util.Objects;

/**
 * Describes an event in the database
 */
public class Location {

    public Location(float latitude, float longitude, String country, String city) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
    }

    /**
     * Latitude of event’s location
     */
    float latitude;

    /**
     * Longitude of event’s location
     */
    float longitude;

    /**
     * Country in which event occurred
     */
    String country;

    /**
     * City in which event occurred
     */
    String city;

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location event = (Location) o;
        return Float.compare(event.latitude, latitude) == 0 &&
                Float.compare(event.longitude, longitude) == 0 &&

                country.equals(event.country) &&
                city.equals(event.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude, country, city);
    }
}
