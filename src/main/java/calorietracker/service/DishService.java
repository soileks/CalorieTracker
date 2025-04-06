package calorietracker.service;


import calorietracker.dto.DishCreateRequest;
import calorietracker.dto.DishDTO;
import calorietracker.exception.ResourceNotFoundException;
import calorietracker.model.Dish;
import calorietracker.repository.DishRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class DishService {
    private final DishRepository dishRepository;

    public DishDTO createDish(@Valid DishCreateRequest request) {
        Dish dish = new Dish();
        dish.setName(request.getName());
        dish.setCaloriesPerServing(request.getCaloriesPerServing());
        dish.setProtein(request.getProtein());
        dish.setFat(request.getFat());
        dish.setCarbs(request.getCarbs());

        Dish savedDish = dishRepository.save(dish);
        return DishDTO.fromEntity(savedDish);
    }

    public DishDTO getDishById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found"));
        return DishDTO.fromEntity(dish);
    }

    public List<DishDTO> getAllDishes() {
        return dishRepository.findAll().stream()
                .map(DishDTO::fromEntity)
                .toList();
    }

    public List<Dish> getDishesByIds(List<Long> ids) {
        List<Dish> dishes = dishRepository.findAllById(ids);
        if (dishes.size() != ids.size()) {
            throw new ResourceNotFoundException("Some dishes not found");
        }
        return dishes;
    }
}
