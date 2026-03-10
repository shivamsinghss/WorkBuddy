package com.workbuddy.workbuddy.controller;

import com.workbuddy.workbuddy.model.TrainingRecord;
import com.workbuddy.workbuddy.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/training")
public class TrainingController {

    @Autowired
    private TrainingService service;

    @GetMapping
    public ResponseEntity<List<TrainingRecord>> getAll() throws IOException {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingRecord> getById(@PathVariable String id) throws IOException {
        TrainingRecord t = service.getById(id);
        return t != null ? ResponseEntity.ok(t) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<TrainingRecord> create(@RequestBody TrainingRecord record) throws IOException {
        return ResponseEntity.ok(service.create(record));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainingRecord> update(@PathVariable String id,
                                                 @RequestBody TrainingRecord record) throws IOException {
        TrainingRecord updated = service.update(id, record);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) throws IOException {
        return service.delete(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
