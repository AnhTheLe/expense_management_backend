package com.example.expense_management.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "expense_categories")
public class ExpenseCategories extends BaseEntity {

    @Column(name = "category_name")
    private String categoryName;


    @OneToMany(mappedBy = "expenseCategory")
    private List<UserExpenses> userExpensesList;
}
