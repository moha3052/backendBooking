package com.example.backendbooking2.Repository;

import com.example.backendbooking2.Entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {
    @Query("SELECT en FROM Admin en WHERE en.name = :name OR en.email = :email")
    Optional<Admin> findByNameOrEmail(@Param("name") String name,@Param("email")String email);
}
