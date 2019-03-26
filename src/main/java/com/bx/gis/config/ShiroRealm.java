package com.bx.gis.config;


import com.bx.gis.entity.UPermission;
import com.bx.gis.entity.URole;
import com.bx.gis.entity.UUser;
import com.bx.gis.mapper.UPermissionDao;
import com.bx.gis.mapper.URoleDao;
import com.bx.gis.mapper.UUserDao;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShiroRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(ShiroRealm.class);

    //一般这里都写的是servic，这里省略直接调用dao
    @Autowired
    private UUserDao uUserDao;
    @Autowired
    private URoleDao uRoleDao;
    @Autowired
    private UPermissionDao uPermissionDao;

    /**
     * 登录认证
     * @param authenticationToken
     * @return
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        logger.info("验证当前Subject时获取到token为：" + token.toString());
        //查出是否有此用户
        String username = token.getUsername();
        UUser hasUser = uUserDao.selectAllByName(username);

        if (hasUser != null) {
            // 若存在，将此用户存放到登录认证info中，无需自己做密码对比，Shiro会为我们进行密码对比校验
            List<URole> rlist = uRoleDao.findRoleByUid(Integer.parseInt(hasUser.getId() + ""));//获取用户角色
            List<UPermission> plist = uPermissionDao.findPermissionByUid(hasUser.getId() + "");//获取用户权限
            List<String> roleStrlist=new ArrayList<String>();////用户的角色集合
            List<String> perminsStrlist=new ArrayList<String>();//用户的权限集合
            for (URole role : rlist) {
                roleStrlist.add(role.getName());
            }
            for (UPermission uPermission : plist) {
                perminsStrlist.add(uPermission.getName());
            }
            hasUser.setRoleStrlist(roleStrlist);
            hasUser.setPerminsStrlist(perminsStrlist);
            // 若存在，将此用户存放到登录认证info中，无需自己做密码对比，Shiro会为我们进行密码对比校验
            return new SimpleAuthenticationInfo(hasUser, hasUser.getPswd(), getName());
        }

        return null;
    }

    /**
     * 权限认证
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        logger.info("##################执行Shiro权限认证##################");
        UUser user = (UUser) principalCollection.getPrimaryPrincipal();
        if (user != null) {
            //权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            //用户的角色集合
            info.addRoles(user.getRoleStrlist());
            //用户的权限集合
            info.addStringPermissions(user.getPerminsStrlist());

            return info;
        }
        // 返回null的话，就会导致任何用户访问被拦截的请求时，都会自动跳转到unauthorizedUrl指定的地址
        return null;
    }
}