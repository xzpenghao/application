package com.springboot.util.chenbin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class IDUtil {
    public static String getBinID() {
        StringBuffer sb = new StringBuffer();
        sb.append("Bin-").append(new SimpleDateFormat("yyMMddHHmmss").format(new Date())).append("-")
                .append(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        return sb.toString();
    }
}
