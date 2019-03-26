package com.bx.gis.entity;

import lombok.Data;

/**
 * @Description 资源特色
 * @Author Barton
 * @Date 2019/3/25
 * @Version V1.0
 **/
@Data
public class BxSubjectivity {
    /*ID编号*/
    private int id;
    /*单品编号*/
    private String productId;
    /*流行性-对应等级*/
    private int epidemic;
    /*休闲性-对应等级*/
    private int recreational;
    /*怀旧性-对应等级*/
    private int nostalgic;
    /*浪漫性-对应等级*/
    private int romantic;
    /*亲子性-对应等级*/
    private int parentChild;
    /*自然性-对应等级*/
    private int naturalness;
    /*奇特性-对应等级*/
    private int singularity;
    /*刺激性-对应等级*/
    private int excitement;
    /*文化性-对应等级*/
    private int culture;
    /*观赏性-对应等级*/
    private int ornamental;
    /*参与性-对应等级*/
    private int participatory;
    /*标志性-对应等级*/
    private int iconic;


}
