package com.workbuddy.workbuddy.controller;

import com.workbuddy.workbuddy.model.PerformanceReview;
import com.workbuddy.workbuddy.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/performance")
public class PerformanceController {

    @Autowired
    private PerformanceService service;

    @GetMapping
    public ResponseEntity<List<PerformanceReview>> getAll() throws IOException {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerformanceReview> getById(@PathVariable String id) throws IOException {
        PerformanceReview r = service.getById(id);
        return r != null ? ResponseEntity.ok(r) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<PerformanceReview> create(@RequestBody PerformanceReview review) throws IOException {
        return ResponseEntity.ok(service.create(review));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerformanceReview> update(@PathVariable String id,
                                                    @RequestBody PerformanceReview review) throws IOException {
        PerformanceReview updated = service.update(id, review);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) throws IOException {
        return service.delete(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
