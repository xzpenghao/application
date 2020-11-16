package com.springboot.service.chenbin;

import com.springboot.entity.chenbin.personnel.PersonnelUnitEntity;
import com.springboot.entity.chenbin.personnel.req.PersonnelUnitReqEntity;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public interface OuterInterfaceHandleService {
    Object getPersonnelUnits(PersonnelUnitReqEntity personnelUnitReq) throws UnsupportedEncodingException;


    Object getContractRecordxx(Map<String,String> params);
}
