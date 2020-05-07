package com.nikosraptis.nrdp.dataprocess.ride;

import com.nikosraptis.nrdp.dataprocess.DistanceCalculatorStrategy;

import static org.junit.Assert.assertThat;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class HaversineFormulaUnitTest {

    private DistanceCalculatorStrategy classUnderTest = new HaversineFormula();

    @Rule
    public ExpectedException exceptionGrabber = ExpectedException.none();
    
    @Test
    public void testDistanceForTwoDifferentPoints() {

        /* Preparation */
        RoutePoint startPoint = new RoutePoint(38.017711, 23.834016, 0L);
        RoutePoint endPoint = new RoutePoint(37.998360, 23.772984, 0L);

        /* Execution */
        double distance = classUnderTest.betweenTwoPoints(startPoint, endPoint);

        /* Validation */
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_EVEN);

        String expectedDistance = "5.77";
        assertThat(df.format(distance), is(expectedDistance));
    }

    @Test
    public void testDistanceForThreeDifferentPoints() {

        /* Preparation */
        RoutePoint startPoint = new RoutePoint(38.017711, 23.834016, 0L);
        RoutePoint middletPoint = new RoutePoint(38.013710, 23.820177, 0L);
        RoutePoint endPoint = new RoutePoint(37.998360, 23.772984, 0L);

        List<RoutePoint> route = new ArrayList<>();
        route.add(startPoint);
        route.add(middletPoint);
        route.add(endPoint);

        /* Execution */
        double distance = classUnderTest.sumBetweenListOfPoints(route);

        /* Validation */
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_EVEN);

        String expectedDistance = "5.77";
        assertThat(df.format(distance), is(expectedDistance));
    }

    @Test
    public void testDistanceForTheSamePointTwice() {

        /* Preparation */
        RoutePoint startPoint = new RoutePoint(38.017711, 38.017711, 0L);
        RoutePoint endPoint = new RoutePoint(38.017711, 38.017711, 0L);

        List<RoutePoint> route = new ArrayList<>();
        route.add(startPoint);
        route.add(endPoint);

        /* Execution */
        double distance = classUnderTest.sumBetweenListOfPoints(route);

        /* Validation */
        double expectedDistance = 0;
        assertThat(distance, is(expectedDistance));
    }

    @Test
    public void testDistanceWithOneInvalidPoint() {

        /* Preparation */
        RoutePoint startPoint = new RoutePoint(38.017711, 23.834016, 0L);
        RoutePoint endPoint = new RoutePoint(10032223.345, 3432.4, 0L);

        /* Validation */
        exceptionGrabber.expect(HaversineFormulaException.class);
        classUnderTest.betweenTwoPoints(startPoint, endPoint);
    }

    @Test
    public void testDistanceWithOneNullPoint() {

        /* Validation */
        exceptionGrabber.expect(HaversineFormulaException.class);
        classUnderTest.betweenTwoPoints(null, null);
    }

}