package com.springboot.entity.chenbin.personnel.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenb
 * @version 2020/6/12/012
 * description：
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BooleanWithMsg {
    private boolean bol;
    private String msg;
}
