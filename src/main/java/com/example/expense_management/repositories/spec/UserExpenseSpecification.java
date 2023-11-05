package com.example.expense_management.repositories.spec;

import com.example.expense_management.models.ExpenseCategories;
import com.example.expense_management.models.UserExpenses;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class UserExpenseSpecification {
    public static Specification<UserExpenses> hasNameLike(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.<String>get("expenseName"), "%" + name + "%");
    }

    public static Specification<UserExpenses> hasNoteLike(String note) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.<String>get("note"), "%" + note + "%");
    }

    public static Specification<UserExpenses> hasCategoryNameLike(String categoryName) {
        return (root, query, criteriaBuilder) -> {
            Join<ExpenseCategories, UserExpenses> authorsBook = root.join("expenseCategory");
            return criteriaBuilder.like(authorsBook.get("categoryName"), "%" + categoryName + "%");
        };
    }

}
