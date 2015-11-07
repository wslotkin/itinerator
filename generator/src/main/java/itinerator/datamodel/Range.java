package itinerator.datamodel;

import java.util.Objects;

public abstract class Range<T extends Comparable<? super T>> {

    protected final T start;
    protected final T end;

    public static <T extends Comparable<? super T>> Range<T> of(T start, T end) {
        if (valueIsAtOrAfter(end, start)) {
            return new OrderedRange<>(start, end);
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

    public abstract boolean contains(T value);

    public boolean contains(Range<T> range) {
        if (range instanceof OrderedRange) {
            return containsOrderedRange((OrderedRange<T>) range);
        } else {
            return containsWrappingRange((WrappingRange<T>) range);
        }
    }

    protected abstract boolean containsOrderedRange(OrderedRange<T> range);

    protected abstract boolean containsWrappingRange(WrappingRange<T> range);

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

    @Override
    public String toString() {
        return "Range{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

    private static <T extends Comparable<? super T>> boolean valueIsAtOrAfter(T value, T compare) {
        return value.compareTo(compare) >= 0;
    }

    private static <T extends Comparable<? super T>> boolean valueIsBefore(T value, T compare) {
        return value.compareTo(compare) <= 0;
    }

    private static <T extends Comparable<? super T>> boolean valueIsWithinBounds(T value, T lower, T upper) {
        return valueIsAtOrAfter(value, lower) || valueIsBefore(value, upper);
    }

    private static class OrderedRange<T extends Comparable<? super T>> extends Range<T> {

        private OrderedRange(T start, T end) {
            super(start, end);
        }

        public boolean contains(T value) {
            return valueIsAtOrAfter(value, start) && valueIsBefore(value, end);
        }

        @Override
        protected boolean containsOrderedRange(OrderedRange<T> range) {
            return contains(range.start) && contains(range.end);
        }

        @Override
        protected boolean containsWrappingRange(WrappingRange<T> range) {
            return false;
        }

    }

    private static class WrappingRange<T extends Comparable<? super T>> extends Range<T> {

        private WrappingRange(T start, T end) {
            super(start, end);
        }

        @Override
        public boolean contains(T value) {
            return valueIsWithinBounds(value, start, end);
        }

        @Override
        public boolean contains(Range<T> range) {
            boolean rangeStartIsBeforeEnd = valueIsAtOrAfter(getEnd(), range.start);
            if (rangeStartIsBeforeEnd) {
                return contains(range.start)
                        && (valueIsAtOrAfter(range.end, range.start) && valueIsBefore(range.end, getEnd()));
            } else {
                return contains(range.start) && (valueIsWithinBounds(range.end, range.start, getEnd()));
            }
        }

        @Override
        protected boolean containsOrderedRange(OrderedRange<T> range) {
            if (valueIsAtOrAfter(range.start, start)) {
                return valueIsAtOrAfter(range.end, start);
            } else {
                return valueIsBefore(range.start, end) && valueIsBefore(range.end, end);
            }
        }

        @Override
        protected boolean containsWrappingRange(WrappingRange<T> range) {
            return contains(range.start) && contains(range.end);
        }
    }
}
