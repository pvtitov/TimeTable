package pvtitov.timetable;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import pvtitov.timetable.model.City;
import pvtitov.timetable.model.Model;
import pvtitov.timetable.model.Point;
import pvtitov.timetable.model.Station;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ParseJson parseJson = new ParseJson();
        parseJson.execute();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_timetable:
                Toast.makeText(this, "Расписание", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_info:
                Toast.makeText(this, "Инфо", Toast.LENGTH_SHORT).show();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private String convertJsonToString(){
        String string;
        try {
            InputStream is = getResources().openRawResource(R.raw.all_stations);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            string = new String(buffer, "UTF-8");
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return string;
    }


    private class ParseJson extends AsyncTask<Void, Void, Model> {

        private Model mModel;

        @Override
        protected Model doInBackground(Void... voids) {
            // Чтение из файла. Преобразование всего файла в String.
            InputStream inputStream = getResources().openRawResource(R.raw.all_stations);
            Scanner scanner = new Scanner(inputStream);
            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine());
            }
            String jsonFileAsString = stringBuilder.toString();

            // Разбор JSON и запись в объекты (десериализация)
            try {
                JSONObject root = new JSONObject(jsonFileAsString);
                JSONArray citiesFromJSONArray = root.getJSONArray("citiesFrom");
                List<JSONObject> citiesFromJSONObjects = new ArrayList<>();
                List<City> citiesFrom = new ArrayList<>();

                for (int i = 0; i < citiesFromJSONArray.length(); i++){
                    citiesFromJSONObjects.add(citiesFromJSONArray.getJSONObject(i));
                    JSONObject cityJSONObject = citiesFromJSONObjects.get(i);

                    City city = new City();
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
                    List<Station> stations = new ArrayList<>();

                    for (int j = 0; i < stationsJSONArray.length(); i++) {
                        citiesFromJSONObjects.add(citiesFromJSONArray.getJSONObject(i));
                        JSONObject stationJSONObject = citiesFromJSONObjects.get(i);

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
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mModel;
        }



        @Override
        protected void onPostExecute(Model model) {
            super.onPostExecute(model);
        }
    }
}
