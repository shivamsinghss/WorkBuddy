package com.workbuddy.workbuddy.service;

import com.workbuddy.workbuddy.model.LeaveRequest;
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
public class LeaveService {

    private static final String FILE     = "leave_requests.xlsx";
    private static final String SHEET    = "LeaveRequests";
    private static final String[] HEADERS = {
        "ID", "Employee ID", "Employee Name", "Type",
        "Start Date", "End Date", "Days", "Reason", "Status", "Applied Date"
    };

    @Autowired
    private ExcelStorageHelper storage;

    public List<LeaveRequest> getAll() throws IOException {
        List<LeaveRequest> list = new ArrayList<>();
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

    public LeaveRequest getById(String id) throws IOException {
        return getAll().stream().filter(l -> l.getId().equals(id)).findFirst().orElse(null);
    }

    public LeaveRequest submit(LeaveRequest request) throws IOException {
        request.setId(storage.generateId());
        request.setStatus("Pending");
        request.setAppliedDate(LocalDate.now().toString());
        XSSFWorkbook wb = storage.openOrCreate(FILE);
        XSSFSheet sheet = wb.getSheet(SHEET);
        if (sheet == null) {
            sheet = wb.createSheet(SHEET);
            writeHeader(sheet);
        }
        toRow(request, sheet.createRow(sheet.getPhysicalNumberOfRows()));
        storage.save(wb, FILE);
        return request;
    }

    public LeaveRequest updateStatus(String id, String status) throws IOException {
        List<LeaveRequest> all = getAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equals(id)) {
                all.get(i).setStatus(status);
                rewrite(all);
                return all.get(i);
            }
        }
        return null;
    }

    public boolean delete(String id) throws IOException {
        List<LeaveRequest> all = getAll();
        boolean removed = all.removeIf(l -> l.getId().equals(id));
        if (removed) rewrite(all);
        return removed;
    }

    private void rewrite(List<LeaveRequest> list) throws IOException {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(SHEET);
        writeHeader(sheet);
        int i = 1;
        for (LeaveRequest l : list) toRow(l, sheet.createRow(i++));
        storage.save(wb, FILE);
    }

    private LeaveRequest fromRow(Row row) {
        LeaveRequest l = new LeaveRequest();
        l.setId(storage.getCellValue(row, 0));
        l.setEmployeeId(storage.getCellValue(row, 1));
        l.setEmployeeName(storage.getCellValue(row, 2));
        l.setType(storage.getCellValue(row, 3));
        l.setStartDate(storage.getCellValue(row, 4));
        l.setEndDate(storage.getCellValue(row, 5));
        l.setDays((int) storage.getNumericValue(row, 6));
        l.setReason(storage.getCellValue(row, 7));
        l.setStatus(storage.getCellValue(row, 8));
        l.setAppliedDate(storage.getCellValue(row, 9));
        return l;
    }

    private void toRow(LeaveRequest l, Row row) {
        row.createCell(0).setCellValue(l.getId());
        row.createCell(1).setCellValue(l.getEmployeeId());
        row.createCell(2).setCellValue(l.getEmployeeName());
        row.createCell(3).setCellValue(l.getType());
        row.createCell(4).setCellValue(l.getStartDate());
        row.createCell(5).setCellValue(l.getEndDate());
        row.createCell(6).setCellValue(l.getDays());
        row.createCell(7).setCellValue(l.getReason());
        row.createCell(8).setCellValue(l.getStatus());
        row.createCell(9).setCellValue(l.getAppliedDate());
    }

    private void writeHeader(XSSFSheet sheet) {
        Row h = sheet.createRow(0);
        for (int i = 0; i < HEADERS.length; i++) h.createCell(i).setCellValue(HEADERS[i]);
    }
}
