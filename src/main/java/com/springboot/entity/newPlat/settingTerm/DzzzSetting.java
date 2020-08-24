package com.springboot.entity.newPlat.settingTerm;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author chenb
 * @version 2020/8/21
 * description：电子证照设置
 */
@Data
@Component
@ConfigurationProperties(prefix = "dzzz")
public class DzzzSetting {
    private List<String> ignores;
}
