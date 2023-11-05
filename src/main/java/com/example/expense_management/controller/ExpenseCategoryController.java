package com.example.expense_management.controller;


import com.example.expense_management.dto.CategoryRequest;
import com.example.expense_management.models.ResponseObject;
import com.example.expense_management.services.ExpenseCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class ExpenseCategoryController {

    private final ExpenseCategoryService expenseCategoryService;

    @PostMapping("")
    public ResponseEntity<ResponseObject> createCategory(@RequestBody CategoryRequest name) {
        return expenseCategoryService.createExpenseCategory(name);
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAll() {
        return expenseCategoryService.getAllCategory();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateCategory(@PathVariable Integer id, @RequestBody CategoryRequest name) {
        return expenseCategoryService.updateExpenseCategory(id, name);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteCategory(@PathVariable Integer id) {
        return expenseCategoryService.deleteById(id);
    }

    @DeleteMapping("/delete-by-list-ids")
    public ResponseEntity<ResponseObject> deleteByIds(@RequestBody List ids) {
        return expenseCategoryService.deleteByListIds(ids);
    }
}

