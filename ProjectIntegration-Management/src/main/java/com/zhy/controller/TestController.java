package com.zhy.controller;


import com.zhy.vo.RestfulResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private Logger logger = LoggerFactory.getLogger(TestController.class);

    @PostMapping("/test")
    public RestfulResultVO test(){
        logger.info("mgt运行成功");
        return RestfulResultVO.success("1234", null);
    }

}
