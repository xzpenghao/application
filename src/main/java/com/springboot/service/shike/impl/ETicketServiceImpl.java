package com.springboot.service.shike.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.springboot.component.chenbin.file.ToFTPUploadComponent;
import com.springboot.config.ZtgeoBizException;
import com.springboot.feign.ExchangeWithOtherFeign;
import com.springboot.service.shike.ETicketService;
import com.springboot.util.Base64Util;
import com.springboot.util.TimeUtil;
import com.springboot.vo.TaxAttachment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.springboot.util.Base64Util.base64ToPdf;

/**
 * @author sk
 * @version 2020/1/16
 */
@Slf4j
@Service("eTicketService")
public class ETicketServiceImpl implements ETicketService {

    @Value("${ETicket.Url}")
    private String ETicketUrl;

    @Value("${ETicket.LocalPath}")
    private String LocalPath;

    @Autowired
    private ToFTPUploadComponent toFTPUploadComponent;

    @Autowired(required = false)
    private ExchangeWithOtherFeign otherFeign;

    /**
     * 描述：电子税票请求共享交换平台获取数据
     * 作者：sk
     * 日期：2020/4/22
     * 参数：{
     * @param receiptNumbers 申请编号集合
     * @param ftpFlag ftp标志位
     * }
     * 返回：结果集
     * 更新记录：更新人：{}，更新日期：{}
    */
    @Override
    public List<TaxAttachment> ETicketQuery(List<String> receiptNumbers, String ftpFlag) {
        if (null == receiptNumbers || "".equals(receiptNumbers)||StringUtils.isBlank(ftpFlag)){
            throw new ZtgeoBizException("参数格式错误");
        }
        log.info("查询集合:{},ftp类型:{}",receiptNumbers,ftpFlag);
        //1.0请求税务接口
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("sqbh", receiptNumbers.get(0));
        ObjectRestResponse<String> result = otherFeign.getETicket(paramMap);
        log.info("查询参数:{}",JSON.toJSONString(paramMap));
        if (null == result){
            throw new ZtgeoBizException("查询无响应");
        }
        log.info("查询结果:{}",JSON.toJSONString(result));
        //2.0处理税务返回数据
        List<TaxAttachment> taxAttachments;
        if (result.getStatus() != HttpStatus.HTTP_OK){
            throw new ZtgeoBizException("查询信息异常："+result.getMessage());
        }else {
            taxAttachments = JSONArray.parseArray(result.getData(),TaxAttachment.class);
        }
        if (CollUtil.isEmpty(taxAttachments)){
            throw new ZtgeoBizException("查询数据为空");
        }

        //3.0文件保存
        switch (ftpFlag){
            case "0":
                taxAttachments = dealETicketWithLocal(taxAttachments);
                break;
            case "1":
                taxAttachments = dealETicketWithFtp(taxAttachments);
                break;
            default:
                throw new ZtgeoBizException("未识别的ftp标志位");
        }
        return taxAttachments;
    }

    /**
     * ftp保存并处理附件数据
     * @param taxAttachments 附件数据
     * @return 处理后的附件数据
     */
    private List<TaxAttachment> dealETicketWithFtp(List<TaxAttachment> taxAttachments){
        List<TaxAttachment> taxAttachmentList = new ArrayList<>();
        log.info("taxAttachments:{}",taxAttachments);
        for (TaxAttachment taxAttachment:taxAttachments) {
            List<TaxAttachment.ETax> dzsps = taxAttachment.getDzsps();
            log.info("dzsps:{}",dzsps);
            List<TaxAttachment.ETax> dzspList = new ArrayList<>();
            if (null!=dzsps&&!"".equals(dzsps)){
                for (TaxAttachment.ETax eTax:dzsps){
                    log.info("base64:{}",eTax.getImgBase64());
                    if (null!=eTax.getImgBase64()&&!"".equals(eTax.getImgBase64())){
                        String date = TimeUtil.getDateString(new Date());
                        String path = "/"+date.substring(0,4)+"/"+date.substring(5,7)+"/"+date.substring(8);
                        String fileName =eTax.getDzsphm()+ "_"+ UUID.randomUUID().toString().substring(0,4)+".pdf";
                        String base64Data = eTax.getImgBase64();
                        byte[] base64Byte = Base64Util.decode(base64Data);
                        String ftpPath = toFTPUploadComponent.ycslUpload(
                                base64Byte,
                                path,
                                fileName,
                                "ycsl"
                        ).replaceAll("/", "\\\\");
                        dzspList.add(new TaxAttachment.ETax(eTax,ftpPath,"1",base64Byte.length));
                    }else{
                        dzspList.add(eTax);
                    }
                }
            }
            taxAttachment.setDzsps(dzspList);
            TaxAttachment attachment = new TaxAttachment(taxAttachment);
            taxAttachmentList.add(attachment);
        }
        return taxAttachmentList;
    }


    /**
     * 本地保存并处理附件数据
     * @param taxAttachments 附件数据
     * @return 处理后的附件数据
     */
    private List<TaxAttachment> dealETicketWithLocal(List<TaxAttachment> taxAttachments){
        List<TaxAttachment> taxAttachmentList = new ArrayList<>();
        log.info("taxAttachments:{}",taxAttachments);
        for (TaxAttachment taxAttachment:taxAttachments) {
            List<TaxAttachment.ETax> dzsps = taxAttachment.getDzsps();
            log.info("dzsps:{}",dzsps);
            List<TaxAttachment.ETax> dzspList = new ArrayList<>();
            if (null!=dzsps&&!"".equals(dzsps)){
                for (TaxAttachment.ETax eTax:dzsps){
                    log.info("base64:{}",eTax.getImgBase64());
                    if (null!=eTax.getImgBase64()&&!"".equals(eTax.getImgBase64())){
                        String date = TimeUtil.getDateString(new Date());
                        String datePath = "/"+date.substring(0,4)+"/"+date.substring(5,7)+"/"+date.substring(8)+"/";
                        String finalPath = LocalPath+datePath;
                        String fileName =eTax.getDzsphm()+ "_"+UUID.randomUUID().toString().substring(0,4)+".pdf";
                        String base64Data = eTax.getImgBase64();

                        if (base64ToPdf(finalPath,base64Data,fileName)){
                            String filePath = finalPath+fileName;
                            filePath =filePath.replaceAll("/", "\\\\");
                            dzspList.add(new TaxAttachment.ETax(eTax,filePath,"0",Base64Util.decode(base64Data).length));
                        }else {
                            log.error("文件：{}保存错误",finalPath+fileName);
                        }
                    }else{
                        dzspList.add(eTax);
                    }
                }
            }
            taxAttachment.setDzsps(dzspList);
            taxAttachmentList.add(new TaxAttachment(taxAttachment));
        }
        return taxAttachmentList;
    }


}
