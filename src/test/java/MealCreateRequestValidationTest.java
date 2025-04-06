import calorietracker.dto.MealCreateRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MealCreateRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsCorrectThenNoViolations() {
        // Arrange
        MealCreateRequest request = new MealCreateRequest(
                1L,
                LocalDateTime.now(),
                List.of(1L, 2L)
        );

        // Act
        Set<ConstraintViolation<MealCreateRequest>> violations = validator.validate(request);

        // Assert
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenUserIdIsNullThenOneViolation() {
        // Arrange
        MealCreateRequest request = new MealCreateRequest(
                null,
                LocalDateTime.now(),
                List.of(1L)
        );

        // Act
        Set<ConstraintViolation<MealCreateRequest>> violations = validator.validate(request);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("userId must not be null", violations.iterator().next().getMessage());
        assertEquals("userId", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void whenMealTimeIsNullThenOneViolation() {
        // Arrange
        MealCreateRequest request = new MealCreateRequest(
                1L,
                null,
                List.of(1L)
        );

        // Act
        Set<ConstraintViolation<MealCreateRequest>> violations = validator.validate(request);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("mealTime must not be null", violations.iterator().next().getMessage());
        assertEquals("mealTime", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void whenDishIdsIsNullThenOneViolation() {
        // Arrange
        MealCreateRequest request = new MealCreateRequest(
                1L,
                LocalDateTime.now(),
                null
        );

        // Act
        Set<ConstraintViolation<MealCreateRequest>> violations = validator.validate(request);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("dishIds must not be null", violations.iterator().next().getMessage());
        assertEquals("dishIds", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void whenAllFieldsInvalidThenThreeViolations() {
        // Arrange
        MealCreateRequest request = new MealCreateRequest(null, null, null);

        // Act
        Set<ConstraintViolation<MealCreateRequest>> violations = validator.validate(request);

        // Assert
        assertEquals(3, violations.size());
    }
}