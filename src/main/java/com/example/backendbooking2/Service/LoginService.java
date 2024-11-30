package com.example.backendbooking2.Service;

import com.example.backendbooking2.DTO.LoginDto;
import com.example.backendbooking2.Entity.Admin;
import com.example.backendbooking2.Repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LoginService {
    private final AdminRepository adminData;
    private final BCryptPasswordEncoder passwordEncoder;

    public boolean authCredentials(LoginDto loginDto){
       String email = loginDto.getEmail();
       String name = loginDto.getName();
       String rawPassword= loginDto.getPassword();
        Optional<Admin> adminEmailorName= adminData.findByNameOrEmail(name, email);
        if (adminEmailorName.isEmpty())
        {
            return false;
        }
        Admin admin = adminEmailorName.get();
       return passwordEncoder.matches(rawPassword,admin.getPassword());
    }
}
