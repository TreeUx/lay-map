package com.bx.gis.mapper;

import com.bx.gis.entity.UUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description TODO
 * @Author Breach
 * @Date 2019/2/18
 * @Version V1.0
 **/
@Mapper
public interface UUserDao {

    UUser selectAllByName(String username);
}
