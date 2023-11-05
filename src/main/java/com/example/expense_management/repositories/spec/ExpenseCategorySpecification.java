package com.example.expense_management.repositories.spec;

import com.example.expense_management.models.ExpenseCategories;
import com.example.expense_management.models.UserExpenses;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class ExpenseCategorySpecification {
    public static Specification<ExpenseCategories> getCategoryByUserIs(Integer userId) {
        return (root, query, criteriaBuilder) -> {
            Join<ExpenseCategories, UserExpenses> userExpensesJoin = root.join("userExpensesList");
            Predicate userIdPredicate = criteriaBuilder.equal(userExpensesJoin.get("userId"), userId);
            return query.where(userIdPredicate).distinct(true).getRestriction();
        };
    }
}
