package calorietracker.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MealCreateRequest {
    @NotNull(message = "userId must not be null")
    private Long userId;

    @NotNull(message = "mealTime must not be null")
    private LocalDateTime mealTime;

    @NotEmpty(message = "dishIds must not be null")
    private List<Long> dishIds;
}
