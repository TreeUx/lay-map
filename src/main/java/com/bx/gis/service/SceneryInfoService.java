package com.bx.gis.service;

import com.bx.gis.entity.BxCommodityCommon;
import com.bx.gis.entity.BxSubjectivity;

import java.util.List;
import java.util.Map;

public interface SceneryInfoService {
    List<String> querySceneyInfo(String scenery_name);

    int addNewSceneryInfo(BxCommodityCommon bcc);

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
