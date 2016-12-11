package com.github.wslotkin.itinerator.generator.datamodel;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RangeTest {

    private Range<Integer> fiveToNineRange;
    private Range<Integer> nineToFiveRange;

    @Before
    public void before() {
        fiveToNineRange = Range.of(5, 9);
        nineToFiveRange = Range.of(9, 5);
    }

    @Test
    public void testContainsValueForOrderedRange() {
        verifyContains(fiveToNineRange, 5, 6, 7, 8, 9);
        verifyDoesNotContain(fiveToNineRange, 1, 2, 3, 4, 10);
    }

    @Test
    public void testContainsRangeForOrderedRange() {
        verifyContains(fiveToNineRange, Range.of(5, 9), Range.of(5, 8), Range.of(6, 9), Range.of(6, 8));
        verifyDoesNotContain(fiveToNineRange, Range.of(1, 2), Range.of(4, 10), Range.of(4, 9), Range.of(5, 10));
    }

    @Test
    public void testContainsValueForWrappingRange() {
        verifyContains(nineToFiveRange, 1, 2, 3, 4, 5, 9, 10);
        verifyDoesNotContain(nineToFiveRange, 6, 7, 8);
    }

    @Test
    public void testContainsRangeForWrappingRange() {
        verifyContains(nineToFiveRange, Range.of(9, 10), Range.of(10, 11), Range.of(1, 2), Range.of(4, 5));
        verifyDoesNotContain(nineToFiveRange, Range.of(4, 6), Range.of(5, 9), Range.of(6, 9), Range.of(6, 10));
    }

    @Test
    public void testContainsWrappingRangeForOrderedRange() {
        verifyDoesNotContain(fiveToNineRange, Range.of(9, 5), Range.of(2, 1), Range.of(9, 10), Range.of(4, 5));
    }

    @Test
    public void testContainsWrappingRangeForWrappingRange() {
        verifyContains(nineToFiveRange, Range.of(9, 5), Range.of(9, 4), Range.of(10, 5), Range.of(10, 4));
        verifyDoesNotContain(nineToFiveRange, Range.of(9, 6), Range.of(8, 5), Range.of(8, 6));
    }

    @SafeVarargs
    private static <T extends Comparable<T>> void verifyContains(Range<T> range, T... inputs) {
        for (T input : inputs) {
            assertTrue(range.contains(input));
        }
    }

    @SafeVarargs
    private static <T extends Comparable<T>> void verifyDoesNotContain(Range<T> range, T... inputs) {
        for (T input : inputs) {
            assertFalse(range.contains(input));
        }
    }

    @SafeVarargs
    private static <T extends Comparable<T>> void verifyContains(Range<T> range, Range<T>... inputs) {
        for (Range<T> input : inputs) {
            assertTrue(range.contains(input));
        }
    }

    @SafeVarargs
    private static <T extends Comparable<T>> void verifyDoesNotContain(Range<T> range, Range<T>... inputs) {
        for (Range<T> input : inputs) {
            assertFalse("Unexpected result for range: " + input, range.contains(input));
        }
    }
}