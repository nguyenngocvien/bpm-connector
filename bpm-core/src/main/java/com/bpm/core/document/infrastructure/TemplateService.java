package com.bpm.core.document.infrastructure;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class TemplateService {

    private final String templatePath = "templates/";

    public byte[] generateWordDocument(String templateName, Map<String, Object> data) throws IOException {
        File templateFile = new File(templatePath + templateName);
        XWPFDocument doc = new XWPFDocument(new FileInputStream(templateFile));

        for (XWPFParagraph p : doc.getParagraphs()) {
            for (XWPFRun run : p.getRuns()) {
                String text = run.getText(0);
                if (text != null) {
                    for (Map.Entry<String, Object> entry : data.entrySet()) {
                        text = text.replace("${" + entry.getKey() + "}", entry.getValue().toString());
                    }
                    run.setText(text, 0);
                }
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        doc.write(baos);
        doc.close();
        return baos.toByteArray();
    }

    // Excel version
    public byte[] generateExcelDocument(String templateName, Map<String, Object> data) throws IOException {
        File templateFile = new File(templatePath + templateName);
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(templateFile));

        for (Sheet sheet : workbook) {
            for (Row row : sheet) {
                for (Cell cell : row) {
                    if (cell.getCellType() == CellType.STRING) {
                        String text = cell.getStringCellValue();
                        for (Map.Entry<String, Object> entry : data.entrySet()) {
                            text = text.replace("{{" + entry.getKey() + "}}", entry.getValue().toString());
                        }
                        cell.setCellValue(text);
                    }
                }
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();
        return baos.toByteArray();
    }
}
