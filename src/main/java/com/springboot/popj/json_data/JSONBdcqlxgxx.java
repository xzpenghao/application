package com.springboot.popj.json_data;

import com.springboot.util.TimeUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

@Data
public class JSONBdcqlxgxx implements Serializable {
    private String infoId;                          //主键
    private String receiptNumber;                   //收件编号-sj_sjsq表主键
    private String acceptanceNumber;            //不动产内网受理编号
    private String immovableSite;               //不动产坐落（收件单坐落）
    private String immovableCertificateNo;       //不动产权证号
    private String houseCertificateNo;           //房产证号
    private String landCertificateNo;            //土地证号
    private String waterNumber;                  //水号
    private String electricNumber;               //电号
    private String gasNumber;                    //气号
    private String wiredNumber;                  //有线电视号
    private Date registrationDate;             //登记日期
    private String certificateType;              //证书类型(与sjsq中证书类型保持一致）
    private BigDecimal architecturalArea;               //建筑面积
    private BigDecimal houseArchitecturalArea;          //套内建筑面积
    private BigDecimal apportionmentArchitecturalArea;  //分摊建筑面积
    private BigDecimal atticArea;                       //阁楼面积
    private BigDecimal garageArea;                       //车库面积
    private BigDecimal storeroomArea;                       //储藏室面积
    private String houseObtainingWays;                   //房屋取得方式
    private BigDecimal houseObtainingPrice;             //房屋取得价格
    private String housePlanningPurpose;                //房屋规划用途
    private String houseValuationAmount;                //房屋评估金额
    private String houseType;                           //房屋类型
    private String houseNature;                         //房屋性质
    private String houseRightType;               //房屋权利类型
    private String houseRightNature;             //房屋权利性质

    private String landRightType;                //土地权利类型
    private String landRightNature;              //土地权利性质
    private Date landUseRightStartingDate;     //土地使用权起始日期
    private Date landUseRightEndingDate;       //土地使用终止日期
    private String landUseRightOwner;            //土地使用权人
    private String landUseTimeLimit;                //土地使用期限
    private String landPurpose;                     //土地用途
    private String landObtainWay;                   //土地取得途径
    private BigDecimal commonLandArea;                  //共有土地面积
    private BigDecimal singleLandArea;                  //独用土地面积
    private BigDecimal shareLandArea;                   //分摊土地面积
    private BigDecimal buildingParcelArea;                  //建筑宗地面积

    private String remarks;                      //备注附记
    private String serviceCode;                  //服务code
    private String dataJson;                     //原始数据json
    private String dataType;                     //数据类型（存量/新增）
    private String dataComeFromMode;                //数据获取方式
    private String preservationMan;                 //数据提交人
    private Date insertTime;                   //入库时间
    private String ext1;                         //扩展字段1
    private String ext2;                         //扩展字段2
    private String ext3;                         //扩展字段3
    private String glImmovableVoList;          //关联的不动产数据
    private String glObligeeVoList;            //关联的权利人数据
    private String glObligorVoList;            //关联的义务人数据
    private String itsRightVoList;          //他项权列表
    private String glAgentVoList;               //关联代理人
    private String glAgentObligorVoList;       //关联义务代理人数据

    private String eBookCert;                   //PDF电子证书
    private String bookPics;                    //宗地图，分层分户图
    /** 提供单位 */
    private String provideUnit;



    public void setRegistrationDate(String registrationDate) throws ParseException {
        this.registrationDate = TimeUtil.getTimeFromString(registrationDate);
    }

    public void setLandUseRightStartingDate(String landUseRightStartingDate) throws ParseException {
        this.landUseRightStartingDate = TimeUtil.getDateFromString(landUseRightStartingDate);
    }

    public void setLandUseRightEndingDate(String landUseRightEndingDate) throws ParseException {
        this.landUseRightEndingDate = TimeUtil.getDateFromString(landUseRightEndingDate);
    }

    public void setInsertTime(String insertTime) throws ParseException {
        this.insertTime =  TimeUtil.getTimeFromString(insertTime);
    }
}
