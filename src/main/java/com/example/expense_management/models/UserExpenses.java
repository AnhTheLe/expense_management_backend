package com.example.expense_management.models;

import com.example.expense_management.models.auth.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="user_expenses")
public class UserExpenses extends BaseEntity{

    @Column(columnDefinition = "integer default 0")
    private int amount;

    private String note;

    @Column(name = "expense_name")
    private String expenseName;

    @Column(name = "user_id", insertable=false, updatable=false)
    private Integer userId;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "expense_date")
    private Date expenseDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "category_id", insertable=false, updatable=false)
    @JsonIgnore
    private ExpenseCategories expenseCategory;

}
