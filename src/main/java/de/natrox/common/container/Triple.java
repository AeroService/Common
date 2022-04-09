package de.natrox.common.container;

/**
 * Contains a immutable triple of three values.
 *
 * @param <A> value A
 * @param <B> value B
 * @param <C> value C
 */
public record Triple<A, B, C>(A first, B second, C third) {

    /**
     * Creates a new triple.
     *
     * @param x   first value
     * @param y   second value
     * @param z   third value
     * @param <X> type of first value
     * @param <Y> type of second value
     * @param <Z> type of third value
     * @return the new created triple
     */
    public static <X, Y, Z> Triple<X, Y, Z> of(X x, Y y, Z z) {
        return new Triple<>(x, y, z);
    }
}
