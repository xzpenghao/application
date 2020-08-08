package com.springboot.entity.newPlat.transInner.req.fromZY;

import com.springboot.entity.newPlat.transInner.req.fromZY.domain.NewBdcFlowRespData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewBdcFlowResponse {
    private String code;
    private String msg;
    private List<NewBdcFlowRespData> data;
}