package pvtitov.timetable.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Павел on 21.10.2017.
 */

public class Country {
    @SerializedName("countryTitle")
    private String country;

    private List<City> citiesTo;
    private List<City> citiesFrom;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<City> getCitiesTo() {
        return citiesTo;
    }

    public void setCitiesTo(List<City> citiesTo) {
        this.citiesTo = citiesTo;
    }

    public List<City> getCitiesFrom() {
        return citiesFrom;
    }

    public void setCitiesFrom(List<City> citiesFrom) {
        this.citiesFrom = citiesFrom;
    }
}
