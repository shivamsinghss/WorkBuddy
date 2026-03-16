package com.workbuddy.workbuddy.storage;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Component
public class ExcelStorageHelper {

    @Value("${workbuddy.data.path:./data}")
    private String dataPath;

    @PostConstruct
    public void init() throws IOException {
        // Resolve to absolute path so data location is stable regardless of
        // which directory the JVM is started from (e.g. build/libs vs project root).
        File dir = new File(dataPath).getCanonicalFile();
        dir.mkdirs();
        dataPath = dir.getAbsolutePath();
    }

    public XSSFWorkbook openOrCreate(String fileName) throws IOException {
        File file = new File(dataPath, fileName);
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                return new XSSFWorkbook(fis);
            }
        }
        return new XSSFWorkbook();
    }

    public void save(XSSFWorkbook workbook, String fileName) throws IOException {
        File file = new File(dataPath, fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
        workbook.close();
    }

    public String getCellValue(Row row, int index) {
        if (row == null) return "";
        Cell cell = row.getCell(index);
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING  -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                double d = cell.getNumericCellValue();
                yield d == Math.floor(d) ? String.valueOf((long) d) : String.valueOf(d);
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default      -> "";
        };
    }

    public double getNumericValue(Row row, int index) {
        if (row == null) return 0;
        Cell cell = row.getCell(index);
        if (cell == null) return 0;
        return switch (cell.getCellType()) {
            case NUMERIC -> cell.getNumericCellValue();
            case STRING  -> {
                try { yield Double.parseDouble(cell.getStringCellValue()); }
                catch (NumberFormatException e) { yield 0; }
            }
            default -> 0;
        };
    }

    public String generateId() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
