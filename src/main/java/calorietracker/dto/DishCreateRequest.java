package calorietracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishCreateRequest {
    @NotBlank(message = "name must not be null")
    private String name;

    @Positive(message = "caloriesPerServing must be > 0")
    private double caloriesPerServing;

    @PositiveOrZero(message = "protein must be >= 0")
    private double protein;

    @PositiveOrZero(message = "fat must be >= 0")
    private double fat;

    @PositiveOrZero(message = "carbs must be >= 0")
    private double carbs;
}
