package com.workbuddy.workbuddy.controller;

import com.workbuddy.workbuddy.model.RecruitmentPost;
import com.workbuddy.workbuddy.service.RecruitmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/recruitment")
public class RecruitmentController {

    @Autowired
    private RecruitmentService service;

    @GetMapping
    public ResponseEntity<List<RecruitmentPost>> getAll() throws IOException {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecruitmentPost> getById(@PathVariable String id) throws IOException {
        RecruitmentPost r = service.getById(id);
        return r != null ? ResponseEntity.ok(r) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<RecruitmentPost> create(@RequestBody RecruitmentPost post) throws IOException {
        return ResponseEntity.ok(service.create(post));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecruitmentPost> update(@PathVariable String id,
                                                  @RequestBody RecruitmentPost post) throws IOException {
        RecruitmentPost updated = service.update(id, post);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<RecruitmentPost> close(@PathVariable String id) throws IOException {
        RecruitmentPost post = service.getById(id);
        if (post == null) return ResponseEntity.notFound().build();
        post.setStatus("Closed");
        RecruitmentPost updated = service.update(id, post);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) throws IOException {
        return service.delete(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
