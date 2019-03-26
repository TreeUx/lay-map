package com.bx.gis.controller;

import com.bx.gis.common.ImgMailUtil;
import com.bx.gis.entity.UUser;
import com.bx.gis.service.UserManageService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @Description TODO
 * @Author Breach
 * @Date 2019/2/19
 * @Version V1.0
 **/
@Controller
@RequestMapping("")
public class UserManageController {
    private static Logger logger = LoggerFactory.getLogger(UserManageController.class);
    @Autowired
    UserManageService userManageService;

    /**
     * @Author Breach
     * @Description 查询旅行社id和运营部id信息
     * @Date 2019/2/19
     * @Param
     */
    @RequestMapping(value = "/selectTraIdAndOpDeptidInfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> selectTraIdAndOpDeptidInfo() {
        Map<String, Object> result = new HashMap<>();
        UUser user = (UUser) SecurityUtils.getSubject().getPrincipal();
//        String username = user.getNickname(); //获取当前登录用户的用户名
        int id = user.getId(); //获取当前登录用户的Id
        try {
            List<UUser> userList = userManageService.selectTraIdAndOpDeptidInfo(id); //获取用户旅行社Id和运营部id信息
            System.out.println(userList);
            result.put("status", 200);
            result.put("msg", "获取用户信息成功");
            result.put("userList", userList);
        } catch (Exception e) {
            result.put("status", 403);
            result.put("msg", "请求异常");
            logger.error("请求异常", e);
        }
        return result;
    }
    /**
     * @return java.util.Map<java.lang.String       ,       java.lang.Object>
     * @Author Breach
     * @Description 查询用户下子用户信息
     * @Date 2019/2/19
     * @Param
     */
    @RequestMapping(value = "/selectUserInfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> selectUserInfo() {
        Map<String, Object> para = new HashMap<>();
        UUser user = (UUser) SecurityUtils.getSubject().getPrincipal();
//        String username = user.getNickname(); //获取当前登录用户的用户名
        int id = user.getId(); //获取当前登录用户的Id
        int parent_id = user.getParent_id(); //上级id
        para.put("id", id);
        para.put("parent_id", parent_id);
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        Map<String, Object> result = new HashMap<>();
        try {
            List<UUser> userInfoList = userManageService.selectUserInfo(para);
            System.out.println(userInfoList);
            result.put("status", 200);
            result.put("msg", "获取用户信息成功");
            result.put("id", id);
            result.put("userInfoList", userInfoList);
        } catch (Exception e) {
            result.put("status", 403);
            result.put("msg", "请求异常");
            logger.error("请求异常", e);
        }
        return result;
    }

    /**
     * @Author Breach
     * @Description 查询用户下子用户分页数据信息
     * @Date 2019/2/19
     * @Param
     */
    @RequestMapping(value = "/selectUserPageInfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> selectUserPageInfo(HttpServletRequest request) {
        Map<String, Object> para = new HashMap<>();
        UUser user = (UUser) SecurityUtils.getSubject().getPrincipal();
        int id = user.getId(); //获取当前登录用户的Id
        int parent_id = user.getParent_id(); //上级id
        int currentPage = Integer.parseInt(request.getParameter("currentPage")); //当前页
        currentPage = (currentPage-1)*10;
        para.put("id", id);
        para.put("parent_id", parent_id);
        para.put("currentPage", currentPage); //当前页数
        para.put("perNum", 10); //每页展示条数
        Map<String, Object> result = new HashMap<>();
        try {
            List<UUser> userPageInfoList = userManageService.selectUserPageInfo(para); //查询分页数据
            System.out.println(userPageInfoList);
            result.put("status", 200);
            result.put("msg", "获取用户信息成功");
            result.put("id", id);
            result.put("userPageInfoList", userPageInfoList);
        } catch (Exception e) {
            result.put("status", 403);
            result.put("msg", "请求异常");
            logger.error("请求异常", e);
        }
        return result;
    }

    /**
     * @return java.util.Map<java.lang.String       ,       java.lang.Object>
     * @Author Breach
     * @Description 根据条件查询用户数据
     * @Date 2019/2/21
     * @Param request
     */
    @RequestMapping(value = "/searchUserInfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> searchUserInfo(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        UUser user = new UUser();
        String username = request.getParameter("username"); //用户名称
        int userId = Integer.parseInt(request.getParameter("userId")); //当前用户id
        user.setNickname(username);
        user.setId(userId);
        try {
            List<UUser> userInfoList = userManageService.searchUserInfo(user); //根据条件查询用户信息
            result.put("status", "200");
            result.put("msg", "查询用户信息成功");
            result.put("userInfoList", userInfoList);

        } catch (Exception e) {
            result.put("status", "403");
            result.put("msg", "请求异常");
            logger.error("请求异常", e);
        }
        return result;
    }

    /**
     * @return java.util.Map<java.lang.String       ,       java.lang.Object>
     * @Author Breach
     * @Description 查询用户是否存在
     * @Date 2019/2/21
     * @Param request
     */
    @RequestMapping(value = "/checkUsername", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> checkUsername(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        String username = request.getParameter("username");
        try {
            int num = userManageService.checkUsername(username); //查找用户名是否存在
            if (num == 1) {
                result.put("status", "200");
                result.put("msg", "用户名已存在");
            } else {
                result.put("status", "500");
                result.put("msg", "用户名不存在");
            }

        } catch (Exception e) {
            result.put("status", "403");
            result.put("msg", "请求异常");
            logger.error("请求异常", e);
        }
        return result;
    }

    /**
     * @return java.util.Map<java.lang.String       ,       java.lang.Object>
     * @Author Breach
     * @Description 保存用户信息
     * @Date 2019/2/21
     * @Param request
     */
    @RequestMapping(value = "/saveUserInfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> saveUserInfo(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        UUser user = new UUser();
        List<Map<String, Object>> dataList = new ArrayList<>();
        int parent_id = Integer.parseInt(request.getParameter("parent_id"));
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        int tra_id = Integer.parseInt(request.getParameter("tra_id"));
        int op_deptid = Integer.parseInt(request.getParameter("op_deptid"));
        int operate_id = Integer.parseInt(request.getParameter("operate_id"));
        user.setParent_id(parent_id); //上级id
        user.setNickname(username); //用户名
        user.setPswd(password); //密码
        user.setEmail(email); //邮箱
        user.setTra_id(tra_id); //旅行社id
        user.setOp_deptid(op_deptid); //运营部id
        user.setOperate_id(operate_id); //计调部操作员id
        try {
            int num = userManageService.saveUserInfo(user); //保存用户信息
            if (num == 1) {
                result.put("status", "200");
                result.put("msg", "用户信息保存成功");
            } else {
                result.put("status", "500");
                result.put("msg", "用户信息保存失败");
            }

        } catch (Exception e) {
            result.put("status", "403");
            result.put("msg", "操作异常");
            logger.error("操作异常", e);
        }
        return result;
    }

    /**
     * @return java.util.Map<java.lang.String       ,       java.lang.Object>
     * @Author Breach
     * @Description 修改用户信息
     * @Date 2019/2/21
     * @Param request
     */
    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> updateUserInfo(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        UUser user = new UUser();
        List<Map<String, Object>> dataList = new ArrayList<>();
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        user.setNickname(username); //用户名
        user.setEmail(email); //邮箱
        try {
            int num = userManageService.updateUserInfo(user); //修改用户信息
            if (num == 1) {
                result.put("status", "200");
                result.put("msg", "用户信息修改成功");
            } else {
                result.put("status", "500");
                result.put("msg", "用户信息修改失败");
            }

        } catch (Exception e) {
            result.put("status", "403");
            result.put("msg", "操作异常");
            logger.error("操作异常", e);
        }
        return result;
    }

    /**
     * @return java.util.Map<java.lang.String       ,       java.lang.Object>
     * @Author Breach
     * @Description 删除单个用户信息
     * @Date 2019/2/21
     * @Param request
     */
    @RequestMapping("/delUserInfo")
    @ResponseBody
    public Map<String, Object> delUserInfo(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        int id = Integer.parseInt(request.getParameter("id"));
        try {
            int num = userManageService.delUserInfo(id);
            if (num == 1) {
                result.put("status", "200");
                result.put("msg", "操作成功");
            } else {
                result.put("status", "500");
                result.put("msg", "操作失败!");
            }
        } catch (Exception e) {
            result.put("status", "403");
            result.put("msg", "操作异常!");
            logger.error("操作异常！", e);
        }
        return result;
    }

    /**
     * @return java.util.Map<java.lang.String       ,       java.lang.Object>
     * @Author Breach
     * @Description 批量删除用户信息
     * @Date 2019/2/21
     * @Param request
     */
    @RequestMapping("/batchDelUsersInfo")
    @ResponseBody
    public Map<String, Object> batchDelUsersInfo(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        String id = request.getParameter("id");
        String[] ids = id.split(",");
        int[] iids = new int[ids.length];
        for (int i = 0; i < ids.length; i++) {
            iids[i] = Integer.parseInt(ids[i]);
        }
        try {
            int num = userManageService.batchDelUsersInfo(iids);
            if (num == iids.length) {
                result.put("status", "200");
                result.put("msg", "操作成功");
            } else {
                result.put("status", "500");
                result.put("msg", "操作失败!");
            }
        } catch (Exception e) {
            result.put("status", "403");
            result.put("msg", "操作异常!");
            logger.error("操作异常!", e);
        }
        return result;
    }

    /**
     * @return java.util.Map<java.lang.String       ,       java.lang.Object>
     * @Author Breach
     * @Description 发送验证码到用户邮箱，并保存验证码
     * @Date 2019/2/22
     * @Param request
     */
    @RequestMapping("/sendAndSaveEcodeInfo")
    @ResponseBody
    public Map<String, Object> sendAndSaveEcodeInfo(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        boolean bl = false;
        String mailCode = String.valueOf(new Random().nextInt(899999) + 100000); //随机6位验证码
        UUser user = new UUser();
        String nickname = request.getParameter("nickname"); //用户账号（名称）
        String email = request.getParameter("email"); //邮箱地址
        user.setNickname(nickname);
        user.setEmail(email);
        user.setEcode(mailCode);
        try {
            bl = ImgMailUtil.send_mail(mailCode, email); //发送验证码到用户邮箱
            if (bl) { //验证码发送到邮箱成功
                int num = userManageService.sendAndSaveEcodeInfo(user);
                if (num == 0) {
                    result.put("status", "500");
                    result.put("msg", "操作失败!");
                } else {
                    result.put("status", "200");
                    result.put("msg", "操作成功");
                }
            }
        } catch (Exception e) {
            result.put("status", "403");
            result.put("msg", "操作异常!");
            logger.error("操作异常!", e);
        }
        return result;
    }

    /**
     * @Author Breach
     * @Description 校验用户的验证码信息是否正确
     * @Date 2019/2/22
     * @Param request
     */
    @RequestMapping("/checkUserEcodeInfo")
    @ResponseBody
    public Map<String, Object> checkUserEcodeInfo(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        UUser user = new UUser();
        String nickname = request.getParameter("nickname"); //用户账号（名称）
        String email = request.getParameter("email"); //邮箱地址
        String ecode = request.getParameter("ecode"); //邮箱地址
        user.setNickname(nickname);
        user.setEmail(email);
        user.setEcode(ecode);
        try {
            int res = userManageService.checkUserEcodeInfo(user); //校验
            if (res != 0) {
                result.put("status", "200");
                result.put("msg", "操作成功");
            } else {
                result.put("status", "500");
                result.put("msg", "操作失败!");
            }
        } catch (Exception e) {
            result.put("status", "403");
            result.put("msg", "操作异常!");
            logger.error("操作异常!", e);
        }
        return result;
    }

    /**
     * @Author Breach
     * @Description 保存修改后的用户信息
     * @Date 2019/2/22@
     * @Param request
     */
    @RequestMapping("/updateUserPwdInfo")
    @ResponseBody
    public Map<String, Object> updateUserPwdInfo(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        UUser user = new UUser();
        String nickname = request.getParameter("nickname"); //用户账号（名称）
        String password = request.getParameter("password"); //邮箱地址
        user.setNickname(nickname);
        user.setPswd(password);
        try {
            int res = userManageService.updateUserPwdInfo(user); //保存修改后的用户信息
            if (res != 0) {
                result.put("msg", "操作成功");
                result.put("status", "200");
            } else {
                result.put("status", "500");
                result.put("msg", "操作失败!");
            }
        } catch (Exception e) {
            result.put("status", "403");
            result.put("msg", "操作异常!");
            logger.error("操作异常!", e);
        }
        return result;
    }

    /**
     * @Author Breach
     * @Description 保存用户注册信息
     * @Date 2019/2/22@
     * @Param request
     */
    @RequestMapping(value = "/saveFirstUserInfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> saveFirstUserInfo(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        String mailCode = String.valueOf(new Random().nextInt(899999) + 100000); //随机6位验证码
        boolean bl = false;
        UUser user = new UUser();
        String nickname = request.getParameter("nickname"); //用户账号（名称）
        String pswd = request.getParameter("pswd"); //密码
        String email = request.getParameter("email"); //邮箱地址
        int traId = Integer.parseInt(request.getParameter("traId")); //旅行社Id
        int opDeptid = Integer.parseInt(request.getParameter("opDeptid")); //运营部id
        int operateId = Integer.parseInt(request.getParameter("operateId")); //采线员id
        user.setNickname(nickname); //用户名
        user.setEmail(email); //邮箱地址
        user.setEcode(mailCode); //验证码
        user.setPswd(pswd); //密码
        user.setTra_id(traId); //设置旅行社id
        user.setOp_deptid(opDeptid); //设置运营部id
        user.setOperate_id(operateId); //计调部操作员id
        try {
            bl = ImgMailUtil.send_mail(mailCode, email); //发送验证码到用户邮箱
            if (bl) { //验证码发送到邮箱成功
                int num = userManageService.saveFirstUserInfo(user); //保存用户注册信息
                result.put("status", "200");
                result.put("msg", "操作成功");
            }
        } catch (Exception e) {
            result.put("status", "403");
            result.put("msg", "操作异常!");
            logger.error("操作异常!", e);
        }
        return result;
    }

    /**
     * @Author Breach
     * @Description 修改用户激活状态
     * @Date 2019/2/22@
     * @Param request
     */
    @RequestMapping("/updateFirstUserInfo")
    @ResponseBody
    public Map<String, Object> updateFirstUserInfo(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        UUser user = new UUser();
        String nickname = request.getParameter("nickname"); //用户账号（名称）
        String email = request.getParameter("email"); //邮箱地址
        user.setNickname(nickname); //设置用户名
        user.setEmail(email); //设置邮箱地址
        try {
            int num = userManageService.updateFirstUserInfo(user); //保存用户注册信息
            if (num == 1) {
                result.put("msg", "操作成功");
                result.put("status", "200");
            } else {
                result.put("status", "500");
                result.put("msg", "操作失败!");
            }
        } catch (Exception e) {
            result.put("status", "403");
            result.put("msg", "操作异常!");
            logger.error("操作异常!", e);
        }
        return result;
    }


}
