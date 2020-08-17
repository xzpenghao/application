package com.springboot.service.newPlat.penghao;

import com.springboot.component.chenbin.HttpCallComponent;
import com.springboot.component.chenbin.OtherComponent;
import com.springboot.component.fileMapping.FileNameConfigService;
import com.springboot.component.newPlat.BdcInteractComponent;
import com.springboot.entity.SJ_Fjfile;
import com.springboot.entity.newPlat.settingTerm.FtpSettings;
import com.springboot.entity.newPlat.settingTerm.NewPlatSettings;
import com.springboot.entity.newPlat.transInner.req.fromZY.NewBdcFlowRequest;
import com.springboot.feign.OuterBackFeign;
import com.springboot.feign.newPlat.BdcInteractFeign;
import com.springboot.popj.pub_data.SJ_Sjsq;
import com.springboot.service.newPlat.chenbin.BdcInteractService;
import com.springboot.util.newPlatBizUtil.ParamConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

import static com.springboot.constant.chenbin.BusinessConstant.*;
import static com.springboot.constant.newPlat.chenbin.HandleKeywordConstant.BDC_DATA_TYPE_SC;

/**
 * @Author Peng Hao
 * @Date 2020/8/10 19:47
 * @Version 1.0
 */
@Slf4j
@Service
public class BdcTransToInterService {

    //办事人员name
    @Value("${djj.bsryname}")
    private String bsryname;
    //办事人员登录使用密码
    @Value("${djj.bsrypassword}")
    private String bsrypassword;

    @Autowired
    private BdcInteractService bdcInteractService;

    @Autowired
    private NewPlatSettings newPlatSettings;

    @Autowired
    private FileNameConfigService fileNameConfigService;


   /**
    * 功能描述: 不动产抵押登记转内网 <br>
    * 〈〉
    * @Param: [sjsq, fileVoList]
    * @Return: com.springboot.entity.newPlat.transInner.req.fromZY.NewBdcFlowRequest
    * @Author:  Peng Hao
    * @Date: 2020/8/11 8:56
    */
    public  NewBdcFlowRequest  prepareNewBdcFlowRequestForBDCDYDJ(SJ_Sjsq sjsq, List<SJ_Fjfile> fileVoList) throws ParseException {
        //基本数据生成
        NewBdcFlowRequest newBdcFlowRequest = ParamConvertUtil.getBaseFromSjsq(
                sjsq,                               //收件申请
                newPlatSettings,                    //新平台配置
                NEWPLAT_TURNINNERS_BDCDY,           //新平台业务类型
                BDC_DJLB_DBYW,                      //登记类别
                LZDZ_LDKQ                           //领证地址
        );
        //补全房屋主体信息并补原业务号进转内网主体--根据权属数据
        ParamConvertUtil.fillMainHouseToReqByQlxx(newBdcFlowRequest,sjsq.getImmovableRightInfoVoList(),BDC_DATA_TYPE_SC);
        //补全抵押信息
        newBdcFlowRequest.setDyxx(bdcInteractService.initDyxx(
                sjsq.getReceiptNumber(),
                newBdcFlowRequest.getYywh(),
                newBdcFlowRequest.getYqllx(),
                sjsq.getMortgageContractInfo(),fileVoList)
        );
        return  newBdcFlowRequest;
    }


    /**
     * 功能描述:  不动产预抵押转内网<br>
     * 〈〉
     * @Param: [sjsq, fileVoList]
     * @Return: com.springboot.entity.newPlat.transInner.req.fromZY.NewBdcFlowRequest
     * @Author: Peng Hao
     * @Date: 2020/8/11 8:56
     */
    public  NewBdcFlowRequest  prepareNewBdcFlowRequestForYGJYGDYDJ(SJ_Sjsq sjsq, List<SJ_Fjfile> fileVoList) throws ParseException {
        //基本数据生成
        NewBdcFlowRequest newBdcFlowRequest = ParamConvertUtil.getBaseFromSjsq(
                sjsq,                               //收件申请
                newPlatSettings,                    //新平台配置
                NEWPLAT_TURNINNERS_YGYD,           //新平台业务类型
                BDC_DJLB_ZHYW,                      //登记类别
                LZDZ_LDKQ                           //领证地址
        );
        //补全房屋主体信息并补原业务号进转内网主体--根据权属数据
       // ParamConvertUtil.fillMainHouseToReqByDyxx(newBdcFlowRequest,sjsq.getImmovableCurrentMortgageInfoVoList());
        //暂定 新平台获取
//        newBdcFlowRequest.setHtxx(bdcInteractService.initJyhtxx(sjsq.getTransactionContractInfo()));
        //最外层申请人为买卖双方
        newBdcFlowRequest.setSqrxx(bdcInteractService.transSellersAndBuyersToSqr(sjsq.getTransactionContractInfo()));
        //补全附件列表信息
        newBdcFlowRequest.setFjxx(bdcInteractService.transFjxxFromYcToBdc(fileVoList,BDC_NEW_PLAT_FLOW_KEY_YG,sjsq.getReceiptNumber()));
        //补全抵押信息
        newBdcFlowRequest.setDyxx(bdcInteractService.initDyxx(
                sjsq.getReceiptNumber(),
                newBdcFlowRequest.getYywh(),
                null,
                sjsq.getMortgageContractInfo(),
                fileVoList)
        );
        return newBdcFlowRequest;
    }



    /**
     * 功能描述:不动产抵押注销转内网 <br>
     * 〈〉
     * @Param: [sjsq, fileVoList]
     * @Return: com.springboot.entity.newPlat.transInner.req.fromZY.NewBdcFlowRequest
     * @Author: Peng Hao
     * @Date: 2020/8/11 14:47
     */
    public  NewBdcFlowRequest  prepareNewBdcFlowRequestForDYZXDJ(SJ_Sjsq sjsq, List<SJ_Fjfile> fileVoList) throws ParseException {
        //基本数据生成
        NewBdcFlowRequest newBdcFlowRequest = ParamConvertUtil.getBaseFromSjsq(
                sjsq,                               //收件申请
                newPlatSettings,                    //新平台配置
                NEWPLAT_TURNINNERS_DYZX,           //新平台业务类型
                BDC_DJLB_DBYW,                      //登记类别
                LZDZ_LDKQ                           //领证地址
        );
        //房屋信息
        //补全房屋主体信息并补原业务号进转内网主体--根据权属数据
        ParamConvertUtil.fillMainHouseToReqByDyxx(newBdcFlowRequest,sjsq.getImmovableCurrentMortgageInfoVoList(),BDC_DATA_TYPE_SC);
        //最外层申请为抵押权人
        newBdcFlowRequest.setSqrxx(bdcInteractService.bdcMortDyqrToSqr(sjsq.getImmovableCurrentMortgageInfoVoList()));
        //补全附件列表信息
        newBdcFlowRequest.setFjxx(bdcInteractService.transFjxxFromYcToBdc(fileVoList,BDC_NEW_PLAT_FLOW_KEY_DYZX,sjsq.getReceiptNumber()));
        //附件信息
        return newBdcFlowRequest;
    }




}
