package com.springboot.entity.chenbin.personnel.req;

import com.springboot.popj.pub_data.SJ_Exception_Record;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqSendForWEGEntity {
    private String sqbh;                                //申请编号
    private String taskId;                              //任务ID
    private String handleKey;                           //执行关键字-对应执行异常记录的happen_key
    private String excutFeign;                          //执行feign记录
    private String excutMethod;                         //执行方法
    private boolean execAgain;                          //是否再次执行
    private SJ_Exception_Record exc;                    //异常记录
}
