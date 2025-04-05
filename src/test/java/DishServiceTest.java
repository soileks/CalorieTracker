import calorietracker.dto.DishCreateRequest;
import calorietracker.dto.DishDTO;
import calorietracker.exception.ResourceNotFoundException;
import calorietracker.model.Dish;

import calorietracker.repository.DishRepository;

import calorietracker.service.DishService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
class DishServiceTest {
    @Mock
    private DishRepository dishRepository;
    @InjectMocks
    private DishService dishService;

    @Test
    void createDishValidRequestReturnsDishDTO() {
        DishCreateRequest request = new DishCreateRequest("Салат", 50, 1.5, 3.0, 5.0);
        Dish savedDish = new Dish(1L, "Салат", 50, 1.5, 3.0, 5.0);

        when(dishRepository.save(any(Dish.class))).thenReturn(savedDish);

        DishDTO result = dishService.createDish(request);
        assertEquals("Салат", result.getName());
        assertEquals(50, result.getCaloriesPerServing());
    }

    @Test
    void getDishesByIdsSomeNotFoundThrowsException() {
        when(dishRepository.findAllById(List.of(1L))).thenReturn(List.of());
        assertThrows(ResourceNotFoundException.class,
                () -> dishService.getDishesByIds(List.of(1L)));
    }
}
