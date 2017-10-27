package pvtitov.timetable.model;


import java.util.List;

/**
 * Created by Павел on 21.10.2017.
 */

public class City {
    private String country;
    private String city;
    private Point point;
    private String district;
    private int cityId;
    private String region;
    private List<Station> stations;


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Station getStation(int i) {
        return stations.get(i);
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
