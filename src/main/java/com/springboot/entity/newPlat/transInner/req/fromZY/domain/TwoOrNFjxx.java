package com.springboot.entity.newPlat.transInner.req.fromZY.domain;

import com.springboot.entity.SJ_Fjfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenb
 * @version 2020/8/12
 * description：待操作的附件对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TwoOrNFjxx {
    //来源
    private TransFjxx come;
    //目标
    private List<TransFjxx> to;

    public TwoOrNFjxx initByTwo(String comeSaveType,String comeIfFtpKey,String comePath,String toSaveType,String toIfFtpKey,String toPath){
        this.come = new TransFjxx(comeSaveType,comeIfFtpKey,comePath);
        List<TransFjxx> twoTo = new ArrayList<>();
        twoTo.add(new TransFjxx(toSaveType,toIfFtpKey,toPath));
        this.to = twoTo;
        return this;
    }
}
