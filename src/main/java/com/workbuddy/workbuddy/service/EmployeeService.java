package com.workbuddy.workbuddy.service;

import com.workbuddy.workbuddy.model.Employee;
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
public class EmployeeService {

    private static final String FILE     = "employees.xlsx";
    private static final String SHEET    = "Employees";
    private static final String[] HEADERS = {
        "ID", "First Name", "Last Name", "Email", "Phone",
        "Department", "Position", "Join Date", "Salary", "Status",
        "Username", "Password"
    };

    @Autowired
    private ExcelStorageHelper storage;

    public List<Employee> getAll() throws IOException {
        List<Employee> list = new ArrayList<>();
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

    public Employee getById(String id) throws IOException {
        return getAll().stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
    }

    public Employee create(Employee employee) throws IOException {
        employee.setId(storage.generateId());
        XSSFWorkbook wb = storage.openOrCreate(FILE);
        XSSFSheet sheet = wb.getSheet(SHEET);
        if (sheet == null) {
            sheet = wb.createSheet(SHEET);
            writeHeader(sheet);
        }
        toRow(employee, sheet.createRow(sheet.getPhysicalNumberOfRows()));
        storage.save(wb, FILE);
        return employee;
    }

    public Employee update(String id, Employee updated) throws IOException {
        List<Employee> all = getAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equals(id)) {
                Employee existing = all.get(i);
                // preserve credentials when not supplied by the edit form
                if (updated.getUsername() == null || updated.getUsername().isEmpty()) {
                    updated.setUsername(existing.getUsername());
                }
                if (updated.getPassword() == null || updated.getPassword().isEmpty()) {
                    updated.setPassword(existing.getPassword());
                }
                updated.setId(id);
                all.set(i, updated);
                rewrite(all);
                return updated;
            }
        }
        return null;
    }

    public boolean delete(String id) throws IOException {
        List<Employee> all = getAll();
        boolean removed = all.removeIf(e -> e.getId().equals(id));
        if (removed) rewrite(all);
        return removed;
    }

    private void rewrite(List<Employee> list) throws IOException {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(SHEET);
        writeHeader(sheet);
        int i = 1;
        for (Employee e : list) toRow(e, sheet.createRow(i++));
        storage.save(wb, FILE);
    }

    private Employee fromRow(Row row) {
        Employee e = new Employee();
        e.setId(storage.getCellValue(row, 0));
        e.setFirstName(storage.getCellValue(row, 1));
        e.setLastName(storage.getCellValue(row, 2));
        e.setEmail(storage.getCellValue(row, 3));
        e.setPhone(storage.getCellValue(row, 4));
        e.setDepartment(storage.getCellValue(row, 5));
        e.setPosition(storage.getCellValue(row, 6));
        e.setJoinDate(storage.getCellValue(row, 7));
        e.setSalary(storage.getNumericValue(row, 8));
        e.setStatus(storage.getCellValue(row, 9));
        e.setUsername(storage.getCellValue(row, 10));
        e.setPassword(storage.getCellValue(row, 11));
        return e;
    }

    private void toRow(Employee e, Row row) {
        row.createCell(0).setCellValue(e.getId()         != null ? e.getId()         : "");
        row.createCell(1).setCellValue(e.getFirstName()  != null ? e.getFirstName()  : "");
        row.createCell(2).setCellValue(e.getLastName()   != null ? e.getLastName()   : "");
        row.createCell(3).setCellValue(e.getEmail()      != null ? e.getEmail()      : "");
        row.createCell(4).setCellValue(e.getPhone()      != null ? e.getPhone()      : "");
        row.createCell(5).setCellValue(e.getDepartment() != null ? e.getDepartment() : "");
        row.createCell(6).setCellValue(e.getPosition()   != null ? e.getPosition()   : "");
        row.createCell(7).setCellValue(e.getJoinDate()   != null ? e.getJoinDate()   : "");
        row.createCell(8).setCellValue(e.getSalary());
        row.createCell(9).setCellValue(e.getStatus()     != null ? e.getStatus()     : "");
        row.createCell(10).setCellValue(e.getUsername()  != null ? e.getUsername()   : "");
        row.createCell(11).setCellValue(e.getPassword()  != null ? e.getPassword()   : "");
    }

    private void writeHeader(XSSFSheet sheet) {
        Row h = sheet.createRow(0);
        for (int i = 0; i < HEADERS.length; i++) h.createCell(i).setCellValue(HEADERS[i]);
    }
}
