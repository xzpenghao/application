package com.springboot.entity.newPlat.transInner.req.fromZY;

import com.springboot.entity.newPlat.transInner.req.fromZY.domain.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 描述：组合业务-->主体业务附件放外层，
 *                  从属业务附件放对应数据中
 * 作者：chenb
 * 日期：2020/8/7
 * 更新记录：更新人：{}，更新日期：{}
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewBdcFlowRequest {

    private String sid;                             //sid..
    private String wwdjlxmc;                        //sname..
    private String wwywh;                           //网申业务号..
    private String yywh;                            //原业务号
    private String djlb;                            //登记类别..
    private String jsrid;                           //接收人ID..
    private String jsrmc;                           //接收人名称..
    private String lzdz;                            //领证地址..
    private String sqjly;                           //申请件来源..
    private String tzr;                             //通知人名称..
    private String tzrdh;                           //通知人电话..
    private List<Sqrxx> sqrxx;                      //申请人信息
    private Fwxx fwxx;                              //房屋信息
    private List<Fjxx> fjxx;                        //附件信息（组合业务的主体业务附件以及转移登记时附件）
    private Htxx htxx;                              //合同信息
    private List<Wsxx> wsxx;                        //完税信息
    private List<Dyxx> dyxx;                        //抵押信息
    private Cfxx cfxx;                              //查封信息
    private Jfxx jfxx;                              //解封信息

}