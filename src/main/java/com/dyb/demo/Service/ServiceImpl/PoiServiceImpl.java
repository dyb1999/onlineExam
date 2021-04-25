package com.dyb.demo.Service.ServiceImpl;

import com.dyb.demo.Entity.Question;
import com.dyb.demo.Mapper.QuestionMapper;
import com.dyb.demo.Service.PoiService;
import com.dyb.demo.Utils.ExcelUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
public class PoiServiceImpl implements PoiService {
    @Autowired
    QuestionMapper questionMapper;

    //上传
    @Override
    public ResponseEntity fileUpload(MultipartFile file) {
        if (!ExcelUtil.checkExtension(file)) {
            return new ResponseEntity("请求文件类型错误：后缀名错误", HttpStatus.BAD_REQUEST);
        }
        try {
            if (ExcelUtil.isOfficeFile(file)) {
                Question question = new Question();
                Workbook workbook = ExcelUtil.getWorkbookAuto(file);
                Sheet sheet = workbook.getSheetAt(0);
                int rows = sheet.getPhysicalNumberOfRows();

                for (int i = 2; i < rows; i++) {
                    //从第三行开始，index=2
                    Row row = sheet.getRow(i);
                    Cell cell0 = row.getCell(0);
                    Cell cell1 = row.getCell(1);
                    Cell cell2 = row.getCell(2);//
                    Cell cell3 = row.getCell(3);//
                    Cell cell4 = row.getCell(4);
                    Cell cell5 = row.getCell(5);
                    Cell cell6 = row.getCell(6);
                    Cell cell7 = row.getCell(7);
                    Cell cell8 = row.getCell(8);
                    Cell cell9 = row.getCell(9);
                    Cell cell10 = row.getCell(10);
                    Cell cell11 = row.getCell(11);
                    cell0.setCellType(CellType.STRING);
                    cell1.setCellType(CellType.STRING);
                    cell2.setCellType(CellType.STRING);
                    cell3.setCellType(CellType.STRING);
                    cell4.setCellType(CellType.STRING);
                    cell5.setCellType(CellType.STRING);
                    cell6.setCellType(CellType.STRING);
                    cell7.setCellType(CellType.STRING);
                    cell8.setCellType(CellType.STRING);
                    cell9.setCellType(CellType.STRING);
                    cell10.setCellType(CellType.STRING);
                    cell11.setCellType(CellType.STRING);
                    question.setMajor(cell0.toString());
                    question.setSubject(cell1.toString());
                    question.setUserid(Integer.valueOf(cell2.toString()));
                    question.setType(Integer.valueOf(cell3.toString()));
                    question.setQuestion(cell4.toString());
                    question.setScore(Integer.valueOf(cell5.toString()));
                    question.setDifficulty(Double.valueOf(cell6.toString()));
                    question.setAnswer(cell7.toString());
                    question.setChoiceA(cell8.toString());
                    question.setChoiceB(cell9.toString());
                    question.setChoiceC(cell10.toString());
                    question.setChoiceD(cell11.toString());
                    questionMapper.addQuestion(question);
                }
            } else {
                return new ResponseEntity("请求文件错误：文件类型错误", HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    //下载
    @Override
    public void downLoadExcel(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("题目信息表");

        List<Question> questionList = questionMapper.findAll();

        String fileName = "QuestionList" + ".xls";
        int rowNum = 1;
        String[] headers = {"专业", "科目", "创建人编号", "问题类型", "问题内容", "题目分值", "难度系数", "答案", "选项A", "选项B", "选项C", "选项D"};
        HSSFRow row = sheet.createRow(0);

        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        for (Question question : questionList) {
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(question.getMajor());
            row1.createCell(1).setCellValue(question.getSubject());
            row1.createCell(2).setCellValue(question.getUserid());
            row1.createCell(3).setCellValue(question.getType());
            row1.createCell(4).setCellValue(question.getQuestion());
            row1.createCell(5).setCellValue(question.getScore());
            row1.createCell(6).setCellValue(question.getDifficulty());
            row1.createCell(7).setCellValue(question.getAnswer());
            row1.createCell(8).setCellValue(question.getChoiceA());
            row1.createCell(9).setCellValue(question.getChoiceB());
            row1.createCell(10).setCellValue(question.getChoiceC());
            row1.createCell(11).setCellValue(question.getChoiceD());
            rowNum++;
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;fileName=" + fileName);

        try {
            response.flushBuffer();
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
