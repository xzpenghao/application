package com.springboot.entity.newPlat.settingTerm;

import com.springboot.entity.newPlat.settingTerm.base.BaseTerm;
import lombok.Data;

/**
 * @author chenb
 * @version 2020/8/9
 * description：FTP的设置项实体类
 */
@Data
public class FtpSettingTerm extends BaseTerm {
    private String ftpAddress;
    private String ftpPort;
    private String ftpUsername;
    private String ftpPassword;

    private String ftpTransMode;            //主被动模式，passive-被动/active-主动
    private String env;
}
