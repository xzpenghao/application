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

    @Value("${HouseSet.taxPush}")
    private String taxPushUrl;

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

        List<SJHouseSet> qlrfwtc = new ArrayList<>();
        List<SJHouseSet> ywrfwtc = new ArrayList<>();
        for (SJHouseSet sjHouseSet:sjHouseSetList) {
            if (BOOLEAN_NUMBER_FALSE.equals(sjHouseSet.getObligeeType())){
                // 类型为0 ，为权利人列表
                qlrfwtc.add(sjHouseSet);
            }else if (BOOLEAN_NUMBER_TRUE.equals(sjHouseSet.getObligeeType())){
                ywrfwtc.add(sjHouseSet);
            }else {
                log.info("数据无法识别：{}",JSON.toJSONString(sjHouseSet));
            }
        }
        //2.1   按格式生成数据
        JSONObject taxPushParams = new JSONObject();
        taxPushParams.put("qlrdata",genJsonData(slbh,qlrfwtc));
        taxPushParams.put("ywrdata",genJsonData(slbh,ywrfwtc));
        String result = "";
        BaseResponse response;
        //2.2   发送请求 捕获异常
        try {
            result = HttpUtil.post(taxPushUrl,taxPushParams);
            response = JSON.parseObject(result,BaseResponse.class);
            if (response.getStatus() != HttpStatus.HTTP_OK){
                throw new ZtgeoBizException(StrUtil.isBlank(response.getMessage())? "不动产套次信息推送税务服务失败" : response.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("税务推送失败,请求参数为:{}",JSON.toJSONString(taxPushParams));
            throw new ZtgeoBizException("不动产套次信息推送税务服务失败");
        }

        //3.    数据返回
        return response;
    }


    /**
     * 格式化查询参数
     * @param slbh 受理编号
     * @param sjHouseSetList 权利人或义务人房屋套次列表
     * @return 格式化的查询参数
     */
    private JSONObject genJsonData(String slbh,List<SJHouseSet> sjHouseSetList){
        if (CollUtil.isEmpty(sjHouseSetList)){
            return null;
        }
        JSONObject ycslxx = new JSONObject();
        ycslxx.put("slbh",slbh);
        ycslxx.put("Jyfbs",sjHouseSetList.get(0).getObligeeType());
        JSONArray fwtcxx = new JSONArray();
        for (SJHouseSet sjHouseSet:sjHouseSetList) {
            JSONObject data = new JSONObject();
            // 权证号 -> 不动产证号
            data.put("qzh",sjHouseSet.getRealEstateId());
            // 权证登记时间 -> 登记日期
            data.put("qzdjsj", DateUtil.formatDateTime(sjHouseSet.getRegisterDate()));
            // 权证房屋坐落 -> 权证房屋坐落
            data.put("rzfwzl",sjHouseSet.getSit());
            // 房屋性质 -> 房屋权利性质
            data.put("fwxz",sjHouseSet.getHouseRightNature());
            // 土地性质 -> 土地权利性质
            data.put("tdxz",sjHouseSet.getLandRightNature());
            // 权证登记面积 -> 房屋建筑面积
            data.put("qzdjmj",sjHouseSet.getArchitectureArea());
            // 总层数 -> 总层数
            data.put("zlcs",sjHouseSet.getTotalFloor());
            // 所在层数 -> 所在层
            data.put("szcs",sjHouseSet.getFloor());
            // 房屋用途 -> 房屋规划用途
            if (StrUtil.isBlank(sjHouseSet.getPlannedUsage())){
                data.put("fwyt","201");
            }else{
                switch (sjHouseSet.getPlannedUsage()){
                    case "房屋":
                        data.put("fwyt","200");
                        break;
                    case "居住":
                        data.put("fwyt","201");
                        break;
                    case "商业":
                        data.put("fwyt","202");
                        break;
                    case "办公":
                        data.put("fwyt","203");
                        break;
                    case "商住":
                        data.put("fwyt","204");
                        break;
                    case "附属建筑":
                        data.put("fwyt","205");
                        break;
                    case "工业":
                        data.put("fwyt","206");
                        break;
                    default:
                        data.put("fwyt","299");
                }
            }
            String string = StrUtil.toString(sjHouseSet.getObligeeInfoVoList());
            JSONArray qlrArray = JSONArray.parseArray(string);
            JSONArray qlrxx = new JSONArray();
            for (Object qlrData:qlrArray) {
                JSONObject qlr = new JSONObject();
                Obligee obligee = JSONObject.parseObject(JSON.toJSONString(qlrData),Obligee.class);
                qlr.put("qzr",obligee.getObligeeName());
                qlr.put("qzrzjhm",obligee.getObligeeId());
                qlrxx.add(qlr);
            }
            // 反解权利人信息列表
            data.put("qlrxx",qlrxx);

            // 加入集合
            fwtcxx.add(data);
        }

        JSONObject taxPushData = new JSONObject();
        taxPushData.put("ycslxx",ycslxx);
        taxPushData.put("fwtcxx",fwtcxx);
        return taxPushData;
    }

}
