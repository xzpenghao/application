package com.springboot.service.shike.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.BaseResponse;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.SJHouseSet;
import com.springboot.service.shike.SjHouseSetService;
import com.springboot.util.chenbin.ErrorDealUtil;
import com.springboot.vo.Obligee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.springboot.constant.AdminCommonConstant.BOOLEAN_NUMBER_FALSE;
import static com.springboot.constant.AdminCommonConstant.BOOLEAN_NUMBER_TRUE;

/**
 * @author sk
 * @version 2020/5/21
 * description：房屋套次 业务层实现
 */
@Slf4j
@Service
public class SjHouseSetServiceImpl implements SjHouseSetService {

    @Value("${HouseSet.Bdc}")
    private String houseSetBdc;

    @Value("${HouseSet.Zj}")
    private String houseSetZj;

    @Value("${HouseSet.taxPush}")
    private String taxPushUrl;

    private static final String ORG_BDC = "不动产";
    private static final String ORG_ZJ = "住建";

    public static final String LX_QLR = "权利人";
    public static final String LX_YWR = "义务人";

    /**
     * 描述：根据权利人集合查询房屋套次
     * 作者：sk
     * 日期：2020/5/22
     * 参数：权利人集合 格式为[{"obligeeName":"xxx","obligeeIdType":"身份证","obligeeId":"xxx"}]
     * 返回：房屋套次集合
     * 更新记录：更新人：{}，更新日期：{}
    */
    @Override
    public List<SJHouseSet> queryBdcHouseSetByObligeeList(List<Obligee> obligeeList){
        //1.    参数合法性验证
        if (CollUtil.isEmpty(obligeeList)){
            throw new ZtgeoBizException("参数为空");
        }
        //2.    组织参数，发送请求
        //义务人信息列表 0 卖方（义务人）
        List<Obligee> obligors = new ArrayList<>();
        //权利人信息列表 1买方（权利人）
        List<Obligee> obligees = new ArrayList<>();
        for (Obligee obligee:obligeeList) {
            if (BOOLEAN_NUMBER_FALSE.equals(obligee.getObligeeType())){
                obligors.add(obligee);
            }else {
                obligees.add(obligee);
            }
        }
        //3     返回结果
        List<SJHouseSet> sjHouseSets = new ArrayList<>();
        //3.1   义务人查询
        sendBdcHouseSetQuery(obligors,sjHouseSets);
        //3.1   权利人查询
        sendBdcHouseSetQuery(obligees,sjHouseSets);
        //3.3   为空直接返回
        return sjHouseSets;
    }

    /**
     * 描述：根据权利人集合查询住建房屋套次
     * 作者：sk
     * 日期：2020/9/11
     * 参数：[obligeeList 权利人集合]
     * 返回：房屋套次集合
     * 更新记录：更新人：{}，更新日期：{}
     */
    @Override
    public List<SJHouseSet> queryZjHouseSetByObligeeList(List<Obligee> obligeeList) {
        //1.    参数合法性验证
        if (CollUtil.isEmpty(obligeeList)){
            throw new ZtgeoBizException("参数为空");
        }
        //2.    组织参数，发送请求
        //义务人信息列表 0 卖方（义务人）
        List<Obligee> obligors = new ArrayList<>();
        //权利人信息列表 1买方（权利人）
        List<Obligee> obligees = new ArrayList<>();
        for (Obligee obligee:obligeeList) {
            if (BOOLEAN_NUMBER_FALSE.equals(obligee.getObligeeType())){
                obligors.add(obligee);
            }else {
                obligees.add(obligee);
            }
        }
        //3     返回结果
        List<SJHouseSet> sjHouseSets = new ArrayList<>();
        //3.1   义务人查询
        sendZjHouseSetQuery(obligors,sjHouseSets,LX_YWR);
        //3.1   权利人查询
        sendZjHouseSetQuery(obligees,sjHouseSets,LX_QLR);
        //3.3   为空直接返回
        return sjHouseSets;
    }

    /**
     * 描述：住建套次接口查询
     * 作者：sk
     * 日期：2020/9/9
     * 参数：[obligeeList 权利人集合, sjHouseSets套次集合]
     * 更新记录：更新人：{}，更新日期：{}
    */
    private void sendZjHouseSetQuery(List<Obligee> obligeeList, List<SJHouseSet> sjHouseSets,String qlrlx){
        //1.    非空验证
        if (CollUtil.isEmpty(obligeeList)){
            return;
        }
        //2     构造参数
        Map<String,Object> params = new HashMap<>();
        //2.1   构造参数
        JSONArray qlrArray = new JSONArray();
        for (Obligee obligee:obligeeList) {
            JSONObject qlr = new JSONObject();
            qlr.put("xm",obligee.getObligeeName());
            qlr.put("zjhm",obligee.getObligeeId());
            qlrArray.add(qlr);
        }
        JSONObject qlrParams = new JSONObject();
        qlrParams.put("qlr",qlrArray);
        //2.2   参数格式化
        String formatParams = JSON.toJSONString(qlrParams);
        log.info("格式化后的参数:{}",formatParams);
        String result = "";
        //2.3   发送请求 捕获异常
        try {
            result = HttpUtil.post(houseSetZj,formatParams);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("住建套次查询失败，权利人集合：{}",obligeeList);
            throw new ZtgeoBizException("住建套次查询失败");
        }
        log.info("住建套次查询请求结果：{}",result);
        //3.    组织并返回结果
        if (StrUtil.isBlank(result)){
            return;
        }
        //3.1   获取指定参数
        JSONArray jsonArray = JSON.parseObject(result).getJSONArray("data");
        //3.2   不为空时遍历参数并将权利人集合格式化为字符串
        if (CollUtil.isNotEmpty(jsonArray)){
            for (Object data : jsonArray) {
                SJHouseSet sjHouseSet = new SJHouseSet(data,qlrlx);
                sjHouseSet.setObligeeType(obligeeList.get(0).getObligeeType());
                sjHouseSets.add(sjHouseSet);
            }
        }
    }

    /**
     * 描述：
     * 作者：sk
     * 日期：2020/9/11
     * 参数：[obligeeList 权利人集合, sjHouseSets套次集合]
     * 返回：
     * 更新记录：更新人：{}，更新日期：{}
     */
    private void sendBdcHouseSetQuery(List<Obligee> obligeeList, List<SJHouseSet> sjHouseSets){
        //1.    非空验证
        if (CollUtil.isEmpty(obligeeList)){
            return;
        }
        //2     构造参数
        Map<String,Object> params = new HashMap<>();
        //2.1   构造参数
        params.put("familyMemberList",obligeeList);
        //2.2   参数格式化
        String formatParams = JSON.toJSONString(params);
        log.info("格式化后的参数:{}",formatParams);
        String result = "";
        //2.3   发送请求 捕获异常
        try {
            result = HttpUtil.post(houseSetBdc,formatParams);
            log.error("不动产套次查询结果：{}",result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("不动产套次查询失败，权利人集合：{}",obligeeList);
            throw new ZtgeoBizException("不动产套次查询失败");
        }
        //3.    组织并返回结果
        if (StrUtil.isBlank(result)){
            return;
        }
        //3.1   获取指定参数
        JSONArray jsonArray = JSON.parseObject(result).getJSONArray("data");
        //3.2   不为空时遍历参数并将权利人集合格式化为字符串
        if (CollUtil.isNotEmpty(jsonArray)){
            for (Object data : jsonArray) {
                SJHouseSet sjHouseSet = JSON.parseObject(JSON.toJSONString(data),SJHouseSet.class);
                sjHouseSet.setObligeeInfoVoList(JSON.toJSONString(sjHouseSet.getObligeeInfoVoList()));
                sjHouseSet.setObligeeType(obligeeList.get(0).getObligeeType());
                // 11/16 沭阳新增需求
                //  不动产单元号列表 -> 不动产单元号
                if (CollUtil.isNotEmpty(sjHouseSet.getRealEstateUnitIdList())){
                    sjHouseSet.setImmovableUnicode(JSONArray.toJSONString(sjHouseSet.getRealEstateUnitIdList()));
                }
                //  房屋状态
                if ("true".equals(sjHouseSet.getIsMortgage())){
                    sjHouseSet.setStatus("已抵押");
                }else if ("true".equals(sjHouseSet.getIsAttach())){
                    sjHouseSet.setStatus("已查封");
                }else if ("true".equals(sjHouseSet.getIsDissent())){
                    sjHouseSet.setStatus("已异议");
                }else {
                    sjHouseSet.setStatus("正常");
                }
                //  当不动产证号不存在时,取预告证明号为不动产证号
                if (StrUtil.isBlank(sjHouseSet.getRealEstateId())){
                    sjHouseSet.setRealEstateId(sjHouseSet.getVormerkungId());
                }

                sjHouseSets.add(sjHouseSet);
            }
        }
    }


    /**
     * 描述：税务推送
     * 作者：sk
     * 日期：2020/5/27
     * 参数：受理编号 与 套次集合 map
     * 返回：推送结果
     * 更新记录：更新人：{}，更新日期：{}
     */
    @Override
    public BaseResponse taxPush(Map<String,String> taxPushMap) {
        //1.    参数合法性验证
        if (CollUtil.isEmpty(taxPushMap)){
            throw new ZtgeoBizException("参数为空");
        }
        //2.    数据处理与格式化
        String slbh = taxPushMap.get("slbh");
        List<SJHouseSet> sjHouseSetList = JSONArray.parseArray(StrUtil.toString(taxPushMap.get("sjHouseSetList")),SJHouseSet.class);

        //2.1   格式化数据来源
        List<SJHouseSet> bdcHouseSet = new ArrayList<>();
        List<SJHouseSet> zjHouseSet = new ArrayList<>();
        for (SJHouseSet sjHouseSet:sjHouseSetList) {
            if(ORG_BDC.equals(sjHouseSet.getOrganization())){
                bdcHouseSet.add(sjHouseSet);
            }else if(ORG_ZJ.equals(sjHouseSet.getOrganization())){
                zjHouseSet.add(sjHouseSet);
            }else {
                log.info("数据来源无法识别：{}",JSON.toJSONString(sjHouseSet));
            }
        }

        //3.   按格式生成数据
        JSONObject taxPushParams = new JSONObject();
        taxPushParams.put("suc","0");
        taxPushParams.put("msg","查询成功");

        //3.1  数据对象
        JSONObject dataJson = new JSONObject();
        //3.1.1   一窗受理信息
        JSONObject ycslJson = new JSONObject();
        ycslJson.put("slbh",slbh);
        dataJson.put("ycslxx",ycslJson);
        //3.1.2   不动产信息
        JSONObject bdcxxJson = new JSONObject();
        if (CollUtil.isEmpty(bdcHouseSet)){
            bdcxxJson.put("status","1");
            bdcxxJson.put("errorMsg","不动产查询信息缺失");
        }else {
            bdcxxJson.put("status","0");
            bdcxxJson.put("fwtcxx",genJsonData(bdcHouseSet));
        }
        dataJson.put("bdcxx",bdcxxJson);
        //3.1.3   住建信息
        JSONObject zjxxJson = new JSONObject();
        if (CollUtil.isEmpty(zjHouseSet)){
            zjxxJson.put("status","1");
            zjxxJson.put("errorMsg","房屋交易查询信息缺失");
        }else{
            zjxxJson.put("status","0");
            zjxxJson.put("fwtcxx",genJsonData(zjHouseSet));
        }
        dataJson.put("zjxx",zjxxJson);
        // 推送参数信息构造完成
        taxPushParams.put("data",dataJson);

        log.info("税务推送参数:{}",taxPushParams);
        String result = "";
        BaseResponse response;
        //2.2   发送请求 捕获异常
        try {
            result = HttpUtil.post(taxPushUrl,JSONObject.toJSONString(taxPushParams));
            log.info("套次数据税务推送响应结果："+result);
            response = JSON.parseObject(result,BaseResponse.class);
            if (response.getStatus() != HttpStatus.HTTP_OK){
                throw new ZtgeoBizException(StrUtil.isBlank(response.getMessage())? "不动产套次信息推送税务服务失败" : response.getMessage());
            }
        } catch (Exception e) {
            log.error("税务推送失败,请求参数为:{}",JSON.toJSONString(taxPushParams));
            log.error("原始异常信息："+ ErrorDealUtil.getErrorInfo(e));
            throw new ZtgeoBizException("不动产套次信息推送税务服务失败");
        }

        //3.    数据返回
        return response;
    }


    /**
     * 格式化查询参数
     * @param sjHouseSetList 权利人或义务人房屋套次列表
     * @return 格式化的查询参数
     */
    private JSONObject genJsonData(List<SJHouseSet> sjHouseSetList){
        //  1. 验证参数合法性
        if (CollUtil.isEmpty(sjHouseSetList)){
            return null;
        }

        //  2.  格式化数据来源
        //  2.1 权利人房屋套次信息
        List<SJHouseSet> qlrfwtc = new ArrayList<>();
        //  2.1 义务人房屋套次信息
        List<SJHouseSet> ywrfwtc = new ArrayList<>();
        for (SJHouseSet sjHouseSet:sjHouseSetList) {
            if(BOOLEAN_NUMBER_TRUE.equals(sjHouseSet.getObligeeType())){
                qlrfwtc.add(sjHouseSet);
            }else if (BOOLEAN_NUMBER_FALSE.equals(sjHouseSet.getObligeeType())){
                ywrfwtc.add(sjHouseSet);
            }else {
                log.info("数据来源无法识别：{}",JSON.toJSONString(sjHouseSet));
            }
        }

        //  3.  格式化返回值
        JSONObject fwtcxx = new JSONObject();
        //  3.1 格式化权利人信息
        fwtcxx.put("qlrxx",sortTcInfo(qlrfwtc));
        //  3.1 格式化义务人信息
        fwtcxx.put("ywrxx",sortTcInfo(ywrfwtc));

        //  4.  返回房屋套次信息
        return fwtcxx;
    }

    /**
     * 描述：格式化参数内容
     * 作者：sk
     * 日期：2020/9/10
     * 参数：[fwtcList 套次集合]
     * 返回：格式化后的套次集合
     * 更新记录：更新人：{}，更新日期：{}
     */
    private JSONArray sortTcInfo(List<SJHouseSet> fwtcList){
        JSONArray jsonArray = new JSONArray();
        for (SJHouseSet sjHouseSet:fwtcList) {
            JSONObject data = new JSONObject();
            // 权证号 -> 不动产证号
            data.put("qzh",formatNull(sjHouseSet.getRealEstateId()));
            // 权证登记时间 -> 登记日期
            data.put("qzdjsj", sjHouseSet.getRegisterDate() == null?"":DateUtil.formatDateTime(sjHouseSet.getRegisterDate()));
            // 权证房屋坐落 -> 权证房屋坐落
            data.put("rzfwzl",formatNull(sjHouseSet.getSit()));
            // 房屋性质 -> 房屋权利性质
            data.put("fwxz",formatNull(sjHouseSet.getHouseRightNature()));
            // 土地性质 -> 土地权利性质
            data.put("tdxz",formatNull(sjHouseSet.getLandRightNature()));
            // 权证登记面积 -> 房屋建筑面积
            data.put("qzdjmj",formatNull(sjHouseSet.getArchitectureArea()));
            // 总层数 -> 总层数
            data.put("zlcs",formatNull(sjHouseSet.getTotalFloor()));
            // 所在层数 -> 所在层
            data.put("szcs",formatNull(sjHouseSet.getFloor()));
            // 房屋用途 -> 房屋规划用途
            data.put("fwyt",formatNull(sjHouseSet.getPlannedUsage()));
            // 权利人信息
            String string = StrUtil.toString(sjHouseSet.getObligeeInfoVoList());
            JSONArray qlrArray = JSONArray.parseArray(string);
            StringBuilder qzr = new StringBuilder();
            StringBuilder qzrzjhm = new StringBuilder();
            for (int i = 0; i < qlrArray.size() ; i++) {
                Object qlrData = qlrArray.get(i);
                Obligee obligee = JSONObject.parseObject(JSON.toJSONString(qlrData),Obligee.class);
                qzr.append(obligee.getObligeeName());
                qzrzjhm.append(obligee.getObligeeId());
                if (i < qlrArray.size() -1 ){
                    qzr.append(",");
                    qzrzjhm.append(",");
                }
            }
            //  权证人姓名
            data.put("qzr",qzr);
            //  权证人证件号码
            data.put("qzrzjhm",qzrzjhm);
            // 加入集合
            jsonArray.add(data);
        }
        return jsonArray;
    }

    /**
     * 格式化空值
     * @param data 数据
     * @return 格式化数据
     */
    private String formatNull(String data){
        return StrUtil.isBlank(data)?"":data;
    }
}
