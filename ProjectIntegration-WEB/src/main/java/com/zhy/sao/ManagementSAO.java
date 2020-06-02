package com.zhy.sao;

import com.zhy.sao.impl.ManagementSAOImpl;
import com.zhy.vo.RestfulResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "ProjectIntegration-Management", fallback = ManagementSAOImpl.class)
public interface ManagementSAO {

    @PostMapping("/test")
    RestfulResultVO test();

}
