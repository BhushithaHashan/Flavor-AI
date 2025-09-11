

package com.ai.recipe.recipe_service.service;

import com.ai.recipe.recipe_service.dto.RecipeRequest;
import com.ai.recipe.recipe_service.dto.RecipeResponse;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {


    @Value("${spring.ai.openai.api-key}")
    private String geminiapikey;
    @Value("${spring.ai.openai.chat.options.model}")
    private String model;
    private Client client;
    private GenerateContentResponse geminiresponse;
    public RecipeService(@Value("${spring.ai.openai.api-key}") String apiKey,@Value("${spring.ai.openai.chat.options.model}")String model){
        this.geminiapikey = apiKey;
        this.model = model;
        this.client =  Client.builder().apiKey(geminiapikey).build();
    }
    public RecipeResponse generateRecipe(RecipeRequest request) {
        
        List<String> ingredients = request.getIngredientsList();
        List<String> cuisine = request.getCuisineList();
        List<String> dietary = request.getDietaryList();
        List<String> allergies = request.getAllergiesList();

        // Step 2: Build prompt for Gemini API
        String prompt = buildPrompt(ingredients, cuisine, request.getMealType(), dietary, allergies);

        // Step 3: Call Gemini API (mock here)
        String aiGeneratedRecipe = callGeminiApi(prompt);

        // Step 4: Build RecipeResponse DTO
        return new RecipeResponse(aiGeneratedRecipe);
        
    }

    // -------------------- Helpers -------------------//
    private String buildPrompt(List<String> ingredients, List<String> cuisine,
                               String mealType, List<String> dietary, List<String> allergies) {
        return String.format(
                "Generate a %s recipe using ingredients %s for %s cuisine. Consider dietary restrictions: %s. Avoid: %s.",
                mealType,
                String.join(", ", ingredients),
                String.join(", ", cuisine),
                String.join(", ", dietary),
                String.join(", ", allergies)
        );
    }

    private String callGeminiApi(String prompt) {
        geminiresponse =
        client.models.generateContent(
            model,
            prompt,
            null);

        System.out.println(geminiresponse.text());
        return geminiresponse.text();
    }
}

