package com.springboot.entity.chenbin.personnel.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenb
 * @version 2020/4/29/029
 * description：
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DLFile {
    private String fileName;
    private String fileData;
}
