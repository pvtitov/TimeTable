package pvtitov.timetable.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Павел on 21.10.2017.
 */

public class Station {

    @SerializedName("stationTitle")
    private String station;

    @SerializedName("stationId")
    private int id;


    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
