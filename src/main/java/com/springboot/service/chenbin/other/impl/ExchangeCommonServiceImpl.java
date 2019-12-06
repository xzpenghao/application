package com.springboot.service.chenbin.other.impl;

import com.alibaba.fastjson.JSONObject;
import com.springboot.component.chenbin.file.FromFTPDownloadComponent;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.SJ_Fjfile;
import com.springboot.entity.chenbin.personnel.pub_use.FileEntityForOther;
import com.springboot.service.chenbin.other.ExchangeCommonService;
import com.springboot.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("commService")
public class ExchangeCommonServiceImpl implements ExchangeCommonService {
    @Autowired
    private FromFTPDownloadComponent fromFTPDownloadComponent;

    @Override
    public FileEntityForOther getFileEntityForOther(FileEntityForOther param) {
        param.setRqDepart(null);
        log.info("本次拉取传入完整参数："+ JSONObject.toJSONString(param));
        String path_temp = param.getFtpPath();
        if(StringUtils.isBlank(path_temp)){
            throw new ZtgeoBizException("获取"+param.getFileId()+"号附件时，附件路径不能传入空值");
        }
        log.info("传入路径："+path_temp);
        if(path_temp.contains("\\")){
            path_temp = path_temp.replaceAll("\\\\","/");
        }
        log.info("处理后路径："+path_temp);
        String originFileName = StrUtil.getFTPFileNameByFTPPath(path_temp);
        log.info("传入文件名："+originFileName);
        path_temp = StrUtil.getFTPRemotePathByFTPPath(path_temp);
        log.info("文件存放路径："+path_temp);
        SJ_Fjfile file = new SJ_Fjfile();
        if (fromFTPDownloadComponent.downFile(
                path_temp,
                originFileName,
                file
        )) {
            if(file.getFileContent()!=null){
                BASE64Encoder encoder = new BASE64Encoder();
                String base64 = encoder.encode(file.getFileContent());
                System.out.println("base64:"+base64);
                param.setFileBase64(base64);
            }
        }
        return param;
    }

    @Override
    public List<FileEntityForOther> getFileEntityArrayForOther(List<FileEntityForOther> params) {
        List<FileEntityForOther> results = new ArrayList<FileEntityForOther>();
        if(params!=null){
            for(FileEntityForOther param:params){
                results.add(getFileEntityForOther(param));
            }
        }
        return results;
    }
}
