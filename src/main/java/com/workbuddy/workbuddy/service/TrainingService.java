package com.workbuddy.workbuddy.service;

import com.workbuddy.workbuddy.model.TrainingRecord;
import com.workbuddy.workbuddy.storage.ExcelStorageHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrainingService {

    private static final String FILE     = "training.xlsx";
    private static final String SHEET    = "Training";
    private static final String[] HEADERS = {
        "ID", "Title", "Description", "Trainer",
        "Start Date", "End Date", "Participants", "Status"
    };

    @Autowired
    private ExcelStorageHelper storage;

    public List<TrainingRecord> getAll() throws IOException {
        List<TrainingRecord> list = new ArrayList<>();
        XSSFWorkbook wb = storage.openOrCreate(FILE);
        XSSFSheet sheet = wb.getSheet(SHEET);
        if (sheet != null) {
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) list.add(fromRow(row));
            }
        }
        wb.close();
        return list;
    }

    public TrainingRecord getById(String id) throws IOException {
        return getAll().stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
    }

    public TrainingRecord create(TrainingRecord record) throws IOException {
        record.setId(storage.generateId());
        if (record.getStatus() == null || record.getStatus().isBlank()) record.setStatus("Upcoming");
        XSSFWorkbook wb = storage.openOrCreate(FILE);
        XSSFSheet sheet = wb.getSheet(SHEET);
        if (sheet == null) {
            sheet = wb.createSheet(SHEET);
            writeHeader(sheet);
        }
        toRow(record, sheet.createRow(sheet.getPhysicalNumberOfRows()));
        storage.save(wb, FILE);
        return record;
    }

    public TrainingRecord update(String id, TrainingRecord updated) throws IOException {
        List<TrainingRecord> all = getAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equals(id)) {
                updated.setId(id);
                all.set(i, updated);
                rewrite(all);
                return updated;
            }
        }
        return null;
    }

    public boolean delete(String id) throws IOException {
        List<TrainingRecord> all = getAll();
        boolean removed = all.removeIf(t -> t.getId().equals(id));
        if (removed) rewrite(all);
        return removed;
    }

    private void rewrite(List<TrainingRecord> list) throws IOException {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(SHEET);
        writeHeader(sheet);
        int i = 1;
        for (TrainingRecord t : list) toRow(t, sheet.createRow(i++));
        storage.save(wb, FILE);
    }

    private TrainingRecord fromRow(Row row) {
        TrainingRecord t = new TrainingRecord();
        t.setId(storage.getCellValue(row, 0));
        t.setTitle(storage.getCellValue(row, 1));
        t.setDescription(storage.getCellValue(row, 2));
        t.setTrainer(storage.getCellValue(row, 3));
        t.setStartDate(storage.getCellValue(row, 4));
        t.setEndDate(storage.getCellValue(row, 5));
        t.setParticipants(storage.getCellValue(row, 6));
        t.setStatus(storage.getCellValue(row, 7));
        return t;
    }

    private void toRow(TrainingRecord t, Row row) {
        row.createCell(0).setCellValue(t.getId());
        row.createCell(1).setCellValue(t.getTitle());
        row.createCell(2).setCellValue(t.getDescription());
        row.createCell(3).setCellValue(t.getTrainer());
        row.createCell(4).setCellValue(t.getStartDate());
        row.createCell(5).setCellValue(t.getEndDate());
        row.createCell(6).setCellValue(t.getParticipants() != null ? t.getParticipants() : "");
        row.createCell(7).setCellValue(t.getStatus());
    }

    private void writeHeader(XSSFSheet sheet) {
        Row h = sheet.createRow(0);
        for (int i = 0; i < HEADERS.length; i++) h.createCell(i).setCellValue(HEADERS[i]);
    }
}
