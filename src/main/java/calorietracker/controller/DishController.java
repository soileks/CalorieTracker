package calorietracker.controller;

import calorietracker.dto.DishCreateRequest;
import calorietracker.dto.DishDTO;
import calorietracker.service.DishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/dishes")
@RequiredArgsConstructor
public class DishController {
    private final DishService dishService;

    @PostMapping
    public ResponseEntity<DishDTO> createDish(@Valid @RequestBody DishCreateRequest request) {
        DishDTO createdDish = dishService.createDish(request);
        return ResponseEntity.created(URI.create("/api/dishes/" + createdDish.getId()))
                .body(createdDish);
    }

    @GetMapping
    public ResponseEntity<List<DishDTO>> getAllDishes() {
        return ResponseEntity.ok(dishService.getAllDishes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishDTO> getDish(@PathVariable Long id) {
        return ResponseEntity.ok(dishService.getDishById(id));
    }
}
