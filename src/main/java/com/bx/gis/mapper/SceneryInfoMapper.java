package com.bx.gis.mapper;

import com.bx.gis.entity.BxCommodityCommon;
import com.bx.gis.entity.BxSubjectivity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author Breach
 * @Date 2018/12/27
 * @Version V1.0
 **/
@Mapper
public interface SceneryInfoMapper {
    List<String> querySceneyInfo(String scenery_name);

    int addNewSceneryInfo(BxCommodityCommon bmc);

    int querySceneryIfExist(String scenery_name);

    int addSceneryType(BxSubjectivity bs);

    int deleteNewSceneryInfo(String comCode);

    List<Map<String, Object>> querySceneryInfoByCode(String comCode);

    int querySceneryCount(String scenery_name, String com_code);

    boolean updateNewSceneryInfo(BxCommodityCommon bcc);

    List<Map<String, Object>> querySceneryEntranceInfos(String scenery_name);

    List<Map<String, Object>> querySceneryTrackInfos(String parentid);

    int addPointlineInfos(Map<String, Object> params);

    List<Map<String, Object>> queryNewSceneryPartInfo(String parentid);

    int checkPsw(Map<String, Object> para);
}
