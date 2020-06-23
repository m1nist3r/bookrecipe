package com.portfolio.recipebook.repository;

import com.portfolio.recipebook.model.Recipe;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
    List<Recipe> findAllByUserId(Long id);
}
