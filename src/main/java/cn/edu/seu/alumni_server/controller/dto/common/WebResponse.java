package cn.edu.seu.alumni_server.controller.dto.common;

import cn.edu.seu.alumni_server.common.CONST;
import lombok.Data;

@Data
public class WebResponse<T> {
    int code;
    String message;
    T body;

    public WebResponse(int code, String message, T body) {
        this.code = code;
        this.message = message;
        this.body = body;
    }

    /* 快捷方式 */

    public WebResponse() {
        this.code = CONST.SUCCESS_CODE;
        this.message = CONST.SUCCESS_MESSAGE_DEFAULT;
    }

    public WebResponse<T> success() {
        this.code = CONST.SUCCESS_CODE;
        this.message = CONST.SUCCESS_MESSAGE_DEFAULT;
        return this;
    }

    public WebResponse<T> success(T body) {
        this.code = CONST.SUCCESS_CODE;
        this.message = CONST.SUCCESS_MESSAGE_DEFAULT;
        this.body = body;
        return this;
    }

    public WebResponse<T> fail() {
        this.code = CONST.SUCCESS_CODE;
        this.message = CONST.SUCCESS_MESSAGE_DEFAULT;
        return this;
    }

    public WebResponse<T> fail(T body) {
        this.code = CONST.SUCCESS_CODE;
        this.message = CONST.SUCCESS_MESSAGE_DEFAULT;
        this.body = body;
        return this;
    }
}
