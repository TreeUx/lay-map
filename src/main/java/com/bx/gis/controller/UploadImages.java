package com.bx.gis.controller;

import com.bx.gis.utils.ImageUtil;
import com.bx.gis.utils.JsonResult;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @Description 上传图片
 * @Author Barton
 * @Date 2019/3/25
 * @Version V1.0
 **/
@Controller
@RequestMapping("")
public class UploadImages {
    private static final Logger logger = LoggerFactory.getLogger(SceneryInfoController.class);
    //    private final static String UPLOAD_IMAGES = "_upload/images/";//上传路径
    private final static String UPLOAD_IMAGES = "src/main/resources/static/_upload/";//上传路径
    private final static String MYSQL_IMAGES = "/_upload/";//上传路径

    /**
     * @param request
     * @param session
     * @return java.lang.String
     * @Author Breach
     * @Description 保存图片到指定路径
     * @Date 2018/12/29
     * @Param image
     */
    @RequiresAuthentication
    @RequestMapping("/uploadImages")
    @ResponseBody
    public String uploadImages(@RequestParam("file") MultipartFile image, HttpServletRequest request
            , HttpServletResponse response, HttpSession session) throws IOException {
//        File imageFolder = new File(session.getServletContext().getRealPath(UPLOAD_IMAGES)); //获取用户c盘目录下的地址
        /***********************************/
        System.out.println(System.getProperty("user.dir"));//获取当前项目根路径
        System.out.println(new File(this.getClass().getResource("/").getPath())); //获取target下的路径
        //获取类加载的根路径
//        File imageFolder = new File(this.getClass().getResource("/").getPath()+ File.separator + UPLOAD_IMAGES);
        File imageFolder = new File(System.getProperty("user.dir") + File.separator + UPLOAD_IMAGES);
//        System.out.println(imageFolder);
        /***********************************/
        String fileName = UUID.randomUUID().toString().replace("-", "") + ".jpg";//生成唯一标识，避免文件名重复
        File file = new File(imageFolder, fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (image != null) {
            image.transferTo(file);
            BufferedImage img = ImageUtil.change2jpg(file);//工具类，转成统一jpg 格式，不转也可以,此处可省略
//            BufferedImage img = ImageIO.read(file);
            ImageIO.write(img, "jpg", file);
        }
        JsonResult<String> result = new JsonResult<>();
        result.setData(MYSQL_IMAGES + fileName);//图片保存路径
        result.setCode(100);
        result.setMessage("上传成功");
        return new JSONObject(result).toString();
    }
}
