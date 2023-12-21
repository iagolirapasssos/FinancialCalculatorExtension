/*
 * FinancialCalculator
 * Extension created to assist financial and statistical calculations
 * Version: 1.0
 * Author: Francisco Iago Lira Passos
 * Date: 2023-10-15
 * Docs: https://docs.google.com/document/d/1xk9dMfczvjbbwD-wMsr-ffqkTlE3ga0ocCE1KOb2wvw/pub#h.4jyv4s6bnjrd
 */

package com.bosonshiggs.finanlcialcalculator;

import com.bosonshiggs.finanlcialcalculator.helpers.*;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.common.ComponentCategory;

import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.util.*;
import com.google.appinventor.components.runtime.util.YailList;
import com.google.appinventor.components.runtime.util.YailDictionary;

import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.annotations.SimpleEvent;

import java.text.NumberFormat;
import java.util.Locale;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import com.google.appinventor.components.common.OptionList;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;

@DesignerComponent(
        version = 1,
        description = "Extension to assist financial and statistical calculations",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = "images/extension.png"
)
@UsesLibraries(libraries = "commons-math3-3.6.1.jar")
@SimpleObject(external = true)
public class FinancialCalculator extends AndroidNonvisibleComponent {

    public FinancialCalculator(ComponentContainer container) {
        super(container.$form());
    }
    

    @SimpleFunction(description = "Format an integer as locale currency")
    public String FormatCurrency(long amount, @Options(Currency.class) final String currencyCode) {
    	try {
    		
            try  {
                //Locale locale = new Locale(currencyCode);
            	String[] symbol = currencyCode.split("_");
            	ReportError("Error:" + symbol);
                NumberFormat currencyFormat = NumberFormat.getInstance(new Locale (symbol[0], symbol[1]));
                return currencyFormat.format(amount);
            } catch(Exception f) {
                ReportError("Error:" + f);
                return "";
            }
        } catch (IllegalArgumentException e) {
            ReportError("Error: Invalid locale!");
            return "";
        }
    }
    
    @SimpleFunction(description = "Calculate the mean (average) of a list of integer numbersList")
    public double Mean(YailList numbersList) {
        if ( numbersList != null && !numbersList.isEmpty()) {
        	
        	double sum = 0;
        	int index = 0;
        	
        	for(String e: numbersList.toStringArray()) {
        		try 
        		{
	        		sum += Integer.parseInt(e);
	        		index++;
        		} 
        		catch (NumberFormatException f) 
        		{
        			ReportError("Error: " + f);
        			return Double.NaN;
        		}
        	}
        	return (double)(sum/index);
        } else {
        	return Double.NaN;
        }
        
    }
    
    @SimpleFunction(description = "Calculate the standard deviation of a list of integer numbersList")
    public double StandardDeviation(YailList numbersList, @Options(BooleanValue.class) boolean isPopulational) {
        if (numbersList != null && !numbersList.isEmpty()) {
            double mean = Mean(numbersList);
            double sumOfSquares = 0;
            
            try
            {
	            for (String e : numbersList.toStringArray()) {
	                    int num = Integer.parseInt(e);
	                    sumOfSquares += Math.pow(num - mean, 2);
	            }
	            if (isPopulational) {
	            	return Math.sqrt(sumOfSquares / numbersList.size());
	            } else {
	            	return Math.sqrt(sumOfSquares / (numbersList.size()-1));
	            }
            } 
            catch (NumberFormatException f) 
            {
            	ReportError("Error: " + f);
            	return Double.NaN;
            }
        } else {
        	return Double.NaN;
        }
    }
    
    @SimpleFunction(description = "Calculate the median of a list of integer numbersList")
    public double Median(YailList numbersList) {
        if (numbersList != null && !numbersList.isEmpty()) {
            List<Integer> intList = new ArrayList<>();
            
            try {
                for (String e : numbersList.toStringArray()) {
                    int num = Integer.parseInt(e);
                    intList.add(num);
                }
                Collections.sort(intList);
                int size = intList.size();
                if (size % 2 == 0) {
                    int mid1 = intList.get(size / 2 - 1);
                    int mid2 = intList.get(size / 2);
                    return (double)(mid1 + mid2) / 2;
                } else {
                    return (double)intList.get(size / 2);
                }
            } catch (NumberFormatException f) {
            	ReportError("Error: " + f);
                return Double.NaN;
            }
        } else {
            return Double.NaN;
        }
    }
    
    @SimpleFunction(description = "Calculate the mode of a list of integer numbersList")
    public YailList Mode(YailList numbersList) {
        if (numbersList != null && !numbersList.isEmpty()) {
            List<Integer> intList = new ArrayList<>();

            try {
                for (String e : numbersList.toStringArray()) {
                    int num = Integer.parseInt(e);
                    intList.add(num);
                }

                Map<Integer, Integer> countMap = new HashMap<>();
                int maxCount = 0;

                for (int num : intList) {
                    int count = countMap.getOrDefault(num, 0) + 1;
                    countMap.put(num, count);
                    if (count > maxCount) {
                        maxCount = count;
                    }
                }

                List<Integer> modeList = new ArrayList<>();
                for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
                    if (entry.getValue() == maxCount) {
                        modeList.add(entry.getKey());
                    }
                }

                if (modeList.isEmpty() || modeList.size() == intList.size()) {
                    return new YailList();  // Retorna uma lista vazia se não houver moda ou todos os valores forem únicos
                }

                List<String> result = new ArrayList<>();
                for (int num : modeList) {
                    result.add(Integer.toString(num));
                }

                return YailList.makeList(result);
            } catch (NumberFormatException f) {
                ReportError("Error: " + f);
                return new YailList();
            }
        } else {
            return new YailList();
        }
    }
    
    @SimpleFunction(description = "Calculate the variance of a list of integer numbersList")
    public double Variance(YailList numbersList) {
        if (numbersList != null && !numbersList.isEmpty()) {
            double mean = Mean(numbersList);
            double sumOfSquares = 0;

            try {
                for (String e : numbersList.toStringArray()) {
                    int num = Integer.parseInt(e);
                    sumOfSquares += Math.pow(num - mean, 2);
                }
                return sumOfSquares / (numbersList.size() - 1);
            } catch (NumberFormatException f) {
                ReportError("Error: " + f);
                return Double.NaN;
            }
        } else {
            return Double.NaN;
        }
    }

    @SimpleFunction(description = "Calculate the range of a list of integer numbersList")
    public int Range(YailList numbersList) {
        if (numbersList != null && !numbersList.isEmpty()) {
            List<Integer> intList = new ArrayList<>();

            try {
                for (String e : numbersList.toStringArray()) {
                    int num = Integer.parseInt(e);
                    intList.add(num);
                }
                Collections.sort(intList);
                int size = intList.size();
                return intList.get(size - 1) - intList.get(0);
            } catch (NumberFormatException f) {
                ReportError("Error: " + f);
                return -1; // Indicar erro com valor negativo
            }
        } else {
            return -1; // Indicar erro com valor negativo
        }
    }

    @SimpleFunction(description = "Calculate the sum of a list of integer numbersList")
    public int Sum(YailList numbersList) {
        if (numbersList != null && !numbersList.isEmpty()) {
            int sum = 0;

            for (String e : numbersList.toStringArray()) {
                try {
                    sum += Integer.parseInt(e);
                } catch (NumberFormatException f) {
                    ReportError("Error: " + f);
                    return -1; // Indicar erro com valor negativo
                }
            }
            return sum;
        } else {
            return -1; // Indicar erro com valor negativo
        }
    }
    
    @SimpleFunction(description = "Calculate the weighted median of a list of numbersList and their corresponding weights")
    public double WeightedMedian(YailList numbersList, YailList weights) {
        if (numbersList != null && weights != null && !numbersList.isEmpty() && !weights.isEmpty() && numbersList.size() == weights.size()) {
            List<Double> values = new ArrayList<>();
            List<Double> valueWeights = new ArrayList<>();

            try {
                for (int i = 1; i <= numbersList.size(); i++) {
                    values.add(Double.parseDouble(numbersList.getString(i)));
                    valueWeights.add(Double.parseDouble(weights.getString(i)));
                }
                // Sort values and corresponding weights
                Collections.sort(values);
                Collections.sort(valueWeights);
                int n = values.size();
                double median = 0.0;

                if (n % 2 == 0) {
                    // Even number of values
                    int midIndex1 = n / 2 - 1;
                    int midIndex2 = n / 2;
                    double value1 = values.get(midIndex1);
                    double value2 = values.get(midIndex2);
                    double weight1 = valueWeights.get(midIndex1);
                    double weight2 = valueWeights.get(midIndex2);
                    median = (value1 * weight1 + value2 * weight2) / (weight1 + weight2);
                } else {
                    // Odd number of values
                    int midIndex = n / 2;
                    median = values.get(midIndex);
                }
                return median;
            } catch (NumberFormatException f) {
                ReportError("Error: " + f);
                return Double.NaN;
            }
        } else {
            ReportError("Error: Lists are empty or have different lengths.");
            return Double.NaN;
        }
    }
    
    @SimpleFunction(description = "Calculate simple interest")
    public double SimpleInterest(double principal, double rate, int time) {
        return principal * rate * time / 100.0;
    }

    @SimpleFunction(description = "Calculate compound interest")
    public double CompoundInterest(double principal, double rate, int time) {
        return principal * Math.pow(1 + rate / 100.0, time) - principal;
    }

    @SimpleFunction(description = "Calculate monthly payment for a loan")
    public double MonthlyPayment(double principal, double rate, int time) {
        double r = rate / 100.0 / 12.0;
        int n = time * 12;
        if (r == 0) {
            return principal / n;
        }
        return principal * r / (1 - Math.pow(1 + r, -n));
    }

    @SimpleFunction(description = "Calculate Net Present Value (NPV) of cash flows")
    public double NetPresentValue(YailList cashFlows, double rate) {
        if (cashFlows != null) {
            int n = cashFlows.size();
            double npv = 0.0;
            for (int i = 1; i <= n; i++) {
                try {
                    double cashFlow = Double.parseDouble(cashFlows.getString(i));
                    npv += cashFlow / Math.pow(1 + rate, i);
                } catch (NumberFormatException e) {
                    ReportError("Error: " + e);
                    return Double.NaN;
                }
            }
            return npv;
        } else {
            return Double.NaN;
        }
    }

    
    @SimpleFunction(description = "Calculate future value (FV) of an investment")
    public double FutureValue(double principal, double rate, int time) {
        return principal * Math.pow(1 + rate / 100.0, time);
    }

    @SimpleFunction(description = "Calculate present value (PV) of a future cash flow")
    public double PresentValue(double futureValue, double rate, int time) {
        return futureValue / Math.pow(1 + rate / 100.0, time);
    }

    @SimpleFunction(description = "Calculate the number of periods (time) needed to reach a desired future value")
    public int TimeToReachFutureValue(double principal, double rate, double futureValue) {
        return (int) Math.ceil(Math.log(futureValue / principal) / Math.log(1 + rate / 100.0));
    }

    @SimpleFunction(description = "Calculate the interest rate required to reach a desired future value")
    public double RateToReachFutureValue(double principal, int time, double futureValue) {
        return (Math.pow(futureValue / principal, 1.0 / time) - 1) * 100.0;
    }
    
    @SimpleFunction(description = "Calculate the return on investment (ROI)")
    public double ReturnOnInvestment(double initialInvestment, double finalValue) {
        return ((finalValue - initialInvestment) / initialInvestment) * 100.0;
    }

    @SimpleFunction(description = "Calculate the earnings per share (EPS)")
    public double EarningsPerShare(double netIncome, int numberOfShares) {
        return netIncome / numberOfShares;
    }

    @SimpleFunction(description = "Calculate the price-to-earnings (P/E) ratio")
    public double PriceToEarningsRatio(double sharePrice, double earningsPerShare) {
        return sharePrice / earningsPerShare;
    }

    @SimpleFunction(description = "Calculate the market capitalization of a company")
    public double MarketCapitalization(double sharePrice, int numberOfShares) {
        return sharePrice * numberOfShares;
    }

    @SimpleFunction(description = "Calculate the profit or loss from buying and selling cryptocurrency")
    public double CryptocurrencyProfitLoss(double buyPrice, double sellPrice, double quantity) {
        return (sellPrice - buyPrice) * quantity;
    }
    
    @SimpleFunction(description = "Calculate the cumulative probability of a standard normal distribution up to a specified value")
    public double CumulativeNormalDistribution(double x) {
        NormalDistribution normalDistribution = new NormalDistribution();
        return normalDistribution.cumulativeProbability(x);
    }
    /*

    @SimpleFunction(description = "Calculate the cumulative probability of an exponential distribution up to a specified value")
    public double CumulativeExponentialDistribution(double x, double mean) {
        ExponentialDistribution exponentialDistribution = new ExponentialDistribution(mean);
        return exponentialDistribution.cumulativeProbability(x);
    }

    @SimpleFunction(description = "Calculate the probability of a Poisson distribution for a specified number of events")
    public double PoissonProbability(int k, double lambda) {
        PoissonDistribution poissonDistribution = new PoissonDistribution(lambda);
        return poissonDistribution.probability(k);
    }
    
	*/
    
    @SimpleEvent(description = "Report an error with a custom message")
    public void ReportError(final String errorMessage) {
        if (form != null) {
            form.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    EventDispatcher.dispatchEvent(FinancialCalculator.this, "ReportError", errorMessage);
                }
            });
        }
    }
}
