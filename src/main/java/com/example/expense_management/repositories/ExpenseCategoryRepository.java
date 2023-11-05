package com.example.expense_management.repositories;

import com.example.expense_management.models.ExpenseCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategories, Integer>, JpaSpecificationExecutor<ExpenseCategories> {

    ExpenseCategories findByCategoryName(String name);
    ExpenseCategories findById(int id);


    @Query(value = "SELECT ec.* FROM ExpenseCategories ec " +
            "INNER JOIN UserExpenses ue ON ec.Category_ID = ue.Category_ID " +
            "WHERE ue.User_ID = :userId", nativeQuery = true)
    List<ExpenseCategories> findExpenseCategoriesByUserId(int userId);

    @Query(value = "SELECT ec.* FROM ExpenseCategories ec " +
            "INNER JOIN UserExpenses ue ON ec.Category_ID = ue.Category_ID " +
            "WHERE ue.User_ID = :userId AND ue.date BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<ExpenseCategories> findExpenseCategoriesByUserIdAndDateRange(int userId, Timestamp startDate, Timestamp endDate);
}
