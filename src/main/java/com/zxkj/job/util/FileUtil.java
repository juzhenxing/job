package com.zxkj.job.util;

import com.zxkj.job.exp.JobException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class FileUtil {

    @Value("${job.tmp.path}")
    private String tmpPath;

    public String saveFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        int pointIndex = originalFilename.lastIndexOf(".");
        String fileType = originalFilename.substring(pointIndex + 1);
        String fileName = originalFilename.substring(0, pointIndex) + "_" + System.currentTimeMillis() + "." + fileType;
        File file = new File(tmpPath + fileName);
        //判断目标文件所在的目录是否存在
        if (!file.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            if (!file.getParentFile().mkdirs()) {
                throw JobException.LICENSE_TEMP_DIRECTORY_EXCEPTION;
            } else {
                multipartFile.transferTo(file);
            }
        } else {
            multipartFile.transferTo(file);
        }
        return fileName;
    }

    public ResponseEntity<InputStreamResource> downloadFile(String fileName) throws IOException {
        String filePath = tmpPath + fileName;
        FileSystemResource file = new FileSystemResource(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", new String(file.getFilename().getBytes("UTF-8"),"iso-8859-1")));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(file.getInputStream()));
    }

}
