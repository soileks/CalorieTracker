import calorietracker.dto.DishCreateRequest;
import calorietracker.dto.DishDTO;
import calorietracker.exception.ResourceNotFoundException;
import calorietracker.model.Dish;

import calorietracker.repository.DishRepository;

import calorietracker.service.DishService;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
class DishServiceTest {

    @Mock
    private DishRepository dishRepository;

    @InjectMocks
    private DishService dishService;

    private final Dish testDish = new Dish(1L, "Гречка", 120, 4.5, 1.2, 22.3);
    private final DishCreateRequest testCreateRequest =
            new DishCreateRequest("Гречка", 120, 4.5, 1.2, 22.3);

    @Test
    void createDishWithValidRequestReturnsDishDTO() {
        // Arrange
        when(dishRepository.save(any(Dish.class))).thenReturn(testDish);

        // Act
        DishDTO result = dishService.createDish(testCreateRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Гречка", result.getName());
        assertEquals(120, result.getCaloriesPerServing());
        assertEquals(4.5, result.getProtein());

        verify(dishRepository).save(argThat(dish ->
                dish.getName().equals("Гречка") &&
                        dish.getCaloriesPerServing() == 120
        ));
    }


    @Test
    void getDishByIdWhenExistsReturnsDishDTO() {
        // Arrange
        when(dishRepository.findById(1L)).thenReturn(Optional.of(testDish));

        // Act
        DishDTO result = dishService.getDishById(1L);

        // Assert
        assertEquals(1L, result.getId());
        assertEquals("Гречка", result.getName());
    }

    @Test
    void getDishByIdWhenNotExistsThrowsException() {
        // Arrange
        when(dishRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            dishService.getDishById(999L);
        });
    }

    @Test
    void getAllDishesReturnsListOfDishes() {
        // Arrange
        Dish secondDish = new Dish(2L, "Курица", 165, 31.0, 3.6, 1.0);
        when(dishRepository.findAll()).thenReturn(List.of(testDish, secondDish));

        // Act
        List<DishDTO> result = dishService.getAllDishes();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Гречка", result.get(0).getName());
        assertEquals("Курица", result.get(1).getName());
    }

    @Test
    void getAllDishesWhenEmptyReturnsEmptyList() {
        // Arrange
        when(dishRepository.findAll()).thenReturn(List.of());

        // Act
        List<DishDTO> result = dishService.getAllDishes();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getDishesByIdsWhenAllExistReturnsDishes() {
        // Arrange
        List<Long> ids = List.of(1L, 2L);
        Dish secondDish = new Dish(2L, "Курица", 165, 31.0, 3.6, 1.0);
        when(dishRepository.findAllById(ids)).thenReturn(List.of(testDish, secondDish));

        // Act
        List<Dish> result = dishService.getDishesByIds(ids);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void getDishesByIdsWhenSomeNotFoundThrowsException() {
        // Arrange
        List<Long> ids = List.of(1L, 999L);
        when(dishRepository.findAllById(ids)).thenReturn(List.of(testDish));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            dishService.getDishesByIds(ids);
        });
    }



}
