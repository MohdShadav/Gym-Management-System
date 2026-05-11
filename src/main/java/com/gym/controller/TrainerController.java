package com.gym.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trainer")
public class TrainerController {

    // ✅ Trainer + Admin access
    @PreAuthorize("hasAnyRole('TRAINER','ADMIN')")
    @GetMapping("/dashboard")
    public String dashboard() {
        return "Trainer Dashboard";
    }

    // ✅ Assign workout
    @PreAuthorize("hasRole('TRAINER')")
    @PostMapping("/assign-workout")
    public String assignWorkout() {
        return "Workout Assigned";
    }
}