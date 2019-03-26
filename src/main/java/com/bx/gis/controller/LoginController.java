package com.bx.gis.controller;

import com.bx.gis.entity.UUser;
import com.bx.gis.mapper.URoleDao;
import com.bx.gis.mapper.UUserDao;
import com.bx.gis.service.SceneryInfoService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description TODO
 * @Author Breach
 * @Date 2019/2/18
 * @Version V1.0
 **/
@RequestMapping("")
@Controller
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private URoleDao uRoleDao;

    @Autowired
    private UUserDao uUserDao;

    @Autowired
    SceneryInfoService sceneryInfoService;

    //登录成功后，跳转的页面
//    @RequiresPermissions("/user/select")
    @RequiresAuthentication
    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("name", "景点数据采集");
        return "bxlyMap";
    }

    /**
      * @Author Breach
      * @Description 忘记密码
      * @Date 2019/2/26
      * @Param
      * @return java.lang.String
      */
    @RequestMapping("/forget")
    public String forget(Model model) {
        model.addAttribute("title", "账号管理中心");
        return "forgetPwd";
    }

    //未登录，可以访问的页面
    /*@RequestMapping("/index")
    public String list(Model model) {
        return "login";
    }*/

    /**
     * @Author Breach
     * @Description 用户登录
     * @Date 2019/2/15
     * @Param model
     * @return java.lang.String
     */
    @RequestMapping("/login")
    public String login(Model model) {
        System.out.println(SecurityUtils.getSubject().isRemembered());
        System.out.println(SecurityUtils.getSubject().isAuthenticated());
        model.addAttribute("title", "用户登录");
        return "login";
    }

    /**
      * @Author Breach
      * @Description 用户注册
      * @Date 2019/2/26
      * @Param
      * @return java.lang.String
      */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Model model, HttpServletRequest request) {
        model.addAttribute("title", "用户注册");
        model.addAttribute("tra_id", request.getParameter("traId")); //旅行社id
        model.addAttribute("op_deptid", request.getParameter("opDeptid")); //运营部id
        model.addAttribute("operate_id", request.getParameter("operateId")); //计调部操作员id
        if(request.getParameter("traId") == null || request.getParameter("opDeptid") == null
            || request.getParameter("operateId") == null) {
            return "login";
        }
        return "register";
    }

    /**
      * @Author Breach
      * @Description 未登录状态下直接访问无权限的页面时，跳转到登录页
      * @Date 2019/2/19
      * @Param model
      * @return java.lang.String
      */
    /*@RequestMapping("/error")
    public String error(Model model) {
        model.addAttribute("title", "用户登录");
        return "login";
    }*/

    //登陆验证，这里方便url测试，正式上线需要使用POST方式提交
    @RequestMapping(value = "/ajaxLogins", method = RequestMethod.GET)
    public String index(UUser user) {
        if (user.getNickname() != null && user.getPswd() != null) {
            UsernamePasswordToken token = new UsernamePasswordToken(user.getNickname(), user.getPswd(), "login");
            Subject currentUser = SecurityUtils.getSubject();
            logger.info("对用户[" + user.getNickname() + "]进行登录验证..验证开始");
            try {
                currentUser.login(token);
                //验证是否登录成功
                if (currentUser.isAuthenticated()) {
                    logger.info("用户[" + user.getNickname() + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
                    System.out.println("用户[" + user.getNickname() + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
//                    return "redirect:/success";
                    return "bxlyMap";
                } else {
                    token.clear();
                    System.out.println("用户[" + user.getNickname() + "]登录认证失败,重新登陆");
//                    return "redirect:/login";
                    return "login";
                }
            } catch (UnknownAccountException uae) {
                logger.info("对用户[" + user.getNickname() + "]进行登录验证..验证失败-username wasn't in the system");
            } catch (IncorrectCredentialsException ice) {
                logger.info("对用户[" + user.getNickname() + "]进行登录验证..验证失败-password didn't match");
            } catch (LockedAccountException lae) {
                logger.info("对用户[" + user.getNickname() + "]进行登录验证..验证失败-account is locked in the system");
            } catch (AuthenticationException ae) {
                logger.error(ae.getMessage());
            }
            //验证是否登录成功
            if(currentUser.isAuthenticated()){
                logger.info("用户[" + user.getNickname() + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
                //把当前用户放入session
                Session session = currentUser.getSession();
                UUser tUser = uUserDao.selectAllByName(user.getNickname());
                session.setAttribute("currentUser",tUser);
                return "success";
            }else{
                token.clear();
                return "redirect:login";
            }
        }
        return "login";
    }

    /**
     * ajax登录请求接口方式登陆
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = "/ajaxLogin", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> submitLogin(@RequestParam(value = "nickname") String username, @RequestParam(value = "pswd") String password) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        try {

            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            SecurityUtils.getSubject().login(token);
            resultMap.put("status", 200);
            resultMap.put("message", "登录成功");

        } catch (Exception e) {
            resultMap.put("status", 500);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }
    /*@RequestMapping(value="/ajaxLogin",method=RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> submitLogin(String nickname, String pswd,Boolean rememberMe,Model model) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(nickname, pswd,rememberMe);
            SecurityUtils.getSubject().login(token);
            resultMap.put("status", 200);
            resultMap.put("message", "登录成功");
        } catch (Exception e) {
            resultMap.put("status", 500);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }
*/
    //登出
    @RequestMapping(value = "/logout")
    public String logout() {
        return "logout";
    }

    //错误页面展示
    @GetMapping("/403")
    public String error() {
//        return "error ok!";
        return "error";
    }

    //管理员功能
    @RequiresRoles("admin")
    @RequiresPermissions("管理员添加")
    @RequestMapping(value = "/admin/add")
    public String create() {
        return "add success!";
    }

    //用户功能
    @RequiresRoles("user")
    @RequiresPermissions("用户查询")
    @RequestMapping(value = "/user/select")
    public String detail() {
        return "select success";
    }
}
