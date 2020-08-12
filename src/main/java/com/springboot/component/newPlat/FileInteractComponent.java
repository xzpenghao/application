package com.springboot.component.newPlat;

import com.springboot.entity.newPlat.settingTerm.FtpSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chenb
 * @version 2020/8/12
 * description：全新附件操作component类
 */
@Slf4j
@Component
public class FileInteractComponent {

    @Autowired
    private FtpSettings ftpSettings;

    public void ybcl(){

    }
}
