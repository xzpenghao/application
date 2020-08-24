package com.springboot.entity.newPlat.transInner.req.fromZY.domain;

import com.springboot.entity.SJ_Fjfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.springboot.constant.newPlat.chenbin.HandleKeywordConstant.FTP_USE_FOR_BDC;
import static com.springboot.constant.newPlat.chenbin.HandleKeywordConstant.FTP_USE_FOR_YCSL;

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
        this.come = new TransFjxx(comeSaveType,comeIfFtpKey,comePath,null);
        List<TransFjxx> twoTo = new ArrayList<>();
        twoTo.add(new TransFjxx(toSaveType,toIfFtpKey,toPath,null));
        this.to = twoTo;
        return this;
    }

    public TwoOrNFjxx initYcAndBdcByBase64(String comeBase64,String ycSaveType,String bdcSaveType,SJ_Fjfile file){
        this.come = new TransFjxx("-1",null,null,comeBase64);
        List<TransFjxx> threeTo = new ArrayList<>();
        threeTo.add(new TransFjxx(ycSaveType,FTP_USE_FOR_YCSL,file.getFtpPath(),null));
        if(StringUtils.isNotBlank(bdcSaveType))
            threeTo.add(new TransFjxx(bdcSaveType,FTP_USE_FOR_BDC,file.getBdcMappingPath(),null));
        this.to = threeTo;
        return this;
    }
}
