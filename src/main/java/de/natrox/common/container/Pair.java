package de.natrox.common.container;

/**
 * This class can capture 2 references of 2 types and set or clear the data using first() and
 * second(). It can be used to return multiple objects of a method, or to easily capture multiple
 * objects without creating their own class.
 *
 * @param <A> the first type, which you want to define
 * @param <B> the second type which you want to define
 */
public record Pair<A, B>(A first, B second) {

    /**
     * Creates a new pair.
     *
     * @param x   first value
     * @param y   second value
     * @param <X> type of first value
     * @param <Y> type of second value
     * @return the new created pair
     */
    public static <X, Y> Pair<X, Y> of(X x, Y y) {
        return new Pair<>(x, y);
    }

}
