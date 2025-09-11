package com.ai.recipe.recipe_service.controller;

import com.ai.recipe.recipe_service.dto.RecipeRequest;
import com.ai.recipe.recipe_service.dto.RecipeResponse;
import com.ai.recipe.recipe_service.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    // Free user endpoint
    @PostMapping("/freeuser")
    public ResponseEntity<RecipeResponse> generateFreeRecipe(@RequestBody RecipeRequest request) {
        System.out.println("recipe service /freeuser end point is hit");
        RecipeResponse response = recipeService.generateRecipe(request, null);
        return ResponseEntity.ok(response); // 200 OK
    }

    // Paid/authenticated user endpoint
    @PostMapping("/paiduser")
    public ResponseEntity<RecipeResponse> generateRecipe(@RequestBody RecipeRequest request,
                                                         Authentication authentication) {
        System.out.println("recipe service /paid user end point is hit");
        Long userId = (Long) authentication.getPrincipal(); // from JwtAuthenticationFilter
        RecipeResponse response = recipeService.generateRecipe(request, userId);
        return ResponseEntity.ok(response); // 200 OK
    }

    // Test endpoint
    @PostMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        System.out.println("recipe service is hit");
        return ResponseEntity.status(HttpStatus.OK).body("Recipe service is working!");
    }

    // Optional: return different status if no recipes found
    /*
    @PostMapping("/optional")
    public ResponseEntity<RecipeResponse> optionalEndpoint(@RequestBody RecipeRequest request) {
        RecipeResponse response = recipeService.generateRecipe(request, null);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content
        }
        return ResponseEntity.ok(response);
    }
    */
}
