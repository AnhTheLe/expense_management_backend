package com.example.expense_management.dto;

import com.example.expense_management.models.UserExpenses;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Builder
public class UserExpenseStatisticalByTimeResponse {
    private long totalAmount;
    private int totalExpense;
    private List<UserExpenses> lineItems;
}
