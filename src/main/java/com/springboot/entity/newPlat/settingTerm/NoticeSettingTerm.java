package com.springboot.entity.newPlat.settingTerm;

import com.springboot.entity.newPlat.settingTerm.base.BaseTerm;
import lombok.Data;

import java.util.List;

/**
 * @author chenb
 * @version 2020/8/26
 * description：通知设置项实体类
 */
@Data
public class NoticeSettingTerm extends BaseTerm {
    private String userName;
    private String password;
    private NoticeSettingTermChild noticeSetting;
    private NoticeBdcSettings bdcSettings;
}
