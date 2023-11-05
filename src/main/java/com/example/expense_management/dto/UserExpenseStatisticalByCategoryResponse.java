package com.example.expense_management.dto;

import com.example.expense_management.models.ExpenseCategories;
import com.example.expense_management.models.UserExpenses;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserExpenseStatisticalByCategoryResponse {

    private int totalExpense;

    private long totalAmount;

    private int totalCategory;

    private List<ExpenseCategories> categories;
    private List<UserExpenses>  userExpenses;

}
