package logic;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

public final class CardNumber {

    private static final int VALID_LENGTH = 16;
    /** Iranian BIN assigned to cards issued by this application. */
    public static final String ISSUER_BIN = "62198619";
    private static final Pattern PATTERN = Pattern.compile("^[0-9]+$");
    private static final AtomicLong NEXT_ACCOUNT_SEQUENCE = new AtomicLong();

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

    /**
     * Issues the next card number for this process. The final digit is a Luhn
     * check digit, so accidental transcription errors can be detected later.
     * A durable deployment should source the sequence from its database.
     */
    public static CardNumber issue() {
        long sequence = Math.floorMod(NEXT_ACCOUNT_SEQUENCE.incrementAndGet(), 10_000_000L);
        String prefix = ISSUER_BIN + String.format("%07d", sequence);
        return new CardNumber(prefix + calculateLuhnCheckDigit(prefix));
    }

    private static int calculateLuhnCheckDigit(String firstFifteenDigits) {
        int sum = 0;
        for (int index = 0; index < firstFifteenDigits.length(); index++) {
            int digit = firstFifteenDigits.charAt(index) - '0';
            if (index % 2 == 0) {
                digit *= 2;
                if (digit > 9) digit -= 9;
            }
            sum += digit;
        }
        return (10 - (sum % 10)) % 10;
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
