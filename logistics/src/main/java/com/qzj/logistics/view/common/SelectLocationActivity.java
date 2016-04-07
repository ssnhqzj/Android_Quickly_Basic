package com.qzj.logistics.view.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.qzj.logistics.R;
import com.qzj.logistics.base.BaseActivity;
import com.qzj.logistics.bean.HatArea;
import com.qzj.logistics.bean.HatCity;
import com.qzj.logistics.bean.HatProvince;
import com.qzj.logistics.bean.Toolbar;
import com.qzj.logistics.db.dao.HatAreaDao;
import com.qzj.logistics.db.dao.HatCityDao;
import com.qzj.logistics.db.dao.HatProvinceDao;

import java.util.List;

public class SelectLocationActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    // 返回码
    public static final int RESULT_CODE = 1;
    private static final int TYPE_PROVINCE = 1;
    private static final int TYPE_CITY = 2;
    private static final int TYPE_AREA = 3;

    private Toolbar toolbar;
    private ListView listView;
    private LocationAdapter adapter;
    private String address = "";
    private String provinceName = "";
    private String cityName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        toolbar = new Toolbar();
        toolbar.setBg(R.color.common_main);
        toolbar.setCenterText("选择省份");
        toolbar.setLeftResId(R.mipmap.return_up);
        initToolBar(toolbar);

        initView();
        findProvince();
    }

    private void initView(){
        listView = (ListView) findViewById(R.id.location_listview);
        adapter = new LocationAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_left:
                returnUp();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (adapter.getType()){
            case TYPE_PROVINCE:
                setTbCenterView("选择城市");
                HatProvince hatProvince = adapter.getProvinceList().get(position);
                String provinceId = hatProvince.getProvinceid();
                provinceName = hatProvince.getProvince();
                address += provinceName;
                findCity(provinceId);
                break;
            case TYPE_CITY:
                setTbCenterView("选择城区");
                HatCity hatCity = adapter.getCityList().get(position);
                String cityId = hatCity.getCityid();
                cityName = hatCity.getCity();
                address += cityName;
                findArea(cityId);
                break;
            case TYPE_AREA:
                HatArea hatArea = adapter.getAreaList().get(position);
                address += hatArea.getArea();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("address",address);
                returnIntent.putExtra("area_id",hatArea.getAreaid());
                returnIntent.putExtra("area_name",hatArea.getArea());
                returnIntent.putExtra("area_father",hatArea.getHatCity().getCityid());
                returnIntent.putExtra("city_name",cityName);
                returnIntent.putExtra("province_name",provinceName);
                setResult(RESULT_CODE, returnIntent);
                finish();
                break;
        }
    }

    private void findProvince(){
        adapter.setProvinceList(new HatProvinceDao(this).getHatProvinceForAll());
        adapter.setType(TYPE_PROVINCE);
        adapter.notifyDataSetChanged();
    }

    private void findCity(String provinceId){
        adapter.setCityList(new HatCityDao(this).listByHatProvinceId(provinceId));
        adapter.setType(TYPE_CITY);
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
    }

    private void findArea(String cityId){
        adapter.setAreaList(new HatAreaDao(this).listByHatCityId(cityId));
        adapter.setType(TYPE_AREA);
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
    }

    /**
     * 返回上一级
     */
    private void returnUp(){
        if (adapter.getType() == TYPE_AREA){
            setTbCenterView("选择城市");
            adapter.setType(TYPE_CITY);
            adapter.notifyDataSetChanged();
        }else if(adapter.getType() == TYPE_CITY){
            setTbCenterView("选择省份");
            adapter.setType(TYPE_PROVINCE);
            adapter.notifyDataSetChanged();
        }else{
            this.finish();
        }
    }

    /**
     * 点击物理返回键
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            returnUp();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class LocationAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private List<HatProvince> provinceList;
        private List<HatCity> cityList;
        private List<HatArea> areaList;
        private int type;

        public LocationAdapter(){
            inflater = LayoutInflater.from(SelectLocationActivity.this);
        }

        @Override
        public int getCount() {
            switch (type){
                case TYPE_PROVINCE:
                    return provinceList==null?0:provinceList.size();
                case TYPE_CITY:
                    return cityList==null?0:cityList.size();
                case TYPE_AREA:
                    return areaList==null?0:areaList.size();
            }

            return 0;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HolderView holder = null;
            if (convertView == null){
                holder = new HolderView();
                convertView = inflater.inflate(R.layout.select_location_item,null);
                holder.text = (TextView) convertView.findViewById(R.id.select_location_item_text);

                convertView.setTag(holder);
            }else{
                holder = (HolderView) convertView.getTag();
            }

            switch (type){
                case TYPE_PROVINCE:
                    if (provinceList != null){
                        if (provinceList.get(position) != null && !"".equals(provinceList.get(position)))
                            holder.text.setText(provinceList.get(position).getProvince());
                    }
                    break;
                case TYPE_CITY:
                    if (cityList != null){
                        if (cityList.get(position) != null && !"".equals(cityList.get(position)))
                            holder.text.setText(cityList.get(position).getCity());
                    }
                    break;
                case TYPE_AREA:
                    if (areaList != null){
                        if (areaList.get(position) != null && !"".equals(areaList.get(position)))
                            holder.text.setText(areaList.get(position).getArea());
                    }
                    break;
            }

            return convertView;
        }

        public void setProvinceList(List<HatProvince> provinceList) {
            this.provinceList = provinceList;
        }

        public void setCityList(List<HatCity> cityList) {
            this.cityList = cityList;
        }

        public void setAreaList(List<HatArea> areaList) {
            this.areaList = areaList;
        }

        public List<HatProvince> getProvinceList() {
            return provinceList;
        }

        public List<HatCity> getCityList() {
            return cityList;
        }

        public List<HatArea> getAreaList() {
            return areaList;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        class HolderView {
            public TextView text;
        }
    }

}
