package com.bx.gis.service.Impl;

import com.bx.gis.entity.BxCommodityCommon;
import com.bx.gis.entity.BxSubjectivity;
import com.bx.gis.mapper.SceneryInfoMapper;
import com.bx.gis.service.SceneryInfoService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author Breach
 * @Date 2018/12/27
 * @Version V1.0
 **/
@Service
public class SceneryInfoServiceImpl implements SceneryInfoService {

    @Autowired
    SceneryInfoMapper sceneryInfoMapper;

    /**
      * @Author Barton
      * @Description 查询景区名称信息
      * @Date 2019/3/21
      * @Param scenery_name
      * @return java.util.List<java.lang.String>
      */
    @Override
    public List<String> querySceneyInfo(String scenery_name) {
        return sceneryInfoMapper.querySceneyInfo(scenery_name);
    }

    /**
     * @return int
     * @Author Breach
     * @Description 新增采集的景区单元信息
     * @Date 2019/1/3
     * @Param bmc
     */
    @Override
    public int addNewSceneryInfo(BxCommodityCommon bcc) {
        return sceneryInfoMapper.addNewSceneryInfo(bcc);
    }

    /**
      * @Author Breach
      * @Description 查询添加的景点信息是否存在
      * @Date 2019/1/3
      * @Param scenery_name
      * @return int
      */
    @Override
    public int querySceneryIfExist(String scenery_name) {
        return sceneryInfoMapper.querySceneryIfExist(scenery_name);
    }

    /**
      * @Author Breach
      * @Description 新增景点信息
      * @Date 2019/1/4
      * @Param bcc
      * @return int
      */
    @Override
    public int addSceneryType(BxSubjectivity bs) {
        return sceneryInfoMapper.addSceneryType(bs);
    }

    /**
      * @Author Breach
      * @Description 删除景区商品表及资源特色表中数据
      * @Date 2019/1/4
      * @Param comCode
      * @return int
      */
    @Override
    public int deleteNewSceneryInfo(String comCode) {
        return sceneryInfoMapper.deleteNewSceneryInfo(comCode);
    }

    /**
      * @Author Breach
      * @Description 根据商品code查询商品信息
      * @Date 2019/1/4
      * @Param comCode
      * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
      */
    @Override
    public List<Map<String, Object>> querySceneryInfoByCode(String comCode) {
        return sceneryInfoMapper.querySceneryInfoByCode(comCode);
    }

    /**
      * @Author Breach
      * @Description 查询修改时景点修改的名称是否已经存在
      * @Date 2019/1/4
      * @Param scenery_name
      * @param com_code
      * @return int
      */
    @Override
    public int querySceneryCount(String scenery_name, String com_code) {
        return sceneryInfoMapper.querySceneryCount(scenery_name, com_code);
    }

    /**
      * @Author Breach
      * @Description 修改景点数据
      * @Date 2019/1/4
      * @Param bcc
      * @return int
      */
    @Override
    public boolean updateNewSceneryInfo(BxCommodityCommon bcc) {
        return sceneryInfoMapper.updateNewSceneryInfo(bcc);
    }

    /**
      * @Author Breach
      * @Description 查询景区出入口信息
      * @Date 2019/1/7
      * @Param scenery_name
      * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
      */
    @Override
    public List<Map<String, Object>> querySceneryEntranceInfos(String scenery_name) {
        return sceneryInfoMapper.querySceneryEntranceInfos(scenery_name);
    }

    /**
      * @Author Breach
      * @Description 查询景点下的所有线路轨迹路线信息
      * @Date 2019/1/7
      * @Param parentid
      * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
      */
    @Override
    public List<Map<String, Object>> querySceneryTrackInfos(String parentid) {
        return sceneryInfoMapper.querySceneryTrackInfos(parentid);
    }

    @Override
    public int addPointlineInfos(Map<String, Object> params) {
        return sceneryInfoMapper.addPointlineInfos(params);
    }

    @Override
    public List<Map<String, Object>> queryNewSceneryPartInfo(String parentid) {
        return sceneryInfoMapper.queryNewSceneryPartInfo(parentid);
    }

    /**
      * @Author Breach
      * @Description 校验用户名和密码
      * @Date 2019/2/16
      * @Param para
      * @return int
      */
    @Override
    public int checkPsw(Map<String, Object> para) {
        return sceneryInfoMapper.checkPsw(para);
    }
}
