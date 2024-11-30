package com.example.backendbooking2.Controller;

import com.example.backendbooking2.DTO.LoginDto;
import com.example.backendbooking2.Service.LoginService;
import com.example.backendbooking2.util.GenericResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/authCred")
public class AdminLoginController {
    private final LoginService loginService;
    @PostMapping("/login")
    public ResponseEntity<GenericResponse<LoginDto>> login(@RequestBody @Valid LoginDto dto)
    {
        boolean authenticateLogin = loginService.authCredentials(dto);
        if (authenticateLogin)
        {
            return ResponseEntity.ok(new GenericResponse<>(200,"Login Was SuccessFul",dto));
        }else
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericResponse<>( 401,"Invalid Credentials",null));
        }

    }
}
