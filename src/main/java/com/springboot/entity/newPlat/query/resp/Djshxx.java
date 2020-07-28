package com.springboot.entity.newPlat.query.resp;

import com.springboot.entity.newPlat.query.bizData.Shxx;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author chenb
 * @version 2020/7/24/024
 * description：登记审核信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Djshxx {

    private String ywh;             //业务号
    private List<Shxx> shxxlb;      //审核信息列表

}
