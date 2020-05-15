package com.springboot.entity.chenbin.personnel.OtherEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenb
 * @version 2020/5/12/012
 * description：
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EleWatGasHandle {
    boolean handleEle;   //电处理状态标识
    boolean handleWat;   //水处理状态标识
    boolean handleGas;   //气处理状态标识

    //初始化水电气的执行标志位
    public EleWatGasHandle initThis(){
        this.handleEle = true;
        this.handleWat = false;
        this.handleGas = false;
        return this;
    }
}
