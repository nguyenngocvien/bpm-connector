package com.bpm.core.doc.infrastructure;

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

public class ExcelHelper {
	public static byte[] generateDocument(String templateName, String templatePath, Map<String, Object> data) throws IOException {
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
