package com.atmatrix.greenland.core.filter;

import com.atmatrix.greenland.core.conf.Conf;
import com.atmatrix.greenland.core.login.SSOWebLoginHelper;
import com.atmatrix.greenland.core.path.impl.AntPathMatcher;
import com.atmatrix.greenland.core.user.GreenlandSSOUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * web sso filter
 *
 * @author xuxueli 2018-04-03
 */
public class GreenlandSsoWebFilter extends HttpServlet implements Filter {
    private static Logger logger = LoggerFactory.getLogger(GreenlandSsoWebFilter.class);

    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private String ssoServer;
    private String logoutPath;
    private String excludedPaths;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        ssoServer = filterConfig.getInitParameter(Conf.SSO_SERVER);
        logoutPath = filterConfig.getInitParameter(Conf.SSO_LOGOUT_PATH);
        excludedPaths = filterConfig.getInitParameter(Conf.SSO_EXCLUDED_PATHS);
        logger.info("GreenlandSsoWebFilter init.");
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

        // logout path check
        if (logoutPath != null
                && logoutPath.trim().length() > 0
                && logoutPath.equals(servletPath)) {

            // remove cookie
            SSOWebLoginHelper.removeSessionIdByCookie(req, res);

            // redirect logout
            String logoutPageUrl = ssoServer.concat(Conf.SSO_LOGOUT);
            res.sendRedirect(logoutPageUrl);

            return;
        }

        // valid login user, cookie + redirect
        GreenlandSSOUser ssoUser = SSOWebLoginHelper.loginCheck(req, res);

        // valid login fail
        if (ssoUser == null) {

            String header = req.getHeader("content-type");
            boolean isJson = header != null && header.contains("json");
            if (isJson) {
                logger.info(MessageFormat.format("ssoServer：{0}", ssoServer));
                // json msg
                res.setContentType("application/json;charset=utf-8");
                res.getWriter().println("{\"code\":" + Conf.SSO_LOGIN_FAIL_RESULT.getCode() + ", " +
                        "\"msg\":\"" + Conf.SSO_LOGIN_FAIL_RESULT.getMsg() + "\"" +
                        "\"redirect_url\":\"" + ssoServer + "\"" + "}");
                return;
            } else {

                // total link
                String link = req.getRequestURL().toString();

                // redirect logout
                String loginPageUrl = ssoServer.concat(Conf.SSO_LOGIN)
                        + "?" + Conf.REDIRECT_URL + "=" + link;

                res.sendRedirect(loginPageUrl);
                return;
            }

        }

        // ser sso user
        request.setAttribute(Conf.SSO_USER, ssoUser);


        // already login, allow
        chain.doFilter(request, response);
        return;
    }

}
