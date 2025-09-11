package com.ai.recipe.recipe_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

public class RecipeResponse {
    private String generatedText;
    public RecipeResponse(){};
    public RecipeResponse(String generatedText){
        this.generatedText = generatedText;
    }
}
