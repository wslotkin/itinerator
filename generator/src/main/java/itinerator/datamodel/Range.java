package itinerator.datamodel;

import java.util.Objects;

public class Range<T extends Comparable<? super T>> {

    private final T start;
    private final T end;

    public static <T extends Comparable<? super T>> Range<T> of(T start, T end) {
        return new Range<>(start, end);
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
        return value.compareTo(start) >= 0;
    }

    private boolean isAtOrBeforeEnd(T value) {
        return value.compareTo(end) <= 0;
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
}
