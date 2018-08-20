package com.minivision.openplatform.lmdb.server.common.response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * <Description> <br>
 *
 * @author caixing<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2018年07月13日 <br>
 */
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResult<T extends Object> implements Serializable {

    private static final long serialVersionUID = 4552584033085803852L;

    @JSONField(ordinal = 1)
    private int timeused;

    @JSONField(ordinal = 2)
    private int code;

    @JSONField(ordinal = 3)
    private String message;

    @JSONField(ordinal = 4)
    private T data;

    @Override
    public String toString() {
        return "RestResult(code=" + this.code + ", message=" + this
                .message + ", data=" + JSON.toJSONString(this.data) + ")";
    }

}
