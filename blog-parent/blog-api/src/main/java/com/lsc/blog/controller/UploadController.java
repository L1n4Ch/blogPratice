package com.lsc.blog.controller;

import com.lsc.blog.utils.QiniuUtils;
import com.lsc.blog.vo.Result;
import com.qiniu.common.QiniuException;
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
    public Result upload(@RequestParam("image") MultipartFile file){

        // 拿到文件的原始名称
        String originalFilename = file.getOriginalFilename();
        // 上传随机名称而不是原始名称
        // AfterLast拿到指定字符后面的唯一文件名称，
        // 即 "." 后面的文件名(文件后缀名) 例如abc.png，拿到png
        String fileName = UUID.randomUUID().toString() + "." +StringUtils.substringAfterLast(originalFilename, ".");
        // 上传文件 七牛云 把图片发放到离用户最近的服务器上 降低自身应用服务器的带宽消耗
        boolean upload = qiniuUtils.upload(file, fileName);
        if(upload){
            return Result.success(QiniuUtils.url + fileName);
        }
        return Result.fail(20001,"上传失败");
    }

//    public Result deleteFile(){
//        try {
//            qiniuUtils.deleteAll();
//        } catch (QiniuException e) {
//            e.printStackTrace();
//        }
//        return Result.success(null);
//    }

}
