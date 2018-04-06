package pvtitov.timetable;

import android.content.AsyncTaskLoader;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import pvtitov.timetable.contracts.City;
import pvtitov.timetable.contracts.Point;
import pvtitov.timetable.contracts.Station;

public class ParseJson extends AsyncTaskLoader<List<City>>{

    public static final String BUNDLE_KEY = "loader_arg_key";

    private WeakReference<Context> mContextWeakReference;
    private String mToOrFrom;

    ParseJson(Context context, PickCities toOrFrom) {
        super(context);
        mContextWeakReference = new WeakReference<>(context);
        mToOrFrom = toOrFrom.getValue();
    }

    @Override
    public List<City> loadInBackground() {
        return parseCities();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    public enum PickCities{
        TO("citiesTo"), FROM("citiesFrom");

        public String value;

        PickCities(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }




    private List<City> parseCities() {
        try {
            JSONObject root = new JSONObject(
                    readFromFile(mContextWeakReference.get(), R.raw.all_stations)
            );
            return parseStations(root.getJSONArray(mToOrFrom));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
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


    private List<City> parseStations(JSONArray citiesJSONArray) throws JSONException {

        List<City> cities = new ArrayList<>();
        List<Station> stations;
        City city;

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
            cities.add(city);
        }

        return cities;
    }
}