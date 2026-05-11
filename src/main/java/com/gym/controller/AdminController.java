package com.gym.controller;

import com.gym.dto.UserDTO;
import com.gym.model.User;
import com.gym.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;

    // ✅ Constructor Injection
    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 🔥 Entity → DTO mapper (password hide)
    private UserDTO mapToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    // ✅ Only ADMIN access
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public String dashboard() {
        return "Admin Dashboard 🔥";
    }

    // 🔥 Get ALL Users (DTO)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // 🔥 Get Only Members (DTO)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/members")
    public List<UserDTO> getMembers() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getRole().equalsIgnoreCase("MEMBER"))
                .map(this::mapToDTO)
                .toList();
    }

    // 🔥 Get Only Trainers (DTO)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/trainers")
    public List<UserDTO> getTrainers() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getRole().equalsIgnoreCase("TRAINER"))
                .map(this::mapToDTO)
                .toList();
    }

    // 🔥 Get User by ID (DTO)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found ❌"));

        return mapToDTO(user);
    }

    // 🔥 Delete User
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "User deleted successfully ✅";
    }
}