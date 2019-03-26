package com.bx.gis.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Description TODO
 * @Author Breach
 * @Date 2019/2/18
 * @Version V1.0
 **/
@Data
public class UUser {
    /*id*/
    private int id;
    /*用户名*/
    private String nickname;
    /*Email*/
    private String email;
    /*手机号*/
    private String phone;
    /*密码*/
    private String pswd;
    /*创建时间*/
    private Date create_time;
    /*最后登录时间*/
    private Date last_login_time;
    /*用户上一级*/
    private int parent_id;
    /*旅行社id*/
    private int tra_id;
    /*运营部id*/
    private int op_deptid;
    /*计调部操作员id*/
    private int operate_id;
    /*用户状态*/
    private int state;
    /*激活码/验证码*/
    private String ecode;

    private List<String> roleStrlist;

    private List<String> perminsStrlist;
}
