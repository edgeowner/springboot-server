package com.atmatrix.greenland.core.filter;

import com.atmatrix.greenland.core.conf.Conf;
import com.atmatrix.greenland.core.entity.ReturnT;
import com.atmatrix.greenland.core.path.impl.AntPathMatcher;
import com.atmatrix.greenland.core.user.GreenlandSSOUser;
import com.atmatrix.greenland.core.login.SSOTokenLoginHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * app sso filter
 *
 */
public class GreenlandSsoTokenFilter extends HttpServlet implements Filter {
    private static Logger logger = LoggerFactory.getLogger(GreenlandSsoTokenFilter.class);

    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private String ssoServer;
    private String logoutPath;
    private String excludedPaths;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        ssoServer = filterConfig.getInitParameter(Conf.SSO_SERVER);
        logoutPath = filterConfig.getInitParameter(Conf.SSO_LOGOUT_PATH);
        excludedPaths = filterConfig.getInitParameter(Conf.SSO_EXCLUDED_PATHS);

        logger.info("GreenlandSsoTokenFilter init.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // make url
        String servletPath = req.getServletPath();

        // excluded path check
        if (excludedPaths != null && excludedPaths.trim().length() > 0) {
            for (String excludedPath : excludedPaths.split(",")) {
                String uriPattern = excludedPath.trim();
                // 支持ANT表达式
                if (antPathMatcher.match(uriPattern, servletPath)) {
                    // excluded path, allow
                    chain.doFilter(request, response);
                    return;
                }

            }
        }

        // logout filter
        if (logoutPath != null && logoutPath.trim().length() > 0 && logoutPath.equals(servletPath)) {
            // logout
            SSOTokenLoginHelper.logout(req);

            // response
            res.setStatus(HttpServletResponse.SC_OK);
            res.setContentType("application/json;charset=UTF-8");
            res.getWriter().println("{\"code\":" + ReturnT.SUCCESS_CODE + ", \"msg\":\"\"}");

            return;
        }

        // login filter
        GreenlandSSOUser ssoUser = SSOTokenLoginHelper.loginCheck(req);
        if (ssoUser == null) {

            // response
            res.setStatus(HttpServletResponse.SC_OK);
            res.setContentType("application/json;charset=UTF-8");
            res.getWriter().println("{\"code\":" + Conf.SSO_LOGIN_FAIL_RESULT.getCode() + ", \"msg\":\"" + Conf.SSO_LOGIN_FAIL_RESULT.getMsg() + "\"}");
            return;
        }

        // ser sso user
        request.setAttribute(Conf.SSO_USER, ssoUser);


        // already login, allow
        chain.doFilter(request, response);
        return;
    }


}
