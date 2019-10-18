package com.springboot.entity.chenbin.personnel.other.bank.notice.result.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileInfoVo {
    private String fileName;
    private String fileType;
    private String fileAdress;
    private String fileSequence;
}
