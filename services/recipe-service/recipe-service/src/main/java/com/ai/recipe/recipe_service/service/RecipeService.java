

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
                "Generate an extremely detailed, step-by-step recipe for a '%s' meal of '%s' cuisine.If there are clashing items like vegan and ingrediant having meat mention them breifly in the begenning in a funny friendly way " +
                "The entire response must be a single string formatted for easy breakdown into multiple pages. " +
                "Do not include any other text or formatting besides what is specified below.\n" +
                "\n" +
                "Page 1: INGREDIENTS\n" +
                "List all ingredients with their exact portions and measurements in a numbered or bulleted list. Also, include a brief introduction to the recipe.And try best to use the ingrediant on the user provided .\n" +
                "\n" +
                "---PAGE-BREAK---\n" +
                "Page 2: PREPARATION\n" +
                "Provide a highly detailed, numbered list of preparation steps. The instructions must be very specific and thorough.\n" +
                "\n" +
                "---PAGE-BREAK---\n" +
                "Page 3: COOKING INSTRUCTIONS\n" +
                "Provide a numbered list of detailed cooking steps, including specific temperatures, times, and visual cues. Be sure to describe what the food should look and feel like at each stage.\n" +
                "\n" +
                "---PAGE-BREAK---\n" +
                "Page 4: FINALIZING & SERVING\n" +
                "Provide a detailed, numbered list of steps for plating and serving the dish, including any garnishes or final touches.\n" +
                "\n" +
                "---PAGE-BREAK---\n" +
                "Page 5: TIPS & VARIATIONS\n" +
                "Provide a final page with useful tips, common mistakes to avoid, and suggestions for variations on the recipe.\n" +
                "\n" +
                "Recipe details:\n" +
                "- Required ingredients: %s\n" +
                "- Dietary restrictions: %s\n" +
                "- Allergies to avoid: %s\n" ,
                mealType,
                String.join(" and ", cuisine),
                String.join(", ", ingredients),
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

