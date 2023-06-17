package com.lrt.reggie.controller;

import cn.hutool.core.io.FileUtil;
import com.lrt.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${file.upload.path}")
    private String fileUploadPath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        File uploadParentFile = new File(fileUploadPath);
        if (!uploadParentFile.exists()) {
            // 如果父目录不存在就新建
            uploadParentFile.mkdir();
        }
        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        // 获取文件扩展名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 使用UUID重新生成文件名
        String fileName = UUID.randomUUID().toString() + suffix;
        File fileTmp = new File(fileUploadPath + fileName);
        try {
            file.transferTo(fileTmp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    /**
     * 文件下载
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        try {
            // 根据文件的唯一标识码获取文件
            File uploadFile = new File(fileUploadPath + name);
            // 设置输出流的格式
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            // 读取文件的字节流
            outputStream.write(FileUtil.readBytes(uploadFile));
            outputStream.flush();
            outputStream.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
