package calorietracker.controller;

import calorietracker.dto.MealCreateRequest;
import calorietracker.dto.MealDTO;
import calorietracker.model.Meal;
import calorietracker.service.MealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/meals")
@RequiredArgsConstructor
public class MealController {
    private final MealService mealService;

    @PostMapping
    public ResponseEntity<MealDTO> createMeal(@Valid @RequestBody MealCreateRequest request) {
        MealDTO createdMeal = mealService.createMeal(request);
        return ResponseEntity.created(URI.create("/api/meals/" + createdMeal.getId()))
                .body(createdMeal);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MealDTO> getMeal(@PathVariable Long id) {
        return ResponseEntity.ok(mealService.getMealById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MealDTO>> getUserMeals(
            @PathVariable Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        if (date != null) {
            return ResponseEntity.ok(mealService.getMealsByUserAndDate(userId, date));
        }
        return ResponseEntity.ok(mealService.getMealsByUser(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeal(@PathVariable Long id) {
        mealService.deleteMeal(id);
        return ResponseEntity.noContent().build();
    }
}
