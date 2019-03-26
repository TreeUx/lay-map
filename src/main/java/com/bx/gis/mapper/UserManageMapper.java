package com.bx.gis.mapper;

import com.bx.gis.entity.UUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserManageMapper {
    List<UUser> selectTraIdAndOpDeptidInfo(int id);

    List<UUser> selectUserInfo(Map<String, Object> para);

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
