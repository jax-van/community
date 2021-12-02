package com.jaxvan.community.controller;

import com.jaxvan.community.dto.FileDTO;
import com.jaxvan.community.provider.AliCouldProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class FileController {
    @Autowired
    private AliCouldProvider aliCouldProvider;

    @ResponseBody
    @RequestMapping("/file/upload")
    public Object upload(HttpServletRequest request) {
        MultipartHttpServletRequest multipartHttpServletRequest
                = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartHttpServletRequest.getFile("editormd-image-file");
        FileDTO fileDTO = new FileDTO();
        try {
            String upload =
                    aliCouldProvider.upload(file.getInputStream(), file.getOriginalFilename());
            fileDTO.setSuccess(1);
            fileDTO.setMessage("上传成功");
            fileDTO.setUrl(upload);
            return fileDTO;
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileDTO.setSuccess(0);
        fileDTO.setMessage("上传失败");
        fileDTO.setUrl("");
        return fileDTO;
    }
}
