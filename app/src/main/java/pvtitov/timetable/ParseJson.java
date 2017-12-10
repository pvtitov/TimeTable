package pvtitov.timetable;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import pvtitov.timetable.model.City;
import pvtitov.timetable.model.Model;
import pvtitov.timetable.model.Point;
import pvtitov.timetable.model.Station;

class ParseJson extends AsyncTask<Void, Void, Model> {

    private static OnParseListener mListener;

    private Context mContext;
    private Model mModel;

    ParseJson(Context context,
              Model model) {
        mContext = context;
        mModel = model;
    }

    interface OnParseListener{
        void onParseComplete();
    }

    static void setOnParseListener(OnParseListener listener) {
        mListener = listener;
    }


    @Override
    protected Model doInBackground(Void... voids) {

        /*
        Чтение из файла. Преобразование всего файла в String.
        */
        InputStream inputStream = mContext.getResources().openRawResource(R.raw.all_stations);
        Scanner scanner = new Scanner(inputStream);
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            stringBuilder.append(scanner.nextLine());
        }
        String jsonFileAsString = stringBuilder.toString();

        /*
        Разбор JSON и запись в объекты (десериализация).
        Повторяющийся код для станций отправления и прибытия
        вынесен в метод parseArrayOfCities().
        */
        try {
            JSONObject root = new JSONObject(jsonFileAsString);
            mModel.setCitiesFrom(parseArrayOfCities(root.getJSONArray("citiesFrom")));
            mModel.setCitiesTo(parseArrayOfCities(root.getJSONArray("citiesTo")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mModel;
    }



    @Override
    protected void onPostExecute(Model model) {
        mModel = model;
        if (mListener != null) mListener.onParseComplete();
    }



    private List<City> parseArrayOfCities(JSONArray citiesJSONArray) throws JSONException {

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