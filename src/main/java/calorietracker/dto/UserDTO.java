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
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @Min(1) @Max(120)
    private int age;

    @Min(30) @Max(300)
    private double weight;

    @Min(100) @Max(250)
    private double height;

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