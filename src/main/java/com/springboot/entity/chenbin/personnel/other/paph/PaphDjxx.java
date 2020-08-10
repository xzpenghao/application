package com.springboot.entity.chenbin.personnel.other.paph;

import lombok.Data;

/**
 * @author chenb
 * @version 2020/8/10
 * description：平安普惠冻结信息
 */
@Data
public class PaphDjxx {
    private String djqxq;       //冻结期限起
    private String djqxz;       //冻结期限止
}
