package com.qzj.logistics.app;

/**
 * app访问服务器url常量
 * @author qzj
 */
public class Host {

	/** host信息 */
//	public static final String HOST_IP = "121.201.60.155";
	public static final String HOST_IP = "192.168.0.131";
//	public static final String HOST_IP = "192.168.0.123";
//	public static final String HOST_IP = "192.168.0.126";

	public static final Integer HOST_PORT = 80;
	public static final String PROJECT = "Print";

	/** basePath */
	public static final String BASE_URL = "http://" + HOST_IP + "/" + PROJECT + "/";

	/**获取城市区划**/
	public static final String GET_AREA = BASE_URL + "appCity/loadCity";

	/**获取注册验证码**/
	public static final String GET_VAR_CODE = BASE_URL + "appUser/sendVerCode";

	/**注册**/
	public static final String REGISTER = BASE_URL + "appUser/register";

	/**登陆**/
	public static final String LOGIN = BASE_URL + "appUser/login";

	/**找回密码**/
	public static final String RESET_PW = BASE_URL + "appUser/resetPassword";

	/**信息修改**/
	public static final String USER_UPDATE = BASE_URL + "appUser/update";

	/**加载当前用户的所有的地址**/
	public static final String LOAD_SENDER_ADDR = BASE_URL + "appAddrMore/loadAddrMore";

	/**保存所有的地址**/
	public static final String SAVE_SENDER_ADDR = BASE_URL + "appAddrMore/saveAddrMore";

	/**设置默认地址**/
	public static final String UPDATE_SENDER_ADDR = BASE_URL + "updateAddrMoreState";

	/**删除地址**/
	public static final String DELETE_SENDER_ADDR = BASE_URL + "appAddrMore/delete";

	/**历史联系人**/
	public static final String HIS_RECEIVER_ADDR = BASE_URL + "appHisReceiver/getAddrMorePage";

	/**打印快递单**/
	public static final String PRINT_BILLS = BASE_URL + "appSendRecord/printExpressList";

	/**检测软件版本是否有更新**/
	public static final String CHECK_VERSION_APK = BASE_URL + "appEdition/checkApkEdition";

	/**检测区划是否有更新**/
	public static final String CHECK_VERSION_AREA = BASE_URL + "appEdition/checkCityEdition";
}
