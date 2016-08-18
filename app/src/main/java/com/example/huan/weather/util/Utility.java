package com.example.huan.weather.util;

import android.text.TextUtils;

import com.example.huan.weather.model.City;
import com.example.huan.weather.model.County;
import com.example.huan.weather.model.Province;
import com.example.huan.weather.model.WeatherDB;

/**
 * Created by huan on 2016/8/18.
 */
public class Utility {
    public synchronized static boolean handleProvincesResponse(WeatherDB weatherDB, String response) {
        if (!TextUtils.isEmpty(response)) {
            String[] allprovinces = response.split(",");
            if (allprovinces!=null&&allprovinces.length>0) {
                for (String p : allprovinces) {
                    String[] array = p.split("\\|");
                    Province province=new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    weatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }
    public synchronized static boolean handleCitiesResponse(WeatherDB weatherDB, String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCities = response.split(",");
            if (allCities!=null&&allCities.length>0) {
                for (String c : allCities) {
                    String[] array = c.split("\\|");
                    City city=new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    weatherDB.saveCity(city);
                }
                return true;
            }

        }
        return false;
    }
    public synchronized static boolean handleCountiesResponse(WeatherDB weatherDB, String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCounties= response.split(",");
            if (allCounties!=null&&allCounties.length>0) {
                for (String c : allCounties) {
                    String[] array = c.split("\\|");
                    County county=new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    weatherDB.saveCounty(county);
                }
                return true;
            }

        }
        return false;
    }


}
