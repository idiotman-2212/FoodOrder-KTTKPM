package com.kttkpm.FoodOrder.Controller;

import com.kttkpm.FoodOrder.Entity.UserEntity;
import com.kttkpm.FoodOrder.Helper.GlobalData;
import com.kttkpm.FoodOrder.Payload.Request.SignUpRequest;
import com.kttkpm.FoodOrder.Repository.RoleRepository;
import com.kttkpm.FoodOrder.Repository.UserRepository;
import com.kttkpm.FoodOrder.Service.EmailService;
import com.kttkpm.FoodOrder.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
public class LoginController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EmailService emailService;

    @GetMapping("/login")
    public String login(){
        GlobalData.cart.clear();
        return "login";
    }//page login

    @PostMapping("/login")
    public String login(@ModelAttribute(name = "loginForm") SignUpRequest signUpRequest, Model m) {
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
    //Quên mật khẩu
    @GetMapping("/forgotpassword")
    public String forgotPass(Model model) {
        return "forgotpassword";
    }

    @PostMapping("/forgotpassword")
    public String resetPassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        UserEntity user = userRepository.findByEmail(email);
        if (user != null) {
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            user.setTokenExpiration(LocalDateTime.now().plusHours(1));
            userRepository.save(user);
            String resetLink = "http://localhost:9999/reset/" + token;
            emailService.sendEmail(email, "Password Reset Request",
                    "To reset your password, visit the following link: " + resetLink);
            redirectAttributes.addFlashAttribute("message", "An email has been sent with instructions to reset your password.");
        } else {
            redirectAttributes.addFlashAttribute("error", "That email does not exist in our database.");
        }
        return "redirect:/forgotpassword";
    }

    @GetMapping("/reset/{token}")
    public String showResetForm(@PathVariable String token, Model model, RedirectAttributes redirectAttributes) {
        UserEntity user = userRepository.findByResetToken(token);
        if (user == null || user.getTokenExpiration().isBefore(LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("error", "That token is invalid or has expired.");
            return "redirect:/forgotpassword";
        }
        model.addAttribute("token", token);
        return "resetpassword";
    }

    @PostMapping("/reset/{token}")
    public String resetPassword(@PathVariable String token, @RequestParam("password") String password, RedirectAttributes redirectAttributes) {
        UserEntity user = userRepository.findByResetToken(token);
        if (user == null || user.getTokenExpiration().isBefore(LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("error", "That token is invalid or has expired.");
            return "redirect:/forgotpassword";
        }
        user.setPassword(passwordEncoder.encode(password));
        user.setResetToken(null);
        user.setTokenExpiration(null);
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("message", "Your password has been updated!");
        return "redirect:/login";
    }
}

