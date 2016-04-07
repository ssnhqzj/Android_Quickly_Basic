package com.qzj.logistics.view.books.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qzj.logistics.view.books.model.BookItem;
import com.qzj.logistics.view.books.widget.ContactItemInterface;
import com.qzj.logistics.view.books.widget.pinyin.PinYin;

import java.util.ArrayList;
import java.util.List;

/**
 * 357个中国城市
 * @author ck
 * @since 2014年2月7日 16:20:32
 */
public class BookData
{
	static String phoneBookJson = "{'phonebooks':" +
			"[" +
			"{'user_name':'小李1','telephone':'13511111111'}," +
			"{'user_name':'小李2','telephone':'13522222222'}," +
			"{'user_name':'小李3','telephone':'13533333333'}," +
			"{'user_name':'小李4','telephone':'13544444444'}," +
			"{'user_name':'小李5','telephone':'13555555555'}" +
//			"{'user_name':'小李1','telephone':'13511111111'}," +
//			"{'user_name':'小李2','telephone':'13522222222'}," +
//			"{'user_name':'小李3','telephone':'13533333333'}," +
//			"{'user_name':'小李4','telephone':'13544444444'}," +
//			"{'user_name':'小李5','telephone':'13555555555'}" +
//			"{'user_name':'小李1','telephone':'13511111111'}," +
//			"{'user_name':'小李2','telephone':'13522222222'}," +
//			"{'user_name':'小李3','telephone':'13533333333'}," +
//			"{'user_name':'小李4','telephone':'13544444444'}," +
//			"{'user_name':'小李5','telephone':'13555555555'}" +
//			"{'user_name':'小李1','telephone':'13511111111'}," +
//			"{'user_name':'小李2','telephone':'13522222222'}," +
//			"{'user_name':'小李3','telephone':'13533333333'}," +
//			"{'user_name':'小李4','telephone':'13544444444'}," +
//			"{'user_name':'小李5','telephone':'13555555555'}" +
			"]" +
			"}";

	public static List<ContactItemInterface> getSampleContactList()
	{
		List<ContactItemInterface> list = new ArrayList<ContactItemInterface>();
		JSONObject jsonObject = JSON.parseObject(phoneBookJson);
		JSONArray jsonArray = jsonObject.getJSONArray("phonebooks");
		for(int i = 0; i < jsonArray.size(); i++)
		{
			BookItem phoneBookItem = JSON.toJavaObject(jsonArray.getJSONObject(i), BookItem.class);
			if(phoneBookItem != null){
				list.add(new BookItem(phoneBookItem.getUser_name(), PinYin.getPinYin(phoneBookItem.getUser_name()), phoneBookItem.getTelephone(),phoneBookItem.getRbState()));
			}
		}

		return list;
	}

}
