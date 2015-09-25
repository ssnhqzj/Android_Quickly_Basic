package comm.lib.uiutils;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import comm.lib.utils.VolleyUtil;
import gzt.com.commlib.R;

/**
 * 图片轮播
 * @author qzj
 *
 */
public class AdvViewPager extends RelativeLayout implements ViewPager.OnPageChangeListener {
	
	private Context context;
	private View view;
	private ViewPager viewPager;
	private RelativeLayout viewPagerParent;
	private ImageView[] tips;
	private String[] urls;
	private FixedSpeedScroller scroller;
	private boolean isAutoScroll;
	private OnAdvClickListener advClickListener;

	/**
	 * 切换图片时动画执行时间
	 */
	private int spaceTime;
	/**
	 * 每张图片停留时间
	 */
	private int sleepTime;
	
	/**
	 * tips选中背景
	 */
	private int tipsCrrentBg;
	/**
	 * tips未选中背景
	 */
	private int tipsOtherBg;
	/**
	 * tips位置
	 */
	private int tipsGravity;
	/**
	 * tips宽
	 */
	private int tipsWidth;
	/**
	 * tips高
	 */
	private int tipsHeight;
	//tips上下左右间距
	private int tipsMarginLeft;
	private int tipsMarginTop;
	private int tipsMarginRight;
	private int tipsMarginBottom;
	
	public AdvViewPager(Context context) {
		this(context, null);
	}

	public AdvViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}
	
	private void initView() {
		view = LayoutInflater.from(context).inflate(R.layout.viewpager_layout, this);
		viewPager = (ViewPager) view.findViewById(R.id.viewPager);
		viewPagerParent = (RelativeLayout) view.findViewById(R.id.view_pager_parent);
	}

	public void build(){
		LinearLayout lLayout_tip = new LinearLayout(context);
		lLayout_tip.setOrientation(LinearLayout.HORIZONTAL);
		LayoutParams layoutParams_tip = new LayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		layoutParams_tip.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		if(tipsGravity != 0){
			layoutParams_tip.addRule(tipsGravity, RelativeLayout.TRUE);
		}else{
			layoutParams_tip.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		}

		viewPagerParent.addView(lLayout_tip, layoutParams_tip);
		tips = new ImageView[urls.length];
		for(int i=0; i<urls.length; i++){
			ImageView iv_tip = new ImageView(context);
			iv_tip.setLayoutParams(new LayoutParams(tipsWidth==0?10:tipsWidth,tipsHeight==0?10:tipsHeight));  
            tips[i] = iv_tip;  
            
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));  
            layoutParams.leftMargin = tipsMarginLeft!=0 ? tipsMarginLeft:0;  
            layoutParams.topMargin = tipsMarginTop!=0 ? tipsMarginTop:5;
            layoutParams.rightMargin = tipsMarginRight!=0 ? tipsMarginRight:5;
            layoutParams.bottomMargin = tipsMarginBottom!=0 ? tipsMarginBottom:20;
            lLayout_tip.addView(iv_tip, layoutParams);
		}
		
		viewPager.setAdapter(new AdvAdapter());
		viewPager.setOnPageChangeListener(this);  
		viewPager.setCurrentItem((urls.length) * 1000);
		setScrollerTime(spaceTime==0 ? 700:spaceTime);
		viewPager.setIsAutoScroll(isAutoScroll);
		if(isAutoScroll){
			viewPager.startAutoRoll();
		}
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int selectItem) {
		setTipsImageBackground(selectItem%urls.length);
	}


    private void setTipsImageBackground(int selectItems){  
        for(int i=0; i<tips.length; i++){  
            if(i == selectItems){  
            	if(tipsCrrentBg != 0){
            		tips[i].setBackgroundResource(tipsCrrentBg);  
            	}else{
            		tips[i].setBackgroundResource(R.drawable.viewpager_point_1);  
            	}
            }else{  
            	if(tipsOtherBg != 0){
            		tips[i].setBackgroundResource(tipsOtherBg);  
            	}else{
            		tips[i].setBackgroundResource(R.drawable.viewpager_point_0);  
            	}
            }  
        }  
    }
    
    /**
     * 设置adv宽高
     * @param width
     * @param height
     * @return
     */
    public AdvViewPager setAdvWidthAndHeight(int width, int height){
    	LayoutParams viewPagerParams = null;
    	if(viewPager.getLayoutParams() == null){
    		viewPagerParams = (LayoutParams) viewPager.getLayoutParams();
    	}else{
    		viewPagerParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    	}
		viewPagerParams.width = width;
		viewPagerParams.height = height;
		
		viewPagerParent.setLayoutParams(viewPagerParams);
		return this;
    }
    
    private void setScrollerTime(int scrollerTime){
        try {
            if(scroller!=null){
                 scroller.setmDuration(scrollerTime);
            }else{
//				scroller= new FixedSpeedScroller(viewPager.getContext(),new AccelerateInterpolator());
                scroller= new FixedSpeedScroller(viewPager.getContext());
                scroller.setmDuration(scrollerTime);
                viewPager.setmScroller(scroller);
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

	class AdvAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			
			return arg0 == arg1;
		}
		
		@Override  
		public Object instantiateItem(ViewGroup container, final int position) {
			if(urls != null && urls.length > 0){
				ImageView iv = new ImageView(context);
				iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
				VolleyUtil.getInstance(context).initImageLoader().loadImage(iv,urls[position%urls.length],true);
				container.addView(iv);
				iv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(advClickListener != null){
							advClickListener.onAdvClick(position%urls.length);
						}
					}
				});

				return iv;
			}

			return null;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
	        object = null;
		}  
	}

	/**
	 * 设置图片的url集合
	 * @param urls
	 * @return
	 */
	public AdvViewPager setUrls(String[] urls){
    	this.urls = urls;
    	return this;
    }

	/**
	 * 设置是否自动滚动
	 * @param isAuto
	 * @return
	 */
    public AdvViewPager setIsAutoScroll(boolean isAuto){
    	this.isAutoScroll = isAuto;
    	
    	return this;
    }

    /**
     * 设置tips的选中和未选中的背景
     * @param tipsCrrentBg 选中背景
     * @param tipsOtherBg 未选中背景
     */
	public AdvViewPager setTipsBg(int tipsCrrentBg, int tipsOtherBg) {
		this.tipsCrrentBg = tipsCrrentBg;
		this.tipsOtherBg = tipsOtherBg;
		
		return this;
	}

	/**
	 * 设置tips的位置
	 * @param tipsGravity：
	 * 默认RelativeLayout.CENTER_HORIZONTAL
	 */
	public AdvViewPager setTipGravity(int tipsGravity) {
		this.tipsGravity = tipsGravity;
		
		return this;
	}

	/**
	 * 设置tips的宽高
	 * @param width
	 * @param height
	 * @return
	 */
	public AdvViewPager setTipsSize(int width, int height){
		this.tipsWidth = width;
		this.tipsHeight = height;
		
		return this;
	}
	
	/**
	 * 设置tips的margin属性
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 * @return
	 */
	public AdvViewPager setTipsMargin(int left,int top,int right,int bottom){
		this.tipsMarginLeft = left;
		this.tipsMarginTop = top;
		this.tipsMarginRight = right;
		this.tipsMarginBottom = bottom;
		
		return this;
	}

	/**
	 * 切换图片时动画执行时间
	 */
	public AdvViewPager setSpaceTime(int spaceTime) {
		this.spaceTime = spaceTime;
		
		return this;
	}

	/**
	 * 每张图片停留时间
	 */
	public AdvViewPager setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
		
		return this;
	}

	/**
	 * 设置图片点击监听
	 * @param advClickListener
	 */
	public void setAdvClickListener(OnAdvClickListener advClickListener) {
		this.advClickListener = advClickListener;
	}

	/**
	 * 图片点击事件接口
	 */
	public interface OnAdvClickListener {
		public void onAdvClick(int position);
	}
	
}
