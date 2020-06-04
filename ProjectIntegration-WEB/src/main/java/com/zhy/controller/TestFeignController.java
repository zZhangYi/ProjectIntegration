package com.zhy.controller;

import com.zhy.sao.ManagementSAO;
import com.zhy.vo.RestfulResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestFeignController {

    @Autowired
    private ManagementSAO managementSAO;

    @PostMapping("/testFeign")
    public RestfulResultVO testFeign() {
        return managementSAO.test();
    }

}
