package com.springboot.util;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class StrUtil {

    // 通过FTP路径获取文件名
    public static String getFTPFileNameByFTPPath(String ftppath) {
        String ftpFileName = null;
        String[] splitTmp = ftppath.split("/");
        ftpFileName = splitTmp[splitTmp.length - 1];
        return ftpFileName;
    }

    // 通过FTP路径获取FTP目录
    public static String getFTPRemotePathByFTPPath(String ftppath) {
        String remotePath = null;
        StringBuffer sb;
        if (ftppath.startsWith("/")) {
            sb = new StringBuffer();
        } else {
            sb = new StringBuffer("/");
        }
        String[] splitTmp = ftppath.split("/");
        for (int i = 0; i < splitTmp.length - 1; i++) {
            sb.append(splitTmp[i]).append("/");
        }
        remotePath = sb.deleteCharAt(sb.length() - 1).toString();
        log.info("remotePath"+remotePath);
        return remotePath;
    }

    public static String getFTPUrl(String path){
        String remotePath="/"+DateUtils.getNowYear()+"/"+DateUtils.getNowMonth()+"/"+DateUtils.getNowDay()+path;
        return remotePath;
    }
    public static String getFTPAdress(String fileAdress){
        String remotePath="/"+DateUtils.getNowYear()+"/"+DateUtils.getNowMonth()+"/"+DateUtils.getNowDay()+fileAdress;
        return remotePath;
    }


    public static Map<String,String> mapStringToMap(String str){
        str=str.substring(1, str.length()-1);
        String[] strs=str.split(",");
        Map<String,String> map = new HashMap<String, String>();
        for (String string : strs) {
            String key=string.split("=")[0];
            String value=string.split("=")[1];
            map.put(key, value);
        }
        return map;
    }

}
