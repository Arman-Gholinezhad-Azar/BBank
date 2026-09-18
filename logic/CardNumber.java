package logic;

import java.util.Objects;
import java.util.regex.Pattern;

public final class CardNumber {

    private static final int VALID_LENGTH = 16;
    private static final Pattern PATTERN = Pattern.compile("^[0-9]+$");

    private final String value;

    public CardNumber(String cardNumber) {
        Objects.requireNonNull(cardNumber, "Card number cannot be NULL!");

        String normalized = cardNumber.trim();

        if (normalized.length() != VALID_LENGTH) {
            throw new IllegalArgumentException(
                String.format("Card Number must be %d digits!", VALID_LENGTH)
            );
        }

        if (!PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException(
                "Card Number must contain only digits"
            );
        }

        this.value = normalized;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "CardNumber{**** **** **** " + value.substring(12) + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardNumber that = (CardNumber) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}