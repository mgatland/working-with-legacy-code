package com.rapaul.example.staticmethod;

import java.math.BigDecimal;
import java.util.List;

public class ResultAggregator {
	
	public AggregateSummary aggregateSummary(Patient patient) {
		List<Result> pendingResults = getResultsFor(patient);
		validateResults(pendingResults);
		
		int sum = 0;
		int minimum = Integer.MAX_VALUE;
		int maximum = Integer.MIN_VALUE;
		for (Result result : pendingResults) {
			sum += result.getValue();
			minimum = Math.min(minimum, result.getValue());
			maximum = Math.max(maximum, result.getValue());
		}
		return createAggregateSummary(pendingResults.size(), sum, minimum, maximum);
	}

	protected AggregateSummary createAggregateSummary(int numberOfResults, int sum, int minimum, int maximum) {
		BigDecimal count = new BigDecimal(numberOfResults);
		BigDecimal mean = new BigDecimal(sum).divide(count);
		return new AggregateSummary(mean, minimum, maximum);
	}

	protected void validateResults(List<Result> pendingResults) {
		if (pendingResults.size() == 0) {
			throw new IllegalStateException("There are no results for this patient.");
		}
	}

	protected List<Result> getResultsFor(Patient patient) {
		return ResultFetcher.getResultsFor(patient);
	}

}
