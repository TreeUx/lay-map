package com.bx.gis.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
  * @Author Breach
  * @Description 设置项目启动后，浏览器自动打开首页
  * @Date 2019/3/14
  * @return
  */
@Component
public class MyCommandRunner implements CommandLineRunner {
    private static Logger logger = LoggerFactory.getLogger(MyCommandRunner.class);
    @Value("${spring.web.loginurl}")
    private String loginUrl;

    @Value("${spring.web.googleexcute}")
    private String googleExcutePath;

    @Value("${spring.auto.openurl}")
    private boolean isOpen;

    @Override
    public void run(String... args) throws Exception {
        if(isOpen) {
            String cmd = googleExcutePath +" "+ loginUrl;
            Runtime run = Runtime.getRuntime();
            try {
                run.exec(cmd);
                logger.debug("启动浏览器打开项目成功");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
    }
}

