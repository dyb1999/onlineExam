package com.dyb.demo.Controller;

import com.dyb.demo.Service.PoiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/excel")
public class ExcelFileController {
    @Autowired
    PoiService poiService;

    @PostMapping(value = "fileUpload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> fileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        return poiService.fileUpload(file);
    }

    @GetMapping("/downLoadExcel")
    public void downLoadExcel(HttpServletResponse response) {
        poiService.downLoadExcel(response);
    }
}
