package com.zhy.sao.impl;

import com.zhy.sao.ManagementSAO;
import com.zhy.vo.RestfulResultVO;

public class ManagementSAOImpl implements ManagementSAO {

    @Override
    public RestfulResultVO test() {
        return buildErrorResponse();
    }

    private RestfulResultVO buildErrorResponse() {
        RestfulResultVO response = new RestfulResultVO();
        response.fail("error", "error", null);
        return response;
    }

}
