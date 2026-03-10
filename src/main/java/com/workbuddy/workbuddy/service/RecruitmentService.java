package com.workbuddy.workbuddy.service;

import com.workbuddy.workbuddy.model.RecruitmentPost;
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
public class RecruitmentService {

    private static final String FILE     = "recruitment.xlsx";
    private static final String SHEET    = "Recruitment";
    private static final String[] HEADERS = {
        "ID", "Title", "Department", "Description", "Location",
        "Type", "Status", "Posted Date", "Closing Date", "Applicants"
    };

    @Autowired
    private ExcelStorageHelper storage;

    public List<RecruitmentPost> getAll() throws IOException {
        List<RecruitmentPost> list = new ArrayList<>();
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

    public RecruitmentPost getById(String id) throws IOException {
        return getAll().stream().filter(r -> r.getId().equals(id)).findFirst().orElse(null);
    }

    public RecruitmentPost create(RecruitmentPost post) throws IOException {
        post.setId(storage.generateId());
        if (post.getStatus() == null || post.getStatus().isBlank()) post.setStatus("Open");
        if (post.getPostedDate() == null || post.getPostedDate().isBlank())
            post.setPostedDate(LocalDate.now().toString());
        XSSFWorkbook wb = storage.openOrCreate(FILE);
        XSSFSheet sheet = wb.getSheet(SHEET);
        if (sheet == null) {
            sheet = wb.createSheet(SHEET);
            writeHeader(sheet);
        }
        toRow(post, sheet.createRow(sheet.getPhysicalNumberOfRows()));
        storage.save(wb, FILE);
        return post;
    }

    public RecruitmentPost update(String id, RecruitmentPost updated) throws IOException {
        List<RecruitmentPost> all = getAll();
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
        List<RecruitmentPost> all = getAll();
        boolean removed = all.removeIf(r -> r.getId().equals(id));
        if (removed) rewrite(all);
        return removed;
    }

    private void rewrite(List<RecruitmentPost> list) throws IOException {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(SHEET);
        writeHeader(sheet);
        int i = 1;
        for (RecruitmentPost r : list) toRow(r, sheet.createRow(i++));
        storage.save(wb, FILE);
    }

    private RecruitmentPost fromRow(Row row) {
        RecruitmentPost r = new RecruitmentPost();
        r.setId(storage.getCellValue(row, 0));
        r.setTitle(storage.getCellValue(row, 1));
        r.setDepartment(storage.getCellValue(row, 2));
        r.setDescription(storage.getCellValue(row, 3));
        r.setLocation(storage.getCellValue(row, 4));
        r.setType(storage.getCellValue(row, 5));
        r.setStatus(storage.getCellValue(row, 6));
        r.setPostedDate(storage.getCellValue(row, 7));
        r.setClosingDate(storage.getCellValue(row, 8));
        r.setApplicants((int) storage.getNumericValue(row, 9));
        return r;
    }

    private void toRow(RecruitmentPost r, Row row) {
        row.createCell(0).setCellValue(r.getId());
        row.createCell(1).setCellValue(r.getTitle());
        row.createCell(2).setCellValue(r.getDepartment());
        row.createCell(3).setCellValue(r.getDescription());
        row.createCell(4).setCellValue(r.getLocation());
        row.createCell(5).setCellValue(r.getType());
        row.createCell(6).setCellValue(r.getStatus());
        row.createCell(7).setCellValue(r.getPostedDate());
        row.createCell(8).setCellValue(r.getClosingDate() != null ? r.getClosingDate() : "");
        row.createCell(9).setCellValue(r.getApplicants());
    }

    private void writeHeader(XSSFSheet sheet) {
        Row h = sheet.createRow(0);
        for (int i = 0; i < HEADERS.length; i++) h.createCell(i).setCellValue(HEADERS[i]);
    }
}
