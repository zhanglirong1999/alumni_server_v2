package cn.edu.seu.alumni_server.interceptor.registration;

import cn.edu.seu.alumni_server.common.CONST;
import cn.edu.seu.alumni_server.common.web_response_dto.WebResponseByObject;
import cn.edu.seu.alumni_server.service.AccountService;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class RegistrationRequiredInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	AccountService accountService;

	@Override
	public boolean preHandle(
		HttpServletRequest request,
		HttpServletResponse response,
		Object handler
	) throws Exception {
		RegistrationRequired temp = this.registrationRequired(handler);
		if (temp == null) {
			return true;
		} else {
			return this.hasRegistered(request, response);
		}
	}

	RegistrationRequired registrationRequired(Object handler) {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		return handlerMethod.getMethod().getAnnotation(RegistrationRequired.class);
	}

	boolean hasRegistered(
		HttpServletRequest request,
		HttpServletResponse response
	) throws IOException {
		Long aid = (Long) request.getAttribute(CONST.ACL_ACCOUNTID);
		boolean ans = this.accountService.hasRegistered(
			aid
		);
		if (!ans) {
			// 没有注册, 重写结果
			response.reset();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = response.getWriter();
			WebResponseByObject object =
				new WebResponseByObject().fail("请完成注册进行更多操作", null);
			object.setStatus(403);
			pw.write(
				new Gson().toJson(
					object
				)
			);
			pw.flush();
			pw.close();
		}
		return ans;
	}
}
