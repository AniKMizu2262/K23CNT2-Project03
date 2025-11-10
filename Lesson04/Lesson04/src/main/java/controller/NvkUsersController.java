package controller;

import dto.NvkUsersDTO;
import entity.nvkUser;
import service.NvkUsersService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@AllArgsConstructor
@NoArgsConstructor
public class NvkUsersController {

    @Autowired
    NvkUsersService nvkUsersService;

    @GetMapping("/user-list")
    public List<nvkUser> getAllUsers() {
        return nvkUsersService.findAll();
    }

    @PostMapping("/user-add")
    public ResponseEntity<String> addUser(@Valid @RequestBody NvkUsersDTO user) {
        nvkUsersService.create(user);
        return ResponseEntity.ok("Users created successfully");
    }
}