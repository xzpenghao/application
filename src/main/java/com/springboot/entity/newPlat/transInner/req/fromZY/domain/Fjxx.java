/**
 * Copyright 2020 bejson.com
 */
package com.springboot.entity.newPlat.transInner.req.fromZY.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fjxx {

    private String fj;          //附件
    private String fjdz;        //附件地址
    private String sxh;         //顺序号
    private String wjjmc;       //文件夹名称
    private String wjlx;        //文件类型（后缀名）
    private String wjmc;        //文件名称

}