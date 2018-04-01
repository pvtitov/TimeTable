package pvtitov.timetable.database;

import android.provider.BaseColumns;

/**
 * Created by pavel on 01.04.18.
 */

public final class StationsContract {

    private StationsContract(){}

    public static class Entry implements BaseColumns{
        public static final String TABLE_CITIES_TO = "citiesTo";
        public static final String TABLE_CITIES_FROM = "citiesFrom";

        public static final String COLUMN_CITY_COUNTRY = "cityCountry";
        public static final String COLUMN_CITY_DISTRICT = "cityDistrict";
        public static final String COLUMN_CITY_ID = "cityId";
        public static final String COLUMN_CITY_TITLE = "cityTitle";
        public static final String COLUMN_CITY_REGION = "cityRegion";
        public static final String COLUMN_CITY_LATITUDE = "cityLatitude";
        public static final String COLUMN_CITY_LONGITUDE = "cityLongitude";

        public static final String COLUMN_STATION_COUNTRY = "stationCountry";
        public static final String COLUMN_STATION_DISTRICT = "stationDistrict";
        public static final String COLUMN_STATION_CITY_ID = "stationCityId";
        public static final String COLUMN_STATION_CITY_TITLE = "stationCityTitle";
        public static final String COLUMN_STATION_REGION = "stationRegion";
        public static final String COLUMN_STATION_LATITUDE = "stationLatitude";
        public static final String COLUMN_STATION_LONGITUDE = "stationLongitude";
        public static final String COLUMN_STATION_ID = "stationId";
        public static final String COLUMN_STATION_TITLE = "stationTitle";
    }
}
