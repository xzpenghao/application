package com.springboot.entity.newPlat.settingTerm;

import com.springboot.entity.newPlat.settingTerm.base.BaseTerm;
import lombok.Data;

/**
 * @author chenb
 * @version 2020/8/26
 * description：通知子设置项
 */
@Data
public class NoticeSettingTermChild extends BaseTerm {
    private String noticeType;
    private String noticeText;
}
