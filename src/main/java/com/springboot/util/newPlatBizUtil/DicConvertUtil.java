package com.springboot.util.newPlatBizUtil;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * @author chenb
 * @version 2020/7/28/028
 * description：新平台字典操作工具类
 */
public class DicConvertUtil {

    /**
     * 描述：获取目标enum的实际操作方法
     * 作者：chenb
     * 日期：2020/8/3
     * 参数：[pj, compMethName, compRestName, ts]
     * 返回：对应的值
     * 更新记录：更新人：{}，更新日期：{}
     */
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

    /**
     * 描述：通过字典值获取字典名称
     * 作者：chenb
     * 日期：2020/8/3
     * 参数：[val, ts]
     * 返回：字典值取字典名称
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static String getDicNameByVal(String val,Object... ts) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return getTarget(val,"getDicVal","getDicName",ts);
    }

    /**
     * 描述：通过字典名获取字典值
     * 作者：chenb
     * 日期：2020/8/3
     * 参数：[name, ts]
     * 返回：字典名称取字典值
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static String getDicValByName(String name,Object... ts) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return getTarget(name,"getDicName","getDicVal",ts);
    }

    /**
     * 描述：通过关键字标识获取关键字名称
     * 作者：chenb
     * 日期：2020/8/3
     * 参数：[code, ts]
     * 返回：关键字标识取关键字名称
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static String getKeyWordByCode(String code,Object... ts) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return getTarget(code,"getKeyCode","getKeyWord",ts);
    }

    /**
     * 描述：通过关键字名称获取键字标识
     * 作者：chenb
     * 日期：2020/8/3
     * 参数：[word, ts]
     * 返回：关键字名称取键字标识
     * 更新记录：更新人：{}，更新日期：{}
     */
    public static String getKeyCodeByWord(String word,Object... ts) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return getTarget(word,"getKeyWord","getKeyCode",ts);
    }
}
