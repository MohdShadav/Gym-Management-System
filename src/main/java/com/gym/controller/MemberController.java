package com.gym.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    // ✅ All logged-in users
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String profile() {
        return "Member Profile";
    }

    // ✅ Only MEMBER
    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping("/workout")
    public String workoutPlan() {
        return "Your Workout Plan";
    }
}