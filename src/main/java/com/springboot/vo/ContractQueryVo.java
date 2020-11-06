package com.springboot.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: penghao
 * @Date: 2020/11/6 10:15
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractQueryVo {

    private String qyslh; //契约受理号
    private String bah;//契约备案号
    private String status;
    private String basj;

}
