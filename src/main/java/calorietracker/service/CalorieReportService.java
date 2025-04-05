package calorietracker.service;

import calorietracker.exception.ResourceNotFoundException;
import calorietracker.model.Meal;
import calorietracker.model.User;
import calorietracker.model.Goal;
import calorietracker.model.Dish;
import calorietracker.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalorieReportService {
    private final MealRepository mealRepository;

    public DailyReport getDailyReport(Long userId, LocalDate date) {
        List<Meal> meals = mealRepository.findByUserIdAndMealDate(userId, date);
        double totalCalories = meals.stream()
                .flatMap(meal -> meal.getDishes().stream())
                .mapToDouble(Dish::getCaloriesPerServing)
                .sum();
        User user = meals.stream().findFirst()
                .map(Meal::getUser)
                .orElseThrow(() -> new ResourceNotFoundException("No meals found for user"));

        GoalStatus status = calculateGoalStatus(user, totalCalories);

        return new DailyReport(
                date,
                totalCalories,
                user.getDailyCalorieNorm(),
                user.getGoal(),
                status.message(),
                status.isAchieved(),
                meals
        );
    }

    private GoalStatus calculateGoalStatus(User user, double totalCalories) {
        double norm = user.getDailyCalorieNorm();

        return switch (user.getGoal()) {
            case WEIGHT_LOSS -> {
                boolean isAchieved = totalCalories <= norm;
                yield new GoalStatus(
                        isAchieved,
                        isAchieved ? "Дефицит" : "Превышение"
                );
            }
            case WEIGHT_GAIN -> {
                boolean isAchieved = totalCalories >= norm;
                yield new GoalStatus(
                        isAchieved,
                        isAchieved ? "Профицит" : "Недостаток"
                );
            }
            case MAINTENANCE -> {
                double diff = Math.abs(totalCalories - norm);
                boolean isAchieved = diff < 100; // Допуск ±100 ккал
                yield new GoalStatus(
                        isAchieved,
                        isAchieved ? "Норма" : "Отклонение: " + (int) diff + " ккал"
                );
            }
        };
    }

    // Вспомогательный класс для статуса
    private record GoalStatus(boolean isAchieved, String message) {}

    public record DailyReport(
            LocalDate date,
            double totalCalories,
            double dailyNorm,
            Goal goal,                    // Добавили цель
            String goalStatus,            // "Дефицит", "Профицит", "Норма"
            boolean isGoalAchieved,       // Уложился в цель (вместо withinLimit)
            List<Meal> meals
    ) {}
}
