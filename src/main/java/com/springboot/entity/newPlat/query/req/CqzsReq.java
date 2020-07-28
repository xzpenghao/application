package com.springboot.entity.newPlat.query.req;

import com.springboot.popj.warrant.ParametricData;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 *  产权证书查询
 */

@NoArgsConstructor
@Data
public class CqzsReq {

    private String cqzh;
    private String qlrmc;
    private String zl;
    private String fwdm;
    private String bdcdyh;

    public CqzsReq initByYCParams(ParametricData yccs){
        this.cqzh = yccs.getBdczh();
        this.qlrmc = yccs.getQlrmc();
        this.bdcdyh = yccs.getBdcdyh();
        return this;
    }
}
