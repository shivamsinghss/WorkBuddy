package com.workbuddy.workbuddy.controller;

import com.workbuddy.workbuddy.model.LeaveRequest;
import com.workbuddy.workbuddy.service.AttendanceService;
import com.workbuddy.workbuddy.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    @Autowired private LeaveService service;
    @Autowired private AttendanceService attendanceService;

    @GetMapping
    public ResponseEntity<List<LeaveRequest>> getAll() throws IOException {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveRequest> getById(@PathVariable String id) throws IOException {
        LeaveRequest l = service.getById(id);
        return l != null ? ResponseEntity.ok(l) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> submit(@RequestBody LeaveRequest request) throws IOException {
        boolean isWfh = "WFH".equalsIgnoreCase(request.getType());
        boolean isCompOff = "Comp Off".equalsIgnoreCase(request.getType());
        if (!isWfh && !isCompOff) {
            // Deduct 1 leave credit; reject submission if no credits remain
            boolean ok = attendanceService.deductLeave(request.getEmployeeId(), request.getEmployeeName());
            if (!ok) {
                return ResponseEntity.badRequest().body("No leave credits remaining");
            }
        }
        return ResponseEntity.ok(service.submit(request));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<LeaveRequest> approve(@PathVariable String id) throws IOException {
        LeaveRequest l = service.updateStatus(id, "Approved");
        return l != null ? ResponseEntity.ok(l) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<LeaveRequest> reject(@PathVariable String id) throws IOException {
        LeaveRequest l = service.updateStatus(id, "Rejected");
        if (l != null && !"WFH".equalsIgnoreCase(l.getType()) && !"Comp Off".equalsIgnoreCase(l.getType())) {
            // Restore the leave credit when rejected (only for non-WFH requests)
            attendanceService.restoreLeave(l.getEmployeeId(), l.getEmployeeName());
        }
        return l != null ? ResponseEntity.ok(l) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<LeaveRequest> updateStatus(@PathVariable String id,
                                                     @RequestBody Map<String, String> body) throws IOException {
        LeaveRequest l = service.updateStatus(id, body.get("status"));
        return l != null ? ResponseEntity.ok(l) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) throws IOException {
        return service.delete(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
