package com.springboot.entity.chenbin.personnel.other.bank.business.revok.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileInfoVo implements Serializable {
    private String fileName;
    private String fileType;
    private String fileAdress;
    private String fileSequence;
}
