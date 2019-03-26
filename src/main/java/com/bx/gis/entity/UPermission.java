package com.bx.gis.entity;

import lombok.Data;

/**
 * @Description TODO
 * @Author Breach
 * @Date 2019/2/18
 * @Version V1.0
 **/
@Data
public class UPermission {
    /*角色权限id*/
    private int id;
    /*角色权限url*/
    private String url;
    /*角色权限描述*/
    private String name;
}
