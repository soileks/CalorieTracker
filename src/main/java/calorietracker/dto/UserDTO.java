package calorietracker.dto;

import calorietracker.model.Goal;
import calorietracker.model.User;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @NotNull(message = "id must not be null")
    private Long id;

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

    private double dailyCalorieNorm;

    public static UserDTO fromEntity(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAge(user.getAge());
        dto.setHeight(user.getHeight());
        dto.setWeight(user.getWeight());
        dto.setGoal(user.getGoal());
        dto.setDailyCalorieNorm(user.getDailyCalorieNorm());
        return dto;
    }

}