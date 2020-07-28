package com.springboot.entity.newPlat.query.resp;

import com.springboot.entity.newPlat.query.bizData.Dbjgxx;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenb
 * @version 2020/7/24/024
 * description：登记登簿信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Djdbxx {

    private String ywh;         //业务号
    private String zl;          //坐落
    private String djxl;        //登记小类
    private Dbjgxx dbjgxx;      //登簿结果信息

}
