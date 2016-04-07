package com.qzj.logistics.view.books.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.qzj.logistics.R;

public class ContactListView extends ListView implements AbsListView.OnScrollListener {

	protected boolean mIsFastScrollEnabled = false;
	protected IndexScroller mScroller = null;
	protected GestureDetector mGestureDetector = null;

	// additional customization
	protected boolean inSearchMode = false; // whether is in search mode
	protected boolean autoHide = false; // alway show the scroller

	// loading more
	private LayoutInflater inflater;
	private OnScrollBottomListener bottomListener;
	private View footerView;
	private boolean isLoadingMore = false;
	private boolean isEnabledLoading = true;
	private float lastY;
	private boolean isPullUp = false;
	private boolean isCanPullUp = false;

	public ContactListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		inflater = LayoutInflater.from(context);
	}

	public ContactListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		inflater = LayoutInflater.from(context);
	}

	public IndexScroller getScroller()
	{
		return mScroller;
	}

	@Override
	public boolean isFastScrollEnabled()
	{
		return mIsFastScrollEnabled;
	}

	// override this if necessary for custom scroller
	public void createScroller()
	{
		mScroller = new IndexScroller(getContext(), this);
		mScroller.setAutoHide(autoHide);
		mScroller.setShowIndexContainer(true);

		if (autoHide)
			mScroller.hide();
		else
			mScroller.show();
	}

	@Override
	public void setFastScrollEnabled(boolean enabled)
	{
		mIsFastScrollEnabled = enabled;
		if (mIsFastScrollEnabled)
		{
			if (mScroller == null)
			{
				createScroller();
			}
		} else
		{
			if (mScroller != null)
			{
				mScroller.hide();
				mScroller = null;
			}
		}
	}

	@Override
	public void draw(Canvas canvas)
	{
		super.draw(canvas);

		// Overlay index bar
		if (!inSearchMode) // dun draw the scroller if not in search mode
		{
			if (mScroller != null)
				mScroller.draw(canvas);
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		// Intercept ListView's touch event
		switch (ev.getAction()){
			case MotionEvent.ACTION_DOWN:
				lastY = ev.getY();
				isCanPullUp = ViewCompat.canScrollVertically(this, 1);
				break;
			case MotionEvent.ACTION_MOVE:
				if (ev.getY()-lastY < 0){
					isPullUp = true;
				}else{
					isPullUp = false;
				}
				break;
		}

		if (mScroller != null && mScroller.onTouchEvent(ev))
			return true;

		if (mGestureDetector == null)
		{
			mGestureDetector = new GestureDetector(getContext(),
					new GestureDetector.SimpleOnGestureListener()
					{

						@Override
						public boolean onFling(MotionEvent e1, MotionEvent e2,
								float velocityX, float velocityY)
						{
							// If fling happens, index bar shows
							if (mScroller != null) mScroller.show();
							return super.onFling(e1, e2, velocityX, velocityY);
						}

					});
		}
		mGestureDetector.onTouchEvent(ev);

		return super.onTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		return true;
	}

	@Override
	public void setAdapter(ListAdapter adapter)
	{
		super.setAdapter(adapter);
//		if (mScroller != null)
//			mScroller.setAdapter(adapter);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		if (mScroller != null)
			mScroller.onSizeChanged(w, h, oldw, oldh);
	}

	public boolean isInSearchMode()
	{
		return inSearchMode;
	}

	public void setInSearchMode(boolean inSearchMode)
	{
		this.inSearchMode = inSearchMode;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (view.getLastVisiblePosition() == view.getAdapter().getCount()-1){
			if (isCanPullUp && isPullUp && bottomListener != null && isEnabledLoading && !isLoadingMore){
				isLoadingMore = true;
				bottomListener.scrollBottom(view);
				fillFooterView();
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

	}

	private void fillFooterView(){
		if (footerView == null)
			footerView = inflater.inflate(R.layout.listview_scroll_bottom,null);
		this.addFooterView(footerView);
		Log.e("qzj","fill loading more view");
	}

	private void removeFooterView(){
		if (getFooterViewsCount() > 0 && footerView != null){
			removeFooterView(footerView);
		}
	}

	/**
	 * 设置滑动到底部监听
	 * @param bottomListener
	 */
	public void setBottomListener(OnScrollBottomListener bottomListener) {
		this.bottomListener = bottomListener;
	}

	/**
	 * 是否开启加载更多
	 * @param isEnabledLoading
	 */
	public void setIsEnabledLoading(boolean isEnabledLoading) {
		this.isEnabledLoading = isEnabledLoading;
	}

	/**
	 * 加载完成
	 */
	public void loadingComplete(){
		removeFooterView();
		isLoadingMore = false;
	}

	/**
	 * 滑动到底部
	 */
	public interface OnScrollBottomListener{
		void scrollBottom(AbsListView view);
	}
}
