package com.qzj.chat.face;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import com.qzj.chat.uitls.DisplayUtils;
import gzt.com.apptest.R;

public class FaceRelativeLayout extends RelativeLayout implements OnItemClickListener, OnClickListener {
	private Context context;

	/** 显示表情页的viewpager */
	private ViewPager vp_face;

	/** 表情页界面集合 */
	private ArrayList<View> pageViews;

	/** 游标显示布局 */
	private LinearLayout layout_point;

	/** 游标点集合 */
	private ArrayList<ImageView> pointViews;

	/** 表情集合 */
	private List<List<ChatEmoji>> emojis;

	/** 表情区域 */
	private View view;

	private EditText editText;

	/** 表情数据填充器 */
	private List<FaceAdapter> faceAdapters;

	private FaceRelativeLayout faceRelativeLayout;

	/** 当前表情页 */
	private int current = 0;

	private int broadHeight;
	/**
	 * 默认表情显示4行
	 */
	private int pageRowNum = 4;
	private int pageColumnNum;
	private int pageFaceCount;
	private int columnWidth;

	public FaceRelativeLayout(Context context) {
		super(context);
		this.context = context;
	}

	public FaceRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public FaceRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		Init_View();
	}

	public void onCreate() {
		emojis = FaceConversionUtil.getInstace().emojiLists;
		Init_viewPager();
		Init_Point();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		new Thread(new Runnable() {
			@Override
			public void run() {
				FaceConversionUtil.getInstace().getFileText(getContext(), faceRelativeLayout, pageFaceCount);
			}
		}).start();
	}

	/**
	 * 隐藏表情选择框
	 */
	public boolean hideFaceView() {
		// 隐藏表情选择框
		if (view.getVisibility() == View.VISIBLE) {
			view.setVisibility(View.GONE);
			return true;
		}
		return false;
	}

	/**
	 * 初始化控件
	 */
	private void Init_View() {
		faceRelativeLayout = (FaceRelativeLayout) findViewById(R.id.FaceRelativeLayout);
		vp_face = (ViewPager) findViewById(R.id.vp_contains);
		layout_point = (LinearLayout) findViewById(R.id.iv_image);
		view = findViewById(R.id.ll_facechoose);
	}

	/**
	 * 初始化显示表情的viewpager
	 */
	public void Init_viewPager() {
		pageViews = new ArrayList<View>();
		// 左侧添加空页
		View nullView1 = new View(context);
		// 设置透明背景
		nullView1.setBackgroundColor(Color.TRANSPARENT);
		pageViews.add(nullView1);

		// 中间添加表情页
		faceAdapters = new ArrayList<FaceAdapter>();

		for (int i = 0; i < emojis.size(); i++) {
			GridView view = new GridView(context);
			view.setHorizontalScrollBarEnabled(false);
			view.setVerticalScrollBarEnabled(false);
			FaceAdapter adapter = new FaceAdapter(context, emojis.get(i), columnWidth);
			view.setAdapter(adapter);
			faceAdapters.add(adapter);
			view.setOnItemClickListener(this);
			view.setNumColumns(pageColumnNum);
			view.setBackgroundColor(Color.TRANSPARENT);
			view.setHorizontalSpacing(1);
			view.setVerticalSpacing(1);
			view.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
			view.setCacheColorHint(0);
			view.setPadding(5, 0, 5, 0);
			view.setSelector(new ColorDrawable(Color.TRANSPARENT));
			view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
			view.setGravity(Gravity.CENTER);
			pageViews.add(view);
		}

		// 右侧添加空页面
		View nullView2 = new View(context);
		// 设置透明背景
		nullView2.setBackgroundColor(Color.TRANSPARENT);
		pageViews.add(nullView2);
	}

	/**
	 * 初始化游标
	 */
	private void Init_Point() {
		handler.sendEmptyMessage(1);
	}

	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case 1:
					layout_point.removeAllViews();
					pointViews = new ArrayList<ImageView>();
					ImageView imageView;
					for (int i = 0; i < pageViews.size(); i++) {
						imageView = new ImageView(context);
						imageView.setBackgroundResource(R.mipmap.d1);
						LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
								new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
										LayoutParams.WRAP_CONTENT));
						layoutParams.leftMargin = 10;
						layoutParams.rightMargin = 10;
						layoutParams.width = 8;
						layoutParams.height = 8;
						layout_point.addView(imageView, layoutParams);
						if (i == 0 || i == pageViews.size() - 1) {
							imageView.setVisibility(View.GONE);
						}
						if (i == 1) {
							imageView.setBackgroundResource(R.mipmap.d2);
						}
						pointViews.add(imageView);
					}

					Init_Data();
					break;
			}
		}
	};

	/**
	 * 填充数据
	 */
	private void Init_Data() {
		vp_face.setAdapter(new ViewPagerAdapter(pageViews));

		vp_face.setCurrentItem(1);
		current = 0;
		vp_face.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				current = arg0 - 1;
				// 描绘分页点
				draw_Point(arg0);
				// 如果是第一屏或者是最后一屏禁止滑动，其实这里实现的是如果滑动的是第一屏则跳转至第二屏，如果是最后一屏则跳转到倒数第二屏.
				if (arg0 == pointViews.size() - 1 || arg0 == 0) {
					if (arg0 == 0) {
						vp_face.setCurrentItem(arg0 + 1);// 第二屏 会再次实现该回调方法实现跳转.
						pointViews.get(1).setBackgroundResource(R.mipmap.d2);
					} else {
						vp_face.setCurrentItem(arg0 - 1);// 倒数第二屏
						pointViews.get(arg0 - 1).setBackgroundResource(
								R.mipmap.d2);
					}
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}

	/**
	 * 绘制游标背景
	 */
	public void draw_Point(int index) {
		for (int i = 1; i < pointViews.size(); i++) {
			if (index == i) {
				pointViews.get(i).setBackgroundResource(R.mipmap.d2);
			} else {
				pointViews.get(i).setBackgroundResource(R.mipmap.d1);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){

		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ChatEmoji emoji = (ChatEmoji) faceAdapters.get(current).getItem(arg2);
		if (emoji.getId() == R.drawable.face_del_icon) {
			int selection = editText.getSelectionStart();
			String text = editText.getText().toString();
			if (selection > 0) {
				String text2 = text.substring(selection - 1);
				if ("]".equals(text2)) {
					int start = text.lastIndexOf("[");
					int end = selection;
					editText.getText().delete(start, end);
					return;
				}
				editText.getText().delete(selection - 1, selection);
			}
		}
		if (!TextUtils.isEmpty(emoji.getCharacter())) {
			SpannableString spannableString = FaceConversionUtil.getInstace()
					.addFace(getContext(), emoji.getId(), emoji.getCharacter());
			editText.append(spannableString);
		}

	}

	public void setEditText(EditText editText) {
		this.editText = editText;
	}

	public void setBroadHeight(int broadHeight) {
		this.broadHeight = broadHeight;
		columnWidth = broadHeight/pageRowNum;
		pageColumnNum = DisplayUtils.getDisplayWidth(context)/columnWidth;
		pageFaceCount = pageRowNum * pageColumnNum - 1;
	}

	public int getBroadHeight(){
		return this.broadHeight;
	}
}
