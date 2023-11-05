package com.example.expense_management.repositories;

import com.example.expense_management.models.UserExpenses;
import com.example.expense_management.models.auth.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserExpensesRepository extends JpaRepository<UserExpenses, Integer>, JpaSpecificationExecutor<UserExpenses> {

    Optional<UserExpenses> findById(Integer id);

    Optional<UserExpenses> findUserExpensesByIdAndUserEntity(Integer id, UserEntity userEntity);

    Page<UserExpenses> findUserExpensesByCategoryIdAndUserId(Integer categoryId, Integer userId, Pageable pageable);

    List<UserExpenses> findAllByUserIdAndCategoryId(Integer userId, Integer categoryId);

    List<UserExpenses> findAllByUserIdAndCreatedAtBetween(Integer userId, Date from, Date to);

    List<UserExpenses> findAllByCategoryId(Integer categoryId);

    Boolean existsByExpenseName(String expenseName);

    void deleteAllById(Integer id);


//    Page<UserExpenses> findAll(Specification<UserExpenses> spec, Pageable paging);
}
