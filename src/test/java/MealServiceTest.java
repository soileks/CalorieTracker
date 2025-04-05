
import calorietracker.dto.MealCreateRequest;
import calorietracker.dto.MealDTO;
import calorietracker.exception.ResourceNotFoundException;
import calorietracker.model.Dish;
import calorietracker.model.Goal;
import calorietracker.model.Meal;
import calorietracker.model.User;
import calorietracker.repository.DishRepository;
import calorietracker.repository.MealRepository;
import calorietracker.repository.UserRepository;
import calorietracker.service.MealService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;



    @ExtendWith(MockitoExtension.class)
    class MealServiceTest {
        @Mock
        private MealRepository mealRepository;
        @Mock
        private UserRepository userRepository;
        @Mock
        private DishRepository dishRepository;
        @InjectMocks
        private MealService mealService;

        @Test
        void createMealValidRequestReturnsMealDTO() {
            // Arrange
            LocalDateTime mealTime = LocalDateTime.of(2023, 12, 15, 14, 30);
            MealCreateRequest request = new MealCreateRequest(1L, mealTime, List.of(1L, 2L));

            User user = User.builder().id(1L).name("User").email("test@gmail.com").age(30)
                    .weight(70).height(170).goal(Goal.MAINTENANCE).build();
            Dish dish1 = new Dish(1L, "Салат", 50, 1.5, 3.0, 5.0);
            Dish dish2 = new Dish(2L, "Курица", 150, 30.0, 5.0, 0.0);

            Meal unsavedMeal = new Meal();
            unsavedMeal.setUser(user);
            unsavedMeal.setDishes(List.of(dish1, dish2));
            unsavedMeal.setMealTime(mealTime);

            Meal savedMeal = Meal.builder().id(1L).user(user).dishes(List.of(dish1, dish2)).
                    mealTime(mealTime).mealDate(mealTime.toLocalDate()).build();

            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(dishRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(dish1, dish2));
            when(mealRepository.save(any(Meal.class))).thenReturn(savedMeal);

            // Act
            MealDTO result = mealService.createMeal(request);

            // Assert
            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals(LocalDate.of(2023, 12, 15), savedMeal.getMealDate()); // Проверка даты
            verify(mealRepository).save(argThat(meal ->
                    meal.getUser().getId().equals(1L) &&
                            meal.getDishes().size() == 2
            ));
        }

        @Test
        void createMealUserNotFoundThrowsException() {
            MealCreateRequest request = new MealCreateRequest(1L, LocalDateTime.now(), List.of(1L));
            when(userRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> mealService.createMeal(request));
        }

        @Test
        void getMealByIdExistsReturnsMealDTO() {
            // Arrange
            LocalDateTime testTime = LocalDateTime.now();
            User user = User.builder().id(1L).name("User").email("test@gmail.com").age(30)
                    .weight(70).height(170).goal(Goal.MAINTENANCE).build();
            Dish dish = new Dish(1L, "Блюдо", 200, 10, 5, 20);

            Meal meal = Meal.builder().id(1L).user(user).dishes(List.of(dish)).
                    mealTime(testTime).mealDate(testTime.toLocalDate()).build();


            when(mealRepository.findById(1L)).thenReturn(Optional.of(meal));

            // Act
            MealDTO result = mealService.getMealById(1L);

            // Assert
            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals(testTime, result.getMealTime());
            assertEquals(1, result.getDishes().size()); // Проверка количества блюд
        }

        @Test
        void deleteMealNotExistsThrowsException() {
            when(mealRepository.existsById(1L)).thenReturn(false);
            assertThrows(ResourceNotFoundException.class, () -> mealService.deleteMeal(1L));
        }

    }

