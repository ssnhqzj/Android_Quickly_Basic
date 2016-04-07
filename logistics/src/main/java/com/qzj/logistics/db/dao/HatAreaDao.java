package com.qzj.logistics.db.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.qzj.logistics.bean.HatArea;
import com.qzj.logistics.bean.HatCity;
import com.qzj.logistics.db.DatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/25.
 */
public class HatAreaDao {
    public static final String TABLE_NAME = "hat_area";

    private Context context;
    private Dao<HatArea,Integer> hatAreaDao;
    private DatabaseHelper helper;

    public HatAreaDao(Context context) {
        this.context = context;
        helper = DatabaseHelper.getHelper(context);
        hatAreaDao = helper.getDao(HatArea.class);
    }

    /**
     * 清空表中数据
     * @return 返回删除数据条数，异常返回-1
     */
    public int clearTable(){
        int result;
        try {
            result = hatAreaDao.executeRaw("delete from " + TABLE_NAME);
        } catch (SQLException e) {
            e.printStackTrace();
            result = -1;
        }
        return result;
    }

    /**
     * 添加一个城区
     * @param hatArea
     */
    public void add(HatArea hatArea) {
        try {
            hatAreaDao.create(hatArea);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一个城区列表
     * @param list
     */
    public void addAll(ArrayList<HatArea> list) {
        try {
            for (HatArea ha : list){
                HatCity hatCity = new HatCity();
                hatCity.setCityid(ha.getFather());
                ha.setHatCity(hatCity);
                hatAreaDao.create(ha);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据id查询城区及其对应城市
     * @param id
     * @return
     */
    public HatArea getHatAreaWithHatCity(int id) {
        HatArea hatArea = null;
        try {
            hatArea = hatAreaDao.queryForId(id);
            helper.getDao(HatCity.class).refresh(hatArea.getHatCity());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hatArea;
    }

    /**
     * 根据id查询城区
     * @param id
     * @return
     */
    public HatArea get(int id) {
        HatArea hatArea = null;
        try {
            hatArea = hatAreaDao.queryForId(id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hatArea;
    }

    /**
     * 根据城市id查询城区列表
     * @param cityId
     * @return
     */
    public List<HatArea> listByHatCityId(String cityId) {
        try {
            return hatAreaDao.queryBuilder().where().eq("city_id", cityId).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据城区id查询对应的省市区名称字符串
     * @param areaId
     * @return
     */
    public String queryJoinAddr(String areaId){
        if (areaId == null) return null;
        String result = "";
        try {
            GenericRawResults<String[]> rawResults = hatAreaDao.queryRaw(
                    "SELECT p.province,c.city,a.area FROM hat_area a " +
                            "JOIN hat_city c on a.city_id=c.cityid " +
                            "JOIN hat_province p on c.province_id=p.provinceid " +
                            "WHERE a.areaid=" + areaId);
            if (rawResults != null){
                String[] arrs = rawResults.getFirstResult();
                if (arrs == null) return result;
                for (String arr : arrs){
                    result += arr;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 根据城区id查询对应的省市区名称
     * @param areaId
     * @return
     */
    public String[] queryPcr(String areaId){
        if (areaId == null) return null;
        String[] result = null;
        try {
            GenericRawResults<String[]> rawResults = hatAreaDao.queryRaw(
                    "SELECT p.province,c.city,a.area FROM hat_area a " +
                            "JOIN hat_city c on a.city_id=c.cityid " +
                            "JOIN hat_province p on c.province_id=p.provinceid " +
                            "WHERE a.areaid=" + areaId);
            if (rawResults != null){
                String[] arrs = rawResults.getFirstResult();
                return arrs;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
