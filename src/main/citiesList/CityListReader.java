package main.citiesList;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import main.model.City;

/**
 * Created by Katarzyna Wilkosz on $[DATE].
 */
public class CityListReader {
    private List<City> cityList;

    public CityListReader() throws Exception {
        loadJsonFile();
    }

    private void loadJsonFile() throws Exception {

        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("main\\citiesList\\city.list.min.json");

            JsonArray jsonArray = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonArray();

            Gson gson = new Gson();
            Type listType = new TypeToken<List<City>>() {
            }.getType();
            List<City> cityList = gson.fromJson(jsonArray, listType);

            this.cityList = cityList;

        } catch (Exception ex) {
            throw new Exception();
        }
    }

    public List<City> getCityList() {
        return cityList;
    }
}
