package com.example.demo.repository;
import com.example.demo.model.SocialImpact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SocialImpactRepository extends JpaRepository<SocialImpact, Long> {
    @Query("SELECT SUM(s.carbonSaved) FROM SocialImpact s") Double getTotalCarbonSaved();
    @Query("SELECT SUM(s.fuelSaved) FROM SocialImpact s") Double getTotalFuelSaved();
    @Query("SELECT SUM(s.ecoPoints) FROM SocialImpact s") Long getTotalEcoPoints();
    @Query("SELECT SUM(s.moneySaved) FROM SocialImpact s") Double getTotalMoneySaved();
}
