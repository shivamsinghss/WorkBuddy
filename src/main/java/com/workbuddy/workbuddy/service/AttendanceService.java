package com.workbuddy.workbuddy.service;

import com.workbuddy.workbuddy.model.AttendanceRecord;
import com.workbuddy.workbuddy.model.Employee;
import com.workbuddy.workbuddy.model.LeaveBalance;
import com.workbuddy.workbuddy.storage.ExcelStorageHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    private static final String ATT_FILE  = "attendance.xlsx";
    private static final String ATT_SHEET = "Attendance";
    private static final String[] ATT_HEADERS = {
        "ID", "Employee ID", "Employee Name", "Date", "Type",
        "Punch In", "Punch Out", "Month", "Year"
    };

    private static final String BAL_FILE  = "leave_balance.xlsx";
    private static final String BAL_SHEET = "LeaveBalance";
    private static final String[] BAL_HEADERS = {
        "ID", "Employee ID", "Employee Name", "Year", "Month",
        "WFH Credits Total", "WFH Used", "Leave Credits Total", "Leave Used"
    };

    @Autowired private ExcelStorageHelper storage;
    @Autowired private EmployeeService employeeService;

    // ─────────────── Attendance ───────────────

    public List<AttendanceRecord> getAllAttendance() throws IOException {
        List<AttendanceRecord> list = new ArrayList<>();
        XSSFWorkbook wb = storage.openOrCreate(ATT_FILE);
        XSSFSheet sheet = wb.getSheet(ATT_SHEET);
        if (sheet != null) {
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) list.add(attFromRow(row));
            }
        }
        wb.close();
        return list;
    }

    public AttendanceRecord getTodayRecord(String employeeId) throws IOException {
        String today = LocalDate.now().toString();
        return getAllAttendance().stream()
            .filter(r -> employeeId.equals(r.getEmployeeId()) && today.equals(r.getDate()))
            .findFirst().orElse(null);
    }

    public List<AttendanceRecord> getHistory(String employeeId) throws IOException {
        return getAllAttendance().stream()
            .filter(r -> employeeId.equals(r.getEmployeeId()))
            .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
            .collect(Collectors.toList());
    }

    public List<AttendanceRecord> getAllToday() throws IOException {
        String today = LocalDate.now().toString();
        return getAllAttendance().stream()
            .filter(r -> today.equals(r.getDate()))
            .collect(Collectors.toList());
    }

    public AttendanceRecord mark(String employeeId, String employeeName, String type) throws IOException {
        String today = LocalDate.now().toString();
        LocalDate now = LocalDate.now();
        List<AttendanceRecord> all = getAllAttendance();
        AttendanceRecord existing = all.stream()
            .filter(r -> employeeId.equals(r.getEmployeeId()) && today.equals(r.getDate()))
            .findFirst().orElse(null);

        String previousType = existing != null ? existing.getType() : null;

        // Adjust WFH balance
        if ("WFH".equals(type) && !"WFH".equals(previousType)) {
            LeaveBalance bal = getOrCreateBalance(employeeId, employeeName);
            if (bal.getWfhUsed() >= bal.getWfhCreditsTotal()) {
                throw new IllegalStateException("No WFH credits remaining for this month");
            }
            bal.setWfhUsed(bal.getWfhUsed() + 1);
            saveBalance(bal);
        } else if (!"WFH".equals(type) && "WFH".equals(previousType)) {
            LeaveBalance bal = getOrCreateBalance(employeeId, employeeName);
            bal.setWfhUsed(Math.max(0, bal.getWfhUsed() - 1));
            saveBalance(bal);
        }

        if (existing != null) {
            existing.setType(type);
            rewriteAttendance(all);
            return existing;
        } else {
            AttendanceRecord rec = new AttendanceRecord();
            rec.setId(storage.generateId());
            rec.setEmployeeId(employeeId);
            rec.setEmployeeName(employeeName);
            rec.setDate(today);
            rec.setType(type);
            rec.setPunchIn("");
            rec.setPunchOut("");
            rec.setMonth(now.getMonthValue());
            rec.setYear(now.getYear());
            appendAttendance(rec);
            return rec;
        }
    }

    public AttendanceRecord punchIn(String employeeId, String employeeName) throws IOException {
        String today = LocalDate.now().toString();
        String timeNow = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        LocalDate now = LocalDate.now();
        List<AttendanceRecord> all = getAllAttendance();
        AttendanceRecord existing = all.stream()
            .filter(r -> employeeId.equals(r.getEmployeeId()) && today.equals(r.getDate()))
            .findFirst().orElse(null);

        if (existing != null) {
            existing.setPunchIn(timeNow);
            rewriteAttendance(all);
            return existing;
        } else {
            AttendanceRecord rec = new AttendanceRecord();
            rec.setId(storage.generateId());
            rec.setEmployeeId(employeeId);
            rec.setEmployeeName(employeeName);
            rec.setDate(today);
            rec.setType("Present");
            rec.setPunchIn(timeNow);
            rec.setPunchOut("");
            rec.setMonth(now.getMonthValue());
            rec.setYear(now.getYear());
            appendAttendance(rec);
            return rec;
        }
    }

    public AttendanceRecord punchOut(String employeeId) throws IOException {
        String today = LocalDate.now().toString();
        String timeNow = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        List<AttendanceRecord> all = getAllAttendance();
        AttendanceRecord existing = all.stream()
            .filter(r -> employeeId.equals(r.getEmployeeId()) && today.equals(r.getDate()))
            .findFirst().orElse(null);

        if (existing != null) {
            existing.setPunchOut(timeNow);
            rewriteAttendance(all);
        }
        return existing;
    }

    // ─────────────── Leave Balance ───────────────

    public LeaveBalance getOrCreateBalance(String employeeId, String employeeName) throws IOException {
        LocalDate now = LocalDate.now();
        int year = now.getYear(), month = now.getMonthValue();
        List<LeaveBalance> all = getAllBalances();
        LeaveBalance found = all.stream()
            .filter(b -> employeeId.equals(b.getEmployeeId())
                      && b.getYear() == year && b.getMonth() == month)
            .findFirst().orElse(null);
        if (found != null) return found;

        // Auto-create with defaults
        LeaveBalance bal = new LeaveBalance();
        bal.setId(storage.generateId());
        bal.setEmployeeId(employeeId);
        bal.setEmployeeName(employeeName);
        bal.setYear(year);
        bal.setMonth(month);
        bal.setWfhCreditsTotal(10);
        bal.setWfhUsed(0);
        bal.setLeaveCreditsTotal(5);
        bal.setLeaveUsed(0);
        appendBalance(bal);
        return bal;
    }

    public List<LeaveBalance> getAllBalances() throws IOException {
        List<LeaveBalance> list = new ArrayList<>();
        XSSFWorkbook wb = storage.openOrCreate(BAL_FILE);
        XSSFSheet sheet = wb.getSheet(BAL_SHEET);
        if (sheet != null) {
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) list.add(balFromRow(row));
            }
        }
        wb.close();
        return list;
    }

    /** Deduct 1 leave credit. Returns false if no credits remain. */
    public boolean deductLeave(String employeeId, String employeeName) throws IOException {
        LeaveBalance bal = getOrCreateBalance(employeeId, employeeName);
        if (bal.getLeaveUsed() >= bal.getLeaveCreditsTotal()) return false;
        bal.setLeaveUsed(bal.getLeaveUsed() + 1);
        saveBalance(bal);
        return true;
    }

    /** Restore 1 leave credit (when leave is rejected). */
    public void restoreLeave(String employeeId, String employeeName) throws IOException {
        LeaveBalance bal = getOrCreateBalance(employeeId, employeeName);
        bal.setLeaveUsed(Math.max(0, bal.getLeaveUsed() - 1));
        saveBalance(bal);
    }

    /**
     * Add credits to a specific employee or all employees.
     * employeeId = "all" targets every active employee.
     */
    public void addCredits(String employeeId, String employeeName,
                           int wfhCredits, int leaveCredits) throws IOException {
        if ("all".equalsIgnoreCase(employeeId)) {
            List<Employee> employees = employeeService.getAll();
            for (Employee emp : employees) {
                applyCredits(emp.getId(), emp.getFirstName() + " " + emp.getLastName(),
                             wfhCredits, leaveCredits);
            }
        } else {
            applyCredits(employeeId, employeeName, wfhCredits, leaveCredits);
        }
    }

    private void applyCredits(String empId, String empName,
                               int wfhCredits, int leaveCredits) throws IOException {
        LeaveBalance bal = getOrCreateBalance(empId, empName);
        if (wfhCredits   > 0) bal.setWfhCreditsTotal(bal.getWfhCreditsTotal() + wfhCredits);
        if (leaveCredits > 0) bal.setLeaveCreditsTotal(bal.getLeaveCreditsTotal() + leaveCredits);
        saveBalance(bal);
    }

    // ─────────────── Persistence helpers ───────────────

    private void appendAttendance(AttendanceRecord rec) throws IOException {
        XSSFWorkbook wb = storage.openOrCreate(ATT_FILE);
        XSSFSheet sheet = wb.getSheet(ATT_SHEET);
        if (sheet == null) {
            sheet = wb.createSheet(ATT_SHEET);
            writeAttHeader(sheet);
        }
        attToRow(rec, sheet.createRow(sheet.getPhysicalNumberOfRows()));
        storage.save(wb, ATT_FILE);
    }

    private void rewriteAttendance(List<AttendanceRecord> list) throws IOException {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(ATT_SHEET);
        writeAttHeader(sheet);
        int i = 1;
        for (AttendanceRecord r : list) attToRow(r, sheet.createRow(i++));
        storage.save(wb, ATT_FILE);
    }

    private void appendBalance(LeaveBalance bal) throws IOException {
        XSSFWorkbook wb = storage.openOrCreate(BAL_FILE);
        XSSFSheet sheet = wb.getSheet(BAL_SHEET);
        if (sheet == null) {
            sheet = wb.createSheet(BAL_SHEET);
            writeBalHeader(sheet);
        }
        balToRow(bal, sheet.createRow(sheet.getPhysicalNumberOfRows()));
        storage.save(wb, BAL_FILE);
    }

    private void saveBalance(LeaveBalance updated) throws IOException {
        List<LeaveBalance> all = getAllBalances();
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equals(updated.getId())) {
                all.set(i, updated);
                found = true;
                break;
            }
        }
        if (!found) all.add(updated);
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(BAL_SHEET);
        writeBalHeader(sheet);
        int i = 1;
        for (LeaveBalance b : all) balToRow(b, sheet.createRow(i++));
        storage.save(wb, BAL_FILE);
    }

    // ─────────────── Row mappers ───────────────

    private AttendanceRecord attFromRow(Row row) {
        AttendanceRecord r = new AttendanceRecord();
        r.setId(storage.getCellValue(row, 0));
        r.setEmployeeId(storage.getCellValue(row, 1));
        r.setEmployeeName(storage.getCellValue(row, 2));
        r.setDate(storage.getCellValue(row, 3));
        r.setType(storage.getCellValue(row, 4));
        r.setPunchIn(storage.getCellValue(row, 5));
        r.setPunchOut(storage.getCellValue(row, 6));
        r.setMonth((int) storage.getNumericValue(row, 7));
        r.setYear((int) storage.getNumericValue(row, 8));
        return r;
    }

    private void attToRow(AttendanceRecord r, Row row) {
        row.createCell(0).setCellValue(r.getId());
        row.createCell(1).setCellValue(r.getEmployeeId());
        row.createCell(2).setCellValue(r.getEmployeeName());
        row.createCell(3).setCellValue(r.getDate());
        row.createCell(4).setCellValue(r.getType() != null ? r.getType() : "");
        row.createCell(5).setCellValue(r.getPunchIn() != null ? r.getPunchIn() : "");
        row.createCell(6).setCellValue(r.getPunchOut() != null ? r.getPunchOut() : "");
        row.createCell(7).setCellValue(r.getMonth());
        row.createCell(8).setCellValue(r.getYear());
    }

    private LeaveBalance balFromRow(Row row) {
        LeaveBalance b = new LeaveBalance();
        b.setId(storage.getCellValue(row, 0));
        b.setEmployeeId(storage.getCellValue(row, 1));
        b.setEmployeeName(storage.getCellValue(row, 2));
        b.setYear((int) storage.getNumericValue(row, 3));
        b.setMonth((int) storage.getNumericValue(row, 4));
        b.setWfhCreditsTotal((int) storage.getNumericValue(row, 5));
        b.setWfhUsed((int) storage.getNumericValue(row, 6));
        b.setLeaveCreditsTotal((int) storage.getNumericValue(row, 7));
        b.setLeaveUsed((int) storage.getNumericValue(row, 8));
        return b;
    }

    private void balToRow(LeaveBalance b, Row row) {
        row.createCell(0).setCellValue(b.getId());
        row.createCell(1).setCellValue(b.getEmployeeId());
        row.createCell(2).setCellValue(b.getEmployeeName());
        row.createCell(3).setCellValue(b.getYear());
        row.createCell(4).setCellValue(b.getMonth());
        row.createCell(5).setCellValue(b.getWfhCreditsTotal());
        row.createCell(6).setCellValue(b.getWfhUsed());
        row.createCell(7).setCellValue(b.getLeaveCreditsTotal());
        row.createCell(8).setCellValue(b.getLeaveUsed());
    }

    private void writeAttHeader(XSSFSheet sheet) {
        Row h = sheet.createRow(0);
        for (int i = 0; i < ATT_HEADERS.length; i++) h.createCell(i).setCellValue(ATT_HEADERS[i]);
    }

    private void writeBalHeader(XSSFSheet sheet) {
        Row h = sheet.createRow(0);
        for (int i = 0; i < BAL_HEADERS.length; i++) h.createCell(i).setCellValue(BAL_HEADERS[i]);
    }
}
