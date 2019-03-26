package com.bx.gis.service;

import com.bx.gis.entity.UUser;

import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author Breach
 * @Date 2019/2/20
 * @Version V1.0
 **/
public interface UserManageService {
    List<UUser> selectUserInfo(Map<String, Object> para);

    List<UUser> selectTraIdAndOpDeptidInfo(int id);

    int delUserInfo(int id);

    int batchDelUsersInfo(int[] iids);

    int checkUsername(String username);

    int saveUserInfo(UUser user);

    int updateUserInfo(UUser user);

    List<UUser> searchUserInfo(UUser user);

    int sendAndSaveEcodeInfo(UUser user);

    int checkUserEcodeInfo(UUser user);

    int updateUserPwdInfo(UUser user);

    int updateFirstUserInfo(UUser user);

    int saveFirstUserInfo(UUser user);

    List<UUser> selectUserPageInfo(Map<String, Object> para);
}
