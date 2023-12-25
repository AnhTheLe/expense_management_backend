package com.example.expense_management.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserExpenseResponse{

        private Integer id;
        private Integer userId;
        private Integer categoryId;
        private Integer amount;
        private String expenseName;
        private String note;
        private String categoryName;
        private String categoryImage;
        private Date createdAt;
        private Date updatedAt;
        private Date expenseDate;
}
