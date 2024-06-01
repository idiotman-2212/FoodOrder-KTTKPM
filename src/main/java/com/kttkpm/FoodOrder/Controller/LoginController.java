package com.kttkpm.FoodOrder.Controller;

import com.kttkpm.FoodOrder.Entity.UserEntity;
import com.kttkpm.FoodOrder.Helper.GlobalData;
import com.kttkpm.FoodOrder.Payload.Request.SignUpRequest;
import com.kttkpm.FoodOrder.Repository.RoleRepository;
import com.kttkpm.FoodOrder.Repository.UserRepository;
import com.kttkpm.FoodOrder.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
    @GetMapping("/forgotpassword")
    public String forgotPass(Model model) {
        model.addAttribute("userDTO", new UserEntity());
        return "forgotpassword";
    }
}

