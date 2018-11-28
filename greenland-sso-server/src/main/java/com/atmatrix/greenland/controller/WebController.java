package com.atmatrix.greenland.controller;

import com.atmatrix.greenland.core.conf.Conf;
import com.atmatrix.greenland.core.login.SSOWebLoginHelper;
import com.atmatrix.greenland.core.store.SsoLoginStore;
import com.atmatrix.greenland.core.store.SsoSessionIdHelper;
import com.atmatrix.greenland.core.user.GreenlandSSOUser;
import com.atmatrix.greenland.core.model.UserInfo;
import com.atmatrix.greenland.core.result.ReturnT;
import com.atmatrix.greenland.core.util.CookieUtil;
import com.atmatrix.greenland.service.UserService;
import com.atmatrix.greenland.util.web.WebUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Controller
public class WebController {

    @Autowired
    private UserService userService;

    @Value("${uri.admin}")
    private String uriAdmin;

    @Value("${uri.confirm}")
    private String uriConfirm;

    @Value("${cookie.domain}")
    private String cookieDomain;

    @RequestMapping("/")
    public String index(Model model, HttpServletRequest request, HttpServletResponse response) {

        // login check
        GreenlandSSOUser ssoUser = SSOWebLoginHelper.loginCheck(request, response);

        if (ssoUser == null) {
//            return "redirect:http://192.168.1.146:9000/#/index";
            return "redirect:/login";
        } else {
            model.addAttribute("ssoUser", ssoUser);
            return "index";
        }
    }

    @RequestMapping(Conf.SSO_LOGIN)
    public String login(Model model, HttpServletRequest request, HttpServletResponse response) {

        // login check
        GreenlandSSOUser ssoUser = SSOWebLoginHelper.loginCheck(request, response);
        log.info(MessageFormat.format("登录中{0}",new Gson().toJson(ssoUser)));
        if (ssoUser != null) {

            // success redirect
            String redirectUrl = request.getParameter(Conf.REDIRECT_URL);
            if (redirectUrl!=null && redirectUrl.trim().length()>0) {
                String sessionId = SSOWebLoginHelper.getSessionIdByCookie(request);
                String redirectUrlFinal = redirectUrl + "?" + Conf.SSO_SESSIONID + "=" + sessionId;
                return "redirect:" + redirectUrlFinal;
            } else {
                return "redirect:/";
            }
        }

        model.addAttribute("errorMsg", request.getParameter("errorMsg"));
        model.addAttribute(Conf.REDIRECT_URL, request.getParameter(Conf.REDIRECT_URL));
        return "login";
    }

    /**
     * Login page
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("/login2")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> login2(Model model, HttpServletRequest request, HttpServletResponse response) {

        // login check
        GreenlandSSOUser ssoUser = SSOWebLoginHelper.loginCheck(request, response);

        log.info(MessageFormat.format("登录中{0}", new Gson().toJson(ssoUser)));
        if (ssoUser != null) {

            // success redirect
            String redirectUrl = request.getParameter(Conf.REDIRECT_URL);
            if (redirectUrl != null && redirectUrl.trim().length() > 0) {
                String sessionId = SSOWebLoginHelper.getSessionIdByCookie(request);
                String redirectUrlFinal = redirectUrl + "?" + Conf.SSO_SESSIONID + "=" + sessionId;
                return WebUtil.result("redirect:" + redirectUrlFinal);
            } else {
                return WebUtil.error("redirect:" + redirectUrl);
            }

        }

        model.addAttribute("errorMsg", request.getParameter("errorMsg"));
        model.addAttribute(Conf.REDIRECT_URL, request.getParameter(Conf.REDIRECT_URL));
        return WebUtil.error(model);
    }


    /**
     * Login
     *
     * @param request
     * @param redirectAttributes
     * @param userId
     * @param password
     * @return
     */
//    @RequestMapping("/doLogin")
//    public String doLogin(HttpServletRequest request,
//                          HttpServletResponse response,
//                          RedirectAttributes redirectAttributes,
//                          String userId,
//                          String password,
//                          String ifRemember) {
//
//        boolean ifRem = (ifRemember != null && "on".equals(ifRemember)) ? true : false;
//
//        // valid login
//        ReturnT<UserInfo> result = userService.findUser(userId, password);
//        if (result.getCode() != ReturnT.SUCCESS_CODE) {
//            redirectAttributes.addAttribute("errorMsg", result.getMsg());
//
//            redirectAttributes.addAttribute(Conf.REDIRECT_URL, request.getParameter(Conf.REDIRECT_URL));
//            return "redirect:/login";
//        }
//
//        // 1、make greenland-sso user
//        GreenlandSSOUser ssoUser = new GreenlandSSOUser();
//        ssoUser.setUserid(String.valueOf(result.getData().getUserId()));
//        ssoUser.setUsername(result.getData().getUsername());
//        ssoUser.setVersion(UUID.randomUUID().toString().replaceAll("-", ""));
//        ssoUser.setExpireMinite(SsoLoginStore.getRedisExpireMinite());
//        ssoUser.setExpireFreshTime(System.currentTimeMillis());
//
//
//        // 2、make session id
//        String sessionId = SsoSessionIdHelper.makeSessionId(ssoUser);
//
//        // 3、login, store storeKey + cookie sessionId
//        SSOWebLoginHelper.login(response, sessionId, ssoUser, ifRem);
//
//        // 4、return, redirect sessionId
//        String redirectUrl = request.getParameter(Conf.REDIRECT_URL);
//        if (redirectUrl != null && redirectUrl.trim().length() > 0) {
//            String redirectUrlFinal = redirectUrl + "?" + Conf.SSO_SESSIONID + "=" + sessionId;
//            return "redirect:" + redirectUrlFinal;
//        } else {
//            return "redirect:/";
//        }
//
//    }

    /**
     * Login
     *
     * @param request
     * @param redirectAttributes
     * @param userId
     * @param password
     * @return
     */
    @RequestMapping("/doLogin")
    @ResponseBody
    public  ResponseEntity<Map<String, Object>> doLogin(HttpServletRequest request,
                          HttpServletResponse response,
                          RedirectAttributes redirectAttributes,
                          String userId,
                          String password,
                          String ifRemember) {

        boolean ifRem = (ifRemember != null && "on".equals(ifRemember)) ? true : false;
        ifRem = true;

        // valid login
        ReturnT<UserInfo> result = userService.findUser(userId, password);
        if (result.getCode() != ReturnT.SUCCESS_CODE) {
            redirectAttributes.addFlashAttribute("errorMsg", result.getMsg());
            redirectAttributes.addFlashAttribute("userId", userId);
            redirectAttributes.addFlashAttribute(Conf.REDIRECT_URL, request.getParameter(Conf.REDIRECT_URL));
            return WebUtil.error(result);
        }

        // 1、make greenland-sso user
        GreenlandSSOUser ssoUser = new GreenlandSSOUser();
        ssoUser.setUserid(String.valueOf(result.getData().getUserId()));
        ssoUser.setUsername(result.getData().getUsername());
        ssoUser.setVersion(UUID.randomUUID().toString().replaceAll("-", ""));
        ssoUser.setExpireMinite(SsoLoginStore.getRedisExpireMinite());
        ssoUser.setExpireFreshTime(System.currentTimeMillis());


        // 2、make session id
        String sessionId = SsoSessionIdHelper.makeSessionId(ssoUser);
        response.setHeader("Access-Control-Allow-Credentials", "true");

        // 3、login, store storeKey + cookie sessionId
        SSOWebLoginHelper.login(response, sessionId, ssoUser, ifRem);
        CookieUtil.set(response, "greenland_sso_sessionid", sessionId, ifRem);


        // 4、return, redirect sessionId
        String redirectUrl = request.getParameter(Conf.REDIRECT_URL);
        if (redirectUrl != null && redirectUrl.trim().length() > 0) {
            String redirectUrlFinal = redirectUrl + "?" + Conf.SSO_SESSIONID + "=" + sessionId;
            return WebUtil.error(result);
        } else {
            // TODO: 2018/11/27  判断权限跳转不同的rui
            String redirect;
            if (true){
                redirect = uriAdmin;
            }else{
                redirect = uriConfirm;
            }
            int age = ifRem ? 2147483647 : -1;
            Map<String, Object> data = new HashMap<>();
            data.put("redirect", redirect);
            data.put("greenland_sso_sessionid",sessionId);
            data.put("age",age);
            data.put("domain",cookieDomain);
            return WebUtil.result(data);
        }

    }

    /**
     * Logout
     *
     * @param request
     * @param redirectAttributes
     * @return
     */
//    @RequestMapping(Conf.SSO_LOGOUT)
//    public String logout(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
//
//        // logout
//        SSOWebLoginHelper.logout(request, response);
//
//        redirectAttributes.addAttribute(Conf.REDIRECT_URL, request.getParameter(Conf.REDIRECT_URL));
//        return "redirect:/login";
//    }

    @RequestMapping(Conf.SSO_LOGOUT)
    @ResponseBody
    public ResponseEntity<Map<String, Object>>  logout(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {

        // logout
        SSOWebLoginHelper.logout(request, response);

        redirectAttributes.addAttribute(Conf.REDIRECT_URL, request.getParameter(Conf.REDIRECT_URL));
        Map<String, Object> data = new HashMap<>();
        data.put(Conf.REDIRECT_URL, request.getParameter(Conf.REDIRECT_URL));
        return WebUtil.result(data);
    }

}