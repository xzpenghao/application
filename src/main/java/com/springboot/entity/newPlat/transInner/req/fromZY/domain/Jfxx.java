/**
 * Copyright 2020 bejson.com
 */
package com.springboot.entity.newPlat.transInner.req.fromZY.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Jfxx {

    private String bdcdyh;
    private String bdczh;
    private String cfwh;
    private List<Fjxx> fjxx;
    private String fwdm;
    private String id;
    private String jfjg;
    private String jfsj;
    private String jfwh;
    private String jfwj;
    private String jfyy;
    private String yywh;

}