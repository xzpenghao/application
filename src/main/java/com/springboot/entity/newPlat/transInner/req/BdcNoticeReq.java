package com.springboot.entity.newPlat.transInner.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author chenb
 * @version 2020/7/30/030
 * description：不动产节点通知body
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BdcNoticeReq {
    private String jdbs;            //节点标识
    private List<String> ywhlb;     //业务列表
    private String wsywh;           //网申业务号（这里是一窗的收件申请编号）
}
