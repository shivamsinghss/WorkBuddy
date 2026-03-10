package com.workbuddy.workbuddy.service;

import com.workbuddy.workbuddy.model.PayrollRecord;
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
public class PayrollService {

    private static final String FILE     = "payroll.xlsx";
    private static final String SHEET    = "Payroll";
    private static final String[] HEADERS = {
        "ID", "Employee ID", "Employee Name", "Month", "Year",
        "Basic Salary", "Bonus", "Deductions", "Net Salary", "Status", "Paid Date"
    };

    @Autowired
    private ExcelStorageHelper storage;

    public List<PayrollRecord> getAll() throws IOException {
        List<PayrollRecord> list = new ArrayList<>();
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

    public PayrollRecord getById(String id) throws IOException {
        return getAll().stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    public PayrollRecord create(PayrollRecord record) throws IOException {
        record.setId(storage.generateId());
        if (record.getStatus() == null || record.getStatus().isBlank()) record.setStatus("Pending");
        record.setNetSalary(record.getBasicSalary() + record.getBonus() - record.getDeductions());
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

    public PayrollRecord markPaid(String id) throws IOException {
        List<PayrollRecord> all = getAll();
        for (PayrollRecord p : all) {
            if (p.getId().equals(id)) {
                p.setStatus("Paid");
                p.setPaidDate(LocalDate.now().toString());
                rewrite(all);
                return p;
            }
        }
        return null;
    }

    public PayrollRecord update(String id, PayrollRecord updated) throws IOException {
        List<PayrollRecord> all = getAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equals(id)) {
                updated.setId(id);
                updated.setNetSalary(updated.getBasicSalary() + updated.getBonus() - updated.getDeductions());
                all.set(i, updated);
                rewrite(all);
                return updated;
            }
        }
        return null;
    }

    public boolean delete(String id) throws IOException {
        List<PayrollRecord> all = getAll();
        boolean removed = all.removeIf(p -> p.getId().equals(id));
        if (removed) rewrite(all);
        return removed;
    }

    private void rewrite(List<PayrollRecord> list) throws IOException {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(SHEET);
        writeHeader(sheet);
        int i = 1;
        for (PayrollRecord p : list) toRow(p, sheet.createRow(i++));
        storage.save(wb, FILE);
    }

    private PayrollRecord fromRow(Row row) {
        PayrollRecord p = new PayrollRecord();
        p.setId(storage.getCellValue(row, 0));
        p.setEmployeeId(storage.getCellValue(row, 1));
        p.setEmployeeName(storage.getCellValue(row, 2));
        p.setMonth(storage.getCellValue(row, 3));
        p.setYear((int) storage.getNumericValue(row, 4));
        p.setBasicSalary(storage.getNumericValue(row, 5));
        p.setBonus(storage.getNumericValue(row, 6));
        p.setDeductions(storage.getNumericValue(row, 7));
        p.setNetSalary(storage.getNumericValue(row, 8));
        p.setStatus(storage.getCellValue(row, 9));
        p.setPaidDate(storage.getCellValue(row, 10));
        return p;
    }

    private void toRow(PayrollRecord p, Row row) {
        row.createCell(0).setCellValue(p.getId());
        row.createCell(1).setCellValue(p.getEmployeeId());
        row.createCell(2).setCellValue(p.getEmployeeName());
        row.createCell(3).setCellValue(p.getMonth());
        row.createCell(4).setCellValue(p.getYear());
        row.createCell(5).setCellValue(p.getBasicSalary());
        row.createCell(6).setCellValue(p.getBonus());
        row.createCell(7).setCellValue(p.getDeductions());
        row.createCell(8).setCellValue(p.getNetSalary());
        row.createCell(9).setCellValue(p.getStatus());
        row.createCell(10).setCellValue(p.getPaidDate() != null ? p.getPaidDate() : "");
    }

    private void writeHeader(XSSFSheet sheet) {
        Row h = sheet.createRow(0);
        for (int i = 0; i < HEADERS.length; i++) h.createCell(i).setCellValue(HEADERS[i]);
    }
}
