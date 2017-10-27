package pvtitov.timetable;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import pvtitov.timetable.model.City;
import pvtitov.timetable.model.Model;

public class ParseJson extends AsyncTask<Void, Void, Model> {

    Context mContext;

    public ParseJson(Context context){
        mContext = context;
    }

    @Override
    protected Model doInBackground(Void... voids) {
        // Чтение из файла. Преобразование всего файла в String.
        InputStream inputStream = mContext.getResources().openRawResource(R.raw.all_stations);
        Scanner scanner = new Scanner(inputStream);
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            stringBuilder.append(scanner.nextLine());
        }
        String jsonFileAsString = stringBuilder.toString();

        // Разбор JSON и запись в объекты (десериализация)
        Model model = new Model();
        try {
            JSONObject root = new JSONObject(jsonFileAsString);
            model.setCitiesFrom(parseJSONArray(root.getJSONArray("citiesFrom")));
            model.setCitiesTo(parseJSONArray(root.getJSONArray("citiesTo")));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("happy", e.getStackTrace().toString());
        }
        return model;
    }



    @Override
    protected void onPostExecute(Model model) {
        super.onPostExecute(model);
        Toast.makeText(mContext, model.getCityFrom(10).getCity(), Toast.LENGTH_SHORT).show();
    }

    private List<City> parseJSONArray(JSONArray citiesJSONArray) throws JSONException {

        List<City> cities = new ArrayList<>();

        for (int i = 0; i < citiesJSONArray.length(); i++){
            JSONObject cityJSONObject = citiesJSONArray.getJSONObject(i);

            City city = new City();
            city.setCountry(cityJSONObject.getString("countryTitle"));
            city.setDistrict(cityJSONObject.getString("districtTitle"));
            city.setCityId(cityJSONObject.getInt("cityId"));
            city.setCity(cityJSONObject.getString("cityTitle"));
            city.setRegion(cityJSONObject.getString("regionTitle"));

                /*
                JSONObject cityPointJSONObject = cityJSONObject.getJSONObject("point");
                Point pointOfCity = new Point();
                pointOfCity.setLatitude(cityPointJSONObject.getDouble("latitude"));
                pointOfCity.setLongitude(cityPointJSONObject.getDouble("longitude"));
                city.setPoint(pointOfCity);


                JSONArray stationsJSONArray = cityJSONObject.getJSONArray("stations");
                List<Station> stations = new ArrayList<>();

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
                */
            cities.add(city);
        }


        return cities;
    }
}