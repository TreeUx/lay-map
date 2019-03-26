package com.bx.gis.mapper;

import com.bx.gis.entity.UPermission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description TODO
 * @Author Breach
 * @Date 2019/2/18
 * @Version V1.0
 **/
@Mapper
public interface UPermissionDao {
    List<UPermission> findPermissionByUid(String id);
}
