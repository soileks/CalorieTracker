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

    private String name;

    private String email;

    private int age;

    private double weight;

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