package com.qzj.logistics.view.books;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qzj.empty.EmptyLayout;
import com.qzj.logistics.R;
import com.qzj.logistics.app.Host;
import com.qzj.logistics.app.MyApplication;
import com.qzj.logistics.base.BaseActivity;
import com.qzj.logistics.bean.HisReceiver;
import com.qzj.logistics.bean.HisReceiverList;
import com.qzj.logistics.bean.PageBean;
import com.qzj.logistics.bean.Toolbar;
import com.qzj.logistics.view.books.adapter.BookAdapter;
import com.qzj.logistics.view.books.model.BookItem;
import com.qzj.logistics.view.books.widget.ContactItemInterface;
import com.qzj.logistics.view.books.widget.ContactListViewImpl;
import com.qzj.logistics.view.deliver.DeliverGoodsFragment;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.JsonOkHttpUtils;
import com.zhy.http.okhttp.callback.JsonCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookActivity extends BaseActivity implements TextWatcher,View.OnClickListener {
	private static final int WHAT_JSON_PROJECT_PHONES = 1;
	private boolean isFirstLoad = true;
	private Context context_ = BookActivity.this;

	private ContactListViewImpl listview;

	private EditText searchBox;
	private Object searchLock = new Object();
	boolean inSearchMode = false;

	private final static String TAG = "BookActivity";

	List<ContactItemInterface> contactList;
	List<ContactItemInterface> filterList;
	private BookAdapter adapter;
	private SearchListTask curSearchTask = null;

	private PageBean pageBean;
	private ImageView search;
	private String searchKey;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_book);

		Toolbar toolbar = new Toolbar();
		toolbar.setBg(R.color.common_main);
		toolbar.setCenterText("历史联系人");
		toolbar.setLeftResId(R.mipmap.return_up);
		toolbar.setRightText("确定");
		initToolBar(toolbar);

		pageBean = new PageBean();
		filterList = new ArrayList<ContactItemInterface>();
		contactList = new ArrayList<ContactItemInterface>();

		initEmptyLayout();
		search = (ImageView) findViewById(R.id.input_search_search);
		search.setOnClickListener(this);
		listview = (ContactListViewImpl) this.findViewById(R.id.listview);
		listview.setFastScrollEnabled(false);

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parent, View v, int position, long id) {
				List<ContactItemInterface> list = inSearchMode ? filterList : contactList;
				if (list == null) return;
				for (int i = 0; i < list.size(); i++) {
					if (i == position) {
						list.get(position).setRbState(true);
						continue;
					}

					list.get(i).setRbState(false);
				}

				adapter.setItems(list);
				adapter.notifyDataSetChanged();
			}
		});

		listview.setBottomListener(new ContactListViewImpl.OnScrollBottomListener() {
			@Override
			public void scrollBottom(AbsListView view) {
				Log.e("qzj", ">>>>>>>>>>>>>>>scroll bottom>>>>>>>>>>>>>>>>>>>");
				loadHisReceiver(searchKey);
			}
		});

		searchBox = (EditText) findViewById(R.id.input_search_query);
		searchBox.addTextChangedListener(this);

		adapter = new BookAdapter(context_, R.layout.phone_book_item);
		adapter.setInSearchMode(false);
		listview.setInSearchMode(false);
		listview.setAdapter(adapter);
		loadHisReceiver(null);
	}


	@Override
	public void afterTextChanged(Editable s) {
		String searchString = searchBox.getText().toString().trim().toUpperCase();
		if (searchString == null || "".equals(searchString)){
			searchKey = null;
			adapter.setItems(contactList);
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// do nothing
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.toolbar_left:
				this.finish();
				break;

			// 搜索
			case R.id.input_search_search:
				searchKey = searchBox.getText().toString();
				if (searchKey != null && !"".equals(searchKey)){
					inSearchMode = true;
					filterList.clear();
					pageBean.setCurrentPage(0);
				}
				loadHisReceiver(searchKey);
				break;

			// 确定
			case R.id.toolbar_right:
				if (adapter != null){
					List<ContactItemInterface> list = adapter.getItems();
					if (list != null && list.size() > 0){
						HisReceiver selectedItem = null;
						for (int i=0;i<list.size();i++){
							if (list.get(i).getRbState())
								selectedItem = (HisReceiver) list.get(i);
						}

						Intent intent = new Intent();
						if (selectedItem != null){
							intent.putExtra("his_receiver",selectedItem);
							setResult(DeliverGoodsFragment.RESULT_CODE_HISTORY, intent);
						}else
							setResult(-1, intent);

						this.finish();
					}
				}
				break;
		}
	}

	/**
	 * 加载历史收件人
	 * searchStr: 搜索条件
	 */
	private void loadHisReceiver(String searchStr) {
		Map<String,Object> params = new HashMap<>();
		params.put("dqpage",pageBean.getNextPage());
		HisReceiver hisReceiver = new HisReceiver();
		hisReceiver.setUser_id(((MyApplication) getApplication()).getUser().getUser_id());
		if (searchStr != null && !"".equals(searchStr)){
			hisReceiver.setName(searchStr);
		}
		params.put("hisReceiver",hisReceiver);
		JsonOkHttpUtils.getInstance().post(Host.HIS_RECEIVER_ADDR, params)
				.executeJson(new JsonCallBack() {
					@Override
					public void onBefore(Request request) {
						super.onBefore(request);
						if (emptyLayout != null && isFirstLoad){
							emptyLayout.setEmptyType(EmptyLayout.LOADING);
						}
					}

					@Override
					public void onError(Request request, Exception e) {
						Log.e("qzj", "onError:" + e.getMessage());
						if (emptyLayout != null && isFirstLoad){
							emptyLayout.setEmptyType(EmptyLayout.LOADING_FAILED);
						}
					}

					@Override
					public void onResponse(JSONObject response) {
						Log.e("qzj", "onResponse:" + response.toString());
						HisReceiverList hisReceiverList = JSON.toJavaObject(response,HisReceiverList.class);
						if (hisReceiverList != null && hisReceiverList.getResultCode() == 1){
							pageBean.setTotalPage(hisReceiverList.getPagecount());
							pageBean.setCurrentPage(pageBean.getNextPage());

							List<HisReceiver> list = hisReceiverList.getHisReceiverList();
							if (list != null && list.size() > 0){
								if (inSearchMode){
									filterList.addAll(list);
									adapter.setItems(filterList);
								}else{
									contactList.addAll(list);
									adapter.setItems(contactList);
								}
								adapter.notifyDataSetChanged();
							}
						}else{
							if (emptyLayout != null && isFirstLoad){
								emptyLayout.setEmptyType(EmptyLayout.NO_DATA);
							}
						}
					}

					@Override
					public void onAfter() {
						super.onAfter();
						listview.loadingComplete();
						if (pageBean.isLastPage())
							listview.setIsEnabledLoading(false);

						if (emptyLayout != null && isFirstLoad){
							emptyLayout.dismiss();
						}
						isFirstLoad = false;
					}
				});
	}

	private class SearchListTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params)
		{
			filterList.clear();
			String keyword = params[0];
			inSearchMode = (keyword.length() > 0);
			if (inSearchMode)
			{
				// get all the items matching this
				for (ContactItemInterface item : contactList)
				{
					BookItem contact = (BookItem) item;
					boolean isPinyin = false;
					boolean isChinese = false;
					boolean isPhone = false;

					if(contact.getFullName() != null){
						isPinyin = contact.getFullName().toUpperCase().indexOf(keyword) > -1;
					}

					if(contact.getUser_name() != null){
						isChinese = contact.getUser_name().toUpperCase().indexOf(keyword) > -1;
					}

					if(contact.getTelephone() != null){
						isPhone = contact.getTelephone().indexOf(keyword) > -1;
					}

					if (isPinyin || isChinese || isPhone)
					{
						filterList.add(item);
					}
				}
			}
			return null;
		}

		protected void onPostExecute(String result) {
			synchronized (searchLock) {
				if (inSearchMode) {
					adapter = new BookAdapter(context_, R.layout.phone_book_item);
					adapter.setItems(filterList);
					adapter.setInSearchMode(true);
					listview.setInSearchMode(true);
					listview.setAdapter(adapter);
				} else {
					adapter = new BookAdapter(context_, R.layout.phone_book_item);
					adapter.setItems(contactList);
					adapter.setInSearchMode(false);
					listview.setInSearchMode(false);
					listview.setAdapter(adapter);
				}
			}
		}
	}

	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			listview.loadingComplete();
		}
	};
}
