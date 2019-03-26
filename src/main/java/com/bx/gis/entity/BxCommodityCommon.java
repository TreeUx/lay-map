package com.bx.gis.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Description TODO
 * @Author Breach
 * @Date 2018/12/20
 * @Version V1.0
 **/
@Data
public class BxCommodityCommon {
    /*商品编码*/
    private String comCode;
    /*商家编码*/
    private String merCode;
    /*代理编码*/
    private String merAgency;
    /*国家编码*/
    private String comPassport;
    /*商品名称*/
    private String comName;
    /*商品类型*/
    private int comType;
    /*商品数量*/
    private String comQuantity;
    /*商品双向出入口坐标*/
    private String comDuplex;
    /*详细地址*/
    private String comAddress;
    /*服务起始时间*/
    private Date comBegining;
    /*服务结束时间*/
    private Date comMoment;
    /*最佳游玩时长*/
    private int comBest;
    /*单品介绍*/
    private String comIntroduce;
    /*商品层级*/
    private String comLevel;
    /*国家*/
    private String state;
    /*城市*/
    private String city;
    /*父指针*/
    private int parentid;
    /*中心点坐标*/
    private String comCentral;
    /*轨迹*/
    private String comTrack;
    /*轨迹方向*/
    private int comDirection;
    /*轨迹宽度*/
    private int lineWidth;
    /*定制游运营部id*/
    private int bxOpDeptid;
    /*运营部名称*/
    private String opDeptName;
    /*父id*/
    private int parent;
}
