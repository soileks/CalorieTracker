package calorietracker.repository;

import calorietracker.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByUserId(Long userId);
    List<Meal> findByUserIdAndMealDate(Long userId, LocalDate mealDate);
}
