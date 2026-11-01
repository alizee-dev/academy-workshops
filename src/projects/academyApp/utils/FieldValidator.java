package projects.academyApp.utils;

public final class FieldValidator {
    private FieldValidator () {

    }

    public static String requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank.");
        }
        return value.trim();
    }

    public static int requirePositive(int value, String fieldName) {
        if(value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be greater than zero");
        }
        return value;
    }

    public static <T> T requireNonNull(T value, String fieldName) {
        if(value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null.");
        }
        return value;
    }
}
