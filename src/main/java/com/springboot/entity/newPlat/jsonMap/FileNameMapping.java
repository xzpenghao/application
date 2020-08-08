package com.springboot.entity.newPlat.jsonMap;

import lombok.Data;

import java.util.List;

/**
 * @author chenb
 * @version 2020/8/8
 * description：不动产附件名称JSON文件映射类
 */
@Data
public class FileNameMapping {
    private String sid;
    private String sname;
    private String lkey;
    private List<String> mappingName;
}
