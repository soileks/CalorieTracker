import calorietracker.dto.UserDTO;
import calorietracker.model.Goal;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsValidThenNoViolations() {
        // Arrange
        UserDTO dto = new UserDTO(
                1L,
                "Maks",
                "Maks@example.com",
                20,
                80.0,
                185.0,
                Goal.MAINTENANCE,
                2000.0
        );

        // Act
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "Должно быть 0 нарушений при валидных данных");
    }

    @Test
    void whenNameIsBlankThenOneViolation() {
        UserDTO dto = new UserDTO(1L, " ", "email@test.com", 30, 70.0, 175.0, Goal.MAINTENANCE, 2000.0);
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        ConstraintViolation<UserDTO> violation = violations.iterator().next();
        assertEquals("name must not be null", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
    }

    @Test
    void whenEmailIsInvalidThenOneViolation() {
        UserDTO dto = new UserDTO(1L, "John", "invalid-email", 30, 70.0, 175.0, Goal.MAINTENANCE, 2000.0);
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        ConstraintViolation<UserDTO> violation = violations.iterator().next();
        assertEquals("input correct email please", violation.getMessage());
        assertEquals("email", violation.getPropertyPath().toString());
    }

    @Test
    void whenAgeIsZeroThenOneViolation() {
        UserDTO dto = new UserDTO(1L, "John", "john@test.com", 0, 70.0, 175.0, Goal.MAINTENANCE, 2000.0);
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("age must be greater than or equal to 1", violations.iterator().next().getMessage());
    }

    @Test
    void whenAgeIs121ThenOneViolation() {
        UserDTO dto = new UserDTO(1L, "John", "john@test.com", 121, 70.0, 175.0, Goal.MAINTENANCE, 2000.0);
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("age must be less than or equal to 120", violations.iterator().next().getMessage());
    }

    @Test
    void whenWeightIs29ThenOneViolation() {
        UserDTO dto = new UserDTO(1L, "John", "john@test.com", 30, 29.0, 175.0, Goal.MAINTENANCE, 2000.0);
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("weight must be greater than or equal to 30", violations.iterator().next().getMessage());
    }

    @Test
    void whenWeightIs301ThenOneViolation() {
        UserDTO dto = new UserDTO(1L, "John", "john@test.com", 30, 301.0, 175.0, Goal.MAINTENANCE, 2000.0);
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("weight must be less than or equal to 300", violations.iterator().next().getMessage());
    }

    @Test
    void whenHeightIs99ThenOneViolation() {
        UserDTO dto = new UserDTO(1L, "John", "john@test.com", 30, 70.0, 99.0, Goal.MAINTENANCE, 2000.0);
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("height must be greater than or equal to 100", violations.iterator().next().getMessage());
    }

    @Test
    void whenHeightIs251ThenOneViolation() {
        UserDTO dto = new UserDTO(1L, "John", "john@test.com", 30, 70.0, 251.0, Goal.MAINTENANCE, 2000.0);
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("height must be less than or equal to 250", violations.iterator().next().getMessage());
    }

    @Test
    void whenAllFieldsInvalidThenMultipleViolations() {
        UserDTO dto = new UserDTO(
                null,
                " ",
                "invalid",
                0,
                20.0,
                90.0,
                null,
                1800.0
        );

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(dto);

        assertEquals(7, violations.size());
    }
}
