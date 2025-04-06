import calorietracker.dto.UserCreateRequest;
import calorietracker.model.Goal;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserCreateRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsValidThenNoViolations() {
        // Arrange
        UserCreateRequest request = new UserCreateRequest(
                "Maks",
                "Maks@example.com",
                20,
                80.0,
                185.0,
                Goal.MAINTENANCE
        );
        // Act
        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(request);
        // Assert
        assertTrue(violations.isEmpty(), "Должно быть 0 нарушений при валидных данных");
    }

    @Test
    void whenNameIsBlankThenOneViolation() {
        // Arrange
        UserCreateRequest request = new UserCreateRequest( " ", "email@test.com", 30, 70.0, 175.0, Goal.MAINTENANCE);
        // Act
        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(request);
        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<UserCreateRequest> violation = violations.iterator().next();
        assertEquals("name must not be null", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
    }

    @Test
    void whenEmailIsInvalidThenOneViolation() {
        // Arrange
        UserCreateRequest request = new UserCreateRequest( "John", "invalid-email", 30, 70.0, 175.0, Goal.MAINTENANCE);
        // Act
        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(request);
        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<UserCreateRequest> violation = violations.iterator().next();
        assertEquals("input correct email please", violation.getMessage());
        assertEquals("email", violation.getPropertyPath().toString());
    }

    @Test
    void whenAgeIsZeroThenOneViolation() {
        // Arrange
        UserCreateRequest request = new UserCreateRequest( "John", "john@test.com", 0, 70.0, 175.0, Goal.MAINTENANCE);
        // Act
        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(request);
        // Assert
        assertEquals(1, violations.size());
        assertEquals("age must be greater than or equal to 1", violations.iterator().next().getMessage());
    }

    @Test
    void whenAgeIs121ThenOneViolation() {
        // Arrange
        UserCreateRequest request = new UserCreateRequest( "John", "john@test.com", 121, 70.0, 175.0, Goal.MAINTENANCE);
        // Act
        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(request);
        // Assert
        assertEquals(1, violations.size());
        assertEquals("age must be less than or equal to 120", violations.iterator().next().getMessage());
    }

    @Test
    void whenWeightIs29ThenOneViolation() {
        // Arrange
        UserCreateRequest request = new UserCreateRequest( "John", "john@test.com", 30, 29.0, 175.0, Goal.MAINTENANCE);
        // Act
        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(request);
        // Assert
        assertEquals(1, violations.size());
        assertEquals("weight must be greater than or equal to 30", violations.iterator().next().getMessage());
    }

    @Test
    void whenWeightIs301ThenOneViolation() {
        // Arrange
        UserCreateRequest request = new UserCreateRequest( "John", "john@test.com", 30, 301.0, 175.0, Goal.MAINTENANCE);
        // Act
        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(request);
        // Assert
        assertEquals(1, violations.size());
        assertEquals("weight must be less than or equal to 300", violations.iterator().next().getMessage());
    }

    @Test
    void whenHeightIs99ThenOneViolation() {
        // Arrange
        UserCreateRequest request = new UserCreateRequest( "John", "john@test.com", 30, 70.0, 99.0, Goal.MAINTENANCE);
        // Act
        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(request);
        // Assert
        assertEquals(1, violations.size());
        assertEquals("height must be greater than or equal to 100", violations.iterator().next().getMessage());
    }

    @Test
    void whenHeightIs251ThenOneViolation() {
        // Arrange
        UserCreateRequest request = new UserCreateRequest("John", "john@test.com", 30, 70.0, 251.0, Goal.MAINTENANCE);
        // Act
        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(request);
        // Assert
        assertEquals(1, violations.size());
        assertEquals("height must be less than or equal to 250", violations.iterator().next().getMessage());
    }

    @Test
    void whenAllFieldsInvalidThenMultipleViolations() {
        // Arrange
        UserCreateRequest dto = new UserCreateRequest(
                " ",
                "invalid",
                0,
                20.0,
                90.0,
                null
        );
        // Act
        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(dto);
        // Assert
        assertEquals(6, violations.size());
    }
}
