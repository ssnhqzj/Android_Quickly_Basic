package com.qzj.logistics.db.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.qzj.logistics.bean.HatProvince;
import com.qzj.logistics.db.DatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/25.
 */
public class HatProvinceDao {
    private static final String TABLE_NAME = "hat_province";

    private Context context;
    private Dao<HatProvince,Integer> hatProvinceDao;
    private DatabaseHelper helper;

    public HatProvinceDao(Context context)
    {
        this.context = context;
        helper = DatabaseHelper.getHelper(context);
        hatProvinceDao = helper.getDao(HatProvince.class);
    }

    /**
     * 清空表中数据
     * @return 返回删除数据条数，异常返回-1
     */
    public int clearTable(){
        int result;
        try {
            result = hatProvinceDao.executeRaw("delete from " + TABLE_NAME);
        } catch (SQLException e) {
            e.printStackTrace();
            result = -1;
        }
        return result;
    }

    /**
     * 添加一个省份
     * @param hatProvince
     */
    public void add(HatProvince hatProvince) {
        try {
            hatProvinceDao.create(hatProvince);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一个省份列表
     * @param list
     */
    public void addAll(ArrayList<HatProvince> list) {
        try {
            for (HatProvince hp : list){
                hatProvinceDao.create(hp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取省份列表
     * @return
     */
    public List<HatProvince> getHatProvinceForAll(){
        try {
            return hatProvinceDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
