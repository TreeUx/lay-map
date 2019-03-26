package com.bx.gis.service.Impl;

import com.bx.gis.entity.UUser;
import com.bx.gis.mapper.UserManageMapper;
import com.bx.gis.service.UserManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author Breach
 * @Date 2019/2/20
 * @Version V1.0
 **/
@Service
public class UserManageServiceImpl implements UserManageService {

    @Autowired
    UserManageMapper userManageMapper;

    /**
      * @Author Breach
      * @Description 获取旅行社id和运营部id
      * @Date 2019/2/26
      * @Param
      * @return java.util.List<com.bx.gis.entity.UUser>
      */
    @Override
    public List<UUser> selectTraIdAndOpDeptidInfo(int id) {
        return userManageMapper.selectTraIdAndOpDeptidInfo(id);
    }

    /**
      * @Author Breach
      * @Description 查询用户信息
      * @Date 2019/2/21
      * @Param
      * @return java.util.List<com.bx.gis.entity.UUser>
      */
    @Override
    public List<UUser> selectUserInfo(Map<String, Object> para) {
        return userManageMapper.selectUserInfo(para);
    }

    /**
      * @Author Breach
      * @Description 查询用户分页信息
      * @Date 2019/2/27
      * @Param para
      * @return java.util.List<com.bx.gis.entity.UUser>
      */
    @Override
    public List<UUser> selectUserPageInfo(Map<String, Object> para) {
        return userManageMapper.selectUserPageInfo(para);
    }

    /**
      * @Author Breach
      * @Description 根据条件查询用户信息
      * @Date 2019/2/22
      * @Param user
      * @return java.util.List<com.bx.gis.entity.UUser>
      */
    @Override
    public List<UUser> searchUserInfo(UUser user) {
        return userManageMapper.searchUserInfo(user);
    }

    /**
      * @Author Breach
      * @Description 保存验证码
      * @Date 2019/2/22
      * @Param user
      * @return int
      */
    @Override
    public int sendAndSaveEcodeInfo(UUser user) {
        return userManageMapper.sendAndSaveEcodeInfo(user);
    }

    /**
      * @Author Breach
      * @Description 校验验证码是否正确
      * @Date 2019/2/22
      * @Param user
      * @return int
      */
    @Override
    public int checkUserEcodeInfo(UUser user) {
        return userManageMapper.checkUserEcodeInfo(user);
    }

    /**
      * @Author Breach
      * @Description 保存修改后的用户信息
      * @Date 2019/2/22
      * @Param user
      * @return int
      */
    @Override
    public int updateUserPwdInfo(UUser user) {
        return userManageMapper.updateUserPwdInfo(user);
    }

    /**
      * @Author Breach
      * @Description 激活用户账号
      * @Date 2019/2/26
      * @Param user
      * @return int
      */
    @Override
    public int updateFirstUserInfo(UUser user) {
        return userManageMapper.updateFirstUserInfo(user);
    }

    /**
      * @Author Breach
      * @Description 保存用户注册信息
      * @Date 2019/2/26
      * @Param user
      * @return int
      */
    @Override
    public int saveFirstUserInfo(UUser user) {
        return userManageMapper.saveFirstUserInfo(user);
    }

    /**
      * @Author Breach
      * @Description 删除用户信息
      * @Date 2019/2/21
      * @Param iids
      * @return int
      */
    @Override
    public int delUserInfo(int id) {
        return userManageMapper.delUserInfo(id);
    }

    /**
      * @Author Breach
      * @Description 批量删除用户信息
      * @Date 2019/2/21
      * @Param iids
      * @return int
      */
    @Override
    public int batchDelUsersInfo(int[] iids) {
        return userManageMapper.batchDelUsersInfo(iids);
    }

    /**
      * @Author Breach
      * @Description 查找用户名是否存在
      * @Date 2019/2/21
      * @Param username
      * @return int
      */
    @Override
    public int checkUsername(String username) {
        return userManageMapper.checkUsername(username);
    }

    /**
      * @Author Breach
      * @Description 保存用户信息
      * @Date 2019/2/21
      * @Param para
      * @return int
      */
    @Override
    public int saveUserInfo(UUser user) {
        return userManageMapper.saveUserInfo(user);
    }

    /**
      * @Author Breach
      * @Description 修改用户信息
      * @Date 2019/2/22
      * @Param user
      * @return int
      */
    @Override
    public int updateUserInfo(UUser user) {
        return userManageMapper.updateUserInfo(user);
    }

}
