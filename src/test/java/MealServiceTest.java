
import calorietracker.dto.MealCreateRequest;
import calorietracker.dto.MealDTO;
import calorietracker.exception.ResourceNotFoundException;
import calorietracker.model.Dish;
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

    private final LocalDateTime testMealTime = LocalDateTime.of(2023, 12, 15, 14, 30);
    private final User testUser = User.builder().id(1L).name("User").build();
    private final Dish testDish1 = new Dish(1L, "Салат", 50, 1.5, 3.0, 5.0);
    private final Dish testDish2 = new Dish(2L, "Курица", 150, 30.0, 5.0, 0.0);

    @Test
    void createMealWithValidRequestReturnsMealDTO() {
        // Arrange
        MealCreateRequest request = new MealCreateRequest(1L, testMealTime, List.of(1L, 2L));
        Meal savedMeal = Meal.builder()
                .id(1L)
                .user(testUser)
                .dishes(List.of(testDish1, testDish2))
                .mealTime(testMealTime)
                .mealDate(testMealTime.toLocalDate())
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(dishRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(testDish1, testDish2));
        when(mealRepository.save(any(Meal.class))).thenReturn(savedMeal);

        // Act
        MealDTO result = mealService.createMeal(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(testMealTime, result.getMealTime());
        assertEquals(2, result.getDishes().size());
        verify(mealRepository).save(argThat(meal ->
                meal.getUser().equals(testUser) &&
                        meal.getDishes().size() == 2
        ));
    }

    @Test
    void createMealWithNonExistentUserThrowsException() {
        // Arrange
        MealCreateRequest request = new MealCreateRequest(999L, testMealTime, List.of(1L));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            mealService.createMeal(request);
        });
        verify(dishRepository, never()).findAllById(any());
        verify(mealRepository, never()).save(any());
    }

    @Test
    void createMealWithNonExistentDishesThrowsException() {
        // Arrange
        MealCreateRequest request = new MealCreateRequest(1L, testMealTime, List.of(99L));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(dishRepository.findAllById(List.of(99L))).thenReturn(List.of());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            mealService.createMeal(request);
        });
        verify(mealRepository, never()).save(any());
    }


    @Test
    void getMealByIdWhenExistsReturnsMealDTO() {
        // Arrange
        Meal meal = Meal.builder()
                .id(1L)
                .user(testUser)
                .dishes(List.of(testDish1))
                .mealTime(testMealTime)
                .build();

        when(mealRepository.findById(1L)).thenReturn(Optional.of(meal));

        // Act
        MealDTO result = mealService.getMealById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(testMealTime, result.getMealTime());
        assertEquals(1, result.getDishes().size());
    }

    @Test
    void getMealByIdWhenNotExistsThrowsException() {
        // Arrange
        when(mealRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            mealService.getMealById(999L);
        });
    }

    @Test
    void getMealsByUserWhenExistsReturnsList() {
        // Arrange
        Meal meal1 = Meal.builder().id(1L).user(testUser).dishes(List.of(testDish1)).build();
        Meal meal2 = Meal.builder().id(2L).user(testUser).dishes(List.of(testDish2)).build();

        when(mealRepository.findByUserId(1L)).thenReturn(List.of(meal1, meal2));

        // Act
        List<MealDTO> result = mealService.getMealsByUser(1L);

        // Assert
        assertEquals(2, result.size());
        verify(mealRepository).findByUserId(1L);
    }

    @Test
    void getMealsByUserAndDateWhenExistsReturnsList() {
        // Arrange
        LocalDate testDate = LocalDate.of(2023, 12, 15);
        Meal meal = Meal.builder()
                .id(1L)
                .user(testUser)
                .mealTime(testMealTime)
                .mealDate(testDate)
                .dishes(List.of(testDish1))
                .build();

        when(mealRepository.findByUserIdAndMealDate(1L, testDate))
                .thenReturn(List.of(meal));

        // Act
        List<MealDTO> result = mealService.getMealsByUserAndDate(1L, testDate);

        // Assert
        assertEquals(1, result.size());
        assertEquals(testDate, result.get(0).getMealDate());
    }


    @Test
    void deleteMealWhenExistsDeletesSuccessfully() {
        // Arrange
        when(mealRepository.existsById(1L)).thenReturn(true);

        // Act
        mealService.deleteMeal(1L);

        // Assert
        verify(mealRepository).deleteById(1L);
    }

    @Test
    void deleteMealWhenNotExistsThrowsException() {
        // Arrange
        when(mealRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            mealService.deleteMeal(999L);
        });
        verify(mealRepository, never()).deleteById(any());
    }

}

