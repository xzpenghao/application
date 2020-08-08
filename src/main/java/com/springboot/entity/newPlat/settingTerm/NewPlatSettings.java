package com.springboot.entity.newPlat.settingTerm;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author chenb
 * @version 2020/8/6
 * description：配置的装载类
 */
@Data
@Component
@ConfigurationProperties(prefix = "newplat")
public class NewPlatSettings {
    private List<TurnInnerSettingsTerm> turnInners;

    public TurnInnerSettingsTerm gainTermByKey(String key){
        gainTermInter gainTermInter = key1 ->{
            for(TurnInnerSettingsTerm turnInner:turnInners){
                if(key1.equals(turnInner.getKey()))
                    return turnInner;
            }
            return null;
        };
        return gainTermInter.gainTerm(key);
    }

    interface gainTermInter {
        TurnInnerSettingsTerm gainTerm(String key);
    }
}
