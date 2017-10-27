package pvtitov.timetable.model;

import java.util.List;

/**
 * Created by Павел on 22.10.2017.
 */

public class Model {
    private List<City> citiesFrom;
    private List<City> citiesTo;


    public List<City> getCitiesFrom() {
        return citiesFrom;
    }

    public City getCityFrom(int i) {
        return citiesFrom.get(i);
    }

    public void setCitiesFrom(List<City> citiesFrom) {
        this.citiesFrom = citiesFrom;
    }

    public List<City> getCitiesTo() {
        return citiesTo;
    }

    public City getCityTo(int i) {
        return citiesTo.get(i);
    }

    public void setCitiesTo(List<City> citiesTo) {
        this.citiesTo = citiesTo;
    }
}
