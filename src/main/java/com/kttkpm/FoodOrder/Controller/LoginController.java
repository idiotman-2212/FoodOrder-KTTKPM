package com.kttkpm.FoodOrder.Controller;

import com.kttkpm.FoodOrder.Entity.RoleEntity;
import com.kttkpm.FoodOrder.Entity.UserEntity;
import com.kttkpm.FoodOrder.Payload.Request.SignUpRequest;
import com.kttkpm.FoodOrder.Repository.RoleRepository;
import com.kttkpm.FoodOrder.Repository.UserRepository;
import com.kttkpm.FoodOrder.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/login")
    public String loginSuccess(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/admin";
        } else if (auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            return "redirect:/";
        }
        return "redirect:/login?error";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("signupRequest", new SignUpRequest());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistrationForm(@ModelAttribute("signupRequest") @Valid SignUpRequest signUpRequest,
                                          BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        signUpRequest.setIdRole(2);

        boolean registrationSuccess = userService.insertUser(signUpRequest);
        if (registrationSuccess) {
            return "redirect:/login";
        } else {
            model.addAttribute("error", "Registration failed. Please try again.");
            return "register";
        }
    }
    @GetMapping("/forgotpassword")
    public String forgotPass(Model model) {
        model.addAttribute("userDTO", new UserEntity());
        return "forgotpassword";
    }
}

