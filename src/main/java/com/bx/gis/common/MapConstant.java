package com.bx.gis.common;

import lombok.Data;

/**
  * @Author Breach
  * @Description 地图公共类
  * @Date 2019/3/9
  * @Param null
  * @return
  */
@Data
public class MapConstant {
    //Google API密钥
    public static final String API_KEY = "AIzaSyDFNA6uiTeujVvWsfXJ6l_MYNp7Np5rhgw";
    //Google地址前缀
    public static final String GOOGLE_PREFIX_URL = "https://maps.googleapis.com";
    //Google请求地址后缀（以json格式返回数据）
    public static final String JSON_URL = "/maps/api/geocode/json?address=";
    /******************************************************/
    //Google地方搜索请求地址前缀
    public static final String GOOGLE_PLACE_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=";
    //Google请求地址后缀（以json格式返回数据）
    public static final String PIEFIX_LOCATION = "&location=";
    //Google请求地址后缀（以json格式返回数据）
    public static final String PIEFIX_RADIUS = "&radius=50000&key=";
    /*****************************************************/
    //语言
    public static final String GOOGLE_LANGUAGE = "&language=zh-cn";
    //Google地址后缀
    public static final String GOOGLE_SUFFIX_URL = "&key=";
    //Google请求地址（以xml格式返回数据）
    public static final String XML_URL = "/maps/api/geocode/xml?address=";

    //Baidu请求地址前缀
    public static final String BD_PREFIX_URL = "http://api.map.baidu.com";
    //Baidu请求地址后缀（地点检索）
    public static final String BD_SUFFIX_URL = "/place/v2/search?";
    //Baidu请求参数query（地区）（必填项）
    public static final String BD_QUERY = "q=";
    //Baidu请求参数region（城市）（必填项）
    public static final String BD_REGION = "&region=";
    //Baidu请求参数city_limit（必填项）
    public static final String BD_CITY_LIMIT = "&city_limit=";
    //Baidu请求参数tag（可选项）
    public static final String BD_TAG = "&tag=";
    //Baidu请求参数scope（可选项）
    public static final String BD_SCOPE = "&scope=";
    //Baidu请求参数page_size（可选项）
    public static final String BD_PAGE_SIZE = "&page_size=";
    //Baidu请求参数page_num（可选项）
    public static final String BD_PAGE_NUM = "&page_num=";
    //Baidu请求参数AK（可通用）（必填项）
    public static final String BD_OUTPUT_AND_AK = "&output=json&ak=";
    //Baidu请求地址后缀（路线规划）
    public static final String BD_SUFFIX_transit_URL = "/direction/v2/transit?";
    //Baidu请求参数origin（起点坐标）
    public static final String BD_ORIGIN = "origin=";
    //Baidu请求参数destination（终点坐标）
    public static final String BD_DESTINATION = "&destination=";
    //Baidu AK（浏览器端）
//    public static final String BROWSER_AK = "3jkpsQONO1TMU6ZIn36SajqClVd4Uk5M";//个人
    public static final String BROWSER_AK = "0NIuUlCTTGztFjaP8IPfhW12glBlcxH5";//bx
    //Baidu AK（服务端）
//    public static final String SERVER_AK = "iUvlVpUmE8hrgQUH4psrZ9BuDk0cGp1m";//个人
    public static final String SERVER_AK = "TyZ0AadANQFCotrdzl3wazPGMWw5cn2R";//bx

    public static final String BAIDU_LY = "https://lvyou.baidu.com/";//百度旅游前缀

    public static final String BAIDU_POI = "http://api.map.baidu.com/place/v2/search?"; //openlayers

    public static final String BX_MAIL = "伴行平台"; //验证码
}
