package com.bx.gis.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bx.gis.common.MapConstant;
import com.bx.gis.common.StringRandom;
import com.bx.gis.entity.BxCommodityCommon;
import com.bx.gis.entity.BxSubjectivity;
import com.bx.gis.service.SceneryInfoService;
import com.bx.gis.utils.RequestUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description TODO
 * @Author Breach
 * @Date 2018/12/27
 * @Version V1.0
 **/
@Controller
@RequestMapping("")
public class SceneryInfoController {
    private static final Logger logger = LoggerFactory.getLogger(SceneryInfoController.class);
    //    private final static String UPLOAD_IMAGES = "_upload/images/";//上传路径
    private final static String UPLOAD_IMAGES = "src/main/resources/static/_upload/";//上传路径
    private final static String MYSQL_IMAGES = "/_upload/";//上传路径

    @Autowired
    SceneryInfoService sceneryInfoService;

    /**
     * @return java.util.Map<java.lang.String   ,   java.lang.Object>
     * @Author Breach
     * @Description 校验用户名密码
     * @Date 2019/2/15
     * @Param
     */
   /* @RequestMapping("/check")
    @ResponseBody
    public Map<String, Object> checkInfo(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> para = new HashMap<>();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        para.put("username", username);
        para.put("password", password);
        int num = sceneryInfoService.checkPsw(para); //校验用户名密码
        if(num != 0) {
            result.put("status", "200");
            result.put("msg", "登录成功");
        } else {
            result.put("status", "500");
            result.put("msg", "登录失败");
        }
        return result;
    }*/

   /* @RequestMapping("/index")
    public String index(Model model) {
        model.addAttribute("name", "景点数据采集");
        return "bxlyMap";
    }*/
    @RequestMapping("/queryCenterPoi")
    @ResponseBody
    public Map<String, Object> checkInfo(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> para = new HashMap<>();
        String scenery_name = request.getParameter("scenery_name");
        String url = MapConstant.BAIDU_POI + MapConstant.BD_QUERY + scenery_name
                + MapConstant.BD_REGION + scenery_name + MapConstant.BD_OUTPUT_AND_AK + MapConstant.SERVER_AK;
        System.out.println(url);
        try {

            JSONObject jo = RequestUtils.sendPost(url);
            JSONArray jos = JSONObject.parseArray(jo.get("results").toString());
            JSONObject locaJo = JSONObject.parseObject(JSONObject.parseObject(jos.get(0).toString()).get("location").toString());
            String location = locaJo.get("lng") + "," + locaJo.get("lat");
            result.put("status", 200);
            result.put("lng", locaJo.get("lng") == null ? 0 : locaJo.get("lng"));
            result.put("lat", locaJo.get("lat") == null ? 0 : locaJo.get("lat"));
            result.put("location", location);
        } catch (Exception e) {
            result.put("status", 500);
            result.put("error", "获取地图失败");
        }
        return result;
    }

    /**
     * @Author Breach
     * @Description 查询搜索的景区数据在景区表中是否存在
     * @Date 2018/12/27
     * @Param
     */
    @RequiresAuthentication
    @RequestMapping("/querySceneryInfo")
    @ResponseBody
    public Map<String, Object> querySceneryInfo(HttpServletRequest request, HttpServletResponse response) {
        String scenery_name = request.getParameter("scenery_name");//景区名称
        Map<String, Object> result = new HashMap<>();
        List<String> list = new ArrayList<>();
        if (scenery_name != "" && scenery_name != null) {
            scenery_name = "%" + scenery_name + "%";
            try {
                list = sceneryInfoService.querySceneyInfo(scenery_name);
                result.put("status", "success");
                result.put("data", list);
            } catch (Exception e) {
                result.put("status", "error");
                result.put("msg", "获取信息失败");
                logger.error("[查询景区信息失败-ErrorMsg:]", e);

            }
        } else {
            result.put("data", list.add(""));
        }
        return result;
    }

    /**
     * @return java.util.Map<java.lang.String       ,       java.lang.Object>
     * @Author Breach
     * @Description 新增采集的景区单元信息
     * @Date 2019/1/3
     * @Param
     */
    @RequiresAuthentication
    @RequestMapping("/addNewSceneryInfo")
    @ResponseBody
    public Map<String, Object> addNewSceneryInfo(HttpServletRequest request) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Map<String, Object> results = new HashMap<>();
        String comDuplex = ""; //双向出入口
        System.out.println(request.getParameter("parentid") == "");
        int parentid = Integer.parseInt(request.getParameter("parentid") == "" ? "0" : request.getParameter("parentid"));//父类id
        String com_code = request.getParameter("com_code");//商品代码
        String scenery_name = request.getParameter("scenery_name");//名称
        String state = request.getParameter("continents");//国家
        String province = request.getParameter("state");//省份
        String city = request.getParameter("city");//城市
        String scenery_address = request.getParameter("scenery_address");//详细地址
        int scenery_type = Integer.parseInt(request.getParameter("scenery_type"));//景点类型（暂未定）
        String scenery_location = request.getParameter("scenery_location");//坐标
        Date scenery_start_time = sdf.parse(request.getParameter("scenery_start_time"));//景区开放开始时间
        Date scenery_end_time = sdf.parse(request.getParameter("scenery_end_time"));//景区开放结束时间
        int com_best = Integer.parseInt(request.getParameter("com_best"));//最佳游玩时间
        String bx_level = request.getParameter("bx_level");//伴行级别
        String scenery_remark = request.getParameter("scenery_remark");//备注
        String[] com_duplex = request.getParameterValues("com_duplex");
        String character_type = request.getParameter("character_type"); //资源特色
        JSONObject characterJo = JSONObject.parseObject(character_type.toString());
        System.out.println(characterJo);
        int epidemic = Integer.parseInt(characterJo.get("epidemic").toString()); //流行性
        int recreational = Integer.parseInt(characterJo.get("recreational").toString()); //休闲性
        int nostalgic = Integer.parseInt(characterJo.get("nostalgic").toString()); //怀旧性
        int romantic = Integer.parseInt(characterJo.get("romantic").toString()); //浪漫性
        int parent_child = Integer.parseInt(characterJo.get("parent_child").toString()); //亲子性
        int naturalness = Integer.parseInt(characterJo.get("naturalness").toString()); //自然性
        int singularity = Integer.parseInt(characterJo.get("singularity").toString()); //奇特性
        int excitement = Integer.parseInt(characterJo.get("excitement").toString()); //刺激性
        int culture = Integer.parseInt(characterJo.get("culture").toString()); //文化性
        int ornamental = Integer.parseInt(characterJo.get("ornamental").toString()); //观赏性
        int participatory = Integer.parseInt(characterJo.get("participatory").toString()); //参与性
        int iconic = Integer.parseInt(characterJo.get("iconic").toString()); //标志性
        if (com_duplex != null && com_duplex.length != 0) {
            for (int i = 0; i < com_duplex.length; i++) { //双向出入口
                comDuplex += (com_duplex[i] + " ");
            }
        }
        BxCommodityCommon bcc = new BxCommodityCommon(); //商品实体类
        BxSubjectivity bs = new BxSubjectivity(); //资源特色实体类
        bcc.setParentid(parentid);
        bcc.setComName(scenery_name);
        bcc.setState(state); //国家
        bcc.setProvince(province); //省份
        bcc.setCity(city); //城市
        bcc.setComAddress(scenery_address);
        bcc.setComCentral(scenery_location);
        bcc.setComDuplex(comDuplex);
        bcc.setComBegining(scenery_start_time);
        bcc.setComMoment(scenery_end_time);
        bcc.setComBest(com_best);
        bcc.setComLevel(bx_level);
        bcc.setComIntroduce(scenery_remark);
        bcc.setComType(scenery_type); //商品类型
        //资源特色
        bs.setEpidemic(epidemic);
        bs.setRecreational(recreational);
        bs.setNostalgic(nostalgic);
        bs.setRomantic(romantic);
        bs.setParentChild(parent_child);
        bs.setNaturalness(naturalness);
        bs.setSingularity(singularity);
        bs.setExcitement(excitement);
        bs.setCulture(culture);
        bs.setOrnamental(ornamental);
        bs.setParticipatory(participatory);
        bs.setIconic(iconic);
        //商品编码
        bs.setProductId(com_code);
        int len = scenery_location.length();
        int index = scenery_location.indexOf(",");
        try {
            //添加前，查询该景点是否已经存在，不存在再添加
            int count = sceneryInfoService.querySceneryIfExist(scenery_name);
            if (com_code == "") {
                //商品编码
                com_code = new StringRandom().getStringRandom(8);
                //商品编码
                bcc.setComCode(com_code);
                //商品编码
                bs.setProductId(com_code);
                if (count > 0) {
                    results.put("msg", "该景点信息已存在");
                    results.put("status", "error");
                    return results;
                } else {
                    //添加景点信息到景点表中
                    int num = sceneryInfoService.addNewSceneryInfo(bcc);
                    //添加景点资源特色到bx_subjectivity特色表中
                    int num1 = sceneryInfoService.addSceneryType(bs);
                    if (num > 0 && num1 > 0) {
                        results.put("msg", "保存成功");
                        results.put("status", "success");
                        //商品编码
                        results.put("comCode", com_code);
                        //经度
                        results.put("lng", scenery_location.substring(0, index));
                        //纬度
                        results.put("lat", scenery_location.substring(index + 1, len));
                    } else {
                        results.put("status", "error");
                        results.put("msg", "保存失败");
                    }
                }
            } else {
                bcc.setComCode(com_code);
                int count1 = sceneryInfoService.querySceneryCount(scenery_name, com_code); //修改时查询修改的景点名称是否已经存在
                if (count1 == 1 || (count1 == 0 && count == 0)) {
                    boolean bl = sceneryInfoService.updateNewSceneryInfo(bcc); //修改景点数据
                    if (bl) {
                        results.put("msg", "修改成功");
                        results.put("status", "success");
                        results.put("comCode", com_code);//商品编码
                        results.put("lng", scenery_location.substring(0, index));//经度
                        results.put("lat", scenery_location.substring(index + 1, len));//纬度
                    } else {
                        results.put("status", "error");
                        results.put("msg", "修改失败");
                    }
                } else {
                    results.put("msg", "该景点信息已存在");
                    results.put("status", "error");
                    return results;
                }
            }

        } catch (Exception e) {
            results.put("status", "error");
            results.put("msg", "操作失败");
            logger.error("[景区单元信息保存异常-ErrorMsg:]", e);
        }
        return results;
    }

    /**
     * @return java.util.Map<java.lang.String       ,       java.lang.Object>
     * @Author Breach
     * @Description 根据商品code查询出商品信息
     * @Date 2019/1/4
     * @Param
     */
    @RequiresAuthentication
    @RequestMapping("/querySceneryInfoByCode")
    @ResponseBody
    public Map<String, Object> querySceneryInfoByCode(HttpServletRequest request) {
        Map<String, Object> results = new HashMap<>();
        String comCode = request.getParameter("comCode");
        try {
            List<Map<String, Object>> sceneryInfoList = sceneryInfoService.querySceneryInfoByCode(comCode);//查询商品信息
            if (!sceneryInfoList.isEmpty() && sceneryInfoList != null) {
                results.put("msg", "查询成功");
                results.put("status", "success");
                results.put("sceneryInfoList", sceneryInfoList);
            } else {
                results.put("status", "error");
                results.put("msg", "查询失败");
            }
        } catch (Exception e) {
            results.put("status", "error");
            results.put("msg", "操作失败");
            logger.error("[景区单元信息查询异常-ErrorMsg:]", e);
        }
        return results;
    }

    /**
     * @return java.util.Map<java.lang.String       ,       java.lang.Object>
     * @Author Breach
     * @Description 删除采集的景点数据
     * @Date 2019/1/4
     * @Param request
     */
    @RequiresAuthentication
    @RequestMapping("/deleteNewSceneryInfo")
    @ResponseBody
    public Map<String, Object> deleteNewSceneryInfo(HttpServletRequest request) {
        Map<String, Object> results = new HashMap<>();
        String comCode = request.getParameter("comCode");
        try {
            int num = sceneryInfoService.deleteNewSceneryInfo(comCode);
            if (num == 2) {
                results.put("msg", "删除成功");
                results.put("status", "success");
            } else {
                results.put("status", "error");
                results.put("msg", "删除失败");
            }
        } catch (Exception e) {
            results.put("status", "error");
            results.put("msg", "操作失败");
            logger.error("[景区单元信息删除异常-ErrorMsg:]", e);
        }
        return results;
    }

    /**
     * @return java.util.Map<java.lang.String       ,       java.lang.Object>
     * @Author Breach
     * @Description 根据搜索条件查询景区出入口信息
     * @Date 2019/1/7
     * @Param request
     */
    @RequiresAuthentication
    @RequestMapping("/queryNewSceneryPartInfo")
    @ResponseBody
    public Map<String, Object> queryNewSceneryPartInfo(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        String parentid = request.getParameter("parentid");
        try {
            List<Map<String, Object>> sceneryPartInfoList = sceneryInfoService.queryNewSceneryPartInfo(parentid);
            result.put("msg", "出入口信息查询成功");
            result.put("status", "success");
            result.put("data", sceneryPartInfoList);

        } catch (Exception e) {
            result.put("status", "error");
            result.put("msg", "操作失败");
            logger.error("[景区出入口信息查询异常-ErrorMsg:]", e);
        }
        return result;
    }

    /**
     * @return java.util.Map<java.lang.String       ,       java.lang.Object>
     * @Author Breach
     * @Description 根据搜索条件查询景区出入口信息
     * @Date 2019/1/7
     * @Param request
     */
    @RequiresAuthentication
    @RequestMapping("/querySceneryEntranceInfos")
    @ResponseBody
    public Map<String, Object> querySceneryEntranceInfos(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        String scenery_name = request.getParameter("scenery_name");
//        scenery_name = "%" + scenery_name + "%";
        try {
            List<Map<String, Object>> sceneryInfoList = sceneryInfoService.querySceneryEntranceInfos(scenery_name);
            result.put("msg", "出入口信息查询成功");
            result.put("status", "success");
            result.put("data", sceneryInfoList);

        } catch (Exception e) {
            result.put("status", "error");
            result.put("msg", "操作失败");
            logger.error("[景区出入口信息查询异常-ErrorMsg:]", e);
        }
        return result;
    }

    /**
     * @return java.util.Map<java.lang.String   ,   java.lang.Object>
     * @Author Breach
     * @Description 保存转换后的线路坐标信息
     * @Date 2019/1/9
     * @Param request
     */
    @RequiresAuthentication
    @RequestMapping("/addPointlineInfos")
    @ResponseBody
    public Map<String, Object> addPointlineInfos(HttpServletRequest request) throws ParseException {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String comCode = new StringRandom().getStringRandom(8);//商品编码
        String parentid = request.getParameter("parentid");
        String transedPointStrs = request.getParameter("transedPointStrs");
        String comName = request.getParameter("comName");
        Date comBegining = sdf.parse("08:00");
        Date comMoment = sdf.parse("20:00");
        int comType = 3; //行
        String comLevel = "6";
        String state = "33"; //国家
        params.put("comName", comName + "度假区LN"); //商品名称
        params.put("transedPointStrs", transedPointStrs); //轨迹
        params.put("parentid", parentid); //父类id
        params.put("comCode", comCode); //商品code
        params.put("comType", comType); //商品类型
        params.put("comBegining", comBegining); //服务开始时间
        params.put("comMoment", comMoment); //服务结束时间
        params.put("comLevel", comLevel); //商品层级
        params.put("state", state); //国家
        try {
            int num = sceneryInfoService.addPointlineInfos(params);
            if (num != 0) {
                result.put("msg", "保存成功");
                result.put("status", "success");
                System.out.println("(SceneryInfoController:413h) 线路信息保存成功");
            } else {
                result.put("status", "error");
                result.put("msg", "保存失败");
            }
        } catch (Exception e) {
            result.put("status", "error");
            result.put("msg", "操作失败");
            logger.error("[景点线路轨迹路线信息保存异常-ErrorMsg:]", e);
        }
        return result;
    }

    /**
     * @return java.util.Map<java.lang.String       ,       java.lang.Object>
     * @Author Breach
     * @Description 根据景点Id查询景点内所有线路轨迹坐标信息
     * @Date 2019/1/7
     * @Param request
     */
    @RequestMapping("/querySceneryTrackInfos")
    @ResponseBody
    @RequiresAuthentication
    public Map<String, Object> querySceneryTrackInfos(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        String parentid = request.getParameter("parentid");
        try {
            List<Map<String, Object>> trackInfoList = sceneryInfoService.querySceneryTrackInfos(parentid);
            if (trackInfoList != null && !trackInfoList.isEmpty()) {
                result.put("msg", "线路轨迹路线信息查询成功");
                result.put("status", "success");
                result.put("data", trackInfoList);
            }
        } catch (Exception e) {
            result.put("status", "error");
            result.put("msg", "操作失败");
            logger.error("[景点线路轨迹路线信息查询异常-ErrorMsg:]", e);
        }
        return result;
    }


}
