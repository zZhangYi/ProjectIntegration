package com.zhy.controller;


import com.zhy.vo.RestfulResultVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @PostMapping("/test")
    public RestfulResultVO test(){
        return RestfulResultVO.success("success", null);
    }

}
