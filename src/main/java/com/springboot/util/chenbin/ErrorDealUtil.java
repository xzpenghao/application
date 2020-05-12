package com.springboot.util.chenbin;

import com.springboot.config.ZtgeoBizException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorDealUtil {
    public static String getErrorInfo(Exception e) {
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            //将出错的栈信息输出到printWriter中
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (pw != null) {
                pw.close();
            }
        }
        return sw.toString();
    }

    public static ZtgeoBizException OnlineErrorTrans(ZtgeoBizException e){
        if (e.getMessage().contains("connect timed out")) {
            return new ZtgeoBizException("接口连接超时");
        }
        if (e.getMessage().contains("404")) {
            return new ZtgeoBizException("请求地址错误");
        }
        return e;
    }
}
