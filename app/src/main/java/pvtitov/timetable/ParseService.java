package pvtitov.timetable;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import pvtitov.timetable.contracts.City;
import pvtitov.timetable.contracts.Point;
import pvtitov.timetable.contracts.Station;
import pvtitov.timetable.database.DatabaseHelper;
import pvtitov.timetable.database.StationsContract;
import pvtitov.timetable.database.ToOrFrom;

public class ParseService extends IntentService {

    public static final String HASH_CODE_KEY = "hash_code_key";
    public static final String ACTION_PARSE_SERVICE = "pvtitov.timetable.PARSING_DONE";

    public ParseService() { super("PARSE_SERVICE"); }

    private DatabaseHelper dbHelper;
    private String mFileAsString;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.d("debugging", "service started");

        mFileAsString = readFromFile(this, R.raw.all_stations);

        if (!hasSameHashCode(mFileAsString)) {
            Log.d("debugging", "service proceeded parsing");
            dbHelper = new DatabaseHelper(this);
            parseCities(ToOrFrom.TO);
            parseCities(ToOrFrom.FROM);


            Intent broadcast = new Intent();
            broadcast.setAction(ACTION_PARSE_SERVICE);
            broadcast.addCategory(Intent.CATEGORY_DEFAULT);
            sendBroadcast(broadcast);
        }
    }

    @SuppressLint("ApplySharedPref")
    private boolean hasSameHashCode(String inputString) {

        SharedPreferences sharedPreferences =
                this.getSharedPreferences(this.getString(R.string.shared_prefs), Context.MODE_PRIVATE);


        if(inputString.hashCode() == sharedPreferences.getInt(HASH_CODE_KEY, 0)) {
            return true;
        }
        else {
            sharedPreferences.edit().putInt(HASH_CODE_KEY, inputString.hashCode()).commit();
            return false;
        }
    }

    private void parseCities(ToOrFrom toOrFrom) {
        try {
            JSONObject root = new JSONObject(mFileAsString);
            parseStations(root.getJSONArray(toOrFrom.getDirection()), toOrFrom);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String readFromFile(Context context, int resource_id) {
        InputStream inputStream = context.getResources().openRawResource(resource_id);
        Scanner scanner = new Scanner(inputStream);
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            stringBuilder.append(scanner.nextLine());
        }
        return stringBuilder.toString();
    }


    private void parseStations(JSONArray citiesJSONArray, ToOrFrom toOrFrom) throws JSONException {

        List<Station> stations;
        City city;

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(toOrFrom.getTable(), null, null);

        for (int i = 0; i < citiesJSONArray.length(); i++){
            JSONObject cityJSONObject = citiesJSONArray.getJSONObject(i);

            city = new City();
            city.setCountry(cityJSONObject.getString("countryTitle"));
            city.setDistrict(cityJSONObject.getString("districtTitle"));
            city.setCityId(cityJSONObject.getInt("cityId"));
            city.setCity(cityJSONObject.getString("cityTitle"));
            city.setRegion(cityJSONObject.getString("regionTitle"));


            JSONObject cityPointJSONObject = cityJSONObject.getJSONObject("point");
            Point pointOfCity = new Point();
            pointOfCity.setLatitude(cityPointJSONObject.getDouble("latitude"));
            pointOfCity.setLongitude(cityPointJSONObject.getDouble("longitude"));
            city.setPoint(pointOfCity);


            JSONArray stationsJSONArray = cityJSONObject.getJSONArray("stations");
            stations = new ArrayList<>();

            for (int j = 0; j < stationsJSONArray.length(); j++) {
                JSONObject stationJSONObject = stationsJSONArray.getJSONObject(j);

                Station station = new Station();
                station.setCountry(stationJSONObject.getString("countryTitle"));
                station.setDistrict(stationJSONObject.getString("districtTitle"));
                station.setCityId(stationJSONObject.getInt("cityId"));
                station.setCity(stationJSONObject.getString("cityTitle"));
                station.setRegion(stationJSONObject.getString("regionTitle"));
                station.setStation(stationJSONObject.getString("stationTitle"));
                station.setStationId(stationJSONObject.getInt("stationId"));

                JSONObject pointJSONObject = stationJSONObject.getJSONObject("point");
                Point pointOfStation = new Point();
                pointOfStation.setLatitude(pointJSONObject.getDouble("latitude"));
                pointOfStation.setLongitude(pointJSONObject.getDouble("longitude"));
                station.setPoint(pointOfStation);

                stations.add(station);
            }
            city.setStations(stations);

            writeToDatabase(database, city, toOrFrom);
        }
    }

    private void writeToDatabase(SQLiteDatabase database, City city, ToOrFrom toOrFrom) {
        ContentValues values = new ContentValues();
        values.put(StationsContract.Entry.COLUMN_CITY_COUNTRY, city.getCountry());
        values.put(StationsContract.Entry.COLUMN_CITY_DISTRICT, city.getDistrict());
        values.put(StationsContract.Entry.COLUMN_CITY_ID, city.getCityId());
        values.put(StationsContract.Entry.COLUMN_CITY_TITLE, city.getCity());
        values.put(StationsContract.Entry.COLUMN_CITY_REGION, city.getRegion());
        values.put(StationsContract.Entry.COLUMN_CITY_LATITUDE, city.getPoint().getLatitude());
        values.put(StationsContract.Entry.COLUMN_CITY_LONGITUDE, city.getPoint().getLongitude());

        for(Station station: city.getStations()){
            values.put(StationsContract.Entry.COLUMN_STATION_COUNTRY, station.getCountry());
            values.put(StationsContract.Entry.COLUMN_STATION_DISTRICT, station.getDistrict());
            values.put(StationsContract.Entry.COLUMN_STATION_CITY_ID, station.getCityId());
            values.put(StationsContract.Entry.COLUMN_STATION_CITY_TITLE, station.getCity());
            values.put(StationsContract.Entry.COLUMN_STATION_REGION, station.getRegion());
            values.put(StationsContract.Entry.COLUMN_STATION_LATITUDE, station.getPoint().getLatitude());
            values.put(StationsContract.Entry.COLUMN_STATION_LONGITUDE, station.getPoint().getLongitude());
            values.put(StationsContract.Entry.COLUMN_STATION_ID, station.getStationId());
            values.put(StationsContract.Entry.COLUMN_STATION_TITLE, station.getStation());
        }

        database.insert(toOrFrom.getTable(), null, values);
    }
}
