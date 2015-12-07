package gzt.com.apptest.Chat.face;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import gzt.com.apptest.R;

public class FaceRelativeLayout extends RelativeLayout implements
		OnItemClickListener, OnClickListener, View.OnFocusChangeListener {
	private Context context;
	/**
	 * 按钮事件监听
	 */
	private IFaceComment iFaceComment;

	/** 表情页的监听事件 */
	private OnCorpusSelectedListener mListener;

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

	/** 输入框 */
	private PasteEditText et_sendmessage;

	/** 表情数据填充器 */
	private List<FaceAdapter> faceAdapters;

	private ImageView faceBtn;

	/** 当前表情页 */
	private int current = 0;
	private ImageView faceimg;
	private ImageView facesend;
	private RelativeLayout et_sendmessage_bg;

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

	public void setOnCorpusSelectedListener(OnCorpusSelectedListener listener) {
		mListener = listener;
	}

	public void setiFaceComment(IFaceComment iFaceComment) {
		this.iFaceComment = iFaceComment;
	}

	/**
	 * 表情选择监听
	 *
	 */
	public interface OnCorpusSelectedListener {

		void onCorpusSelected(ChatEmoji emoji);

		void onCorpusDeleted();
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
	public void onClick(View v) {

		int i = v.getId();
		if (i == R.id.btn_face) {// 隐藏表情选择框
			if (view.getVisibility() == View.VISIBLE) {
				view.setVisibility(View.GONE);
				faceBtn.setImageResource(R.mipmap.comment_face);
			} else {
				hideSoftKeyBoard(view);
				view.setVisibility(View.VISIBLE);
				faceBtn.setImageResource(R.mipmap.face_key_board);
				et_sendmessage_bg.setBackgroundResource(R.drawable.common_bg_gray_edit_buttom_pressed);
			}
			if (iFaceComment!=null){
				iFaceComment.faceOpening(v);
			}

		} else if (i == R.id.btn_img) {
			if (iFaceComment!=null){
				iFaceComment.sendImage(v);
			}
		} else if (i == R.id.btn_send) {
			if (iFaceComment!=null){
				iFaceComment.sendMessage(et_sendmessage);
			}

		} else if (i == R.id.et_sendmessage) {// 隐藏表情选择框
			et_sendmessage_bg.setBackgroundResource(R.drawable.common_bg_gray_edit_buttom_pressed);
			if (view.getVisibility() == View.VISIBLE) {
				view.setVisibility(View.GONE);
				faceBtn.setImageResource(R.mipmap.comment_face);
			}
			if (iFaceComment!=null){
				iFaceComment.editClick(v);
			}

		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus){
			view.setVisibility(View.GONE);
		}
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
		vp_face = (ViewPager) findViewById(R.id.vp_contains);
		et_sendmessage = (PasteEditText) findViewById(R.id.et_sendmessage);
		et_sendmessage_bg=(RelativeLayout)findViewById(R.id.et_sendmessage_rl);
		layout_point = (LinearLayout) findViewById(R.id.iv_image);
		et_sendmessage.setOnClickListener(this);
		et_sendmessage.setOnFocusChangeListener(this);
		faceBtn = (ImageView) findViewById(R.id.btn_face);
		faceimg = (ImageView) findViewById(R.id.btn_img);
		facesend = (ImageView) findViewById(R.id.btn_send);
		faceimg.setOnClickListener(this);
		facesend.setOnClickListener(this);
		faceBtn.setOnClickListener(this);
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
			FaceAdapter adapter = new FaceAdapter(context, emojis.get(i));
			view.setAdapter(adapter);
			faceAdapters.add(adapter);
			view.setOnItemClickListener(this);
			view.setNumColumns(7);
			view.setBackgroundColor(Color.TRANSPARENT);
			view.setHorizontalSpacing(1);
			view.setVerticalSpacing(1);
			view.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
			view.setCacheColorHint(0);
			view.setPadding(5, 0, 5, 0);
			view.setSelector(new ColorDrawable(Color.TRANSPARENT));
			view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ChatEmoji emoji = (ChatEmoji) faceAdapters.get(current).getItem(arg2);
		if (emoji.getId() == R.drawable.face_del_icon) {
			int selection = et_sendmessage.getSelectionStart();
			String text = et_sendmessage.getText().toString();
			if (selection > 0) {
				String text2 = text.substring(selection - 1);
				if ("]".equals(text2)) {
					int start = text.lastIndexOf("[");
					int end = selection;
					et_sendmessage.getText().delete(start, end);
					return;
				}
				et_sendmessage.getText().delete(selection - 1, selection);
			}
		}
		if (!TextUtils.isEmpty(emoji.getCharacter())) {
			if (mListener != null)
				mListener.onCorpusSelected(emoji);
			SpannableString spannableString = FaceConversionUtil.getInstace()
					.addFace(getContext(), emoji.getId(), emoji.getCharacter());
			et_sendmessage.append(spannableString);
		}

	}


	private void toggleSoftKeyBoard(){
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	private void hideSoftKeyBoard(View view){
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	private void showSoftKeyBoard(View view){
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, 0);
	}
}
