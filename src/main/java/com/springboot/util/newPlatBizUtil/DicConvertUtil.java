package com.springboot.util.newPlatBizUtil;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * @author chenb
 * @version 2020/7/28/028
 * description：新平台字典操作工具类
 */
public class DicConvertUtil {
    public static String getTarget(String pj,String compMethName,String compRestName,Object... ts) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (Object t : ts) {
            String dicVal = t.getClass().getMethod(compMethName).invoke(t).toString();
            if(StringUtils.isNotBlank(dicVal)) {
                if (dicVal.equals(pj)) {
                    return t.getClass().getMethod(compRestName).invoke(t).toString();
                }
            }
        }
        return null;
    }
    public static String getDicNameByVal(String val,Object... ts) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return getTarget(val,"getDicVal","getDicName",ts);
    }
    public static String getDicValByName(String name,Object... ts) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return getTarget(name,"getDicName","getDicVal",ts);
    }
}
