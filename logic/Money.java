package logic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Small utility for handling money values.
 *
 * <p>In real banking software, money types and rounding rules are typically
 * encapsulated by a dedicated Money type (with currency). For this exercise we
 * normalize to 2 decimal places and use BigDecimal.</p>
 */
public final class Money {

    private Money() {
    }

    public static BigDecimal normalize(BigDecimal value) {
        Objects.requireNonNull(value, "value");
        try {
            return value.setScale(2, RoundingMode.UNNECESSARY);
        } catch (ArithmeticException ex) {
            throw new IllegalArgumentException("value must have at most 2 decimal places", ex);
        }
    }

    public static void validatePositive(BigDecimal value, String fieldName) {
        Objects.requireNonNull(value, fieldName);
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(fieldName + " must be positive");
        }
    }
}
