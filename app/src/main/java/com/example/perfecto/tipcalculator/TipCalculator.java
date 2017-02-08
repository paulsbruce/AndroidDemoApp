package com.example.perfecto.tipcalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Locale;

/**
 * Created by paulb on 11/29/16.
 * Separation of tip calculation algorithm from view logic.
 */

public class TipCalculator {

    public static boolean UseBigDecimalForCurrency = true;

    public TipCalculationResults Calculate(double totalBillInput, double tipPercentValue, int tipsForNumberOfPeople)
    {

        if(UseBigDecimalForCurrency) { // handle currency operations in BigDecimal, then return to appropriate scale

            BigDecimal percentageOfTip = (BigDecimal.valueOf(totalBillInput).multiply(BigDecimal.valueOf(tipPercentValue))).divide(new BigDecimal(100), RoundingMode.HALF_EVEN);
            BigDecimal totalAmountForTheBill = BigDecimal.valueOf(totalBillInput).add(percentageOfTip);
            BigDecimal tipPerEachPerson = percentageOfTip.divide(new BigDecimal((double)tipsForNumberOfPeople));

            int scale = Currency.getInstance(Locale.getDefault()).getDefaultFractionDigits();
            return new TipCalculationResults(
                    percentageOfTip.setScale(scale, RoundingMode.HALF_EVEN).doubleValue(),
                    totalAmountForTheBill.setScale(scale, RoundingMode.HALF_EVEN).doubleValue(),
                    tipPerEachPerson.setScale(scale, RoundingMode.HALF_EVEN).doubleValue()
            );

        } else { // how not to handle arithmetic operations over currencies...floating point hell

            double percentageOfTip = (totalBillInput * tipPercentValue) / 100;
            double totalAmountForTheBill = totalBillInput + percentageOfTip;
            double tipPerEachPerson = percentageOfTip / (double) tipsForNumberOfPeople; // divide evenly across people...fair

            return new TipCalculationResults(percentageOfTip, totalAmountForTheBill, tipPerEachPerson);

        }
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
