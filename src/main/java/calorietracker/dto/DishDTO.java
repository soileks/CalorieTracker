package calorietracker.dto;


import calorietracker.model.Dish;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;


@Data
public class DishDTO {

    private Long id;

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

    public static DishDTO fromEntity(Dish dish) {
        DishDTO dto = new DishDTO();
        dto.setId(dish.getId());
        dto.setName(dish.getName());
        dto.setCaloriesPerServing(dish.getCaloriesPerServing());
        dto.setProtein(dish.getProtein());
        dto.setFat(dish.getFat());
        dto.setCarbs(dish.getCarbs());
        return dto;
    }

}
