package com.workbuddy.workbuddy.controller;

import com.workbuddy.workbuddy.model.PayrollRecord;
import com.workbuddy.workbuddy.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/payroll")
public class PayrollController {

    @Autowired
    private PayrollService service;

    @GetMapping
    public ResponseEntity<List<PayrollRecord>> getAll() throws IOException {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PayrollRecord> getById(@PathVariable String id) throws IOException {
        PayrollRecord p = service.getById(id);
        return p != null ? ResponseEntity.ok(p) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<PayrollRecord> create(@RequestBody PayrollRecord record) throws IOException {
        return ResponseEntity.ok(service.create(record));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PayrollRecord> update(@PathVariable String id,
                                                @RequestBody PayrollRecord record) throws IOException {
        PayrollRecord updated = service.update(id, record);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<PayrollRecord> markPaid(@PathVariable String id) throws IOException {
        PayrollRecord p = service.markPaid(id);
        return p != null ? ResponseEntity.ok(p) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) throws IOException {
        return service.delete(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
