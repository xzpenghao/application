package com.springboot.popj.pub_data;

import com.springboot.entity.chenbin.personnel.req.ReqSendForWEGEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author chenb
 * @version 2020/5/18/018
 * description：
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SJ_Sdqg_Send_Result {
    private String id;
    private String sqbh;            //收件编号
    private String taskId;          //任务ID
    private String sendKey;         //关键字
    private String sendCompony;     //发送的公司
    private String result;          //结果
    private String resultStr;       //结果字符串
    private String resultJson;      //原结果
    private String insertTime;      //入库时间，不做赋值

    public SJ_Sdqg_Send_Result initSuccessFromWEGEntity(ReqSendForWEGEntity sendTransferEntity,String sendKey){
        this.sqbh = sendTransferEntity.getSqbh();
        this.taskId = sendTransferEntity.getTaskId();
        this.sendKey = sendKey;
        return this;
    }
}
