package com.springboot.entity.newPlat.settingTerm.base;

import com.springboot.entity.newPlat.settingTerm.TurnInnerSettingsTerm;
import lombok.Data;

import java.util.List;

/**
 * @author chenb
 * @version 2020/8/9
 * description：基础配置类
 */
@Data
public class BaseSettings<T extends BaseTerm> {
    private List<T> settings;

    public T gainTermByKey(String key){
        GainSetting<T> gainSetting = key1 ->{
            for(T setting:settings){
                if(key1.equals(setting.getKey()))
                    return setting;
            }
            return null;
        };
        return gainSetting.gainTerm(key);
    }

    interface GainSetting<T> {
        T gainTerm(String key);
    }
}
