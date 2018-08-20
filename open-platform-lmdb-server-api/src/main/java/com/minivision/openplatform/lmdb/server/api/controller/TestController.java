package com.minivision.openplatform.lmdb.server.api.controller;

import com.minivision.openplatform.lmdb.server.common.request.TestParam;
import com.minivision.openplatform.lmdb.server.common.response.RestResult;
import com.minivision.openplatform.lmdb.server.core.lmdb.LmdbOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class TestController {

    private LmdbOperation lmdbOperation;

    @Autowired
    public TestController(LmdbOperation lmdbOperation) {
        this.lmdbOperation = lmdbOperation;
    }

    @RequestMapping(value = "test",method = RequestMethod.GET)
    public RestResult test(@Validated TestParam param){
        RestResult<String> result = new RestResult<>();

        return result;
    }

}
