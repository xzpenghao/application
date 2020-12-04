package com.springboot.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.springboot.vo.Obligee;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.springboot.service.shike.impl.SjHouseSetServiceImpl.LX_QLR;
import static com.springboot.service.shike.impl.SjHouseSetServiceImpl.LX_YWR;

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
    /** 债权人类型 0 卖方（义务人），1买方（权利人） */
    private String obligeeType;

    /* 11/16 沭阳新增需求,增加字段: 1.不动产单元号 2.证书状态 抵押>查封>异议 */
    /** 不动产单元号 */
    private String immovableUnicode;
    /** 不动产单元号列表 */
    private List<String> realEstateUnitIdList;
    /** 证书状态 */
    private String status;
    /** 是否抵押 */
    private String isMortgage;
    /** 是否查封 */
    private String isAttach;
    /** 是否异议 */
    private String isDissent;
    /** 预告证明号 */
    private String vormerkungId;

    /**
     * 描述：住建构造参数
     * 作者：sk
     * 日期：2020/9/9
     * 参数：
     * 返回：
     * 更新记录：更新人：{}，更新日期：{}
    */
    public SJHouseSet(Object data,String qlrlx) {
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(data));
        this.sit = jsonObject.getString("fwdz");
        this.houseRightNature = jsonObject.getString("fwxz");
        this.plannedUsage = jsonObject.getString("fwyt");
        this.architectureArea = jsonObject.getString("zfmj");
        this.floor = jsonObject.getString("szlc");
        this.totalFloor = jsonObject.getString("lczs");
        String zjlx = "";
        if (LX_QLR.equals(qlrlx)){
            zjlx = jsonObject.getString("gmfzjlx");
        }else if (LX_YWR.equals(qlrlx)){
            zjlx = jsonObject.getString("mfzjlx");
        }
        List<Obligee> obligeeList = new ArrayList<>();
        Obligee obligee = new Obligee();
        obligee.setObligeeName(jsonObject.getString("gmfxm"));
        obligee.setObligeeId(jsonObject.getString("gmfzjhm"));
        obligee.setObligeeIdType(zjlx);
        obligeeList.add(obligee);
        this.obligeeInfoVoList = JSON.toJSONString(obligeeList);

    }
}
