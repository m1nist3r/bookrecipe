package com.portfolio.recipebook.controller;

import com.portfolio.recipebook.security.auth.user.UserDto;
import com.portfolio.recipebook.security.auth.user.UserService;
import com.portfolio.recipebook.util.UserEmailAlreadyExistException;
import com.portfolio.recipebook.util.UserNicknameAlreadyExistException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/index.html";
        }
        return "authentication/login";
    }

    @GetMapping("/logout")
    public String getLogoutPage() {
        return "authentication/login";
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "authentication/registration";
    }

    @PostMapping("/registration")
    public ModelAndView registerUserAccount
            (@ModelAttribute("user") @Valid final UserDto userDto, Errors errors) {
        if (errors.hasErrors()) {
            return new ModelAndView("authentication/registration");
        }
        try {
            userService.updateUser(userDto.translateModelToUser());
        } catch (UserEmailAlreadyExistException | UserNicknameAlreadyExistException uaeEx) {
            return new ModelAndView("authentication/registration")
                    .addObject("nickname", uaeEx.getMessage());
        }
        return new ModelAndView("authentication/successRegister",
                "user", userDto.translateModelToUser());
    }

}
