package com.springboot.entity.chenbin.personnel.other.paph;

import lombok.Data;

import java.util.List;

/**
 * @author chenb
 * @version 2020/8/28
 * description：平安普惠的抵押结果，包括1.是否返回结果标识；2.结果详情
 */
@Data
public class PaphDyjg {
    private boolean isCanSee;
    private List<PaphDyxx> pdyxxs;
}
