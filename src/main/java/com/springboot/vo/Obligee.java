package com.springboot.vo;

import lombok.Data;

/**
 * @author sk
 * @version 2020/5/28
 * description：债权人
 */
@Data
public class Obligee {

    /** 债权人姓名 */
    private String obligeeName;
    /** 债权人证件类型 */
    private String obligeeIdType;
    /** 债权人证件号码 */
    private String obligeeId;
    /** 债权人类型 */
    private String obligeeType;

}
