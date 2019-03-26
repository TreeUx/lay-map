package com.bx.gis.common;

import java.util.Random;

/**
 * @Description 生产随机8位不重复的字母+数字码
 * @Author Breach
 * @Date 2018/12/18
 * @Version V1.0
 **/
public class StringRandom {

        //生成随机数字和字母,
        public String getStringRandom(int length) {

            String val = "";
            Random random = new Random();

            //参数length，表示生成几位随机数
            for(int i = 0; i < length; i++) {

                String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
                //输出字母还是数字
                if( "char".equalsIgnoreCase(charOrNum) ) {
                    //输出是大写字母还是小写字母
                    int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                    val += (char)(random.nextInt(26) + temp);
                } else if( "num".equalsIgnoreCase(charOrNum) ) {
                    val += String.valueOf(random.nextInt(10));
                }
            }
            return val.toUpperCase();
        }

        public static void  main(String[] args) {
            for(int i = 0; i < 1000; i++) {
                //测试
                System.out.println(new StringRandom().getStringRandom(8));
            }
        }

}
