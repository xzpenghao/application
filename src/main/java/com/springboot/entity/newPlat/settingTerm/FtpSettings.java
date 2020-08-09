package com.springboot.entity.newPlat.settingTerm;

import com.springboot.entity.newPlat.settingTerm.base.BaseSettings;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author chenb
 * @version 2020/8/9
 * description：ftp配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "ftp")
public class FtpSettings extends BaseSettings<FtpSettingTerm> {
    private FtpDealSetting isDealFtp;
}
