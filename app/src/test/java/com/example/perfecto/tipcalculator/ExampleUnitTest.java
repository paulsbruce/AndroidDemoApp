package com.example.perfecto.tipcalculator;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {

        TipCalculator.TipCalculationResults calculation = new TipCalculator().Calculate(
                28.73, 25, 2
        );

        assertEquals(35.91, calculation.TotalAmountForTheBill, 0.0099);
        assertEquals(7.18, calculation.PercentageOfTip, 0.0099);
        assertEquals(3.59, calculation.TipPerEachPerson, 0.0099);
    }
}