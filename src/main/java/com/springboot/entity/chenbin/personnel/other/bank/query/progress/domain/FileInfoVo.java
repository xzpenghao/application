package com.springboot.entity.chenbin.personnel.other.bank.query.progress.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileInfoVo implements Serializable {
    private String fileName;
    private String fileType;
    private String fileAdress;
    private String fileSequence;
}
