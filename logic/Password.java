package logic;

import java.util.Objects;
import java.util.regex.Pattern;

public final class Password {

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 32;

    private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9@#$%]+$");

    private final String value;

    public Password(String password) {
        Objects.requireNonNull(password, "Password cannot be null");


        if (password.length() < MIN_LENGTH || password.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                String.format("Password must be between %d and %d characters", MIN_LENGTH, MAX_LENGTH)
            );
        }

        if (!PATTERN.matcher(password).matches()) {
            throw new IllegalArgumentException(
                "Password can only contain: a-z, A-Z, 0-9, @, #, $, %"
            );
        }

        this.value = password;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Password{****}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return value.equals(password.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}