package com.example.expense_management.repositories;

import com.example.expense_management.models.auth.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<List<UserEntity>> findByUsernameContainingIgnoreCase(String name);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findById(Integer id);

    Optional<UserEntity> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByPhone(String phone);

    ArrayList<UserEntity> findByIdIn(List<Long> listId);

    Optional<UserEntity> findUserByUsernameOrEmail(String username, String email);
}
