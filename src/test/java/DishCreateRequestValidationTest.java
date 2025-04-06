import calorietracker.dto.DishCreateRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DishCreateRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsValidThenNoViolations() {
        // Arrange
        DishCreateRequest request = new DishCreateRequest(
                "Greek Salad",
                120.0,
                4.5,
                10.0,
                6.0
        );

        // Act
        Set<ConstraintViolation<DishCreateRequest>> violations = validator.validate(request);

        // Assert
        assertTrue(violations.isEmpty(), "Должно быть 0 нарушений при валидных данных");
    }

    @Test
    void whenNameIsBlankThenOneViolation() {
        // Arrange
        DishCreateRequest request = new DishCreateRequest(
                " ",
                120.0,
                4.5,
                10.0,
                6.0
        );

        // Act
        Set<ConstraintViolation<DishCreateRequest>> violations = validator.validate(request);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<DishCreateRequest> violation = violations.iterator().next();
        assertEquals("name must not be null", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
    }

    @Test
    void whenCaloriesNegativeThenOneViolation() {
        // Arrange
        DishCreateRequest request = new DishCreateRequest(
                "Greek Salad",
                -50.0,
                4.5,
                10.0,
                6.0
        );

        // Act
        Set<ConstraintViolation<DishCreateRequest>> violations = validator.validate(request);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<DishCreateRequest> violation = violations.iterator().next();
        assertEquals("caloriesPerServing must be > 0", violation.getMessage());
        assertEquals("caloriesPerServing", violation.getPropertyPath().toString());
    }

    @Test
    void whenProteinNegativeThenOneViolation() {
        // Arrange
        DishCreateRequest request = new DishCreateRequest(
                "Greek Salad",
                120.0,
                -1.0,
                10.0,
                6.0
        );

        // Act
        Set<ConstraintViolation<DishCreateRequest>> violations = validator.validate(request);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<DishCreateRequest> violation = violations.iterator().next();
        assertEquals("protein must be >= 0", violation.getMessage());
        assertEquals("protein", violation.getPropertyPath().toString());
    }

    @Test
    void whenAllNutrientsInvalidThenMultipleViolations() {
        // Arrange
        DishCreateRequest request = new DishCreateRequest(
                "",
                -100.0,  // Отрицательные калории
                -5.0,
                -2.0,
                -10.0
        );

        // Act
        Set<ConstraintViolation<DishCreateRequest>> violations = validator.validate(request);

        // Assert
        assertEquals(5, violations.size());
    }

    @Test
    void whenNameIsNullThenOneViolation() {
        // Arrange
        DishCreateRequest request = new DishCreateRequest(
                null,
                120.0,
                4.5,
                10.0,
                6.0
        );

        // Act
        Set<ConstraintViolation<DishCreateRequest>> violations = validator.validate(request);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<DishCreateRequest> violation = violations.iterator().next();
        assertEquals("name must not be null", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
    }
}