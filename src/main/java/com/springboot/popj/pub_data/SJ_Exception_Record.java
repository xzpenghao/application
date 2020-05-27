package com.springboot.popj.pub_data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import static com.springboot.constant.chenbin.KeywordConstant.*;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sj_exception_record")
public class SJ_Exception_Record implements Serializable {

    @Id
    private String id;                          //主键
    private String receiptNumber;               //收件编号
    private String registerNumber;              //受理编号
    private String businessName;                //业务名称
    private String departCode;                  //部门标识（目前没有）
    private String taskId;                      //任务ID
    private String excType;                     //异常类型(空、JintError<接口执行异常>；FeignError<feign执行异常>；NoticeError<通知（回推）接口>执行异常)
    private String excMsg;                      //异常信息
    private String happenKey;                   //触发关键字（feign类异常独有）
    private String happenFeign;                 //触发异常记录的Feign（feign类异常独有）
    private String happenMethod;                //触发异常记录的feign方法（feign类异常独有）
    private Date happenTime;                    //异常发生时间
    private String taskDirection;               //任务执行方向
    private String noticeType;                  //通知类型（通知类异常独有）
    private String noticeUrl;                   //通知处理的URL（通知类异常独有）
    private String noticeText;                  //通知内容（通知类异常独有）
    private String feignNoticeExecutParams;     //执行参数记录（通知类和feign异常独有）
    private String handleStatus;                //处理状态
    private String handleResult;                //处理结果
    private Date handleTime;                    //异常处理时间
    private String handleMan;                   //处理人
    private String ext1;
    private String ext2;
    private String ext3;

    public SJ_Exception_Record excMsgg(String msg){
        this.excMsg = msg;
        return this;
    }

    public SJ_Exception_Record valuationTaskAndType(String taskId,String excType,String happenKey){
        this.setTaskId(taskId);
        this.setExcType(excType);
        this.setHappenKey(happenKey);
        return this;
    }

    public SJ_Exception_Record initNewExcp(String taskId,String excMsg,String sqbh,String businessName,String direction){
        this.setTaskId(taskId);
        this.setExcMsg(excMsg);
        this.setReceiptNumber(sqbh);
        this.setBusinessName(businessName);
        this.setTaskDirection(direction);
        return this;
    }

    //通知类异常的转型
    public SJ_Exception_Record transitThisToNotice(String param,String noticeType,String noticeUrl,String noticeText){
        this.setExcType(EXC_TYPE_NOTICEERROR);
        this.setFeignNoticeExecutParams(param);
        this.setNoticeType(noticeType);
        this.setNoticeUrl(noticeUrl);
        this.setNoticeText(noticeText);
        return this;
    }

    //feign类异常的转型
    public SJ_Exception_Record transitThisToFeign(String param,String happenKey,String happenFeign,String happenMethod){
        this.setExcType(EXC_TYPE_FEIGNERROR);
        this.setFeignNoticeExecutParams(param);
        this.setHappenKey(happenKey);
        this.setHappenFeign(happenFeign);
        this.setHappenMethod(happenMethod);
        return this;
    }

    //获取样例
    public SJ_Exception_Record giveAnExample(){
        SJ_Exception_Record example = new SJ_Exception_Record();

        example.setTaskId(this.getTaskId());
        example.setExcType(this.getExcType());
        example.setReceiptNumber(this.getReceiptNumber());
        example.setTaskDirection(this.getTaskDirection());
        if(StringUtils.isBlank(this.getExcType()) || EXC_TYPE_JINTERROR.equals(this.getExcType())){
            example.setBusinessName(this.getBusinessName());
        } else if(EXC_TYPE_FEIGNERROR.equals(this.getExcType())){
            example.setHappenFeign(this.getHappenFeign());
            example.setHappenMethod(this.getHappenMethod());
            example.setHappenKey(this.getHappenKey());
        } else if(EXC_TYPE_NOTICEERROR.equals(this.getExcType())){
            example.setNoticeType(this.getNoticeType());
        } else {
            //未定义的异常类型，理论上准予记录（返回整体就行）
            example = this;
            log.info("获取异常样例时出现未定义的异常类型，异常ID已记录为："+this.getId()+"; 触发的收件为：“"+ this.getReceiptNumber() +"” & “"+ this.getRegisterNumber() +"”触发的任务为：" + this.getTaskId());
        }
        return example;
    }
}
