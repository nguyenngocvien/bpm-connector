package com.bpm.core.doc.infrastructure;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class WordHelper {
	public static byte[] generateDocument(String templateName, String templatePath, Map<String, Object> data) throws IOException {
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
}
