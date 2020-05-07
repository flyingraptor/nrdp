package com.nikosraptis.nrdp.dataprocess.percentile;

import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PercentileCalculatorUnitTest {

    private PercentileCalculator classUnderTest = new PercentileCalculator();

    @Rule
    public ExpectedException exceptionGrabber = ExpectedException.none();

    @Test
    public void testWith10PercentAnd5UniqueNumbers() {

        /* Preparation */
        List<Long> numbers = create5UniqueNumbers();
        /* Boundaries of percentiles of these 5 numbers are */
        /* 10th 1000 */
        /* 25th 1500 */
        /* 50th 3000 */
        /* 75th 4500 */
        /* 90th 5000 */

        /* Execution */
        long percentile = classUnderTest.percentileOf(numbers, 10); // 10% Percentile

        /* Validation */
        assertThat(percentile, is(1000L)); // It's falling under the 1st boundary so 1000 should be returned
    }

    @Test
    public void testWith25PercentAnd5UniqueNumbers() {

        /* Preparation */
        List<Long> numbers = create5UniqueNumbers();
        /* Boundaries of percentiles of these 5 numbers are */
        /* 10th 1000 */
        /* 25th 1500 */
        /* 50th 3000 */
        /* 75th 4500 */
        /* 90th 5000 */

        /* Execution */
        long percentile = classUnderTest.percentileOf(numbers, 25); // 25% Percentile

        /* Validation */
        assertThat(percentile, is(2000L));
    }

    @Test
    public void testWith50PercentAnd5UniqueNumbers() {

        /* Preparation */
        List<Long> numbers = create5UniqueNumbers();

        /* Execution */
        long percentile = classUnderTest.percentileOf(numbers, 50); // 50% Percentile

        /* Validation */
        assertThat(percentile, is(3000L));
    }

    @Test
    public void testWith75PercentAnd5UniqueNumbers() {

        /* Preparation */
        List<Long> numbers = create5UniqueNumbers();

        /* Execution */
        long percentile = classUnderTest.percentileOf(numbers, 75); // 75% Percentile

        /* Validation */
        assertThat(percentile, is(4000L));
    }

    @Test
    public void testWith95PercentAnd5UniqueNumbers() {

        /* Preparation */
        List<Long> numbers = create5UniqueNumbers();

        /* Execution */
        long percentile = classUnderTest.percentileOf(numbers, 95); // 95% Percentile

        /* Validation */
        assertThat(percentile, is(5000L));
    }

    @Test
    public void testWith95PercentAnd1Number() {

        /* Preparation */
        List<Long> numbers = create5Number();

        /* Execution */
        long percentile = classUnderTest.percentileOf(numbers, 95); // 95% Percentile

        /* Validation */
        assertThat(percentile, is(1000L));
    }

    @Test
    public void testWith10PercentAnd1Number() {

        /* Preparation */
        List<Long> numbers = create5Number();

        /* Execution */
        long percentile = classUnderTest.percentileOf(numbers, 10); // 10% Percentile

        /* Validation */
        assertThat(percentile, is(1000L));
    }

    @Test
    public void testWith95PercentAndNoNumbers() {

        /* Preparation */
        List<Long> numbers = new ArrayList<>();

        /* Execution */
        long percentile = classUnderTest.percentileOf(numbers, 95);

        /* Validation */
        assertThat(percentile, is(0L));
    }

    @Test
    public void testWith0PercentAnd5Numbers() {

        /* Preparation */
        List<Long> numbers = create5Number();

        /* Validation */
        exceptionGrabber.expect(PercentileException.class);
        classUnderTest.percentileOf(numbers, 0);
    }

    @Test
    public void testWithNullList() {
        /* Validation */
        exceptionGrabber.expect(PercentileException.class);
        classUnderTest.percentileOf(null, 95);
    }

    private List<Long> create5Number() {

        List<Long> numbers = new ArrayList<>();
        numbers.add(1000L);
        return numbers;
    }

    private List<Long> create5UniqueNumbers() {

        List<Long> numbers = new ArrayList<>();
        numbers.add(1000L);
        numbers.add(2000L);
        numbers.add(3000L);
        numbers.add(4000L);
        numbers.add(5000L);
        return numbers;
    }

}