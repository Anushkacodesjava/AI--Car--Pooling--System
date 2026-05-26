package com.example.demo.repository;
import com.example.demo.model.PoolUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface PoolUserRepository extends JpaRepository<PoolUser, Long> {
    Optional<PoolUser> findByEmail(String email);
    List<PoolUser> findByRole(String role);
    List<PoolUser> findByIsActiveTrue();
    @Query("SELECT COUNT(u) FROM PoolUser u WHERE u.isActive = true") long countActive();
}
