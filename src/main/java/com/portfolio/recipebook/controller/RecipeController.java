package com.portfolio.recipebook.controller;

import com.portfolio.recipebook.dto.RecipeDto;
import com.portfolio.recipebook.security.auth.RecipeBookUserPrinciple;
import com.portfolio.recipebook.security.auth.user.User;
import com.portfolio.recipebook.security.auth.user.UserService;
import com.portfolio.recipebook.service.RecipeService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class RecipeController {

    private final RecipeService recipeService;
    private final UserService userService;

    public RecipeController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @GetMapping("/recipes")
    public String recipeList(Model model) {
        model.addAttribute("recipes", recipeService.getAll());
        model.addAttribute("user", getUser());
        return "recipe/list";
    }

    @GetMapping("/recipes/my")
    public String myRecipeList(Model model) {
        model.addAttribute("recipes", recipeService.getAllMy());
        model.addAttribute("user", getUser());
        return "recipe/my";
    }

    @GetMapping("/recipes/{recipeId}")
    public String recipeIndex(@PathVariable("recipeId") Long recipeId,
                              Model model) {
        RecipeDto recipe = recipeService.getById(recipeId);
        User user = getUser();
        boolean isSameUser = recipe.getUser().getId() == user.getId();
        model.addAttribute("recipe", recipe);
        model.addAttribute("user", user);
        model.addAttribute("isSameUser", isSameUser);
        return "recipe/index";
    }

    @GetMapping("/recipes/{recipeId}/form")
    public String recipeEditForm(@PathVariable("recipeId") Long recipeId,
                                 Model model) {
        model.addAttribute("recipe", recipeService.getById(recipeId));

        model.addAttribute("user", getUser().getId());
        return "recipe/form";
    }

    @GetMapping("/recipes/form")
    public String recipeForm(Model model) {

        model.addAttribute("user", getUser().getId());
        return "recipe/form";
    }

    private User getUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            RecipeBookUserPrinciple userDetails = (RecipeBookUserPrinciple) authentication.getPrincipal();
            return userService.findOneUser(userDetails.getId()).orElse(User.builder().id(-1L).build());
        } catch (Exception e) {
            return User.builder().id(-1L).build();
        }
    }
}
