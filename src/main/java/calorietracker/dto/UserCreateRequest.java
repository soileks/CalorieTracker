package calorietracker.dto;

import calorietracker.model.Goal;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {
    @NotBlank(message = "name must not be null")
    private String name;

    @NotBlank(message = "email must not be null")
    @Email(message = "input correct email please")
    private String email;

    @Min(value = 1, message = "age must be greater than or equal to 1")
    @Max(value = 120, message = "age must be less than or equal to 120")
    private int age;

    @Min(value = 30, message = "weight must be greater than or equal to 30")
    @Max(value = 300, message = "weight must be less than or equal to 300")
    private double weight;

    @Min(value = 100, message = "height must be greater than or equal to 100")
    @Max(value = 250, message = "height must be less than or equal to 250")
    private double height;
    @NotNull(message = "goal must not be null")
    private Goal goal;
}
