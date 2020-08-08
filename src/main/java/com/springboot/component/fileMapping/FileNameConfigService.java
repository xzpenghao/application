package com.springboot.component.fileMapping;

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

//    public List<SidConfig> getSidConfigInfo() {
//        try {
//
//            InputStream inputStream = this.getClass().getResourceAsStream("/config-sid.json");
//
//            byte[] bytes = new byte[inputStream.available()];
//            inputStream.read(bytes);
//            String str = new String(bytes, "UTF-8");
//            return JSONObject.parseArray(str, SidConfig.class);
//        } catch (Exception e) {
//            log.info("读取SID配置文件异常", e);
//            throw new RuntimeException("读取SID配置文件异常！" + e.getMessage());
//        }
//    }
}
