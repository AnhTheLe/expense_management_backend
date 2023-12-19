package com.example.expense_management.services;

import com.example.expense_management.dto.CategoryRequest;
import com.example.expense_management.models.ExpenseCategories;
import com.example.expense_management.models.ResponseObject;
import com.example.expense_management.models.UserExpenses;
import com.example.expense_management.repositories.ExpenseCategoryRepository;
import com.example.expense_management.repositories.UserExpensesRepository;
import com.example.expense_management.repositories.spec.*;
import com.example.expense_management.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
public class ExpenseCategoryService {

    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final UserExpensesRepository userExpensesRepository;


    public List<ExpenseCategories> findExpenseCategoriesByUserIdAndDateRange(String startDate, String endDate) {
        List<ExpenseCategories> expenseCategories = null;

        Integer currentUserId = UserUtil.getCurrentUserId();
        Specification<ExpenseCategories> spec = where(null);

        if (!startDate.isEmpty()) {
            LocalDate date = LocalDate.parse(startDate);
            MySpecification esCategoriesStartDate = new MySpecification();
            esCategoriesStartDate.add(new SearchCriteria("createdAt", date, SearchOperation.DATE_START));
            spec = spec.and(esCategoriesStartDate);
        }

        if (!endDate.isEmpty()) {
            LocalDate date = LocalDate.parse(endDate);
            MySpecification esCategoriesEndDate = new MySpecification();
            esCategoriesEndDate.add(new SearchCriteria("createdAt", date, SearchOperation.DATE_END));
            spec = spec.and(esCategoriesEndDate);
        }

        spec = spec.and(ExpenseCategorySpecification.getCategoryByUserIs(currentUserId));

        expenseCategories = expenseCategoryRepository.findAll(spec);

        return expenseCategories;
    }

    public ResponseEntity<ResponseObject> createExpenseCategory(CategoryRequest expenseCategories) {
        ExpenseCategories newExpenseCategories = new ExpenseCategories();
        if (expenseCategoryRepository.findByCategoryName(expenseCategories.getName()) != null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("error", "Expenditure type name already exists", ""));
        }
        newExpenseCategories.setCategoryName(expenseCategories.getName());
        newExpenseCategories.setCategoryImage(expenseCategories.getImage());
        expenseCategoryRepository.save(newExpenseCategories);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseObject("success", "", expenseCategoryRepository.save(newExpenseCategories)));
    }

    public ResponseEntity<ResponseObject> updateExpenseCategory(Integer id, CategoryRequest expenseCategories) {
        ExpenseCategories expenseCategory = expenseCategoryRepository.findById(id).orElse(null);
        if (expenseCategory == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("error", "This type of spending does not exist", ""));
        }
        if (expenseCategoryRepository.findByCategoryName(expenseCategories.getName()) != null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("error", "This type of spending already exists", ""));
        }
        expenseCategory.setCategoryName(expenseCategories.getName());
        expenseCategory.setCategoryImage(expenseCategories.getImage());
        expenseCategoryRepository.save(expenseCategory);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "", expenseCategoryRepository.save(expenseCategory)));
    }

    public ResponseEntity<ResponseObject> getAllCategory() {
        List<ExpenseCategories> expenseCategories = expenseCategoryRepository.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "", expenseCategories));
    }


    public ResponseEntity<ResponseObject> deleteById(Integer id) {
        ExpenseCategories expenseCategory = expenseCategoryRepository.findById(id).orElse(null);
        if (expenseCategory == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("error", "This type of spending does not exist", ""));
        }
        List<UserExpenses> userExpenses = userExpensesRepository.findAllByCategoryId(id);
        for(UserExpenses userExpense : userExpenses) {
            userExpense.setCategoryId(null);
            userExpensesRepository.save(userExpense);
        }
        expenseCategoryRepository.delete(expenseCategory);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "Deleted data successfully", ""));
    }

    public ResponseEntity<ResponseObject> deleteByListIds(List<Integer> ids) {
        List<ExpenseCategories> expenseCategories = new ArrayList<>();
        for(Integer id : ids) {
            ExpenseCategories expenseCategory = expenseCategoryRepository.findById(id).orElse(null);
            if (expenseCategory != null) {
                expenseCategories.add(expenseCategory);
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject("error", "This type of spending does not exist", ""));
            }
        }

//        List<UserExpenses> userExpenses = userExpensesRepository.findAllByCategoryIdIn(ids);
        for(Integer id : ids) {
            List<UserExpenses> userExpenses = userExpensesRepository.findAllByCategoryId(id);
            for(UserExpenses userExpense : userExpenses) {
                userExpense.setCategoryId(null);
                userExpensesRepository.save(userExpense);
            }
        }
        expenseCategoryRepository.deleteAll(expenseCategories);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "Deleted data successfully", ""));
    }
}
