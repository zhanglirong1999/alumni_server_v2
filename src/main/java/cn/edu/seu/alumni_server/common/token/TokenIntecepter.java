package cn.edu.seu.alumni_server.common.token;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenIntecepter extends GenericFilterBean {

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
            throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        try {
            final String token = request.getHeader("authorization");
            String accountId = TokenUtil.parseJWT(token);
            if (accountId != null) {
                request.setAttribute("accountId", accountId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        chain.doFilter(req, res);
    }
}