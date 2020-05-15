package com.springboot.entity.chenbin.personnel.req;

import com.springboot.popj.pub_data.SJ_Exception_Record;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReqSendForWEGEntity {
    private String sqbh;
    private String taskId;
    private String handleKey;
    private String excutFeign;
    private String excutMethod;
    private boolean execAgain;
    private SJ_Exception_Record exc;
}
