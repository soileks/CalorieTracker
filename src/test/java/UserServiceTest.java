


import calorietracker.dto.UserDTO;
import calorietracker.model.Goal;
import calorietracker.model.User;
import calorietracker.repository.UserRepository;
import calorietracker.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private UserService userService;

    @Test
    void createUserValidDataReturnsUserDTO() {
        UserDTO inputDTO = new UserDTO(null, "Test", "test@mail.com", 30, 70, 170, Goal.MAINTENANCE, 2000);
        User user = new User(1L, "Test", "test@mail.com", 30, 70, 170, Goal.MAINTENANCE, 2000);
        UserDTO expectedDTO = new UserDTO(1L, "Test", "test@mail.com", 30, 70, 170, Goal.MAINTENANCE, 2000);

        when(modelMapper.map(inputDTO, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(expectedDTO);

        UserDTO result = userService.createUser(inputDTO);
        assertEquals(1L, result.getId());
    }

    @Test
    void createUser_CalculatesCalorieNorm() {
        // Arrange
        UserDTO inputDTO = new UserDTO();
        inputDTO.setName("Test");
        inputDTO.setAge(30);
        inputDTO.setWeight(70.0); // кг
        inputDTO.setHeight(170.0); // см
        inputDTO.setGoal(Goal.WEIGHT_LOSS);

        User userWithoutNorm = new User();
        userWithoutNorm.setName("Test");
        userWithoutNorm.setAge(30);
        userWithoutNorm.setWeight(70.0);
        userWithoutNorm.setHeight(170.0);
        userWithoutNorm.setGoal(Goal.WEIGHT_LOSS);

        User userWithNorm = new User(); // Копия с рассчитанной нормой
        userWithNorm.setName("Test");
        userWithNorm.setAge(30);
        userWithNorm.setWeight(70.0);
        userWithNorm.setHeight(170.0);
        userWithNorm.setGoal(Goal.WEIGHT_LOSS);
        userWithNorm.calculateDailyCalorieNorm(); // Рассчитываем норму

        when(modelMapper.map(inputDTO, User.class)).thenReturn(userWithoutNorm);
        when(userRepository.save(userWithoutNorm)).thenReturn(userWithNorm);
        when(modelMapper.map(userWithNorm, UserDTO.class)).thenReturn(inputDTO);

        // Act
        UserDTO result = userService.createUser(inputDTO);

        // Assert
        verify(userRepository).save(userWithoutNorm);
        assertNotNull(userWithNorm.getDailyCalorieNorm());
        assertTrue(userWithNorm.getDailyCalorieNorm() > 0);
    }
}
