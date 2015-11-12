package org.books.util;

/**
 * Helper Class for localizing enum values
 */
public final class EnumTranslator {

    public static String getMessageKey(Enum<?> e) {
        return e.getClass().getSimpleName() + '.' + e.name();
    }

}
