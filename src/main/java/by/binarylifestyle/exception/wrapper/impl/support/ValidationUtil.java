package by.binarylifestyle.exception.wrapper.impl.support;

import java.util.Arrays;
import java.util.Objects;

public final class ValidationUtil {
    public static void requireNotNull(Object o, String name) {
        if (o == null) {
            throw new IllegalArgumentException(String.format("`%s` is null", name));
        }
    }

    public static void requireAllNotNull(Object[] array, String name) {
        if (Arrays.stream(array).anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException(String.format("`%s` contains null(s)", name));
        }
    }

    public static void requireNotEmpty(Object[] array, String name) {
        if (array.length == 0) {
            throw new IllegalArgumentException(String.format("`%s` is empty", name));
        }
    }
}
