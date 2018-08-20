package com.minivision.openplatform.lmdb.server.api.controller;

import com.minivision.openplatform.lmdb.server.common.response.RestResult;
import com.minivision.openplatform.lmdb.server.core.lmdb.LmdbOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/lmdb")
public class LmdbServerController {

    private LmdbOperation lmdbOperation;

    @Autowired
    public LmdbServerController(LmdbOperation lmdbOperation) {
        this.lmdbOperation = lmdbOperation;
    }

    @RequestMapping(value = "postValToDb",method = RequestMethod.GET)
    public RestResult postValToDb(){
        RestResult<String> restResult = new RestResult<>();
        Map<byte[],byte[]> map = new HashMap<>();
        for(int i = 1;i<=100000;i++){
            map.put(("caixing"+i).getBytes(StandardCharsets.UTF_8),("maochunhua"+i).getBytes(StandardCharsets.UTF_8));
        }
        lmdbOperation.putValueToDb("faceset01",map);
        return restResult;
    }

    @RequestMapping(value = "getValByKey/{key}",method = RequestMethod.GET)
    public RestResult getValByKey(@PathVariable("key") String key){
        RestResult<Map<String,String>> restResult = new RestResult<>();
        ByteBuffer val = lmdbOperation.getValueByKey("faceset01",key.getBytes(StandardCharsets.UTF_8));
        String value = StandardCharsets.UTF_8.decode(val).toString();
        Map<String,String> data = new HashMap<>();
        data.put(key,value);
        restResult.setData(data);
        return restResult;
    }

    @RequestMapping(value = "getDbCount",method = RequestMethod.GET)
    public RestResult getDbCount(){
        RestResult<Map<String,Integer>> restResult = new RestResult<>();
        int count = lmdbOperation.getDbCount("faceset01");
        Map<String,Integer> map = new HashMap<>();
        map.put("faceset01Count",count);
        restResult.setData(map);
        return restResult;
    }

    @RequestMapping(value = "getDbVal",method = RequestMethod.GET)
    public RestResult getDbVal(){
        RestResult<Map<String,String>> restResult = new RestResult<>();
        Map<byte[],byte[]> map = lmdbOperation.getAllValueByDbName("faceset01");
        Map<String,String> resultMap = new HashMap<>();
        for(Map.Entry<byte[],byte[]> entry:map.entrySet()){
            resultMap.put(new String(entry.getKey(),StandardCharsets.UTF_8),new String(entry.getValue(),StandardCharsets.UTF_8));
        }
        restResult.setData(resultMap);
        return restResult;
    }

}
