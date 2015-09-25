package com.gjt.common.utils.http;

/**
 * app访问服务器url常量
 * 
 * @author qzj
 */
public class HostUrl {

	/** host信息 */
//	public static final String HOST_IP = "121.201.60.156";
	public static final String HOST_IP = "192.168.0.113";

	public static final Integer HOST_PORT = 80;
	public static final String PROJECT = "WJH_IM";
//	public static final String PACKAGE = "app";

	/** basePath */
	public static final String BASE_URL = "http://" + HOST_IP + "/" + PROJECT + "/";

	public static final String MAIN_LOAD_IMAGE = BASE_URL + "appAdv/getMainAdvs";
	/**登陆**/
	public static final String MAIN_LOGIN = BASE_URL + "appLogin/login";

	/**产品中心数据列表**/
	public static final String PRODUCT_CENTER_MAININIT=BASE_URL+"appProduct/getProductType";
	/**产品中心广告图片**/
	public static final String PRODUCT_CENTER_MAINIADV=BASE_URL+"appProduct/getProductAdv";
	/**产品列表**/
	public static final String PRODUCT_LIST=BASE_URL+"appProduct/getProductLByTId";
	/**产品列表详情**/
	public static final String PRODUCT_INFO=BASE_URL+"appProduct/getProductinfo";
	/**产品中心**/
	public static final String PROJECTTYPE=BASE_URL+"appProject/getProjectType";
	/**工程师**/
	public static final String ENGINEERS=BASE_URL+"appEngineer/toEngineerJsp";
	/**工程案列**/
	public static final String ENGINEERING_CASE=BASE_URL+"appProject/getProjectType";
	/**公司简介**/
	public static final String COMPANY_PROFILE=BASE_URL+"appCompany/toCompanyJsp";
	/**招贤纳才**/
	public static final String TALENT_RECRUITMENT=BASE_URL+"appRecruitment/toRecruitmentJsp";
	/**最新活动列表**/
	public static final String NEW_ACTIVITIES=BASE_URL+"appActivity/getActivities";
	public static final String NEW_ACTIVITIES_INFO=BASE_URL+"appActivity/getActivityinfo";

	/**我的爱家--项目列表**/
	public static final String HOME_PROJECT_LIST = BASE_URL+"appMyProject/getProjects";
	/**我的爱家--项目详情**/
	public static final String HOME_PROJECT_DETAIL = BASE_URL+"appMyProject/getProjectinfo";
	/**我的爱家--项目详情--图纸**/
	public static final String HOME_PROJECT_DETAIL_DRAWING = BASE_URL+"appMyProject/getProjetDetail";
	/**我的爱家--项目详情--合同**/
	public static final String HOME_PROJECT_DETAIL_CONTRACT = BASE_URL+"appMyProject/getProjetDetail";
	/**我的爱家--项目详情--配置**/
	public static final String HOME_PROJECT_DETAIL_CONFIG = BASE_URL+"appMyProject/getProjetDetail";
	/**我的爱家--项目详情--施工日记**/
	public static final String HOME_PROJECT_DETAIL_DIARY = BASE_URL+"appMyProject/getDiaryInfo";
	/**我的爱家--项目详情--监理日志**/
	public static final String HOME_PROJECT_DETAIL_LOG = BASE_URL+"appMyProject/getDiary";
	public static final String HOME_PROJECT_DETAIL_KNOWN = BASE_URL+"appMyProject/updateKnow";
	public static final String HOME_PROJECT_DETAIL_OK = BASE_URL+"appMyProject/updateOk";
	/**我的爱家--项目详情--发送评论**/
	public static final String HOME_PROJECT_SEND_COMMENT = BASE_URL+"appMessage/comment";
	/**我的爱家--项目详情--发送回复**/
	public static final String HOME_PROJECT_SEND_REPLY = BASE_URL+"appMessage/postReplies";
	/**我的爱家--项目详情--售后服务**/
	public static final String HOME_PROJECT_DETAIL_AFTERSALE = BASE_URL+"appComplain/complain";
	/**我的爱家--项目详情--客服热线**/
	public static final String HOME_PROJECT_DETAIL_HOTLINE = BASE_URL+"appMyProject/getProjectType";

	public static final String VERSION_DOWNLOAD = BASE_URL+"appVersion/findNewestVersion";

	public static final String HOME_PROJECT_POLLING = BASE_URL+"appMyProject/getReminds";
}
