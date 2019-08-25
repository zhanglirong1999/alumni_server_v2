package cn.edu.seu.alumni_server.common.token;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Acl acl = this.queryRequestAcl(request, handler);
        if (null == acl) {
            return true;
        } else {
            return checkToken(request, response, acl);
        }
    }

    Acl queryRequestAcl(HttpServletRequest request, Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Acl acl = (Acl) handlerMethod.getMethod().getAnnotation(Acl.class);
        if (acl == null) {
            acl = (Acl) handlerMethod.getBeanType().getAnnotation(Acl.class);
        }
        return acl;
    }

    Boolean checkToken(HttpServletRequest request, HttpServletResponse response, Acl acl) throws Exception {
        String token = request.getHeader("Authorization");

        if (!TokenUtil.checkToken(token)) {
            response.setContentType("text/plain;charset=UTF-8");
            response.sendError(401);
            return false;
        } else {
            request.setAttribute("accountId", TokenUtil.getTokenInfo(token));
        }
        return true;
    }
}