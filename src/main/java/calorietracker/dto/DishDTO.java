package calorietracker.dto;


import calorietracker.model.Dish;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;


@Data
public class DishDTO {

    private Long id;

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
