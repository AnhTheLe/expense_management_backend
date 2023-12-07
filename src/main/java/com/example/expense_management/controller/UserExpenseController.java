package com.example.expense_management.controller;

import com.example.expense_management.dto.UserDto.CurrentUserDto;
import com.example.expense_management.dto.UserExpenseRequest;
import com.example.expense_management.models.ResponseObject;
import com.example.expense_management.models.auth.UserEntity;
import com.example.expense_management.repositories.UserRepository;
import com.example.expense_management.services.UserExpenseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.example.expense_management.utils.UserUtil.getCurrentUserId;

@RestController
@RequestMapping("/api/v1/user-expenses")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Slf4j
public class UserExpenseController {

    private final UserExpenseService userExpenseService;
    private final UserRepository userRepository;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAll(
            @RequestParam(defaultValue = "", name = "name") String name,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "", name = "start_date") String startDate,
            @RequestParam(defaultValue = "", name = "end_date") String endDate,
            @RequestParam(defaultValue = "", name = "note") String note,
            @RequestParam(defaultValue = "", name = "category_name") String categoryName
    ) {
        return userExpenseService.getAllUserExpenses(page, size, startDate, endDate, name, note, categoryName);
    }

    @PostMapping("")
    public ResponseEntity<ResponseObject> create(@RequestBody UserExpenseRequest userExpenseRequest) {
        return userExpenseService.createUserExpense(userExpenseRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> get(@PathVariable Integer id) {
        return userExpenseService.getUserExpenseDetail(id);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<ResponseObject> getByCategory(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size
    ) {
        return userExpenseService.getListUserExpenseByCategoryId(id, page, size);
    }

    @GetMapping("/statistical")
    public ResponseEntity<ResponseObject> getStatisticalByCategory(
            @RequestParam(defaultValue = "", name = "start_date") String startDate,
            @RequestParam(defaultValue = "", name = "end_date") String endDate
    ) {
        return userExpenseService.getUserExpenseStatisticalByCategoryAndTimeRange(startDate, endDate);
    }

    @GetMapping("/statistical-by-time")
    public ResponseEntity<ResponseObject> getStatisticalByTime(
            @RequestParam(defaultValue = "", name = "start_date") String startDate,
            @RequestParam(defaultValue = "", name = "end_date") String endDate
    ) {
        return userExpenseService.getUserExpenseStatisticalByTimeRange(startDate, endDate);
    }


}
