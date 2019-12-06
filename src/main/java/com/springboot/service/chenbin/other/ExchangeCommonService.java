package com.springboot.service.chenbin.other;

import com.springboot.entity.chenbin.personnel.pub_use.FileEntityForOther;

import java.util.List;

public interface ExchangeCommonService {
    FileEntityForOther getFileEntityForOther(FileEntityForOther param);
    List<FileEntityForOther> getFileEntityArrayForOther(List<FileEntityForOther> params);
}
