package com.portfolio.recipebook.service.impl;

import com.portfolio.recipebook.command.RecipeCommand;
import com.portfolio.recipebook.dto.RecipeDto;
import com.portfolio.recipebook.mapper.ImageMapper;
import com.portfolio.recipebook.mapper.RecipeMapper;
import com.portfolio.recipebook.model.Difficulty;
import com.portfolio.recipebook.model.Recipe;
import com.portfolio.recipebook.repository.RecipeRepository;
import com.portfolio.recipebook.security.auth.RecipeBookUserPrinciple;
import com.portfolio.recipebook.security.auth.user.User;
import com.portfolio.recipebook.security.auth.user.UserRepository;
import com.portfolio.recipebook.service.RecipeService;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public List<RecipeDto> getAll() {
        List<RecipeDto> recipeDtoList = new ArrayList<>();
        recipeRepository.findAll().forEach(recipe -> recipeDtoList.add(RecipeMapper.toDto(recipe)));
        return recipeDtoList
                .stream()
                .sorted(Comparator.comparing(RecipeDto::getId).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public RecipeDto create(RecipeCommand recipeCommand) throws IOException {
        Recipe recipe = new Recipe();
        recipe.setId(recipeCommand.getId());
        recipe.setCookTime(recipeCommand.getCookTime());
        recipe.setDescription(recipeCommand.getDescription());
        recipe.setTitle(recipeCommand.getTitle());
        recipe.setDifficulty(Difficulty.valueOf(recipeCommand.getDifficulty()));
        recipe.setUser(userRepository.findById(recipeCommand.getUserId()).orElseThrow());
        if (recipeCommand.getImage().getSize() == 0) {
            MultipartFile multipartFile = new MockMultipartFile("defaultImage",
                    new FileInputStream(new File("src/main/resources/static/img/empty.png"))
            );
            recipe.setImage(ImageMapper.toByteArray(multipartFile));
            System.out.println(multipartFile.getName());
        }
        return RecipeMapper.toDto(recipeRepository.save(recipe));
    }

    @Override
    @Transactional
    public RecipeDto update(RecipeCommand recipeCommand) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeCommand.getId());
        if (recipeOptional.isPresent() && recipeOptional.get().getUser().getId() == getUser().getId()) {
            Recipe recipe = recipeOptional.get();
            recipe.setCookTime(recipeCommand.getCookTime());
            recipe.setDescription(recipeCommand.getDescription());
            recipe.setTitle(recipeCommand.getTitle());
            recipe.setDifficulty(Difficulty.valueOf(recipeCommand.getDifficulty()));
            if (recipeCommand.getImage().getSize() != 0) {
                recipe.setImage(ImageMapper.toByteArray(recipeCommand.getImage()));
            }
            return RecipeMapper.toDto(recipeRepository.save(recipe));
        } else {
            throw new RuntimeException("Recipe not found");
        }
    }

    @Override
    @Transactional
    public List<RecipeDto> getAllMy() {
        List<RecipeDto> recipeDtoList = new ArrayList<>();
        recipeRepository.findAllByUserId(getUser().getId())
                .forEach(recipe -> recipeDtoList.add(RecipeMapper.toDto(recipe)));
        return recipeDtoList
                .stream()
                .sorted(Comparator.comparing(RecipeDto::getId).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long recipeId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isPresent() && recipeOptional.get().getUser().getId() == getUser().getId()) {
            recipeRepository.delete(recipeOptional.get());
        } else {
            throw new RuntimeException("Recipe not found");
        }
    }

    @Override
    @Transactional
    public RecipeDto getById(Long recipeId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isPresent()) {
            return RecipeMapper.toDto(recipeOptional.get());
        } else {
            throw new RuntimeException("Recipe not found");
        }
    }

    private User getUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            RecipeBookUserPrinciple userDetails = (RecipeBookUserPrinciple) authentication.getPrincipal();
            return userRepository.findById(userDetails.getId()).orElse(User.builder().id(-1L).build());
        } catch (Exception e) {
            return User.builder().id(-1L).build();
        }
    }
}
