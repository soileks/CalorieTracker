package calorietracker.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MealCreateRequest {
    @NotNull
    private Long userId;

    @NotNull
    private LocalDateTime mealTime;

    @NotEmpty
    private List<Long> dishIds;
}
