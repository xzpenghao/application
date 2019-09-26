package com.springboot.component;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.config.Msgagger;
import com.springboot.constant.penghao.BizOrBizExceptionConstant;
import com.springboot.entity.DsEntity;
import com.springboot.entity.ParamEntity;
import com.springboot.popj.FwInfo;
import com.springboot.popj.GlImmovable;
import com.springboot.popj.RelatedPerson;
import com.springboot.popj.netSign.BusinessContract;
import com.springboot.popj.netSign.GlHouseBuyer;
import com.springboot.popj.netSign.GlHouseSeller;
import com.springboot.popj.pub_data.SJ_Sjsq;
import com.springboot.popj.pub_data.Sj_Info_Qsxx;
import com.springboot.popj.register.HttpRequestMethedEnum;
import com.springboot.util.chenbin.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * 针对于宿迁流程Component
 */
@Component
@Slf4j
public class SqRealEstateMortgageComponent {
    @Value("${sq.jyht.api_id}")
    private String jyapiId;
    @Value("${sq.jyht.from_user}")
    private String jyfromUser;
    @Value("${sq.qsxx.api_id}")
    private String qsapiId;
    @Value("${sq.qsxx.from_user}")
    private String qsfromUser;
    @Value("${sq.jyht.ip}")
    private String ip;
    @Value("${sq.jyht.post}")
    private String post;
    @Autowired
    private RealEstateMortgageComponent realEstateMortgageComponent;



    public ObjectRestResponse sqTaxation(DsEntity dsEntity){
        ObjectRestResponse resultRV = new ObjectRestResponse();
        Map<String,String> header=new HashMap<>();
        header.put("api_id",qsapiId);
        header.put("from_user",qsfromUser);
        Map<String,String> param=new HashMap<>();
        if (StringUtils.isNotEmpty(dsEntity.getHtbh())){
            param.put("HTBH",dsEntity.getHtbh());
        }else if (StringUtils.isNotEmpty(dsEntity.getSwjgdm())){
            param.put("swjgdm",dsEntity.getSwjgdm());
        }else if (StringUtils.isNotEmpty(dsEntity.getSwrydm())){
            param.put("swrydm",dsEntity.getSwrydm());
        }
        String resultJson="{\"formdata\":\"\",\"suc\":\"1\",\"griddata\":[{\"XTSPHM\":\"320180404000309258\",\"HTBH\":\"2320120180404946623\",\"NSRSBH\":\"32090219890612103X\",\"NSRMC\":\"周盛\",\"ZRFCSFBZ\":\"1\",\"DZSPHM\":\"320180404000309258\",\"PZZL_DM\":\"000001031\",\"PZZG_DM\":\"2321031171\",\"PZHM\":\"08133964\",\"SKSSQQ\":\"2018-04-04\",\"SKSSQZ\":\"2018-04-04\",\"ZSXM_DM\":\"10119\",\"ZSPM_DM\":\"101191211\",\"ZSZM_DM\":\"null\",\"ZSXM_MC\":\"契税\",\"ZSPM_MC\":\"存量房（商品住房买卖）\",\"ZSZM_MC\":\"null\",\"JSYJ\":2550000,\"SL_1\":0.03,\"SJJE\":51000,\"ZGSWSKFJ_DM\":\"13201150100\",\"ZSSWJG_DM\":\"13201151600\",\"SKSSSWJG_DM\":\"13201150000\",\"ZGSWSKFJ_MC\":\"国家税务总局南京市江宁区税务局税源管理一科\",\"ZSSWJG_MC\":\"国家税务总局南京市江宁区税务局第一税务所\",\"SKSSSWJG_MC\":\"国家税务总局南京市江宁区税务局\",\"kjrq\":\"2018-04-04\",\"BZ\":\" 共有人：江颖超，周盛 房源编号:F32011520180017792 房屋坐落地址:托乐嘉花园友邻居4幢801室 权属转移面积:149.33平米 合同日期:2018-04-04,\"},{\"XTSPHM\":\"320180404000309258\",\"HTBH\":\"2320120180404946623\",\"NSRSBH\":\"320911198901072829\",\"NSRMC\":\"江颖超\",\"ZRFCSFBZ\":\"1\",\"DZSPHM\":\"320180404000309258\",\"PZZL_DM\":\"000001031\",\"PZZG_DM\":\"2321031171\",\"PZHM\":\"08133964\",\"SKSSQQ\":\"2018-04-04\",\"SKSSQZ\":\"2018-04-04\",\"ZSXM_DM\":\"10119\",\"ZSPM_DM\":\"101191211\",\"ZSZM_DM\":\"null\",\"ZSXM_MC\":\"契税\",\"ZSPM_MC\":\"存量房（商品住房买卖）\",\"ZSZM_MC\":\"null\",\"JSYJ\":2550000,\"SL_1\":0.03,\"SJJE\":51000,\"ZGSWSKFJ_DM\":\"13201150100\",\"ZSSWJG_DM\":\"13201151600\",\"SKSSSWJG_DM\":\"13201150000\",\"ZGSWSKFJ_MC\":\"国家税务总局南京市江宁区税务局税源管理一科\",\"ZSSWJG_MC\":\"国家税务总局南京市江宁区税务局第一税务所\",\"SKSSSWJG_MC\":\"国家税务总局南京市江宁区税务局\",\"kjrq\":\"2018-04-04\",\"BZ\":\" 共有人：江颖超，周盛 房源编号:F32011520180017792 房屋坐落地址:托乐嘉花园友邻居4幢801室 权属转移面积:149.33平米 合同日期:2018-04-04,\"},{\"XTSPHM\":\"320180404000311789\",\"HTBH\":\"2320120180404946623\",\"NSRSBH\":\"420122197903230055\",\"NSRMC\":\"李军\",\"ZRFCSFBZ\":\"0\",\"DZSPHM\":\"320180404000311789\",\"PZZL_DM\":\"000001031\",\"PZZG_DM\":\"2321031171\",\"PZHM\":\"08133963\",\"SKSSQQ\":\"2018-04-04\",\"SKSSQZ\":\"2018-04-04\",\"ZSXM_DM\":\"10106\",\"ZSPM_DM\":\"101060902\",\"ZSZM_DM\":\"1010609022320001\",\"ZSXM_MC\":\"个人所得税\",\"ZSPM_MC\":\"个人房屋转让所得\",\"ZSZM_MC\":\"房屋转让附征1% \",\"JSYJ\":2550000,\"SL_1\":0.01,\"SJJE\":25500,\"ZGSWSKFJ_DM\":\"13201150100\",\"ZSSWJG_DM\":\"13201151600\",\"SKSSSWJG_DM\":\"13201150000\",\"ZGSWSKFJ_MC\":\"国家税务总局南京市江宁区税务局税源管理一科\",\"ZSSWJG_MC\":\"国家税务总局南京市江宁区税务局第一税务所\",\"SKSSSWJG_MC\":\"国家税务总局南京市江宁区税务局\",\"kjrq\":\"2018-04-04\",\"BZ\":\" 房源编号:F32011520180017792 房屋坐落地址:托乐嘉花园友邻居4幢801室 权属转移面积:149.33平米 合同日期:2018-04-04,\"}],\"msg\":\"查询成功\"}";
        //String json=HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpPost,"application/json","http://"+ip+":"+post+"/WxfcjyJmssendAction.do",param,header);
        JSONObject resultObject=JSONObject.fromObject(resultJson);
        JSONArray jsonArray=resultObject.getJSONArray("griddata");
        List<Sj_Info_Qsxx> qsxxList=new ArrayList<>();
        if (null != jsonArray){
            if (jsonArray.size() > 1){
                resultRV.setMessage(Msgagger.DSXX_GUODUO);
                resultRV.setStatus(20500);
                return resultRV;
            }
            for (int i = 0; i < jsonArray.size(); i++) {
                clTaxation(jsonArray.getJSONObject(i),qsxxList);
            }
        }
        return resultRV.data(qsxxList);
    }



    /**
     * 二手房交易合同处理
     * @param paramEntity
     * @return
     */
    public ObjectRestResponse sqTransactionContract(ParamEntity paramEntity) throws IOException {
        ObjectRestResponse resultRV = new ObjectRestResponse();
        Map<String,String> header=new HashMap<>();
        String data="";
//        Map<String,String> param=new HashMap<>();
//        if (StringUtils.isNotEmpty(paramEntity.getBdcdyh())){
//            data="bdcdyh="+paramEntity.getBdcdyh()+"&orgId="+1;
//        }else if (StringUtils.isNotEmpty(paramEntity.getHtbah())){
//           data="htbah="+paramEntity.getHtbah()+"&orgId="+1;
//        }else if (StringUtils.isNotEmpty(paramEntity.getComDate()) && StringUtils.isNotEmpty(paramEntity.getBdcdyh())){
//            data="bdcdyh="+paramEntity.getBdcdyh()+"&orgId="+1+"&ComDate="+paramEntity.getComDate();
//        }else if (StringUtils.isNotEmpty(paramEntity.getHtbah()) && StringUtils.isNotEmpty(paramEntity.getComDate())){
//            data="htbah="+paramEntity.getHtbah()+"orgId="+1+"&ComDate="+paramEntity.getComDate();
//        }
        String param= com.alibaba.fastjson.JSONObject.toJSONString(paramEntity, SerializerFeature.PrettyFormat);
//        String resultJosn="{\"dataInfo\":{\"sInfo\":\"[{\\\"BDCDYH\\\":null,\\\"DJ\\\":6310.34,\\\"DLMC\\\":null,\\\"DZHTFullUrl\\\":" +
//                "\\\"http://222.187.193.194:8054/sqfc/docx/TNAV?action=runtime.Preview&tmpid=5&docid=20002428&syflag=1\\\"," +
//                "\\\"FWCX\\\":null,\\\"FWDY\\\":5031,\\\"FWFH\\\":\\\"1805\\\",\\\"FWJG\\\":\\\"钢筋混凝土结构\\\",\\\"FWLX\\\":11," +
//                "\\\"FWQDSJ\\\":null,\\\"FWXZ\\\":\\\"市场化商品房\\\",\\\"FWYT\\\":11,\\\"FWZH\\\":5031,\\\"GMFDHHM\\\":\\\"18751080832\\\"," +
//                "\\\"GMFDZ\\\":null,\\\"GMFGJ\\\":null,\\\"GMFXM\\\":\\\"王宁,袁成波\\\",\\\"GMFZJHM\\\":\\\"321302198904190021,321323198806192818\\\"," +
//                "\\\"GMFZJLX\\\":\\\"身份证\\\",\\\"HTBAH\\\":\\\"SQCLF-201907080073\\\",\\\"HTJE\\\":256200,\\\"HTQDRQ\\\":\\\"2019-07-08\\\"," +
//                "\\\"JDXZ\\\":null,\\\"JZMJ\\\":40.6,\\\"LCZS\\\":\\\"22\\\",\\\"MFDHHM\\\":\\\"19825762768\\\",\\\"MFDZ\\\":null,\\\"MFGJ\\\":null," +
//                "\\\"MFXM\\\":\\\"陈莹石,李五将\\\",\\\"MFZJHM\\\":\\\"320819197306302918,321302197709228421\\\",\\\"MFZJLX\\\":\\\"身份证\\\"," +
//                "\\\"PGJG\\\":null,\\\"SZLC\\\":18,\\\"XQMC\\\":null,\\\"XZQH\\\":\\\"321302\\\"," +
//                "\\\"YWLX\\\":\\\"存量房网签合同备案\\\",\\\"ZJLXDH\\\":null}]\"},\"success\":true,\"errorCode\":\"0\"}\n";
        String resultJosn=HttpClientUtil.post(jyfromUser,jyapiId,param,"http://"+ip+":"+post+"/sqservice/sh/secondInfo");
        if (resultJosn.equals("0")){
            resultRV.setMessage(Msgagger.INTERCE_NULL);
            resultRV.setStatus(20500);
            return  resultRV;
        }
        JSONObject jyObject=JSONObject.fromObject(resultJosn);
        JSONObject dataInfo=jyObject.getJSONObject("dataInfo");
        JSONArray sInfoArray=dataInfo.getJSONArray("sInfo");
        List<BusinessContract> businessContractList=new ArrayList<>();
        if (null != sInfoArray) {
            for (int i = 0; i < sInfoArray.size(); i++) {
                clTransactionContract(sInfoArray.getJSONObject(i), businessContractList);
            }
        }
        return resultRV.data(businessContractList);
    }


    private void clTaxation(JSONObject jsonObject, List<Sj_Info_Qsxx> sj_info_qsxxList){
        Sj_Info_Qsxx sjInfoQsxx=new Sj_Info_Qsxx();
        sjInfoQsxx.setXtsphm(jsonObject.getString("XTSPHM"));
        sjInfoQsxx.setHtbh(jsonObject.getString("HTBH"));
        sjInfoQsxx.setNsrsbh(jsonObject.getString("NSRSBH"));
        sjInfoQsxx.setNsrmc(jsonObject.getString("NSRMC"));
        sjInfoQsxx.setZrfcsfbz(jsonObject.getString("ZRFCSFBZ"));
        sjInfoQsxx.setDzsphm(jsonObject.getString("DZSPHM"));
        sjInfoQsxx.setPzzlDm(jsonObject.getString("PZZL_DM"));
        sjInfoQsxx.setPzzgDm(jsonObject.getString("PZZG_DM"));
        sjInfoQsxx.setPzhm(jsonObject.getString("PZHM"));
        sjInfoQsxx.setSkssqq(jsonObject.getString("SKSSQQ"));
        sjInfoQsxx.setSkssqz(jsonObject.getString("SKSSQZ"));
        sjInfoQsxx.setZsxmDm(jsonObject.getString("ZSXM_DM"));
        sjInfoQsxx.setZspmDm(jsonObject.getString("ZSPM_DM"));
        sjInfoQsxx.setZszmDm(jsonObject.getString("ZSZM_DM"));
        sjInfoQsxx.setZsxmMc(jsonObject.getString("ZSXM_MC"));
        sjInfoQsxx.setZspmMc(jsonObject.getString("ZSPM_MC"));
        sjInfoQsxx.setZszmMc(jsonObject.getString("ZSZM_MC"));
        sjInfoQsxx.setJsyj(jsonObject.getString("JSYJ"));
        sjInfoQsxx.setSl(jsonObject.getString("SL_1"));
        sjInfoQsxx.setSjje(jsonObject.getLong("SJJE"));
        sjInfoQsxx.setZgswskfjDm(jsonObject.getString("ZSSWJG_DM"));
        sjInfoQsxx.setZsswjgDm(jsonObject.getString("SKSSSWJG_DM"));
        sjInfoQsxx.setSkssswjgDm(jsonObject.getString("ZGSWSKFJ_MC"));
        sjInfoQsxx.setZgswskfjMc(jsonObject.getString("ZSSWJG_MC"));
        sjInfoQsxx.setZsswjgMc(jsonObject.getString("SKSSSWJG_MC"));
        sjInfoQsxx.setKjrq(jsonObject.getString("kjrq"));
        sjInfoQsxx.setBz(jsonObject.getString("BZ"));
        sjInfoQsxx.setProvideUnit(Msgagger.GXPT);
        sjInfoQsxx.setDataComeFromMode(Msgagger.INTERCE_DY);
        sjInfoQsxx.setDataJson(jsonObject.toString());
        sj_info_qsxxList.add(sjInfoQsxx);
    }






    /**
     * 处理数据
     * @param jsonObject
     * @param businessContractList
     */
    private  void clTransactionContract(JSONObject jsonObject, List<BusinessContract> businessContractList){
        BusinessContract businessContract=new BusinessContract();
        businessContract.setContractRecordNumber(jsonObject.getString("HTBAH"));
        businessContract.setContractSignTime(jsonObject.getString("HTQDRQ"));
        businessContract.setContractAmount(jsonObject.getString("HTJE"));
        businessContract.setProvideUnit(Msgagger.GXPT);
        businessContract.setDataComeFromMode(Msgagger.INTERCE_DY);
        businessContract.setDataJson(jsonObject.toString());
        List<GlImmovable> glImmovableList=new ArrayList<>();
        GlImmovable glImmovable=new GlImmovable();
        glImmovable.setImmovableType(Msgagger.FANGDI);
        FwInfo fwInfo=new FwInfo();
        fwInfo.setSeatNumber(jsonObject.getString("FWZH"));
        fwInfo.setRoomMark(jsonObject.getString("FWFH"));
        fwInfo.setTotalStorey(jsonObject.getString("LCZS"));
        fwInfo.setLocationStorey(jsonObject.getString("SZLC"));
        fwInfo.setArchitecturalArea(jsonObject.getString("JZMJ"));
        fwInfo.setImmovableUnitNumber(jsonObject.getString("BDCDYH"));
        glImmovable.setFwInfo(fwInfo);
        glImmovableList.add(glImmovable);
        businessContract.setGlImmovableVoList(glImmovableList);
        //权利人
        List<GlHouseSeller> glHouseSellerVoList=new ArrayList<>();
        GlHouseSeller glHouseSeller=new GlHouseSeller();
        glHouseSeller.setObligeeName(jsonObject.getString("MFXM"));
        glHouseSeller.setObligeeType(BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_QLR);
        RelatedPerson relatedPerson=new RelatedPerson();
        relatedPerson.setObligeeDocumentType(realEstateMortgageComponent.getZjlb(jsonObject.getString("MFZJLX")));
        relatedPerson.setObligeeName(jsonObject.getString("MFXM"));
        relatedPerson.setObligeeDocumentNumber(jsonObject.getString("MFZJHM"));
        glHouseSeller.setRelatedPerson(relatedPerson);
        glHouseSellerVoList.add(glHouseSeller);
        businessContract.setGlHouseSellerVoList(glHouseSellerVoList);
        //购买人
        List<GlHouseBuyer> glHouseBuyerList=new ArrayList<>();
        GlHouseBuyer glHouseBuyer=new GlHouseBuyer();
        glHouseBuyer.setObligeeName(jsonObject.getString("GMFXM"));
        glHouseBuyer.setObligeeType(BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_GFZ);
        RelatedPerson gmrelatedPerson=new RelatedPerson();
        gmrelatedPerson.setObligeeDocumentType(realEstateMortgageComponent.getZjlb(jsonObject.getString("GMFZJLX")));
        gmrelatedPerson.setObligeeName(jsonObject.getString("GMFXM"));
        gmrelatedPerson.setObligeeDocumentNumber(jsonObject.getString("GMFZJHM"));
        glHouseBuyer.setRelatedPerson(relatedPerson);
        glHouseBuyerList.add(glHouseBuyer);
        businessContract.setGlHouseBuyerVoList(glHouseBuyerList);
        businessContractList.add(businessContract);
    }



}
