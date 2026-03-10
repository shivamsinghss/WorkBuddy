package com.workbuddy.workbuddy.service;

import com.workbuddy.workbuddy.model.PerformanceReview;
import com.workbuddy.workbuddy.storage.ExcelStorageHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PerformanceService {

    private static final String FILE     = "performance.xlsx";
    private static final String SHEET    = "PerformanceReviews";
    private static final String[] HEADERS = {
        "ID", "Employee ID", "Employee Name", "Review Period",
        "Reviewer Name", "Rating", "Comments", "Goals", "Status", "Review Date"
    };

    @Autowired
    private ExcelStorageHelper storage;

    public List<PerformanceReview> getAll() throws IOException {
        List<PerformanceReview> list = new ArrayList<>();
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

    public PerformanceReview getById(String id) throws IOException {
        return getAll().stream().filter(r -> r.getId().equals(id)).findFirst().orElse(null);
    }

    public PerformanceReview create(PerformanceReview review) throws IOException {
        review.setId(storage.generateId());
        if (review.getStatus() == null || review.getStatus().isBlank()) review.setStatus("Pending");
        if (review.getReviewDate() == null || review.getReviewDate().isBlank())
            review.setReviewDate(LocalDate.now().toString());
        XSSFWorkbook wb = storage.openOrCreate(FILE);
        XSSFSheet sheet = wb.getSheet(SHEET);
        if (sheet == null) {
            sheet = wb.createSheet(SHEET);
            writeHeader(sheet);
        }
        toRow(review, sheet.createRow(sheet.getPhysicalNumberOfRows()));
        storage.save(wb, FILE);
        return review;
    }

    public PerformanceReview update(String id, PerformanceReview updated) throws IOException {
        List<PerformanceReview> all = getAll();
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
        List<PerformanceReview> all = getAll();
        boolean removed = all.removeIf(r -> r.getId().equals(id));
        if (removed) rewrite(all);
        return removed;
    }

    private void rewrite(List<PerformanceReview> list) throws IOException {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(SHEET);
        writeHeader(sheet);
        int i = 1;
        for (PerformanceReview r : list) toRow(r, sheet.createRow(i++));
        storage.save(wb, FILE);
    }

    private PerformanceReview fromRow(Row row) {
        PerformanceReview r = new PerformanceReview();
        r.setId(storage.getCellValue(row, 0));
        r.setEmployeeId(storage.getCellValue(row, 1));
        r.setEmployeeName(storage.getCellValue(row, 2));
        r.setReviewPeriod(storage.getCellValue(row, 3));
        r.setReviewerName(storage.getCellValue(row, 4));
        r.setRating((int) storage.getNumericValue(row, 5));
        r.setComments(storage.getCellValue(row, 6));
        r.setGoals(storage.getCellValue(row, 7));
        r.setStatus(storage.getCellValue(row, 8));
        r.setReviewDate(storage.getCellValue(row, 9));
        return r;
    }

    private void toRow(PerformanceReview r, Row row) {
        row.createCell(0).setCellValue(r.getId());
        row.createCell(1).setCellValue(r.getEmployeeId());
        row.createCell(2).setCellValue(r.getEmployeeName());
        row.createCell(3).setCellValue(r.getReviewPeriod());
        row.createCell(4).setCellValue(r.getReviewerName());
        row.createCell(5).setCellValue(r.getRating());
        row.createCell(6).setCellValue(r.getComments());
        row.createCell(7).setCellValue(r.getGoals());
        row.createCell(8).setCellValue(r.getStatus());
        row.createCell(9).setCellValue(r.getReviewDate());
    }

    private void writeHeader(XSSFSheet sheet) {
        Row h = sheet.createRow(0);
        for (int i = 0; i < HEADERS.length; i++) h.createCell(i).setCellValue(HEADERS[i]);
    }
}
