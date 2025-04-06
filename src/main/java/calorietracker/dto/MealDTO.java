package calorietracker.dto;

import calorietracker.model.Dish;
import calorietracker.model.Meal;
import calorietracker.model.User;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MealDTO {
    @NotNull(message = "id must not be null")
    private Long id;
    @NotNull(message =  "user must not be null")
    private UserDTO user;
    @NotNull(message = "dishes must not be null")
    private List<DishDTO> dishes;
    @NotNull(message = "mealTime must not be null")
    private LocalDateTime mealTime;
    private LocalDate mealDate; // Для удобства отчетов
    private double totalCalories;

    public static MealDTO fromEntity(Meal meal) {
        MealDTO dto = new MealDTO();
        dto.setId(meal.getId());
        dto.setUser(UserDTO.fromEntity(meal.getUser()));
        dto.setDishes(meal.getDishes().stream()
                .map(DishDTO::fromEntity)
                .toList());
        dto.setMealTime(meal.getMealTime());
        dto.setMealDate(meal.getMealDate());
        dto.setTotalCalories(calculateTotalCalories(meal.getDishes()));
        return dto;
    }

    private static double calculateTotalCalories(List<Dish> dishes) {
        return dishes.stream()
                .mapToDouble(Dish::getCaloriesPerServing)
                .sum();
    }
}
