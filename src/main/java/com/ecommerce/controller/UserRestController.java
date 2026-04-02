package com.ecommerce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dto.AuthResponseDTO;
import com.ecommerce.dto.UserLoginRequestDTO;
import com.ecommerce.dto.UserRegisterRequestDTO;
import com.ecommerce.dto.UserResponseDTO;
import com.ecommerce.service.UserService;

@RestController
@RequestMapping("/auth/users")
public class UserRestController {

    private final UserService userService;

    //Constructor Injection
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    //REGISTER
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRegisterRequestDTO dto) {
        UserResponseDTO response = userService.register(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //LOGIN
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody UserLoginRequestDTO dto) {
        AuthResponseDTO response = userService.login(dto);
        return ResponseEntity.ok(response);
    }

    //GET USER BY ID
    @GetMapping("/get/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    //GET ALL USERS
    @GetMapping("/getAll")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    //UPDATE USER
    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UserRegisterRequestDTO dto) {

        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    //DELETE USER
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}