package com.kttkpm.FoodOrder.Controller;

import com.kttkpm.FoodOrder.Entity.RoleEntity;
import com.kttkpm.FoodOrder.Entity.UserEntity;
import com.kttkpm.FoodOrder.Helper.GlobalData;
import com.kttkpm.FoodOrder.Payload.Request.SignUpRequest;
import com.kttkpm.FoodOrder.Repository.RoleRepository;
import com.kttkpm.FoodOrder.Repository.UserRepository;
import com.kttkpm.FoodOrder.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class LoginController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/login")
    public String login(){
        GlobalData.cart.clear();
        return "login";
    }//page login

    @PostMapping("/login")
    public String login(@ModelAttribute(name = "loginForm") SignUpRequest signUpRequest, Model m) {
        log.info("Start login");
        UserEntity user = userRepository.findByEmail(signUpRequest.getEmail());

        if (user != null && passwordEncoder.matches(signUpRequest.getPassword(), user.getPassword())) {
            m.addAttribute("username", signUpRequest.getEmail());
            m.addAttribute("password", signUpRequest.getPassword());
            return "index";
        } else {
            m.addAttribute("error", "Incorrect Username & Password");
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerGet(Model model){
        model.addAttribute("signupRequest", new SignUpRequest());
        return "register";
    } //page register

    @PostMapping("/register")
    public String processRegistrationForm(@ModelAttribute("signupRequest") @Valid SignUpRequest signUpRequest,
                                          BindingResult bindingResult, Model model) {
        log.info("Register new user");
        if (bindingResult.hasErrors()) {
            log.info("Errors found");
            return "register";
        }
        signUpRequest.setIdRole(2);

        boolean registrationSuccess = userService.insertUser(signUpRequest);
        if (registrationSuccess) {
            log.info("User registered successfully");
            return "redirect:/login";
        } else {
            log.info("User registration failed");
            model.addAttribute("error", "Registration failed. Please try again.");
            return "register";
        }
    }
    @GetMapping("/forgot-password")
    public String forgotPass(Model model) {
        model.addAttribute("userDTO", new UserEntity());
        return "forgotpassword";
    }
}

