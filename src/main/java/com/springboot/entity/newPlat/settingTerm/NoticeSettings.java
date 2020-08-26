package com.springboot.entity.newPlat.settingTerm;

import com.springboot.entity.newPlat.settingTerm.base.BaseSettings;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author chenb
 * @version 2020/8/26
 * description：通知类设置
 */
@Data
@Component
@ConfigurationProperties(prefix = "notice")
public class NoticeSettings extends BaseSettings<NoticeSettingTerm> {

}
