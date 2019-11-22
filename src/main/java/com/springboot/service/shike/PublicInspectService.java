package com.springboot.service.shike;

import com.springboot.vo.JudicialQueryVo;

public interface PublicInspectService {
    JudicialQueryVo JudicialQuery(String bdczh,String type);
}
