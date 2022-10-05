package com.mszlu.blog.controller;

import com.mszlu.blog.utils.QiniuUtils;
import com.mszlu.blog.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("upload")
public class UploadController {
    @Autowired
    private QiniuUtils qiniuUtils;

    @PostMapping
    //MultipartFile file spring专门接收文件的一个类型
    public Result upload(@RequestParam("image") MultipartFile file){
        //唯一的文件名称
        String fileName = UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
        //上传文件 上传到哪里呢？ 上传到七牛云 云服务器，按量付费 速度快 把图片发放到离用户最近的服务器上 降低自身应用服务器的带宽消耗
        boolean upload = qiniuUtils.upload(file, fileName);
        if (upload){
            return Result.success(QiniuUtils.url + fileName);
        }
        return Result.fail(20001,"上传失败");
    }
}
