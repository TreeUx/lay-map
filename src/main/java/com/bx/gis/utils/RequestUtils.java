package com.bx.gis.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author: kevin
 * @date: 2018/10/23 17:15
 */
public class RequestUtils {
    /**
     * 向指定的 URL发送远程POST方法的请求
     * @param  url:url发送请求的
     * @param  str: DES加密json字符串为String的请求参数
     * @return 所代表远程资源的响应结果
     */
    public static JSONObject sendPost(String url, String str) {
        PrintWriter out = null;
        BufferedReader in = null;
        JSONObject jsonObject = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestMethod("POST");
            // 发送POST请求必须设置下面的属性
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            //设置请求属性
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(str);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            while ((line = in.readLine()) != null) {
                result += line;
            }
            //将返回结果转换为字符串
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            throw new RuntimeException("远程通路异常"+e.toString());
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return jsonObject;
    }

    public static JSONObject sendPost(String url) {
        PrintWriter out = null;
        BufferedReader in = null;
        JSONObject jsonObject = null;
        String result = "";
        try {
            URL realUrl = new URL(url);

            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
            // 设置通用的请求属性
//            conn.setRequestMethod("POST");
            conn.setRequestMethod("GET");
            // 发送POST请求必须设置下面的属性
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.setUseCaches(false);
            //设置请求属性
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.connect();
            // 获取URLConnection对象对应的输出流
//            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
//            out.print(str);
            // flush输出流的缓冲
//            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            while ((line = in.readLine()) != null) {
                result += line;
            }

            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            throw new RuntimeException("远程通路异常"+e.toString());
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return jsonObject;
    }



}
