package com.minivision.openplatform.lmdb.server.common.utils;

import com.alibaba.fastjson.JSON;
import com.minivision.openplatform.lmdb.server.common.exception.ErrorCode;
import com.minivision.openplatform.lmdb.server.common.response.RestResult;
import com.minivision.openplatform.lmdb.server.common.response.RestResultBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharEncoding;

import javax.servlet.ServletResponse;
import java.io.PrintWriter;

/**
 * <Description> <br>
 *
 * @author caixing<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2018年07月13日 <br>
 */
@Slf4j
public class WebUtils {


    private WebUtils(){}


    /**
     * 直接使用 response 输出JSON
     *
     * @param response
     * @param errorCode
     */
    public static void outJSON(ServletResponse response, ErrorCode errorCode, Object data) {
        RestResult restResult = null;
        if (data != null) {
            restResult = RestResultBuilder.builder().errorCode(errorCode).data(data).build();
        } else {
            restResult = RestResultBuilder.builder().errorCode(errorCode).build();
        }
        outJSON(response, restResult);
    }

    /**
     * 直使用 response 输出JSON
     *
     * @param response
     * @param errorCode
     */
    public static void outJSON(ServletResponse response, ErrorCode errorCode) {
        outJSON(response, RestResultBuilder.builder().errorCode(errorCode).build());
    }

    /**
     * 直使用 response 输出JSON
     *
     * @param response
     * @param content
     */
    public static void outJSON(ServletResponse response, Object content) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding(CharEncoding.UTF_8);//设置编码
            response.setContentType("application/json; charset=utf-8");//设置返回类型
            out = response.getWriter();
            out.println(JSON.toJSONString(content));//输出
        } catch (Exception e) {
            log.error("json out error:{}", e.getMessage());
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
    }

}
