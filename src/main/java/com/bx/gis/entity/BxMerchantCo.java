package com.bx.gis.entity;

import lombok.Data;
import javax.persistence.Column;
import java.util.Date;

/**
 * @Description TODO
 * @Author Breach
 * @Date 2019/1/2
 * @Version V1.0
 **/
@Data
public class BxMerchantCo {
    private int id;
    /*删除状态*/
    private int delState;
    /*商品单入口*/
    private String merEntrance;
    /*商品双向出入口*/
    private String merDuplex;
    /*服务起始时刻*/
    private Date merBegining;
    /*服务截止时刻*/
    private Date merMoment;
    /*最佳游玩时长*/
    private Date merBest;
    /*商品单出口*/
    private String merExit;
    /*商品介绍*/
    @Column(columnDefinition = "TEXT")//设置字段类型为text类型
    private String merIntroduce;
    /*时区*/
    private String timezone;
    /*夏令时间编移秒数*/
    private int dstOffset;
    /*坐标点位置时间协调世界编移秒数*/
    private int rawOffset;
    /*国家id*/
    private int state;
    /*省id*/
    private int province;
    /*市id*/
    private int city;
    /*区id*/
    private int district;
    /*中心点坐标*/
    private String merCentral;
    /*轨迹*/
    @Column(columnDefinition = "TEXT")//设置字段类型为text类型
    private String merTrack;
    /*轨迹方向*/
    private int merDirection;
    /*轨迹宽度*/
    private int lineWidth;
    /*父id编号*/
    private int parentId;
    /*创建时间*/
    private Date merTime;
    /*交通信息*/
    @Column(columnDefinition = "TEXT")
    private String trafficInfo;
    /*门票信息*/
    private String ticketInfo;
    /*商家名称*/
    private String merName;
    /*商家位置*/
    private String merLocation;
    /*商家详细地址*/
    private String merAddress;


}
