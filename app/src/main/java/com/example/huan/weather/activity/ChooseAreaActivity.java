package com.example.huan.weather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.huan.weather.R;
import com.example.huan.weather.model.City;
import com.example.huan.weather.model.County;
import com.example.huan.weather.model.Province;
import com.example.huan.weather.model.WeatherDB;
import com.example.huan.weather.util.HttpCallbackListener;
import com.example.huan.weather.util.HttpUtil;
import com.example.huan.weather.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huan on 2016/8/18.
 */
public class ChooseAreaActivity extends Activity {
    public static final int LEVEL_PROVINCE=0;
    public static final int LEVEL_CITY=1;
    public static final int LEVEL_COUNTY=2;

    private ProgressDialog mProgressDialog;
    private TextView mTextView;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private WeatherDB mWeatherDB;
    private List<String> dataList = new ArrayList<>();
    private List<Province> mProvinceList;
    private List<City> mCityList;
    private List<County> mCountyList;
    private Province selectedProvince;
    private City selectedCity;
    private int currentLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        mListView = (ListView) findViewById(R.id.list_view);
        mTextView = (TextView) findViewById(R.id.title_text);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
        mListView.setAdapter(mAdapter);
        mWeatherDB = WeatherDB.getInstance(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (currentLevel==LEVEL_PROVINCE) {
                    selectedProvince = mProvinceList.get(i);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = mCityList.get(i);
                    queryCounties();
                }
            }
        });
        queryProvinces();
    }

    private void queryProvinces() {
        mProvinceList=mWeatherDB.loadProvinces();
        if (mProvinceList.size()>0) {
            dataList.clear();
            for (Province province : mProvinceList) {
                dataList.add(province.getProvinceName());

            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            mTextView.setText("中国");
            currentLevel=LEVEL_PROVINCE;
        }else{
            queryFromServer(null, "province");
        }
    }
    private void queryCities() {
        mCityList=mWeatherDB.loadCities(selectedProvince.getId());
        if (mCityList.size()>0) {
            dataList.clear();
            for (City city : mCityList) {

                dataList.add(city.getCityName());
            }


            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            mTextView.setText(selectedProvince.getProvinceName());
            currentLevel=LEVEL_CITY;
        }else{
            queryFromServer(selectedProvince.getProvinceCode(), "city");
        }
    }



    private void queryCounties() {
        mCountyList = mWeatherDB.loadCounties(selectedCity.getId());
        if (mCountyList.size()>0) {
            dataList.clear();
            for (County county : mCountyList) {
                dataList.add(county.getCountyName());

            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            mTextView.setText(selectedCity.getCityName());
            currentLevel=LEVEL_COUNTY;
        }else{
            queryFromServer(selectedCity.getCityCode(),"county");
        }
    }

    private void queryFromServer(final String code, final String type) {
        String address;
        if (!TextUtils.isEmpty(code)) {
            address = "http://wwww.weather.com.cn/data/list3/city" + code + ".xml";
        }else{
            address = "http://wwww.weather.com.cn/data/list3/city.xml";
        }
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result=false;
                if ("province".equals(type)) {
                    result = Utility.handleProvincesResponse(mWeatherDB, response);
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void showProgressDialog() {
    }

}
