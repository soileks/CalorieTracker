package calorietracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Positive(message = "Calories must be positive")
    private double caloriesPerServing;

    @PositiveOrZero(message = "Protein must be positive or zero")
    private double protein;

    @PositiveOrZero(message = "Fat must be positive or zero")
    private double fat;

    @PositiveOrZero(message = "Carbs must be positive or zero")
    private double carbs;
}