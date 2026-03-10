package com.workbuddy.workbuddy.controller;

import com.workbuddy.workbuddy.model.AttendanceRecord;
import com.workbuddy.workbuddy.model.LeaveBalance;
import com.workbuddy.workbuddy.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService service;

    /** GET today's attendance record for one employee */
    @GetMapping("/today/{employeeId}")
    public ResponseEntity<AttendanceRecord> getToday(@PathVariable String employeeId) throws IOException {
        AttendanceRecord rec = service.getTodayRecord(employeeId);
        return rec != null ? ResponseEntity.ok(rec) : ResponseEntity.noContent().build();
    }

    /** GET all attendance records for one employee (history) */
    @GetMapping("/history/{employeeId}")
    public ResponseEntity<List<AttendanceRecord>> getHistory(@PathVariable String employeeId) throws IOException {
        return ResponseEntity.ok(service.getHistory(employeeId));
    }

    /** GET all employees' attendance for today (admin) */
    @GetMapping("/all")
    public ResponseEntity<List<AttendanceRecord>> getAllToday() throws IOException {
        return ResponseEntity.ok(service.getAllToday());
    }

    /** GET leave balance for employee (auto-creates if missing) */
    @GetMapping("/balance/{employeeId}")
    public ResponseEntity<LeaveBalance> getBalance(@PathVariable String employeeId,
                                                   @RequestParam(defaultValue = "") String name) throws IOException {
        return ResponseEntity.ok(service.getOrCreateBalance(employeeId, name));
    }

    /** POST mark attendance: Present | WFH | Absent */
    @PostMapping("/mark")
    public ResponseEntity<?> mark(@RequestBody Map<String, String> body) throws IOException {
        String employeeId   = body.get("employeeId");
        String employeeName = body.getOrDefault("employeeName", "");
        String type         = body.get("type");
        if (employeeId == null || type == null)
            return ResponseEntity.badRequest().body("employeeId and type are required");
        try {
            AttendanceRecord rec = service.mark(employeeId, employeeName, type);
            return ResponseEntity.ok(rec);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /** POST punch in */
    @PostMapping("/punch-in")
    public ResponseEntity<AttendanceRecord> punchIn(@RequestBody Map<String, String> body) throws IOException {
        String employeeId   = body.get("employeeId");
        String employeeName = body.getOrDefault("employeeName", "");
        return ResponseEntity.ok(service.punchIn(employeeId, employeeName));
    }

    /** POST punch out */
    @PostMapping("/punch-out")
    public ResponseEntity<?> punchOut(@RequestBody Map<String, String> body) throws IOException {
        String employeeId = body.get("employeeId");
        AttendanceRecord rec = service.punchOut(employeeId);
        return rec != null ? ResponseEntity.ok(rec) : ResponseEntity.notFound().build();
    }

    /**
     * POST add credits — admin only.
     * Body: { "employeeId": "all" | "<id>", "employeeName": "...", "wfhCredits": 10, "leaveCredits": 5 }
     * Set wfhCredits or leaveCredits to 0 to skip that type.
     */
    @PostMapping("/add-credits")
    public ResponseEntity<String> addCredits(@RequestBody Map<String, Object> body) throws IOException {
        String employeeId   = (String) body.getOrDefault("employeeId", "all");
        String employeeName = (String) body.getOrDefault("employeeName", "");
        int wfhCredits   = body.get("wfhCredits")   != null ? ((Number) body.get("wfhCredits")).intValue()   : 10;
        int leaveCredits = body.get("leaveCredits") != null ? ((Number) body.get("leaveCredits")).intValue() : 5;
        service.addCredits(employeeId, employeeName, wfhCredits, leaveCredits);
        String target = "all".equalsIgnoreCase(employeeId) ? "all employees" : "employee " + employeeId;
        return ResponseEntity.ok("Credits added to " + target +
            " (WFH +" + wfhCredits + ", Leave +" + leaveCredits + ")");
    }
}
