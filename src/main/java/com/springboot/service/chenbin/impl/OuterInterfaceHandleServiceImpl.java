package com.springboot.service.chenbin.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.google.common.collect.Maps;
import com.springboot.component.chenbin.file.FromFTPDownloadComponent;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.SJ_Fjfile;
import com.springboot.entity.chenbin.personnel.PersonnelUnitEntity;
import com.springboot.entity.chenbin.personnel.punit.PersonCheckEntity;
import com.springboot.entity.chenbin.personnel.punit.PersonEntity;
import com.springboot.entity.chenbin.personnel.req.PersonnelUnitReqEntity;
import com.springboot.entity.chenbin.personnel.req.ReqForPersonBase64Entity;
import com.springboot.entity.chenbin.personnel.req.ReqForPersonEntity;
import com.springboot.entity.chenbin.personnel.resp.JDPersonnelUnitEntity;
import com.springboot.entity.chenbin.personnel.resp.PersonnelResponseEntity;
import com.springboot.entity.chenbin.personnel.resp.PersonnelResponseListEntity;
import com.springboot.entity.chenbin.personnel.resp.PersonnelResponseSingleEntity;
import com.springboot.feign.ExchangeWithOuterFeign;
import com.springboot.service.chenbin.OuterInterfaceHandleService;
import com.springboot.vo.ContractQueryVo;
import com.springboot.vo.TaxAttachment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sun.misc.BASE64Encoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("outerIntfService")
public class OuterInterfaceHandleServiceImpl implements OuterInterfaceHandleService {

    @Autowired
    private FromFTPDownloadComponent fromFTPDownloadComponent;

    @Autowired
    private ExchangeWithOuterFeign exchangeFeign;

    @Override
    public Object getPersonnelUnits(PersonnelUnitReqEntity personnelUnitReq) throws UnsupportedEncodingException {
        String mc = personnelUnitReq.getMc();
        String zjhm = personnelUnitReq.getZjhm();
        Map<String,Object> params = new HashMap<String,Object>();

        if(StringUtils.isNotBlank(personnelUnitReq.getZjlx())){
            String code = "";
            JDPersonnelUnitEntity rv_jd = null;
            boolean isBase64 = false;
            switch (personnelUnitReq.getZjlx()){
                case "身份证":
                    List<ReqForPersonEntity> ps = new ArrayList<ReqForPersonEntity>();
                    ReqForPersonBase64Entity p = new ReqForPersonBase64Entity();
                    p.setXm(mc);
                    p.setSfzh(zjhm);
                    code = "ga_info_check";
                    if(StringUtils.isNotBlank(personnelUnitReq.getFjlj())){
                        code = "ga_check";
                        isBase64 = true;
                        SJ_Fjfile fj = new SJ_Fjfile();
                        if (fromFTPDownloadComponent.downFile(
                                personnelUnitReq.getFjlj().substring(0, personnelUnitReq.getFjlj().lastIndexOf("\\")),
                                personnelUnitReq.getFjlj().substring(personnelUnitReq.getFjlj().lastIndexOf("\\") + 1),
                                fj
                        )) {
                            BASE64Encoder encoder = new BASE64Encoder();
                            String base64 = encoder.encode(fj.getFileContent());
                            p.setXczp(base64);
                        }
                    } else if(StringUtils.isNotBlank(personnelUnitReq.getXczpBase64())){
                        code = "ga_check";
                        isBase64 = true;
                        System.out.println("进入人像比对参数验证");
                        log.info("进入人像比对参数验证");
                        p.setXczp(personnelUnitReq.getXczpBase64());
                    }
                    ps.add(p);
                    params.put("datalist",ps);
                    if(isBase64){
                        System.out.println("执行人像比对接口");
                        log.info("执行人像比对接口");
                        rv_jd = exchangeFeign.rkkrxbd(params);
                    }else {
                        String encSp =  URLEncoder.encode(JSONObject.toJSONString(params), "UTF-8");
                        log.info("转码后，人数据："+encSp);
                        rv_jd = exchangeFeign.rkkjzxxcx(encSp);
                    }
                    break;
                default:
                    throw new ZtgeoBizException("公安接口输入证件类型错误");
            }

            if(rv_jd!=null){
                if(StringUtils.isNotBlank(rv_jd.getCode()) && rv_jd.getCode().equals("200")){
                    JSONObject result_obj = JSONObject.parseObject(rv_jd.getData());
                    String status = result_obj.getString("status");
                    String msg = result_obj.getString("msg");
                    if(StringUtils.isNotBlank(status)){
                        if(status.equals("0")){
                            JSONArray oa = result_obj.getJSONArray("data");
                            if(isBase64){
                                List<PersonCheckEntity> as = oa.toJavaList(PersonCheckEntity.class);
                                for(PersonCheckEntity a:as){
                                    a.setEntityType(code);
                                }
                                return as;
                            }else{
                                List<PersonEntity> bs = oa.toJavaList(PersonEntity.class);
                                for(PersonEntity b:bs){
                                    b.setEntityType(code);
                                }
                                return bs;
                            }
                        }else if(status.equals("-1") || status.equals("-2") || status.equals("-3")){
                            throw new ZtgeoBizException(msg);
                        }else{
                            throw new ZtgeoBizException("业务接口响应异常，状态码不在识别范围");
                        }
                    }else{
                        throw new ZtgeoBizException("业务接口响应异常，缺失状态码");
                    }
                }else{
                    throw new ZtgeoBizException(rv_jd.getMsg());
                }
            } else {
                throw new ZtgeoBizException("接口调用失败，返回值为null");
            }
        }else{
            throw new ZtgeoBizException("证件类型不可为空");
        }
    }

    @Override
    public Object getContractRecordxx(String keycode, String qyslh) {
        Map<String,Object> params = Maps.newHashMap();
        params.put("keycode",keycode);
        params.put("qyslh",qyslh);
        Map<String,Object> map = exchangeFeign.qyslhfrdbaxx(params); //契约号获取契约备案号
        log.info("查询参数:{}", JSON.toJSONString(map));
        if (null == map){
            throw new ZtgeoBizException("查询无响应");
        }
        log.info("查询结果:{}",JSON.toJSONString(map));

        //处理交易返回数据
        if (map.get("statusCode").toString().equals("-1")){
            throw new ZtgeoBizException("查询信息异常："+map.get("msg"));
        }
        ContractQueryVo contractQueryVo = JSONObject.parseObject(JSONObject.toJSONString(map.get("data")),ContractQueryVo.class);
        if (null == contractQueryVo){
            throw new ZtgeoBizException("查询数据为空");
        }
        return  contractQueryVo;
    }

    public void dododod(){

//        case "统一社会信用代码":
//        String top_1 = zjhm.substring(0,1);
//        String top_2 = zjhm.substring(1,2);
//        switch (top_1){
//            case "1":
//
//                break;
//            case "5":
//                params.put("org_name",mc);
//                params.put("usc_code",zjhm);
//                Map<String,Object> result = null;
//                switch (top_2){
//                    case "1":       //社会团体
//                        code = "mz_social_group_check";
//                        //调用
//                        result = exchangeFeign.shttfrdjzscx(params);
//                        break;
//                    case "2":       //民办非企业单位
//                        code = "mz_private_unit_check";
//                        //调用
//                        result = exchangeFeign.mbfqydwdjzscx(params);
//                        break;
//                    case "3":       //基金会
//                        code = "mz_foundation_check";
//                        //调用
//                        result = exchangeFeign.jjhfrdjzscx(params);
//                        break;
//                    case "9":
//                        throw new ZtgeoBizException("暂不支持验证的机构编号");
//                    default:
//                        throw new ZtgeoBizException("暂不支持验证的机构编号");
//                }
//                if(result!=null){
//                    String status = (String)result.get("status");
//                    String msg = (String)result.get("msg");
//                    if(StringUtils.isNotBlank(status) && status.equals("0")){
//                        rv_list = new PersonnelResponseListEntity();
//                        rv_list.setStatus(status);
//                        rv_list.setMsg(msg);
//                        rv_list.setData((List<Object>) result.get("data"));
//                    }else{
//                        rv_single = new PersonnelResponseSingleEntity();
//                        rv_single.setStatus(status);
//                        rv_single.setMsg(msg);
//                    }
//                }
//                break;
//            case "9":
//                if(mc.contains("银行")){
//                    code = "gs_bank_check";
//                }else{
//                    code = "gs_other_check";
//                    params.put("entname",mc);
//                    params.put("uniscid",zjhm);
//                    rv_single = exchangeFeign.qyjbxxyz(params);
//                }
//                break;
//            default:
//                throw new ZtgeoBizException("暂不支持验证的机构编号");
//        }
//        break;
    }
}
