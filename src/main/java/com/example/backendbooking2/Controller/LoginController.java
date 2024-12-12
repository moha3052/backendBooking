package com.example.backendbooking2.Controller;

import com.example.backendbooking2.DTO.AuthToken;
import com.example.backendbooking2.DTO.LoginDto;
import com.example.backendbooking2.Entity.User;
import com.example.backendbooking2.Service.LoginService;
import com.example.backendbooking2.util.GenericResponse;
import com.example.backendbooking2.util.JWTutil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/authCred")
public class LoginController {
    private final LoginService loginService;
    private final JWTutil jwTutil;

    @PostMapping("/login")
    public ResponseEntity<GenericResponse<AuthToken>> login(@RequestBody @Valid LoginDto dto) {
        boolean authenticateLogin = loginService.authCredentials(dto);
        if (authenticateLogin) {
            User user = loginService.getUserOrEmail(dto.getName(), dto.getEmail());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new GenericResponse<>("Invalid Credentials", null));
            }
            // Generate JWT Token
            String jwtToken = jwTutil.generateToken(user.getEmail(), user.getRole().name());
            AuthToken authToken = new AuthToken(jwtToken);
            return ResponseEntity.ok(new GenericResponse<>(200, "Login Was Successful", authToken));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new GenericResponse<>("Invalid Credentials", null));
        }
    }



    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody LoginDto signUpData){
       return loginService.signUpUser(signUpData)
               .map(successFull -> ResponseEntity
                       .ok(new GenericResponse<>(201,"User Was Created Successfully!!",successFull)))
               .orElseGet(() -> ResponseEntity
                       .status(HttpStatus.NOT_ACCEPTABLE)
                       .body(new GenericResponse<>("Invalid Input", null)));
    }
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('EMPLOYEE')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCredentials(@PathVariable Long id, @RequestBody LoginDto updatedData){

        return loginService.updateUser(updatedData,id)
                .map(updation -> ResponseEntity
                        .ok(new GenericResponse<>("User Was Created Successfully!!",updation)))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new GenericResponse<>("Invalid Input", null)));
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id){
        try{
            loginService.deleteAccount(id);
            return ResponseEntity
                    .ok(new GenericResponse<>(200,"The User Was Deleted Successfully!",null));
        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GenericResponse<>("The User Was Not Found!", null));
        }
    }
}