package com.ai.recipe.recipe_service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeRequest {
    private String ingredients;
    private String cuisine;
    private String mealType;
    private String dietary;
    private String allergies;

    // utility methods
    public List<String> getIngredientsList() {
        return List.of(ingredients.split("\\s*,\\s*"));
    }
    public List<String> getCuisineList() {
        return List.of(cuisine.split("\\s*,\\s*"));
    }
    public List<String> getDietaryList() {
        return List.of(dietary.split("\\s*,\\s*"));
    }
    public List<String> getAllergiesList() {
        return List.of(allergies.split("\\s*,\\s*"));
    }
    
    public String  getMealType(){
        return mealType;
    }
    // getters & setters
}
