package com.dyb.demo.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Service
public interface PoiService {
    //表格上传导入数据库
    ResponseEntity fileUpload(MultipartFile file);

    //从数据库导出所有题目的表格，并下载到本地
    void downLoadExcel(HttpServletResponse response);
}
