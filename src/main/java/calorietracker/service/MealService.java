package calorietracker.service;

import calorietracker.dto.MealCreateRequest;
import calorietracker.dto.MealDTO;
import calorietracker.exception.ResourceNotFoundException;
import calorietracker.model.Dish;
import calorietracker.model.Meal;
import calorietracker.model.User;
import calorietracker.repository.DishRepository;
import calorietracker.repository.MealRepository;
import calorietracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MealService {
    private final MealRepository mealRepository;
    private final UserRepository userRepository;
    private final DishRepository dishRepository;


    public MealDTO createMeal(MealCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Dish> dishes = dishRepository.findAllById(request.getDishIds());
        if (dishes.size() != request.getDishIds().size()) {
            throw new ResourceNotFoundException("Some dishes not found");
        }

        Meal meal = new Meal();
        meal.setUser(user);
        meal.setDishes(dishes);
        meal.setMealTime(request.getMealTime());

        Meal savedMeal = mealRepository.save(meal);
        return MealDTO.fromEntity(savedMeal);
    }

    public MealDTO getMealById(Long id) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found"));
        return MealDTO.fromEntity(meal);
    }

    public List<MealDTO> getMealsByUser(Long userId) {
        return mealRepository.findByUserId(userId).stream()
                .map(MealDTO::fromEntity)
                .toList();
    }

    public List<MealDTO> getMealsByUserAndDate(Long userId, LocalDate date) {
        return mealRepository.findByUserIdAndMealDate(userId, date).stream()
                .map(MealDTO::fromEntity)
                .toList();
    }

    public void deleteMeal(Long id) {
        if (!mealRepository.existsById(id)) {
            throw new ResourceNotFoundException("Meal not found");
        }
        mealRepository.deleteById(id);
    }
}