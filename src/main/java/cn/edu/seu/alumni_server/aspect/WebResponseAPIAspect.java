package cn.edu.seu.alumni_server.aspect;

import cn.edu.seu.alumni_server.common.dto.WebResponseByObject;
import cn.edu.seu.alumni_server.common.dto.WebServiceExceptionMessage;
import cn.edu.seu.alumni_server.common.dto.WebServiceSuccessMessage;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class WebResponseAPIAspect {

	@Pointcut("@annotation(cn.edu.seu.alumni_server.annotation.WebResponseAPIMethod)")
	public void methodAPI() {
	}

	@Pointcut("@within(cn.edu.seu.alumni_server.annotation.WebResponseAPIController)")
	public void typeAPI() {
	}

	@Around("methodAPI()")
	public Object wrapResultForMethod(ProceedingJoinPoint proceedingJoinPoint) {
		Object ans;
		try {
			ans = proceedingJoinPoint.proceed();
		} catch (Throwable throwable) {
			ans = new WebServiceExceptionMessage(throwable.getMessage());
		}
		return this.wrapWebResultToWebResponseByObject(ans);
	}

	@Around("typeAPI()")
	public Object wrapResultForType(ProceedingJoinPoint proceedingJoinPoint) {
		Object ans;
		try {
			ans = proceedingJoinPoint.proceed();
		} catch (Throwable throwable) {
			ans = new WebServiceExceptionMessage(throwable.getMessage());
		}
		return this.wrapWebResultToWebResponseByObject(ans);
	}

	private WebResponseByObject wrapWebResultToWebResponseByObject(Object ans) {
		if (ans instanceof WebServiceExceptionMessage) {
			return new WebResponseByObject().fail((WebServiceExceptionMessage) ans);
		} else if (ans instanceof WebServiceSuccessMessage) {
			return new WebResponseByObject().success(
				(WebServiceSuccessMessage) ans
			);
		} else {
			return new WebResponseByObject().success(ans);
		}
	}

}