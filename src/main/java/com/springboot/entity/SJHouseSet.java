package com.springboot.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author sk
 * @version 2020/5/20
 * description：房屋套次 实例
 */
@Data
@NoArgsConstructor
public class SJHouseSet {

    /** 房屋套次ID */
    private String houseSetId;
    /** 使用记录ID */
    private String recordId;
    /** 数据获取机构 */
    private String organization;
    /** 不动产证号 */
    private String realEstateId;
    /** 登记日期 */
    private Date registerDate;
    /** 权证人集合 */
    private Object obligeeInfoVoList;
    /** 权证房屋坐落 */
    private String sit;
    /** 房屋权利性质 */
    private String houseRightNature;
    /** 房屋规划用途 */
    private String plannedUsage;
    /** 土地权利性质 */
    private String landRightNature;
    /** 房屋建筑面积 */
    private String architectureArea;
    /** 所在层 */
    private String floor;
    /** 总层数 */
    private String totalFloor;

}
