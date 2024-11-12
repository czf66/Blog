package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.example.common.Result;
import com.example.dto.FileVO;
import org.apache.commons.compress.utils.Lists;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @date 2021/3/17 10:16
 * @description 文件上传
 */
@RestController
@RequestMapping("/files")
public class FileController {

    // 文件上传存储路径
    private static final String filePath = System.getProperty("user.dir") + "/src/main/resources/static/file/";

    /**
     * 单文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        String flag = System.currentTimeMillis() + ""; // 时间戳
        String fileName = file.getOriginalFilename();
        try {
            // 文件存储形式，时间戳-文件名
            FileUtil.writeBytes(file.getBytes(), filePath + flag + "-" + fileName);
            System.out.println(fileName + "--上传成功");
            Thread.sleep(1L);
        } catch (Exception e) {
            System.err.println(fileName + "--文件上传失败");
        }
        System.out.println("***"+flag);
        // 返回照片的名字
        return Result.success(flag + "-" + fileName);
    }

    /**
     * 多文件上传
     * @param request
     * @return
     */
    @PostMapping("/upload/multiple")
    public Result<List<FileVO>> multipleUpload(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("files");
        List<FileVO> fileVOS = Lists.newArrayList();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }
            String flag = System.currentTimeMillis() + "";
            String fileName = file.getOriginalFilename();
            FileVO fileVO = new FileVO();
            fileVO.setFlag("http://localhost:9999/files/" + flag);
            fileVO.setFileName(file.getOriginalFilename());
            fileVOS.add(fileVO);
            try {
                FileUtil.writeBytes(file.getBytes(), filePath + flag + "-" + fileName);
                System.out.println(fileName + "--上传成功");
                Thread.sleep(1L);
            } catch (Exception e) {
                System.err.println(fileName + "--文件上传失败");
            }

        }
        return Result.success(fileVOS);
    }

    /**
     * 富文本文件上传
     */
    @PostMapping("/editor/upload")
    public Dict editorUpload(MultipartFile file) {
        String flag;
        synchronized (FileController.class) {
            flag = System.currentTimeMillis() + "";
            ThreadUtil.sleep(1L);
        }
        String fileName = file.getOriginalFilename();
        try {
            if (!FileUtil.isDirectory(filePath)) {
                FileUtil.mkdir(filePath);
            }
            // 文件存储形式：时间戳-文件名
            FileUtil.writeBytes(file.getBytes(), filePath + flag + "-" + fileName);  // /src/main/resources/static/file/1697438073596-avatar.png
            System.out.println(fileName + "--上传成功");

        } catch (Exception e) {
            System.err.println(fileName + "--文件上传失败");
        }
        String http = "http://localhost:9999/files/";
        return Dict.create().set("errno", 0).set("data", CollUtil.newArrayList(Dict.create().set("url", http +  flag + "-" + fileName)));
    }

    @PostMapping("/uploadVideo")
    public Dict uploadVideo(@RequestParam("video") MultipartFile file) {
        String flag;
        synchronized (FileController.class) {
            flag = System.currentTimeMillis() + "";
            ThreadUtil.sleep(1L);
        }
        String fileName = file.getOriginalFilename();
        try {
            if (!FileUtil.isDirectory(filePath)) {
                FileUtil.mkdir(filePath);
            }
            // 文件存储形式：时间戳-文件名
            FileUtil.writeBytes(file.getBytes(), filePath + flag + "-" + fileName);  // /src/main/resources/static/file/1697438073596-avatar.png
            System.out.println(fileName + "--上传成功");

        } catch (Exception e) {
            System.err.println(fileName + "--文件上传失败");
        }
        String http = "http://localhost:9999/files/";
        return Dict.create().set("errno", 0).set("data", CollUtil.newArrayList(Dict.create().set("url", http +  flag + "-" + fileName)));
    }

    /**
     * 获取文件
     * @param flag
     * @param response
     */
    @GetMapping("/{flag}") // 传的是图片名称：1622011254634-3.jpg
    public void avatarPath(@PathVariable String flag, HttpServletResponse response) {
        OutputStream os;
        String basePath = System.getProperty("user.dir") + "/src/main/resources/static/file/";
        //获取static/file/下所有图片的名称
        List<String> fileNames = FileUtil.listFileNames(basePath);
        //将传过来的图片名称进行对比
        String avatar = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");
        try {
            if (StrUtil.isNotEmpty(avatar)) {
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(avatar, "UTF-8"));
                response.setContentType("application/octet-stream");
                // 从文件中读出字节流，并存到bytes中
                byte[] bytes = FileUtil.readBytes(basePath + avatar);
                os = response.getOutputStream();
                //将bytes写到浏览器中
                os.write(bytes);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            System.out.println("文件下载失败");
        }
    }

}
