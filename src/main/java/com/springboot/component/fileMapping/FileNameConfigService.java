package com.springboot.component.fileMapping;

import com.alibaba.fastjson.JSONObject;
import com.springboot.entity.newPlat.jsonMap.FileNameMapping;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * 业务流程SID配置文件读取服务
 */
@Service(value = "fileNameConfigService")
public class FileNameConfigService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    public List<FileNameMapping> getFileMapConfigInfo() {
        try {

            InputStream inputStream = this.getClass().getResourceAsStream("/fileNameMapping.json");

            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            String str = new String(bytes, "UTF-8");
            return JSONObject.parseArray(str, FileNameMapping.class);
        } catch (Exception e) {
            log.error("读取FileNaem配置文件异常", e);
            throw new RuntimeException("读取SID配置文件异常！" + e.getMessage());
        }
    }

    public FileNameMapping gainFileMapByKey(String key){
        if(StringUtils.isNotBlank(key)) {
            List<FileNameMapping> fileNameMappings = getFileMapConfigInfo();
            for (FileNameMapping fileNameMapping : fileNameMappings) {
                if (key.equals(fileNameMapping.getLkey()))
                    return fileNameMapping;
            }
        }
        return null;
    }
}
