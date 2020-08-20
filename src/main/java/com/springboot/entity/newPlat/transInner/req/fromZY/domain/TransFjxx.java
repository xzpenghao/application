package com.springboot.entity.newPlat.transInner.req.fromZY.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenb
 * @version 2020/8/13
 * description：传输附件描述实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransFjxx {
    private String saveType;        //0-本地；1-ftp；-1-base64
    private String ifFtpKey;
    private String path;
    private String fileBase64;
}
