package com.springboot.constant.chenbin;

/**
 * @author chenb
 * @version 2020/5/14/014
 * description：
 */
public class KeywordConstant {
    public final static String EXC_TYPE_JINTERROR="JintError";
    public final static String EXC_TYPE_FEIGNERROR="FeignError";
    public final static String EXC_TYPE_NOTICEERROR="NoticeError";

    public final static String EXC_DIRECTION_OUTER = "0";
    public final static String EXC_DIRECTION_INNER = "1";

    //水电气广的happenKey
    public final static String EXC_SDQG_HAPPEN_KEY_ALL = "All";
    public final static String EXC_SDQG_HAPPEN_KEY_WAT = "Wat";
    public final static String EXC_SDQG_HAPPEN_KEY_ELE = "Ele";
    public final static String EXC_SDQG_HAPPEN_KEY_GAS = "Gas";
    public final static String EXC_SDQG_HAPPEN_KEY_TV = "TV";

    //身份证附件的关键字
    public final static String KEYWORD_OF_ANNEX_IDCARD = "身份证照片附件";

    public final static String CKIND_OF_ANNEX_FOLDER = "文件夹";
    public final static String CKIND_OF_ANNEX_FILE = "文件";
    public final static String CARD_NAME_OF_XCTP = "现场图片";
    public final static String CARD_NAME_OF_ZJZTP = "身份证图片";
    public final static String CARD_NAME_OF_YSJG = "衍生结果";
    public final static String CARD_NAME_OF_RZJG = "人证对比结果";
    public final static String CARD_NAME_OF_RZJG_RENAME = "人证结果";

    //自来水公司
    public final static String WATER_NAME_YK = "YKZLS";
    public final static String WATER_NAME_SY = "SYZLS";
    public final static String WATER_NAME_SS = "SSZLS";

    //地区性常量
    public final static String DATA_SOURCE_DEPART_BDC = "宿迁市不动产登记中心";
    public final static String DATA_SOURCE_DEPART_ZJJ = "宿迁市住建局";
    public final static String DATA_SOURCE_DEPART_SWJ = "宿迁市税务局";

    //不动产单元库标识
    public final static String INFO_TABLE_CODE_BDCQL = "bdcqlxgxx";
    public final static String INFO_TABLE_CODE_BDCDY = "bdcdyxgxx";
    public final static String INFO_TABLE_CODE_JYHT = "jyhtxx";
    public final static String INFO_TABLE_CODE_DYHT = "dyhtxx";
    public final static String INFO_TABLE_CODE_QWXX = "qsxx";

    //它项权
    public final static String BDC_ITS_RIGHT_DY = "抵押";
    public final static String BDC_ITS_RIGHT_CF = "查封";
    public final static String BDC_ITS_RIGHT_YY = "异议";
    public final static String BDC_ITS_RIGHT_DJ = "冻结";

    //节点类型
    public final static String BDC_NOTICE_JD_SL = "acceptNotice";
    public final static String BDC_NOTICE_JD_SH = "verifyNotice";
    public final static String BDC_NOTICE_JD_DB = "resultNotice";
    public final static String BDC_NOTICE_JD_FQ = "discardNotice";

    //handle结果标识
    public final static String YCSL_HANDLE_RESULT_PASS = "审核通过";
    public final static String YCSL_HANDLE_RESULT_NO_PASS = "审核不通过";
    public final static String YCSL_HANDLE_RESULT_SUCCESS = "成功";
    public final static String YCSL_HANDLE_RESULT_UNSUCCESS = "不成功";
    public final static String BDC_DB_JG_SUCCESS = "登簿成功";

    //规范化登记类型
    public final static String BDC_YWLX_NAME_QS = "权属";
    public final static String BDC_YWLX_NAME_QSZX = "权属注销";
    public final static String BDC_YWLX_NAME_DY = "抵押";
    public final static String BDC_YWLX_NAME_DYZX = "抵押注销";
    public final static String BDC_YWLX_NAME_CF = "查封";
    public final static String BDC_YWLX_NAME_CFZX = "查封注销";
    public final static String BDC_YWLX_NAME_YG = "预告";
    public final static String BDC_YWLX_NAME_YGZX = "预告注销";
    public final static String BDC_YWLX_NAME_YY = "异议";
    public final static String BDC_YWLX_NAME_YYZX = "异议注销";

    //处理服务设置(回写)
    public final static String YCSL_SERVICE_CODE_BDCSHCLJG = "ImmovableHandleResultService";
    public final static String YCSL_SERVICE_CODE_BDCDBCLJG = "ImmovableBookingResultService";
    public final static String YCSL_SERVICE_CODE_BDCYGJG = "ForecastElectronicCertificationService";
    public final static String YCSL_SERVICE_CODE_BDCQZJG = "ImmovableElectronicCertificate";
    public final static String YCSL_SERVICE_CODE_BDCDYJG = "MortgageElectronicCertificate";
    public final static String YCSL_SERVICE_CODE_BDCYGZXJG = "ForecastElectronicCertCancellation";
    public final static String YCSL_SERVICE_CODE_BDCQZZXJG = "ImmovableElectronicCertCancellation";
    public final static String YCSL_SERVICE_CODE_BDCDYZXJG = "MortgageElectronicCertCancellation";
    //处理服务设置(查询)
    public final static String YCSL_SERVICE_CODE_BDCDYSJ = "MortgageCertificateService";
    public final static String YCSL_SERVICE_CODE_BDCQLSJ = "OwnershipCertificateServiceWithItsRight";
}
