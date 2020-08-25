package com.springboot.service.newPlat.chenbin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.component.chenbin.HttpCallComponent;
import com.springboot.component.chenbin.OtherComponent;
import com.springboot.component.fileMapping.FileNameConfigService;
import com.springboot.component.newPlat.BdcInteractComponent;
import com.springboot.component.newPlat.FileInteractComponent;
import com.springboot.config.Msgagger;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.SJ_Fjfile;
import com.springboot.entity.chenbin.personnel.resp.OtherResponseEntity;
import com.springboot.entity.newPlat.jsonMap.FileNameMapping;
import com.springboot.entity.newPlat.settingTerm.FtpSettings;
import com.springboot.entity.newPlat.settingTerm.NewPlatSettings;
import com.springboot.entity.newPlat.transInner.req.BdcNoticeReq;
import com.springboot.entity.newPlat.transInner.req.NewBdcFlowCheckReq;
import com.springboot.entity.newPlat.transInner.req.fromZY.NewBdcFlowRequest;
import com.springboot.entity.newPlat.transInner.req.fromZY.domain.*;
import com.springboot.feign.OuterBackFeign;
import com.springboot.feign.newPlat.BdcInteractFeign;
import com.springboot.popj.pub_data.*;
import com.springboot.popj.register.JwtAuthenticationRequest;
import com.springboot.service.newPlat.penghao.BdcTransToInterService;
import com.springboot.util.SysPubDataDealUtil;
import com.springboot.util.TimeUtil;
import com.springboot.util.chenbin.ErrorDealUtil;
import com.springboot.util.newPlatBizUtil.ParamConvertUtil;
import com.springboot.util.newPlatBizUtil.ResultConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import static com.springboot.constant.AdminCommonConstant.BOOLEAN_NUMBER_TRUE;
import static com.springboot.constant.chenbin.BusinessConstant.*;
import static com.springboot.constant.newPlat.chenbin.HandleKeywordConstant.*;

/**
 * @author chenb
 * @version 2020/7/30/030
 * description：不动产交互服务
 */
@Slf4j
@Scope("prototype")
@Service
public class BdcInteractService {

    //办事人员name
    @Value("${djj.bsryname}")
    private String bsryname;
    //办事人员登录使用密码
    @Value("${djj.bsrypassword}")
    private String bsrypassword;

    @Autowired
    private OuterBackFeign backFeign;

    @Autowired
    private BdcInteractFeign bdcInteractFeign;

    @Autowired
    private OtherComponent otherComponent;

    @Autowired
    private HttpCallComponent httpCallComponent;

    @Autowired
    private BdcInteractComponent bdcInteractComponent;

    @Autowired
    private BdcTransToInterService bdcTransToInterService;

    @Autowired
    private FtpSettings ftpSettings;

    @Autowired
    private NewPlatSettings newPlatSettings;

    @Autowired
    private FileNameConfigService fileNameConfigService;

    @Autowired
    private FileInteractComponent fileInteractComponent;

    /**
     * 描述：通知的异步处理模块
     * 作者：chenb
     * 日期：2020/8/3
     * 参数：[bizStamp, noticeBody, resp]
     * 返回：void
     * 更新记录：更新人：{}，更新日期：{}
     */
    public void noticeMe(BdcNoticeReq noticeBody, HttpServletResponse resp){
        log.info("BDC->YCSL：【"+noticeBody.getWsywh()+"】接入不动产办件节点通知模块,主线程(节点通知响应线程)执行！");
        try {
            resp.setContentType("application/json;charset=UTF-8");
            OutputStream out = resp.getOutputStream();
            out.write(JSONObject.toJSONString(new ObjectRestResponse<String>().data("通知接收成功！")).getBytes("UTF-8"));
            out.flush();
            out.close();
        } catch (IOException e){
            log.error("BDC->YCSL：【"+noticeBody.getWsywh()+"】接入不动产办件节点通知模块,通知接收返回异常，导致异常原因：" + ErrorDealUtil.getErrorInfo(e));
            throw new ZtgeoBizException("通知失败，出现IO异常，请知悉");
        }

        log.info("=================BDC->YCSL：【"+noticeBody.getWsywh()+"】接入不动产办件节点通知模块：分割线（之后的异常将不会再抛出）=================");
        //声明token信息
        String token = null;
        try {
            token = backFeign.getToken(new JwtAuthenticationRequest(bsryname, bsrypassword)).getData();
        } catch (Exception e){
            log.error("BDC->YCSL：【"+noticeBody.getWsywh()+"】接入不动产办件节点通知模块,获取通知处理用户时出现错误，错误信息为："
                    +ErrorDealUtil.getErrorInfo(e));
        }
        if(StringUtils.isNotBlank(token)) {
            String useToken = token;
            //实例化分支线程的执行容器
            ExecutorService executor = Executors.newCachedThreadPool();
            //定义分支线程任务
            FutureTask<String> future = new FutureTask<String>(new Callable<String>() {
                public String call() {//建议抛出异常
                    log.info("BDC->YCSL：【" + noticeBody.getWsywh() + "】接入不动产办件节点通知模块：执行分支线程(节点通知处理线程)");
                    //执行节点通知
                    bdcInteractComponent.handleNotice(useToken,noticeBody);
                    return "分支线程执行结束";
                }
            });
            //执行分支线程
            executor.execute(future);
            //处理分支返回结果
            try {
                // 创建数据
                String result = future.get(); //取得结果，同时设置超时执行时间为5秒。
                log.info("BDC->YCSL：【" + noticeBody.getWsywh() + "】接入不动产办件节点通知模块,通知执行结果：" + result);
            } catch (Exception e) {
                log.info("BDC->YCSL：【" + noticeBody.getWsywh() + "】接入不动产办件节点通知模块,“节点通知处理线程”捕获到执行异常");
                log.error("BDC->YCSL：【" + noticeBody.getWsywh() + "】接入不动产办件节点通知模块,“节点通知处理线程”出现未知异常：" + ErrorDealUtil.getErrorInfo(e));
                //这里做异常处理(分支线程产生的)
                e = ErrorDealUtil.OnlineErrorTrans(e);
                //调用回写异常信息(通知类异常)进Rec（暂未完成）

            } finally {
                executor.shutdown();
            }
        }
    }

    public String commonCreatNewPlatProc(String commonInterfaceAttributer,String checkAlready,String flowKey)throws ParseException {
        //声明返回结果
        String back = "不动产同步创建成功";
        //获取一窗受理操作token
        String token = backFeign.getToken(new JwtAuthenticationRequest(bsryname,bsrypassword)).getData();
        //处理数据
        SJ_Sjsq sjsq = SysPubDataDealUtil.parseReceiptData(
                commonInterfaceAttributer, null, null, null);
        //不动产映射集合判断
        if(sjsq.getBdcMappingVoList()==null || sjsq.getBdcMappingVoList().size()<1){
            throw new ZtgeoBizException("【BdcMapping miss】转内网业务创办时，一窗缺失必要的业务对应集合");
        }
        //创建签收Map
        Map<String,String> params = new HashMap<>();
        //执行创建
        try {
            //debug留痕
            log.debug("【"+sjsq.getReceiptNumber()+"】("+flowKey+")转JSON前：" + commonInterfaceAttributer);
            log.debug("【"+sjsq.getReceiptNumber()+"】("+flowKey+")转JSON后：" + JSONObject.toJSONString(sjsq));
            //预检查是否已经创建成功
            if(BOOLEAN_NUMBER_TRUE.equals(checkAlready)) {
                if(preCheckWsjSucc(sjsq,params))
                    return back;
            }
            //拉取并做附件映射
            //处理附件
            List<SJ_Fjfile> fileVoList = httpCallComponent.getFileVoList(sjsq.getReceiptNumber(), token);
            //准备数据
            NewBdcFlowRequest newBdcFlowRequest = null;
            switch (flowKey) {
                case Msgagger.ESFZYDJ:
                    newBdcFlowRequest = prepareNewBdcFlowRequestForESFZY(sjsq,fileVoList);
                    break;
                case Msgagger.ESFZYJDYDJ:
                    newBdcFlowRequest = prepareNewBdcFlowRequestForESFZYJDY(sjsq,fileVoList);
                    break;
                case Msgagger.BDCDYDJ:
                    newBdcFlowRequest = bdcTransToInterService.prepareNewBdcFlowRequestForBDCDYDJ(sjsq,fileVoList);
                    break;
                case Msgagger.YGJYGDY:
                    newBdcFlowRequest = bdcTransToInterService.prepareNewBdcFlowRequestForYGJYGDYDJ(sjsq,fileVoList);
                    break;
                case Msgagger.DYZXDJ:
                    newBdcFlowRequest = bdcTransToInterService.prepareNewBdcFlowRequestForDYZXDJ(sjsq,fileVoList);
                    break;
            }
            //debug留痕
            log.debug("【"+sjsq.getReceiptNumber()+"】最终传入不动产数据为："+JSONObject.toJSONString(newBdcFlowRequest));
            log.info("开始转内网，时间："+new Date().getTime());
            //执行创建
            creatNewplatFlow(newBdcFlowRequest,sjsq.getBdcMappingVoList(),params);
            return back;
        } catch (ZtgeoBizException e){
            throw e;
        }catch (Exception e){
            log.error("内网办件出现异常，异常详情信息："+ErrorDealUtil.getErrorInfo(e));
            throw new ZtgeoBizException("转内网办件出现异常,联系管理员");
        }finally {
            //签收办件
            otherComponent.signPro(token,bsryname,bsrypassword,sjsq.getReceiptNumber(),params);
        }
    }

    /**
     * 描述：二手房转移登记转内网逻辑
     * 作者：chenb
     * 日期：2020/8/6
     * 参数：[commonInterfaceAttributer, checkAlready]
     * 返回：String
     * 更新记录：更新人：{}，更新日期：{}
     */
    public NewBdcFlowRequest prepareNewBdcFlowRequestForESFZY(SJ_Sjsq sjsq,List<SJ_Fjfile> fileVoList) throws ParseException {
        log.info("进入二手房转移登记数据处理，申请编号："+sjsq.getReceiptNumber());
        //基本数据生成
        NewBdcFlowRequest newBdcFlowRequest = ParamConvertUtil.getBaseFromSjsq(
                sjsq,                               //收件申请
                newPlatSettings,                    //新平台配置
                NEWPLAT_TURNINNERS_ESFZY,           //新平台业务类型
                BDC_DJLB_DBYW,                      //登记类别
                LZDZ_LDKQ                           //领证地址
        );
        //补全房屋主体信息并补原业务号进转内网主体--根据权属数据
        ParamConvertUtil.fillMainHouseToReqByQlxx(newBdcFlowRequest,sjsq.getImmovableRightInfoVoList(),BDC_DATA_TYPE_SC);
        //补全申请人信息
        newBdcFlowRequest.setSqrxx(transSellersAndBuyersToSqr(sjsq.getTransactionContractInfo(),BDC_NEW_PLAT_YW_KEY_QL));
        //补全附件列表信息
        newBdcFlowRequest.setFjxx(transFjxxFromYcToBdc(fileVoList,BDC_NEW_PLAT_FLOW_KEY_ZY,sjsq.getReceiptNumber()));

        return newBdcFlowRequest;
    }

    /**
     * 描述：二手房转移及抵押登记转内网逻辑
     * 作者：chenb
     * 日期：2020/8/6
     * 参数：[commonInterfaceAttributer, checkAlready]
     * 返回：String
     * 更新记录：更新人：{}，更新日期：{}
     */
    public NewBdcFlowRequest prepareNewBdcFlowRequestForESFZYJDY(SJ_Sjsq sjsq,List<SJ_Fjfile> fileVoList) throws ParseException {
        //基本数据生成
        NewBdcFlowRequest newBdcFlowRequest = ParamConvertUtil.getBaseFromSjsq(
                sjsq,                               //收件申请
                newPlatSettings,                    //新平台配置
                NEWPLAT_TURNINNERS_ESFZYDY,           //新平台业务类型
                BDC_DJLB_ZHYW,                      //登记类别
                LZDZ_LDKQ                           //领证地址
        );
        //补全房屋主体信息并补原业务号进转内网主体--根据权属数据
        ParamConvertUtil.fillMainHouseToReqByQlxx(newBdcFlowRequest,sjsq.getImmovableRightInfoVoList(),BDC_DATA_TYPE_SC);
        //补全申请人信息
        newBdcFlowRequest.setSqrxx(transSellersAndBuyersToSqr(sjsq.getTransactionContractInfo(),BDC_NEW_PLAT_YW_KEY_QL));
        //补全附件列表信息
        newBdcFlowRequest.setFjxx(transFjxxFromYcToBdc(fileVoList,BDC_NEW_PLAT_FLOW_KEY_ZY,sjsq.getReceiptNumber()));
        //补全抵押信息
        newBdcFlowRequest.setDyxx(initDyxx(
                sjsq.getReceiptNumber(),
                newBdcFlowRequest.getYywh(),
                newBdcFlowRequest.getYqllx(),
                sjsq.getMortgageContractInfo(),
                fileVoList,
                BDC_NEW_PLAT_YW_KEY_DY
                )
        );
        return newBdcFlowRequest;
    }






    /**
     * 描述：转内网的执行代码
     * 作者：chenb
     * 日期：2020/8/8
     * 参数：[newBdcFlowRequest, bdcMappingVoList, params]
     * 返回：void
     * 更新记录：更新人：{}，更新日期：{}
     */
    public void creatNewplatFlow(NewBdcFlowRequest newBdcFlowRequest,List<Sj_Sjsq_Bdc_Mapping> bdcMappingVoList,Map<String,String> params){
        if(newBdcFlowRequest==null){
            throw new ZtgeoBizException("转内网主体对象生成失败");
        }
        OtherResponseEntity<List<NewBdcFlowRespData>> creResp = bdcInteractFeign.wsbjcj(newBdcFlowRequest);
        log.info("不动产创建流程返回："+JSONObject.toJSONString(creResp));
        creResp.checkSelfIfBdc("不动产流程转内网创建接口");
        dealBdcCreatResult(creResp.getData(),bdcMappingVoList,params);
        if ("success".equals(params.get("isSuccess"))) {
            params.remove("isSuccess","success");
        } else {
            params.remove("isSuccess");
            throw new ZtgeoBizException(creResp.getMsg());
        }
    }

    /**
     * 描述：将出卖和购买人转换为申请人
     * 作者：chenb
     * 日期：2020/8/8
     * 参数：[jyhtxx]
     * 返回：List<Sqrxx>
     * 更新记录：更新人：{}，更新日期：{}
     */
    public List<Sqrxx> transSellersAndBuyersToSqr(Sj_Info_Jyhtxx jyhtxx,String yw_key){
        if(jyhtxx==null)
            throw new ZtgeoBizException("交易合同信息缺失，涉及房屋买卖的登记时，这是不允许的！");
        List<SJ_Qlr_Gl> glQlrs = jyhtxx.getGlHouseBuyerVoList();
        List<SJ_Qlr_Gl> glSellers = jyhtxx.getGlHouseSellerVoList();

        List<SJ_Qlr_Gl> glQlrDlrs = jyhtxx.getGlAgentVoList();
        List<SJ_Qlr_Gl> glYwrDlrs = jyhtxx.getGlAgentSellerVoList();

        //预处理代理人(针对权利人中未给出代理人信息，但代理人集合有值)
        preCheckQlrAndDlr(glQlrs,glQlrDlrs);
        preCheckQlrAndDlr(glSellers,glYwrDlrs);
        //合并集合
        glQlrs.addAll(glSellers);
        //处理申请人
        return ParamConvertUtil.getSqrsByQlrs(glQlrs,yw_key);
    }

    /**
     * 描述：将抵押与抵押权人转换为申请人
     * 作者：chenb
     * 日期：2020/8/8
     * 参数：[jyhtxx]
     * 返回：List<Sqrxx>
     * 更新记录：更新人：{}，更新日期：{}
     */
    public List<Sqrxx> mortDyrsAndDyqrsToSqr(Sj_Info_Dyhtxx dyhtxx,String yw_key){
        //获取抵押/抵押权人
        List<SJ_Qlr_Gl> glQlrs = dyhtxx.getGlMortgagorVoList();
        List<SJ_Qlr_Gl> gldyqrs = dyhtxx.getGlMortgageHolderVoList();

        List<SJ_Qlr_Gl> glQlrDlrs = dyhtxx.getGlMortgagorAgentInfoVoList();

        List<SJ_Qlr_Gl> glDyqrDlrs = dyhtxx.getGlMortgageeAgentInfoVoList();

        //预处理代理人(针对权利人中未给出代理人信息，但代理人集合有值)
        preCheckQlrAndDlr(glQlrs,glQlrDlrs);
        preCheckQlrAndDlr(gldyqrs,glDyqrDlrs);

        //合并集合
        glQlrs.addAll(gldyqrs);
        //处理申请人
        return ParamConvertUtil.getSqrsByQlrs(glQlrs,yw_key);
    }

    public List<Sqrxx> bdcMortDyqrToSqr(List<Sj_Info_Bdcdyxgxx> immovableCurrentMortgageInfoVoList){
        if(immovableCurrentMortgageInfoVoList==null || immovableCurrentMortgageInfoVoList.size()<1){
            throw new ZtgeoBizException("【数据检查未通过】不动产抵押信息为空");
        }
        Sj_Info_Bdcdyxgxx bdcdyxgxx = immovableCurrentMortgageInfoVoList.get(0);
        List<SJ_Qlr_Gl> gldyqrs = bdcdyxgxx.getGlMortgageHolderVoList();
        return ParamConvertUtil.getSqrsByQlrs(gldyqrs,BDC_NEW_PLAT_YW_KEY_DY);
    }

    /**
     * 描述：代理人预检查
     * 作者：chenb
     * 日期：2020/8/14
     * 参数：[glQlrs, glDlrs]
     * 返回：
     * 更新记录：更新人：{}，更新日期：{}
     */
    public void preCheckQlrAndDlr(List<SJ_Qlr_Gl> glQlrs,List<SJ_Qlr_Gl> glDlrs){
        if(glDlrs!=null && glDlrs.size()>0) { //条件1.代理人集合不为空
            if (!glQlrs.stream().anyMatch(glQlr -> glQlr.getRelatedAgent() != null)) { //条件2. 权利中未发现存在代理人信息
                if(glDlrs.size()==1){   //代理人集合长度为1
                    glQlrs.stream().forEach(glQlr -> glQlr.setRelatedAgent(
                            JSONObject.parseObject(JSONObject.toJSONString(glDlrs.get(0).getRelatedPerson()),SJ_Qlr_Info.class)));
                }else{    //代理人集合长度大于1
                    if(glQlrs.size()==glDlrs.size()){   //权利人和代理人集合长度一致
                        for (int i=0;i<glQlrs.size();i++){
                            glQlrs.get(i).setRelatedAgent(glDlrs.get(i).getRelatedPerson());
                        }
                    }else{  //针对于多个代理人进行处理
                        //合并 代理人信息
                        SJ_Qlr_Info hbdlrxx = ParamConvertUtil.getOneQlrByList(glDlrs);
                        String hbdlrxxJSON = JSONObject.toJSONString(hbdlrxx);
                        for(SJ_Qlr_Gl glQlr:glQlrs){
                            glQlr.setRelatedAgent(JSONObject.parseObject(hbdlrxxJSON,SJ_Qlr_Info.class));
                        }
                    }
                }
            }
        }
    }

    /**
     * 描述：一窗受理向不动产发附件（整理）
     * 作者：chenb
     * 日期：2020/8/12
     * 参数：[fileVoList, lkey]
     * 返回：
     * 更新记录：更新人：{}，更新日期：{}
     */
    public List<Fjxx> transFjxxFromYcToBdc(List<SJ_Fjfile> fileVoList,String lkey,String sqbh){
        log.info("进入【"+lkey+"】的附件处理");
        FileNameMapping fileNameMapping = fileNameConfigService.gainFileMapByKey(lkey);
        if(fileNameMapping!=null){
            List<String> fileStandards = fileNameMapping.getMappingName();
            List<Fjxx> fjxxes = new ArrayList<>();
            List<TwoOrNFjxx> willAsynFiles = new ArrayList<>();
            int i=1;
            for(SJ_Fjfile file:fileVoList){
                String logicName = FILE_DEFAULT_MAPPING_FOLDER_NAME;
                if(fileStandards.contains(file.getLogicPath())) {
                    logicName = file.getLogicPath();
                }

                Fjxx fjxx = new Fjxx();
                //文件名称
                String fileName = file.getFileName();
                if (file.getFileName().lastIndexOf(".") > 0) {
                    fileName = file.getFileName().substring(0, file.getFileName().lastIndexOf("."));
                }
                fjxx.setWjmc(fileName+"."+file.getFileExt());
                //文件扩展名
                fjxx.setWjlx(file.getFileExt());
                //文件夹名称
                fjxx.setWjjmc(logicName);
                //设置文件序号
                fjxx.setSxh(Integer.toString(i));
                //通用路径生成
                if(CommonSetBdcFjdz(fjxx, file,file.getSaveType(),lkey,fileNameMapping.getSid(),sqbh)){
                    //设置原路径、去向路径
                    TwoOrNFjxx twoFjxx = new TwoOrNFjxx().initByTwo(
                            file.getSaveType(),         //源文件存储方式（0-硬盘/1-ftp）
                            FTP_USE_FOR_YCSL,         //源文件来源
                            file.getFtpPath(),              //源文件存放地址
                            "1",                //将生成的目标文件存储方式（0-硬盘/1-ftp）
                            FTP_USE_FOR_BDC,              //将生成的目标文件key
                            fjxx.getFjdz()                      //将生成的目标文件存放地址
                    );
                    willAsynFiles.add(twoFjxx);
                }
//                if ("1".equals(file.getSaveType())) {       //后续会将本地文件上传至FTP后将路径赋值
                    fjxxes.add(fjxx);
                    i++;
//                }
            }
            if(willAsynFiles.size()>0) { //启动异步传输
                log.info("转内网办件附件两方异步处理开始！");
                fileInteractComponent.AsynchTrans(willAsynFiles);
            }
            if(fjxxes.size()>0)
                return fjxxes;
        }
        return null;
    }

    /**
     * 功能描述: 处理交易合同信息<br>
     * 〈〉
     * @Param: [jyhtxx]
     * @Return: com.springboot.entity.newPlat.transInner.req.fromZY.domain.Htxx
     * @Author: Administrator
     * @Date: 2020/8/11 11:49
     */
    public Htxx initJyhtxx(Sj_Info_Jyhtxx jyhtxx){
        Htxx htxx = new Htxx();
         if (null != jyhtxx){
            htxx.setHtbah(jyhtxx.getContractNumber());//合同备案号
            htxx.setHtbh(jyhtxx.getContractNumber());//合同编号
            htxx.setHtqdrq(jyhtxx.getContractSignTime());//签订时间
            htxx.setHtbarq(jyhtxx.getContractRecordTime());//备案时间
            htxx.setHtje(Double.parseDouble(jyhtxx.getContractAmount().toString()));//合同金额
            htxx.setHtzt(jyhtxx.getIsReal());//合同状态
         }
        return htxx;
    }

    /**
     * 描述：产生抵押信息
     * 作者：chenb
     * 日期：2020/8/13
     * 参数：[sqbh, yywh, yqllx, dyhtxx, fileVoList]
     * 返回：List<Dyxx>
     * 更新记录：更新人：{}，更新日期：{}
     */
    public List<Dyxx> initDyxx(String sqbh,String yywh,String yqllx,Sj_Info_Dyhtxx dyhtxx,List<SJ_Fjfile> fileVoList,String yw_key) throws ParseException {
        List<Dyxx> dyxxes = null;
        if(dyhtxx!=null){
            dyxxes = new ArrayList<>();
            Dyxx dyxx = new Dyxx();
            dyxx.setBdbzzqse(
                    ResultConvertUtil.getBigDecimalNotThrowNull(
                        "转内网-被担保主债权数额",
                        ResultConvertUtil.getStringFromBigDecimalNotThrowNull(
                            dyhtxx.getCreditAmount().divide(new BigDecimal("10000")),
                            "#.00"
                        )
                    )
            );
            dyxx.setDbfw("详见合同");
            dyxx.setDyfs(dyhtxx.getMortgageMode());
            dyxx.setDyyy(dyhtxx.getMortgageReason());
            dyxx.setZwlxqssj(TimeUtil.getTimeString(TimeUtil.getTimeFromString(dyhtxx.getMortgageStartingDate())));
            dyxx.setZwlxjssj(TimeUtil.getTimeString(TimeUtil.getTimeFromString(dyhtxx.getMortgageEndingDate())));
            dyxx.setDyhtqdrq(TimeUtil.getTimeString(TimeUtil.getTimeFromString(dyhtxx.getContractSignTime())));
            dyxx.setBdcjz(null);
            dyxx.setDysx(null);
            dyxx.setYywh(yywh);
            dyxx.setYqllx(ParamConvertUtil.getNullStrIfk(yqllx));
            SJ_Qlr_Info zwr = ParamConvertUtil.getOneQlrByList(dyhtxx.getGlObligorInfoVoList());
            if(zwr!=null && StringUtils.isNotBlank(zwr.getObligeeName())) {
                dyxx.setZwr(zwr.getObligeeName());
                dyxx.setZwrzjlx(zwr.getObligeeDocumentType());
                dyxx.setZwrzjhm(zwr.getObligeeDocumentNumber());
            }
            dyxx.setSqrxx(mortDyrsAndDyqrsToSqr(dyhtxx,yw_key));
            dyxx.setFjxx(transFjxxFromYcToBdc(fileVoList,BDC_NEW_PLAT_FLOW_KEY_DY,sqbh));
            dyxxes.add(dyxx);
        }
        return dyxxes;
    }

    /**
     * 描述：通用不动产附件地址转内网转换处理
     * 作者：chenb
     * 日期：2020/8/9
     * 参数：
     * 返回：
     * 更新记录：更新人：{}，更新日期：{}
    */
    public boolean CommonSetBdcFjdz(Fjxx fjxx,SJ_Fjfile file,String saveType,String lKey,String sid,String wwywh){
        //声明是否使用异步上传附件标识
        boolean isUseAsyn = false;
        //声明并预处理完整FTP路径
        String remotePath = file.getFtpPath().replaceAll("\\\\","/");
        //截取文件名
        String fileName = remotePath.substring(remotePath.lastIndexOf("/") + 1);
        //之前未进行过附件同步
        if(StringUtils.isBlank(file.getBdcMappingPath())) {
            if ("0".equals(saveType)) { //文件保存在本地,赋值逻辑路径/设置不动产附件操作标识
                isUseAsyn = ftpSettings.getIsDealFtp().getBdc();
                //设置路径到不动产附件对象中
                String logicFtpPath = ParamConvertUtil.initNeedFtpPath("BDC", lKey, fileName, sid, wwywh);
                fjxx.setFjdz(logicFtpPath);
            } else {      //FTP保存
                if (ftpSettings.getIsDealFtp().getBdc()) {//需要操作附件上传，从一窗受理FTP下载并上传至不动产的FTP
                    isUseAsyn = true;
                    //设置路径到不动产附件对象中
                    String logicFtpPath = ParamConvertUtil.initNeedFtpPath("BDC", lKey, fileName, sid, wwywh);
                    fjxx.setFjdz(logicFtpPath);
                    //重定义标准文件FTP或本地路径
                    file.setFtpPath(remotePath);
                } else {  //路径赋值
                    //无操作时，标准FTP路径设置
                    fjxx.setFjdz(remotePath);
                }
            }
        } else {
            //已异步处理过
            fjxx.setFjdz(file.getBdcMappingPath());
        }
        return isUseAsyn;
    }

    /**
     * 描述：外网申请编号不动产创建成功预检查功能
     * 作者：chenb
     * 日期：2020/8/7
     * 参数：String sqbh
     * 返回：Map
     * 更新记录：更新人：{}，更新日期：{}
     */
    public boolean preCheckWsjSucc(SJ_Sjsq sjsq,Map<String,String> params){
        dealCheckWsjSucc(sjsq.getReceiptNumber(),sjsq.getBdcMappingVoList(),params);
        if ("success".equals(params.get("isSuccess"))) {
            params.remove("isSuccess","success");
            return true;
        } else {
            params.remove("isSuccess");
        }
        return false;
    }

    /**
     * 描述：执行外网申请编号不动产创建成功检查接口
     * 作者：chenb
     * 日期：2020/8/7
     * 参数：String sqbh
     * 返回：Map
     * 更新记录：更新人：{}，更新日期：{}
    */
    public void dealCheckWsjSucc(String sqbh, List<Sj_Sjsq_Bdc_Mapping> bdcMappingVoList, Map<String,String> params){
        params.put("receiptNumber", sqbh);
        NewBdcFlowCheckReq wwywh = new NewBdcFlowCheckReq(sqbh);
        OtherResponseEntity<List<NewBdcFlowRespData>> checkResult = bdcInteractFeign.wsjgjc(wwywh);
        checkResult.checkSelfIfBdc("不动产业务创建检查接口");
        dealBdcCreatResult(checkResult.getData(),bdcMappingVoList,params);
    }

    /**
     * 描述：处理不动产业务转内网创建返回结果
     * 作者：chenb
     * 日期：2020/8/7
     * 参数：List<NewBdcFlowRespData> bdcCreatResult,Map<String,String> params
     * 返回：void
     * 更新记录：更新人：{}，更新日期：{}
    */
    public void dealBdcCreatResult(List<NewBdcFlowRespData> bdcCreatResult, List<Sj_Sjsq_Bdc_Mapping> bdcMappingVoList, Map<String,String> params){
        if(bdcCreatResult==null||bdcCreatResult.size()<1){
            params.put("isSuccess","unsuccess");
        }else{
            params.put("isSuccess","success");
            for(Sj_Sjsq_Bdc_Mapping bdcMapping : bdcMappingVoList){
                for(NewBdcFlowRespData bdcCreatRes:bdcCreatResult){
                    //数据自检
                    bdcCreatRes.checkSelfStandard();
                    if(bdcMapping.getSid().equals(bdcCreatRes.getSid())){
                        bdcMapping.setBdcywh(bdcCreatRes.getYwh());
                        bdcMapping.setBdcywlx(bdcCreatRes.getYwlx());
                        break;
                    }
                }
            }
            params.put("bdcMappingVoList", JSONArray.toJSONString(bdcMappingVoList));
        }
    }
}
