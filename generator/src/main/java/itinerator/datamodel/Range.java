package itinerator.datamodel;

import java.util.Objects;

public class Range<T extends Comparable<? super T>> {

    private final T start;
    private final T end;

    public static <T extends Comparable<? super T>> Range<T> of(T start, T end) {
        if (valueIsBefore(start, end)) {
            return new Range<>(start, end);
        } else {
            return new WrappingRange<>(start, end);
        }
    }

    private Range(T start, T end) {
        this.start = start;
        this.end = end;
    }

    public T getStart() {
        return start;
    }

    public T getEnd() {
        return end;
    }

    public boolean contains(T value) {
        return isAtOrAfterStart(value) && isAtOrBeforeEnd(value);
    }

    public boolean contains(Range<T> range) {
        return contains(range.start) && contains(range.end);
    }

    public boolean overlaps(Range<T> range) {
        return isAtOrAfterStart(range.end) && isAtOrBeforeEnd(range.start);
    }

    private boolean isAtOrAfterStart(T value) {
        return valueIsAtOrAfter(value, start);
    }

    private boolean isAtOrBeforeEnd(T value) {
        return valueIsBefore(value, end);
    }

    protected static <T extends Comparable<? super T>> boolean valueIsAtOrAfter(T value, T compare) {
        return value.compareTo(compare) >= 0;
    }

    protected static <T extends Comparable<? super T>> boolean valueIsBefore(T value, T compare) {
        return value.compareTo(compare) <= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        @SuppressWarnings("unchecked") Range<T> range = (Range<T>) o;
        return Objects.equals(start, range.start) &&
                Objects.equals(end, range.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    private static class WrappingRange<T extends Comparable<? super T>> extends Range<T> {
        private WrappingRange(T start, T end) {
            super(start, end);
        }

        @Override
        public boolean contains(T value) {
            return valueIsWithinBounds(value, getStart(), getEnd());
        }

        @Override
        public boolean contains(Range<T> range) {
            return contains(range.start) && valueIsWithinBounds(range.end, range.start, getEnd());
        }

        private static <T extends Comparable<? super T>> boolean valueIsWithinBounds(T value, T lower, T upper) {
            return valueIsAtOrAfter(value, lower) || valueIsBefore(value, upper);
        }
    }
}
