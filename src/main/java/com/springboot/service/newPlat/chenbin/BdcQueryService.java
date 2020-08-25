package com.springboot.service.newPlat.chenbin;

import com.alibaba.fastjson.JSONObject;
import com.springboot.component.newPlat.FileInteractComponent;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.chenbin.personnel.other.paph.PaphEntity;
import com.springboot.entity.chenbin.personnel.req.PaphReqEntity;
import com.springboot.entity.chenbin.personnel.resp.OtherResponseEntity;
import com.springboot.entity.newPlat.query.req.*;
import com.springboot.entity.newPlat.query.resp.*;
import com.springboot.entity.newPlat.settingTerm.DzzzSetting;
import com.springboot.entity.newPlat.settingTerm.FtpSettings;
import com.springboot.entity.newPlat.transInner.req.fromZY.domain.TransFjxx;
import com.springboot.entity.newPlat.transInner.req.fromZY.domain.TwoOrNFjxx;
import com.springboot.feign.newPlat.BdcQueryFeign;
import com.springboot.popj.pub_data.SJ_Info_Bdcqlxgxx;
import com.springboot.popj.pub_data.SJ_Info_Immovable;
import com.springboot.popj.pub_data.Sj_Info_Bdcdyxgxx;
import com.springboot.popj.pub_data.Sj_Sjsq_Bdc_Mapping;
import com.springboot.popj.warrant.ParametricData;
import com.springboot.popj.warrant.ParametricData2;
import com.springboot.util.chenbin.TestRun;
import com.springboot.util.newPlatBizUtil.ResultConvertUtil;
import jdk.nashorn.internal.ir.LiteralNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.springboot.constant.newPlat.chenbin.HandleKeywordConstant.*;

/**
 * @author chenb
 * @version 2020/7/27/027
 * description：不动产查询服务
 */
@Slf4j
@Service
public class BdcQueryService {
    @Autowired
    private BdcQueryFeign bdcQueryFeign;
    @Autowired
    private DzzzSetting dzzzSetting;
    @Autowired
    private FtpSettings ftpSettings;
    @Autowired
    private FileInteractComponent fileInteractComponent;

    /**
     * 描述：不动产权属信息查询，带他项权获取
     * 作者：chenb
     * 日期：2020/7/27/027
     * 参数：[parametricData]
     * 返回：[SJ_Info_Bdcqlxgxx]
     * 更新记录：更新人：{}，更新日期：{}
     */
    public List<SJ_Info_Bdcqlxgxx> queryQzxxWithItsRights(ParametricData parametricData) throws ParseException {
        //1. 定义返回结果集
        List<SJ_Info_Bdcqlxgxx> serviceDataInfos = new ArrayList<>();
        //2. 初始化查询条件
        CqzsReq cqzsReq = new CqzsReq().initByYCParams(parametricData);
        //3. 执行查询操作
        OtherResponseEntity<List<CqzsResponse>> cxjg = bdcQueryFeign.cqzscx(cqzsReq);
        //4. 解析查询的结果数据
        if(cxjg!=null){
            //4.1 记录查询得原始结果
            log.debug("【产权证书查询】--> 不动产接口执行结果（原始结果）："+ JSONObject.toJSONString(cxjg));
            //4.2 解析结果状态
            if(BDC_INTF_HANDLE_RETURN_CODE_SUCCESS.equals(cxjg.getCode())){
                //4.2.1 查询结果转换为一窗准入数据
                List<CqzsResponse> cqzss = cxjg.getData();
                if(cqzss!=null && cqzss.size()>0){
                    for(CqzsResponse cqzs:cqzss){
                        SJ_Info_Bdcqlxgxx sjInfoBdcqlxgxx = ResultConvertUtil.getImmoRightsByCqzs(cqzs);
                        if(sjInfoBdcqlxgxx!=null)
                            serviceDataInfos.add(sjInfoBdcqlxgxx);
                    }
                }
            } else if(BDC_INTF_HANDLE_RETURN_CODE_UNSUCCESS.equals(cxjg.getCode())){
                throw new ZtgeoBizException(cxjg.getMsg());
            }else{
                throw new ZtgeoBizException("返回的接口响应CODE不符合规范，取值范围是[0,1],当前响应：【"+cxjg.getCode()+"】");
            }
        }else{
            log.warn("【产权证书查询】--> 不动产权证查询，返回了null值，这在设计上是超出规范的，请知悉");
        }
        //5. 返回结果集
        return serviceDataInfos;
    }

    /**
     * 描述：不动产抵押证明信息获取，
     *      通过抵押人名称+抵押证明号
     * 作者：chenb
     * 日期：2020/7/29/029
     * 参数：[parametricData]
     * 返回：[Sj_Info_Bdcdyxgxx]
     * 更新记录：更新人：{}，更新日期：{}
     */
    public List<Sj_Info_Bdcdyxgxx> queryDyxxByZmhAndDyr(ParametricData2 parametricData) throws ParseException {
        //1. 定义返回结果集
        List<Sj_Info_Bdcdyxgxx> serviceDataInfos = new ArrayList<>();
        //2. 初始化查询条件
        DyzmReq dyzmReq = new DyzmReq().initByYCParams(parametricData);
        //3. 执行查询操作
        OtherResponseEntity<List<DyzmResponse>> cxjg = bdcQueryFeign.dyzmcx(dyzmReq);
        //4. 解析查询的结果数据
        if(cxjg!=null){
            //4.1 记录查询得原始结果
            log.debug("【抵押证明查询】--> 不动产接口执行结果（原始结果）："+ JSONObject.toJSONString(cxjg));
            //4.2 解析结果状态
            if(BDC_INTF_HANDLE_RETURN_CODE_SUCCESS.equals(cxjg.getCode())){
                //4.2.1 查询结果转换为一窗准入数据
                List<DyzmResponse> dyzms = cxjg.getData();
                if(dyzms!=null && dyzms.size()>0){
                    for(DyzmResponse dyzm:dyzms){
                        Sj_Info_Bdcdyxgxx sjInfoBdcdyxgxx = ResultConvertUtil.getImmoMortsByDyzm(dyzm);
                        if(sjInfoBdcdyxgxx!=null)
                            serviceDataInfos.add(sjInfoBdcdyxgxx);
                    }
                }
            } else if(BDC_INTF_HANDLE_RETURN_CODE_UNSUCCESS.equals(cxjg.getCode())){
                throw new ZtgeoBizException(cxjg.getMsg());
            }else{
                throw new ZtgeoBizException("返回的接口响应CODE不符合规范，取值范围是[0,1],当前响应：【"+cxjg.getCode()+"】");
            }
        }else{
            log.warn("【抵押证明查询】--> 不动产抵押证明查询，返回了null值，这在设计上是超出规范的，请知悉");
        }
        //5. 返回结果集
        return serviceDataInfos;
    }

    /**
     * 描述：不动产单元信息生成楼盘服务数据
     * 作者：chenb
     * 日期：2020/8/18
     * 参数：[bdcdyh]
     * 返回：List<SJ_Info_Immovable>
     * 更新记录：更新人：{}，更新日期：{}
     */
    public List<SJ_Info_Immovable> queryBdcdyxxByDyh(String bdcdyh){
        //1. 定义返回结果集
        List<SJ_Info_Immovable> serviceDataInfos = new ArrayList<>();
        //2. 初始化查询条件
        BdcdyReq bdcdyReq = new BdcdyReq().initDyReq("123",bdcdyh);
        //3. 执行查询操作
        OtherResponseEntity<BdcdyResponse> cxjg = bdcQueryFeign.bdcdycx(bdcdyReq);
        //查询结果留痕
        log.debug(JSONObject.toJSONString(cxjg));
        //4. 解析查询的结果数据
        if(cxjg!=null) {
            cxjg.checkSelfIfBdc("不动产单元信息查询接口");
            BdcdyResponse bdcdyResponse = cxjg.getData();
            if(bdcdyResponse!=null){
                SJ_Info_Immovable serviceDataInfo = ResultConvertUtil.getImmovInfoByDysj(bdcdyResponse);
                serviceDataInfos.add(serviceDataInfo);
            }
        } else {
            log.warn("【不动产单元查询】--> 不动产单元信息查询，返回了null值，这在设计上是超出规范的，请知悉");
        }
        return serviceDataInfos;
    }

    /**
     * 描述：不动产登记资料接口使用，
     *          金融机构（平安普惠）贷前风险控制查询
     * 作者：chenb
     * 日期：2020/7/29/029
     * 参数：[PaphReqEntity]
     * 返回：[PaphEntity]
     * 更新记录：更新人：{}，更新日期：{}
     */
    public List<PaphEntity> queryPaphMort(PaphReqEntity paph,String key){
        //1. 定义返回结果集
        List<PaphEntity> paphEntitys = new ArrayList<>();
        //2. 初始化查询条件
        DjzlReq djzlReq = new DjzlReq().initByPaph(paph,key);
        //3. 执行查询操作
        OtherResponseEntity<List<DjzlResponse>> cxjg = bdcQueryFeign.djzlcx(djzlReq);
        //4. 解析查询的结果数据
        if(cxjg!=null){
            //4.1 记录查询得原始结果
            log.debug("【登记资料查询】--> 不动产接口执行结果（原始结果）："+ JSONObject.toJSONString(cxjg));
            //4.2 解析结果状态
            if(BDC_INTF_HANDLE_RETURN_CODE_SUCCESS.equals(cxjg.getCode())){
                //4.2.1 查询结果转换为一窗准入数据
                List<DjzlResponse> djzls = cxjg.getData();
                if(djzls!=null && djzls.size()>0){
                    for(DjzlResponse djzl:djzls){
                        PaphEntity paphEntity = ResultConvertUtil.getPaphEntity(djzl,key,paph);
                        if(paphEntity!=null)
                            paphEntitys.add(paphEntity);
                    }
                }
            } else if(BDC_INTF_HANDLE_RETURN_CODE_UNSUCCESS.equals(cxjg.getCode())){
                throw new ZtgeoBizException(cxjg.getMsg());
            }else{
                throw new ZtgeoBizException("返回的接口响应CODE不符合规范，取值范围是[0,1],当前响应：【"+cxjg.getCode()+"】");
            }
        }else{
            log.warn("【登记资料查询】--> 登记资料查询，返回了null值，这在设计上是超出规范的，请知悉");
        }
        //5. 返回结果集
        return paphEntitys;
    }

    /**
     * 描述：获取新平台电子证照
     * 作者：chenb
     * 日期：2020/8/21
     * 参数：[bdcMappings]
     * 返回：[List<YcDzzz>]
     * 更新记录：更新人：{}，更新日期：{}
     */
    public List<YcDzzz> postEcerts(List<Sj_Sjsq_Bdc_Mapping> bdcMappings){
        //处理电子证照接口调用参数
        String ywh = bdcMappings
                .stream()
                .map(Sj_Sjsq_Bdc_Mapping :: getBdcywh)
                .collect(Collectors.joining(","));
        DzzzReq dzzzReq = new DzzzReq(ywh);
        //调用接口,获取电子证照
//        OtherResponseEntity<List<Dzzzxx>> respDzzzs = bdcQueryFeign.dzzzcx(dzzzReq).checkSelfIfBdc("电子证照查询接口");
        OtherResponseEntity<List<Dzzzxx>> respDzzzs = TestRun.mockDzzz(ywh).checkSelfIfBdc("电子证照查询接口");
                //处理一窗期望的电子证照结果
        List<YcDzzz> ycDzzzes = ResultConvertUtil.getYcdzzzFromBdcResp(bdcMappings,respDzzzs.getData(),dzzzSetting,ftpSettings);
        //声明待操作的文件集合
        List<TwoOrNFjxx> willAsynFiles = new ArrayList<>();
        //遍历产生的一窗电子证照集合
        for (YcDzzz ycDzzze:ycDzzzes){
            //声明待操作对象
            TwoOrNFjxx twoOrNFjxx = new TwoOrNFjxx();
            //设置来源
            twoOrNFjxx.setCome(new TransFjxx("-1",null,null,ycDzzze.getDzzzwj()));
            //声明并设置去向集合
            List<TransFjxx> to = new ArrayList<>();
            to.add(new TransFjxx(ftpSettings.getYcSaveSetting(),FTP_USE_FOR_YCSL,ycDzzze.getFjlj(),null));
            twoOrNFjxx.setTo(to);
            willAsynFiles.add(twoOrNFjxx);
            //清空文件byte[]和文件base64，提高http传输效率
            ycDzzze.setZznr(null);
            ycDzzze.setDzzzwj(null);
        }
        fileInteractComponent.SynchTrans(willAsynFiles);
        log.info("电子证照拉取成功！");
        return ycDzzzes;
    }
}
