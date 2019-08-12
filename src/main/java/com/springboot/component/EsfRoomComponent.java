package com.springboot.component;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.config.Msgagger;
import com.springboot.entity.SdqEntry;
import com.springboot.popj.pub_data.RespServiceData;
import com.springboot.popj.pub_data.SJ_Info_Bdcqlxgxx;
import com.springboot.popj.pub_data.SJ_Sjsq;
import com.springboot.util.HttpClientUtils;
import com.springboot.util.SysPubDataDealUtil;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.List;

@Component
@Slf4j
public class EsfRoomComponent {


    @Value("${httpclient.windowAcceptanceIp}")
    private String windowAcceptanceIp; //一窗受理ip
    @Value("${httpclient.windowAcceptanceSeam}")
    private String windowAcceptanceSeam; //一窗受理接口
    @Value("${httpclient.ip}")
    private String ip;
    @Value("${httpclient.seam}")
    private String seam;

    /**
     * 二手房水电气回填数据自动接口
     * @param commonInterfaceAttributer
     * @return
     */
    public  ObjectRestResponse getAutoBackfillData(String commonInterfaceAttributer) throws ParseException {
        ObjectRestResponse resultRV = new ObjectRestResponse();
        SJ_Sjsq sjSjsq= SysPubDataDealUtil.parseReceiptData(commonInterfaceAttributer,null,null,null);
        if (null == sjSjsq){
            log.error(Msgagger.SHZH_BAD);
            return resultRV.data(Msgagger.SHZH_BAD);
        }
        SdqEntry sdqEntry=new SdqEntry();
        for (RespServiceData respServiceData:sjSjsq.getServiceDatas()) {
            //判断服务
            if (respServiceData.getServiceCode().equals(Msgagger.SHUISERVICE_CODE)) { //水号
                List<SJ_Info_Bdcqlxgxx> sj_info_bdcqlxgxxes=respServiceData.getServiceDataInfos();
                sdqEntry.setXdhh(sj_info_bdcqlxgxxes.get(0).getWaterNumber());
            } else if (respServiceData.getServiceCode().equals(Msgagger.QISERVICE_CODE)) {//气号
                List<SJ_Info_Bdcqlxgxx> sj_info_bdcqlxgxxes=respServiceData.getServiceDataInfos();
                sdqEntry.setXdhh(sj_info_bdcqlxgxxes.get(0).getGasNumber());
            } else if (respServiceData.getServiceCode().equals(Msgagger.DIANSERVICE_CODE)){ //电号
                List<SJ_Info_Bdcqlxgxx> sj_info_bdcqlxgxxes=respServiceData.getServiceDataInfos();
                sdqEntry.setXdhh(sj_info_bdcqlxgxxes.get(0).getElectricNumber());
            }
        }
        sdqEntry.setSlbh(sjSjsq.getRegisterNumber());
        JSONObject jsonObject=JSONObject.fromObject(sdqEntry);
        String pingtai= HttpClientUtils.getJsonData(jsonObject,"http://"+ip+":"+seam+"/api/services/app/BdcBizNotify/WriteBackSDQ");
        JSONObject pingObject=JSONObject.fromObject(pingtai);
        if ((boolean)pingObject.get("success")==true){
            resultRV.setData(Msgagger.AUTOINTECECG);
            return resultRV;
        }
        resultRV.setData(Msgagger.SHZH_BAD);
        return resultRV;
    }




}
