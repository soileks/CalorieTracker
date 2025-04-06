import calorietracker.dto.UserCreateRequest;
import calorietracker.dto.UserDTO;
import calorietracker.exception.ResourceNotFoundException;
import calorietracker.model.Goal;
import calorietracker.model.User;
import calorietracker.repository.UserRepository;
import calorietracker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserDTO testUserDTO;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .age(30)
                .weight(70.0)
                .height(170.0)
                .goal(Goal.MAINTENANCE)
                .dailyCalorieNorm(2000)
                .build();

        testUserDTO = new UserDTO(
                1L, "Test User", "test@example.com",
                30, 70.0, 170.0, Goal.MAINTENANCE, 2000
        );
    }

    @Test
    void createUserWithValidDataReturnsUserDTO() {
        // Arrange
        UserCreateRequest input = new UserCreateRequest(
                 "New User", "new@example.com",
                25, 65.0, 165.0, Goal.WEIGHT_LOSS
        );

        User newUser = User.builder()
                .name("New User")
                .email("new@example.com")
                .age(25)
                .weight(65.0)
                .height(165.0)
                .goal(Goal.WEIGHT_LOSS)
                .build();

        User savedUser = User.builder()
                .id(2L)
                .name("New User")
                .email("new@example.com")
                .age(25)
                .weight(65.0)
                .height(165.0)
                .goal(Goal.WEIGHT_LOSS)
                .dailyCalorieNorm(1800)
                .build();

        UserDTO expectedDTO = new UserDTO(
                2L, "New User", "new@example.com",
                25, 65.0, 165.0, Goal.WEIGHT_LOSS, 1800
        );

        when(modelMapper.map(input, User.class)).thenReturn(newUser);
        when(userRepository.save(newUser)).thenReturn(savedUser);
        when(modelMapper.map(savedUser, UserDTO.class)).thenReturn(expectedDTO);

        // Act
        UserDTO result = userService.createUser(input);

        // Assert
        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("New User", result.getName());
        assertEquals(1800, result.getDailyCalorieNorm());
        verify(userRepository).save(newUser);
    }

    @Test
    void getUserByIdWhenUserExistsReturnsUserDTO() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(modelMapper.map(testUser, UserDTO.class)).thenReturn(testUserDTO);

        // Act
        UserDTO result = userService.getUserById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test User", result.getName());
    }

    @Test
    void getUserByIdWhenUserNotExistsThrowsException() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(999L);
        });
    }

    @Test
    void getAllUsersReturnsListOfUserDTOs() {
        // Arrange
        User secondUser = User.builder()
                .id(2L)
                .name("Second User")
                .email("second@example.com")
                .age(25)
                .weight(60.0)
                .height(160.0)
                .goal(Goal.WEIGHT_LOSS)
                .dailyCalorieNorm(1700)
                .build();

        UserDTO secondUserDTO = new UserDTO(
                2L, "Second User", "second@example.com",
                25, 60.0, 160.0, Goal.WEIGHT_LOSS, 1700
        );

        when(userRepository.findAll()).thenReturn(List.of(testUser, secondUser));
        when(modelMapper.map(testUser, UserDTO.class)).thenReturn(testUserDTO);
        when(modelMapper.map(secondUser, UserDTO.class)).thenReturn(secondUserDTO);

        // Act
        List<UserDTO> result = userService.getAllUsers();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Test User", result.get(0).getName());
        assertEquals("Second User", result.get(1).getName());
    }

    @Test
    void updateUserWithValidDataReturnsUpdatedUserDTO() {
        // Arrange
        UserCreateRequest updateDTO = new UserCreateRequest(
                "Updated Name", "updated@example.com",
                31, 71.0, 171.0, Goal.WEIGHT_GAIN
        );

        User updatedUser = User.builder()
                .id(1L)
                .name("Updated Name")
                .email("updated@example.com")
                .age(31)
                .weight(71.0)
                .height(171.0)
                .goal(Goal.WEIGHT_GAIN)
                .dailyCalorieNorm(2200)
                .build();

        UserDTO expectedDTO = new UserDTO(
                1L, "Updated Name", "updated@example.com",
                31, 71.0, 171.0, Goal.WEIGHT_GAIN, 2200
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(modelMapper.map(updatedUser, UserDTO.class)).thenReturn(expectedDTO);

        // Act
        UserDTO result = userService.updateUser(1L, updateDTO);

        // Assert
        assertEquals("Updated Name", result.getName());
        assertEquals("updated@example.com", result.getEmail());
        assertEquals(31, result.getAge());
        assertEquals(Goal.WEIGHT_GAIN, result.getGoal());
    }

    @Test
    void updateUserWhenUserNotExistsThrowsException() {
        // Arrange
        UserCreateRequest updateDTO = new UserCreateRequest(
                 "Updated", "updated@example.com",
                31, 71.0, 171.0, Goal.WEIGHT_GAIN
        );

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateUser(999L, updateDTO);
        });
    }


    @Test
    void deleteUserWhenUserExistsDeletesUser() {
        // Arrange
        when(userRepository.existsById(1L)).thenReturn(true);

        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUserWhenUserNotExistsThrowsException() {
        // Arrange
        when(userRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteUser(999L);
        });
    }


    @Test
    void getUserByEmailWhenUserExistsReturnsUserDTO() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(modelMapper.map(testUser, UserDTO.class)).thenReturn(testUserDTO);

        // Act
        UserDTO result = userService.getUserByEmail("test@example.com");

        // Assert
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void getUserByEmailWhenUserNotExistsThrowsException() {
        // Arrange
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserByEmail("nonexistent@example.com");
        });
    }


    @Test
    void updateDailyCalorieNormWithValidDataReturnsUpdatedUserDTO() {
        // Arrange
        double newNorm = 2100;
        User updatedUser = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .age(30)
                .weight(70.0)
                .height(170.0)
                .goal(Goal.MAINTENANCE)
                .dailyCalorieNorm(newNorm)
                .build();

        UserDTO expectedDTO = new UserDTO(
                1L, "Test User", "test@example.com",
                30, 70.0, 170.0, Goal.MAINTENANCE, newNorm
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(modelMapper.map(updatedUser, UserDTO.class)).thenReturn(expectedDTO);

        // Act
        UserDTO result = userService.updateDailyCalorieNorm(1L, newNorm);

        // Assert
        assertEquals(newNorm, result.getDailyCalorieNorm());
        verify(userRepository).save(argThat(user ->
                user.getDailyCalorieNorm() == newNorm));
    }

    @Test
    void updateDailyCalorieNormWhenUserNotExistsThrowsException() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateDailyCalorieNorm(999L, 2100);
        });
    }
}