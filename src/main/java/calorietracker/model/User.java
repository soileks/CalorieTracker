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

    private String name;

    private String email;

    private int age;

    private double weight; // кг

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