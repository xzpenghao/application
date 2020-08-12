package com.springboot.entity.newPlat.transInner.req.fromZY.domain;

import com.springboot.entity.SJ_Fjfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenb
 * @version 2020/8/12
 * description：待操作的附件对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TwoFjxx {
    private String comePath;
    private String toPath;
}
