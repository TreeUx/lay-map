package com.bx.gis.mapper;

import com.bx.gis.entity.URole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description TODO
 * @Author Breach
 * @Date 2019/2/18
 * @Version V1.0
 **/
@Mapper
public interface URoleDao {
    List<URole> findRoleByUid(int id);
}
