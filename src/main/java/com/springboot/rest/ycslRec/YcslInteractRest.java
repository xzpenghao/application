package com.springboot.rest.ycslRec;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenb
 * @version 2020/8/19
 * description：外部对一窗受理发起交互的Rest层
 */
@Slf4j
@Api(tags = "外部与一窗产生交互api")
@RestController
@Scope("prototype")
public class YcslInteractRest {
}
