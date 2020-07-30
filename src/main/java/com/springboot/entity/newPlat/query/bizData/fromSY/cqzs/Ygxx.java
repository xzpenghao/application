package com.springboot.entity.newPlat.query.bizData.fromSY.cqzs;

import com.springboot.entity.newPlat.query.bizData.fromSY.djzl.Qlr;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/***
 *  预告信息
 */

@NoArgsConstructor
@Data
public class Ygxx {

    private String ywh; // 业务号
    private String ygzmh;  //  预告证明号
    private String djsj;  //   登记时间
    private List<Qlr>  qlrlb;

}
