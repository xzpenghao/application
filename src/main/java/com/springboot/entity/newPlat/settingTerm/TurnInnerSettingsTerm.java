package com.springboot.entity.newPlat.settingTerm;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author chenb
 * @version 2020/8/6
 * description：转内网的保障型设置
 */
@Data
public class TurnInnerSettingsTerm {
    private String key;
    private String sid;
    private String sname;
    private String jsrid;
    private String jsrmc;
}
