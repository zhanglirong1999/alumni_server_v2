package cn.edu.seu.alumni_server.controller.dto.common;

import cn.edu.seu.alumni_server.common.CONST;
import lombok.Data;

@Data
public class WebResponse<T> {
    int status;
    String message;
    T data;

    public WebResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    /* 快捷方式 */

    public WebResponse() {
        this.status = CONST.SUCCESS_CODE;
        this.message = CONST.SUCCESS_MESSAGE_DEFAULT;
    }

    public WebResponse<T> success() {
        this.status = CONST.SUCCESS_CODE;
        this.message = CONST.SUCCESS_MESSAGE_DEFAULT;
        return this;
    }

    public WebResponse<T> success(T body) {
        this.status = CONST.SUCCESS_CODE;
        this.message = CONST.SUCCESS_MESSAGE_DEFAULT;
        this.data = body;
        return this;
    }

    public WebResponse<T> fail() {
        this.status = CONST.FAIL_CODE;
        this.message = CONST.SUCCESS_MESSAGE_DEFAULT;
        return this;
    }

    public WebResponse<T> fail(String message) {
        this.status = CONST.FAIL_CODE;
        this.message = message;
        return this;
    }

    public WebResponse<T> fail(String message, T body) {
        this.status = CONST.FAIL_CODE;
        this.message = message;
        this.data = body;
        return this;
    }
}
