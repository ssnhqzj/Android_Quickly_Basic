package com.qzj.logistics.db.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.qzj.logistics.bean.HatCity;
import com.qzj.logistics.bean.HatProvince;
import com.qzj.logistics.db.DatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/25.
 */
public class HatCityDao {
    public static final String TABLE_NAME = "hat_city";

    private Context context;
    private Dao<HatCity,Integer> hatCityDao;
    private DatabaseHelper helper;

    public HatCityDao(Context context) {
        this.context = context;
        helper = DatabaseHelper.getHelper(context);
        hatCityDao = helper.getDao(HatCity.class);
    }

    /**
     * 清空表中数据
     * @return 返回删除数据条数，异常返回-1
     */
    public int clearTable(){
        int result;
        try {
            result = hatCityDao.executeRaw("delete from " + TABLE_NAME);
        } catch (SQLException e) {
            e.printStackTrace();
            result = -1;
        }
        return result;
    }

    /**
     * 添加一个城市
     * @param hatCity
     */
    public void add(HatCity hatCity) {
        try {
            hatCityDao.create(hatCity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一个城市列表
     * @param list
     */
    public void addAll(ArrayList<HatCity> list) {
        try {
            for (HatCity hc : list){
                HatProvince hatProvince = new HatProvince();
                hatProvince.setProvinceid(hc.getFather());
                hc.setHatProvince(hatProvince);
                hatCityDao.create(hc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据id查询城市及其省份
     * @param id
     * @return
     */
    public HatCity getHatCityWithHatProvince(int id)
    {
        HatCity hatCity = null;
        try {
            hatCity = hatCityDao.queryForId(id);
            helper.getDao(HatProvince.class).refresh(hatCity.getHatProvince());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hatCity;
    }

    /**
     * 根据id查询城市
     * @param id
     * @return
     */
    public HatCity get(int id)
    {
        HatCity hatCity = null;
        try {
            hatCity = hatCityDao.queryForId(id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hatCity;
    }

    /**
     * 根据省份id查询城市列表
     * @param provinceId
     * @return
     */
    public List<HatCity> listByHatProvinceId(String provinceId)
    {
        try {
            return hatCityDao.queryBuilder().where().eq("province_id", provinceId).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
