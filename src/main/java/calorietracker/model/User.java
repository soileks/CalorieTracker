package calorietracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Min(value = 1, message = "Age must be at least 1")
    @Max(value = 120, message = "Age must be less than 120")
    private int age;

    @Min(value = 30, message = "Weight must be at least 30 kg")
    @Max(value = 300, message = "Weight must be less than 300 kg")
    private double weight; // кг

    @Min(value = 100, message = "Height must be at least 100 cm")
    @Max(value = 250, message = "Height must be less than 250 cm")
    private double height; // см

    @Enumerated(EnumType.STRING)
    private Goal goal;


    private double dailyCalorieNorm;



    @PrePersist
    @PreUpdate
    public void calculateDailyCalorieNorm() {

        double bmr = 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age);

        switch (goal) {
            case WEIGHT_LOSS:
                dailyCalorieNorm = bmr * 0.85; // 15% дефицит
                break;
            case WEIGHT_GAIN:
                dailyCalorieNorm = bmr * 1.15; // 15% профицит
                break;
            case MAINTENANCE:
            default:
                dailyCalorieNorm = bmr;
        }
    }
}