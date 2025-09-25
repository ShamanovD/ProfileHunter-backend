package com.example.profilehunter.service.auth.repository;

import com.example.profilehunter.model.database.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsernameOrEmailOrPhone(String username, String email, String phone);

    Boolean existsByUsernameOrEmailOrPhone(String username, String email, String phone);

}
