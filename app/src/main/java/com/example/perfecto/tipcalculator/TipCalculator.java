package com.example.perfecto.tipcalculator;

/**
 * Created by paulb on 11/29/16.
 */

public class TipCalculator {

    public TipCalculationResults Calculate(double totalBillInput, double tipPercentValue, int tipsForNumberOfPeople) {

        double percentageOfTip = (totalBillInput * tipPercentValue) / 100;
        double totalAmountForTheBill = totalBillInput + percentageOfTip;
        double tipPerEachPerson = percentageOfTip / (double)tipsForNumberOfPeople;

        return new TipCalculationResults(percentageOfTip, totalAmountForTheBill, tipPerEachPerson);
    }

    public class TipCalculationResults
    {
        public final double PercentageOfTip;
        public final double TotalAmountForTheBill;
        public final double TipPerEachPerson;

        public TipCalculationResults(double percentageOfTip, double totalAmountForTheBill, double tipPerEachPerson) {
            this.PercentageOfTip = percentageOfTip;
            this.TotalAmountForTheBill = totalAmountForTheBill;
            this.TipPerEachPerson = tipPerEachPerson;
        }
    }

}
