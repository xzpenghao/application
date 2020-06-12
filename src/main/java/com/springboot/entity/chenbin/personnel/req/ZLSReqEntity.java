package com.springboot.entity.chenbin.personnel.req;

import com.springboot.popj.pub_data.SJ_Info_Sdqgxx;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenb
 * @version 2020/6/12/012
 * description：
 */
@Data
@NoArgsConstructor
public class ZLSReqEntity extends SDQReqEntity {
    public void assignOrg(SJ_Info_Sdqgxx sdqgxx){
        this.setOrgNo(sdqgxx.getWaterCompony());
    }
}
