package com.springboot.entity.chenbin.personnel.req;

import com.springboot.popj.pub_data.SJ_Info_Sdqgxx;

/**
 * @author chenb
 * @version 2020/6/12/012
 * description：
 */
public class TRQReqEntity extends SDQReqEntity {
    public void assignOrg(SJ_Info_Sdqgxx sdqgxx){
        this.setOrgNo(sdqgxx.getGasCompony());
    }
}
