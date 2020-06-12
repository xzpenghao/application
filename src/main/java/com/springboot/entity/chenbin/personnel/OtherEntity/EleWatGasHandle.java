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
    boolean handleTV = false;    //tv处理状态标识

    //初始化水电气的执行标志位
    public EleWatGasHandle initThis(){
        this.handleEle = true;
        this.handleWat = true;
        this.handleGas = false;
        return this;
    }

    //水电气单水执行
    public EleWatGasHandle onlyWat(){
        this.handleWat = true;
        this.handleEle = false;
        this.handleGas = false;
        return this;
    }
    //水电气单电执行
    public EleWatGasHandle onlyEle(){
        this.handleEle = true;
        this.handleWat = false;
        this.handleGas = false;
        return this;
    }
    //水电气单气执行
    public EleWatGasHandle onlyGas(){
        this.handleGas = true;
        this.handleWat = false;
        this.handleEle = false;
        return this;
    }
    //所有的都不执行
    public EleWatGasHandle allNoExec(){
        this.handleGas = false;
        this.handleWat = false;
        this.handleEle = false;
        return this;
    }
}
