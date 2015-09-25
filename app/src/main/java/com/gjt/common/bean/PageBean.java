package com.gjt.common.bean;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;

/**
 *@autoer yb -yb498869020@hotmail.com
 *@Time 上午10:51:38
 *package com.villageassistant.uhuo.bean;
 *@Description  分页控件接口在baseActivity中实现了的需要在即将使用的Activity中重写需要的方法<br><分页控件需要初始化，调用getPageBean().setmIpage(IPage mIpage)初始化控件> 
 **/
public class PageBean extends Basebean{
	//参数名称
	public static final String CURRENTPAGE= "currentPage";
	public static final String COUNTS="counts";
	/**TODO**/
	private static final long serialVersionUID = 1L;
	public static final int SERCH =2;
	public static final int LOADDATE =1;
	public static final int RELOAD=4;
	public static final int SELECT =3;
	/**
	 * 分页控件接口在baseActivity中实现了的需要在即将使用的Activity中重写需要的方法
	 */
	private IPage mIpage;
	/**
	 * 上下文对象
	 */
	private Context mContext;
	
	/**
	 * 当前页
	 */
	private int currentPage=0;
	/**
	 * 总页数
	 */
	private int PageCount=1;
	
	private int counts=20;
	
	/**
	 * 状态：1表示默认载入LOADDATE
	 * 状态：2表示搜索SERCH
	 * 状态：3表示类别选择SELECT
	 */
	/**
	* TODO<用于规范话分页结构> 
	* @author yb
	* @data: 2014年12月13日 上午11:40:06 
	* @version: V1.0
	 */
	public interface IPage{
		/**
		 * <搜索功能的数据载入（多个数据载入可以用flag判断类型或者其他搜索方式）> 
		* @throw 
		* @author: yb 
		* @return void
		 */
		void loadData(int flag);
	}
	public PageBean(Context context) {
		this.mContext= context;
	}
	
	
	private int state;
	private Dialog loading;
	
	
	public int getCounts() {
		return counts;
	}
	public void setCounts(int counts) {
		this.counts = counts;
	}
	public IPage getmIpage() {
		return mIpage;
	}
	public void setmIpage(IPage mIpage) {
		this.mIpage = mIpage;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		if (state==RELOAD){
			this.state=state;
			return;
		}

		if (this.state!=state && state!=this.RELOAD) {
			clearPage(state);
		}

	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getPageCount() {
		return PageCount;
	}
	public void setPageCount(int pageCount) {
		PageCount = pageCount;
	}
	public int getNextPage() {
		if (getState()==RELOAD && getCurrentPage()!=0){
			return getCurrentPage();
		}

		return ++currentPage;
	}
	public int getUpPage() {
		return --currentPage;
	}
	@Override
	public String toString() {
		return "PageBean [currentPage=" + currentPage + ", PageCount="
				+ PageCount + ", state=" + state + "]";
	}
	/**
	* TODO<清空Page数据并设置状态> 
	* @throw 
	* @author: yb 
	* @return void
	 */
	public void clearPage(int state){
		this.state = state;
		this.currentPage=0;
		this.PageCount=1;
	}
	public void doUpDateComplete(){
		if (loading!=null && loading.isShowing()) {
			loading.dismiss();
		}
	}
	/**
	* TODO<根据载loadFlag判断是否需要加入载入对话框> 
	* @throw 
	* @author: yb 
	* @return void
	 */
	public void doUpdate(boolean loadFlag) {
		if (loadFlag) {
//			loading = DialogUtils.createLoadingDialog(mContext, R.string.loading);
//			loading.show();
		}
		goUpdate();
	}
	/**
	* TODO<无载入对话框> 
	* @throw 
	* @author: yb 
	* @return void
	 */
	public void doUpdate() {
//			loading = DialogUtils.createLoadingDialog(mContext, R.string.loading);
//			loading.show();
		goUpdate();
	}
	/**
	* TODO<执行载入选择> 
	* @throw 
	* @author: yb 
	* @return void
	 */
	private void goUpdate(){
		((Activity)mContext).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				switch (getState()) {
				case 0:
					Log.e("PageBean", "Pagebean Error note state");
					break;
				default :
					mIpage.loadData(getState());
					break;
				}
				
			}
		});
	}
	
}
