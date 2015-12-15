package gzt.com.okhttpdome;

/**
 * app访问服务器url常量
 * @author qzj
 */
public class HostUrl {

	/** host信息 */
	public static final String HOST_IP = "121.201.60.155";
//	public static final String HOST_IP = "192.168.0.131";
//	public static final String HOST_IP = "192.168.0.123";
//	public static final String HOST_IP = "192.168.0.121";

	public static final Integer HOST_PORT = 80;
	public static final String PROJECT = "GJT_IM";

	/** basePath */
	public static final String BASE_URL = "http://" + HOST_IP + "/" + PROJECT + "/";

	/**安装包下载链接**/
	public static final String DOWNLOAD_APK = BASE_URL + "app/appDownload.jsp";

	public static final String MAIN_LOAD_IMAGE = BASE_URL + "appAdv/getMainAdvs";
	/**登陆**/
	public static final String MAIN_LOGIN = BASE_URL + "appAdminUser/sellerLogin";

	/**验证码**/
	public static final String VER_CODE = BASE_URL + "appAdminUser/sendVerCode";

	/**注册**/
	public static final String SIGN_IN = BASE_URL + "appAdminUser/register";

	/**找回密码**/
	public static final String RETAKE_PWD = BASE_URL + "appAdminUser/findPasByPhone";

	/**修改用户**/
	public static final String UPDATE_USER = BASE_URL + "appAdminUser/updateUser";

	/**修改用户头像**/
	public static final String UPDATE_HEAD = BASE_URL + "appAdminUser/upHeadImg";

	/**关注列表**/
	public static final String  COLLECT_LIST = BASE_URL + "appCompany/getConcerns";

	/**检查版本更新**/
	public static final String VERSION_CHECK = BASE_URL+"appVersion/findSellerNewestVersion";

	/**添加合同验证码**/
	public static final String VER_CODE_CONTRACT = BASE_URL + "appMyProject/sendValidateByCode";

	/**添加合同**/
	public static final String ADD_CONTRACT = BASE_URL + "appMyProject/findProjectByCode";

	/**服务热线**/
	public static final String COMPANY_MSG = BASE_URL + "appCompany/getCompanyMessage";

	/**发布日记，日志**/
	public static final String SEND_DIARY = BASE_URL + "appMyProject/builders";

	/**配置清单**/
	public static final String CONFIG_EDIT_LIST = BASE_URL + "appMyProject/selectMyProjectUse";

	/**更新配置清单**/
	public static final String UPDATE_CONFIG_EDIT_LIST = BASE_URL + "appMyProject/updateMyProjectUse";

	public static final String UPDATE_CONFIG_EDIT_LIST_FROM = BASE_URL + "appMyProject/updateApprove";

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
	public static final String HOME_PROJECT_DETAIL = BASE_URL+"appAdminUser/getProjectinfo";
	/**我的爱家--项目详情--图纸**/
	public static final String HOME_PROJECT_DETAIL_DRAWING = BASE_URL+"appMyProject/getProjetBlueprint";
	/**我的爱家--项目详情--合同**/
	public static final String HOME_PROJECT_DETAIL_CONTRACT = BASE_URL+"appMyProject/getProjetBargain";
	/**我的爱家--项目详情--配置**/
	public static final String HOME_PROJECT_DETAIL_CONFIG = BASE_URL+"appMyProject/selectMateriel";
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
	public static final String HOME_PROJECT_DETAIL_HOTLINE = BASE_URL+"appMyProject/businessConsulting";

	public static final String HOME_PROJECT_POLLING = BASE_URL+"appMyProject/getReminds";

	public static final String HOME_PROJECT_PHONE_BOOK = BASE_URL+"appAdminUser/findPhone";


	/**查询公司**/
	public static final String FOUNDRENOVATION_GETCOMPANYBYCODE= BASE_URL+"appCompany/getCompanyByCode”";

	public static final String FOUNDRENOVATION_GETCOMPANIES= BASE_URL+"appCompany/getCompanies";
	//选择省份
	public static final String FOUNDRENOVATION_GETPROVIDENCES= BASE_URL+"appCompany/getProvidences";
	//城市
	public static final String FOUNDRENOVATION_GETCITIES= BASE_URL+"appCompany/getCities";
	//区域
	public static final String FOUNDRENOVATION_GETAREAS= BASE_URL+"appCompany/getAreas";
	/**关注列表**/
	public static final String APPCOMPANY_GETCONCERNS= BASE_URL+"appCompany/getConcerns";
	/**添加关注**/
	public static final String APPCOMPANY_CONCERNCOMPANY= BASE_URL+"appCompany/concernCompany";
	public static final String APPCOMPANY_DELCONCERNCOMPANY= BASE_URL+"appCompany/delConcernCompany";
	/**问题反馈接口**/
	//外部讨论
	public static final String APPMESSAGE_GETMESSAGES= BASE_URL+"appMessage/getMessages";
	public static final String APPMESSAGE_COMMENT= BASE_URL+"appMessage/comment";
	public static final String APPMESSAGE_COMMENTPIC= BASE_URL+"appMessage/commentPic";
	public static final String APPREPLY_FORWARDMESSAGES= BASE_URL+"appMessage/forwardMessages";
	/**产品中心选项卡数据**/
	public static final String APPPRODUCT_GETPRODUCTSORT= BASE_URL+"appProduct/getProductSort";
	public static final String APPPRODUCT_GETPRODUCTADV= BASE_URL+"appProduct/getProductAdv";
	public static final String USER_CENTER_BILL_LIST= BASE_URL+"appMyProject/getMaterialRecord";
	/**我的单据列表**/
	public static final String DOCUMENTS_LIST= BASE_URL+"appMyProject/selectMaterialRecordInfo?approve_id=";

	public static final String DOCUMETNS_STATE= BASE_URL+"appMyProject/updateApproveState";
}
