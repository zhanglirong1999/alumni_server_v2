package cn.edu.seu.alumni_server.common.dto;

import cn.edu.seu.alumni_server.common.CONST;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WebResponseByObject {

	Integer status;
	String message;
	Object data;

	public WebResponseByObject() {
		this.status = CONST.SUCCESS_CODE;
		this.message = CONST.SUCCESS_MESSAGE_DEFAULT;
		this.data = null;
	}

	public WebResponseByObject success(WebServiceSuccessMessage message) {
		this.status = CONST.SUCCESS_CODE;
		this.message = message.getMessage();
		this.data = null;
		return this;
	}

	public WebResponseByObject success(Object _data) {
		this.status = CONST.SUCCESS_CODE;
		this.message = CONST.SUCCESS_MESSAGE_DEFAULT;
		this.data = _data;
		return this;
	}

	public WebResponseByObject fail(WebServiceExceptionMessage _message) {
		return this.fail(_message.getMessage(), null);
	}

	public WebResponseByObject fail(String _message, Object _data) {
		this.status = CONST.FAIL_CODE;
		this.message = _message;
		this.data = _data;
		return this;
	}

}
