package cn.edu.seu.alumni_server.common.config.web;

import cn.edu.seu.alumni_server.common.CONST;
import cn.edu.seu.alumni_server.controller.dto.common.WebResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局Controller层异常处理类
 */
@ControllerAdvice
public class GlobalExceptionResolver {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionResolver.class);

    /**
     * 处理Controller所有异常
     *
     * @param e 异常
     * @return json结果
     */
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public WebResponse handleException(Throwable e) {
        // 打印异常堆栈信息
        LOG.error(e.getMessage(), e);
        return new WebResponse(CONST.FAIL_CODE, e.getMessage(), e.getStackTrace().toString());
    }

}