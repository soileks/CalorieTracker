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
    @NotBlank
    private String name;

    @Positive
    private double caloriesPerServing;

    @PositiveOrZero
    private double protein;

    @PositiveOrZero
    private double fat;

    @PositiveOrZero
    private double carbs;
}
