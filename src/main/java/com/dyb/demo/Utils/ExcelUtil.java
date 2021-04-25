package com.dyb.demo.Utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.expression.Lists;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExcelUtil {

    //检查文件后缀名1
    public static Boolean checkExtension(String extension) {
        List<String> suffix = new ArrayList<>();
        suffix.add("xls");
        suffix.add("xlsx");
        suffix.add("XLS");
        suffix.add("XLSX");
        return suffix.contains(extension);
    }

    //检查文件后缀名2
    public static Boolean checkExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return checkExtension(extension);
    }

    //判断内容是否是office文件1
    public static Boolean isOfficeFile(InputStream inputStream) {
        boolean result = false;
        try {
            FileMagic fileMagic = FileMagic.valueOf(inputStream);
            if (Objects.equals(fileMagic, FileMagic.OLE2) || Objects.equals(fileMagic, FileMagic.OOXML)) {
                result = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //判断内容是否是office文件2
    public static Boolean isOfficeFile(MultipartFile file) throws IOException {
        boolean result = false;
        BufferedInputStream bufferedInputStream = new BufferedInputStream(file.getInputStream());
        result = isOfficeFile(bufferedInputStream);
        return result;
    }

    //通过后缀名判断文件类型2003excel
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    //判断文件类型2007-xlsx
    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    //自动判断文件类型
    public static Workbook getWorkbookAuto(MultipartFile file) throws IOException {
        boolean isExcel2003 = true;
        if (isExcel2007(file.getOriginalFilename())) {
            isExcel2003 = false;
        }
        BufferedInputStream is = new BufferedInputStream(file.getInputStream());
        Workbook wb;
        if (isExcel2003) {
            wb = new HSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook(is);
        }
        return wb;
    }
}

