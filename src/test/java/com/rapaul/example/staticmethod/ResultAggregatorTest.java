package com.rapaul.example.staticmethod;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class ResultAggregatorTest {

	private Patient patient = new Patient("123");

	@Test
	public void cantCreateAnAggregateSummaryIfNoResultsExist() throws Exception {
		try {
			aggregateResultsFor(patient);
			fail();
		} catch (Exception e) {
			assertThat(e.getMessage(), is("There are no results for this patient."));
		}
	}
	
	@Test
	public void calculatesTheMeanOfAllResults() {
		AggregateSummary aggregateSummary = aggregateResultsFor(patient, new Result(1));
		assertThat(aggregateSummary.getMean(), is(new BigDecimal(1)));
	}

	@Test
	public void calculatesTheMaxOfAllResults() {
		AggregateSummary aggregateSummary = aggregateResultsFor(patient, new Result(1), new Result(4));
		assertThat(aggregateSummary.getMaximum(), is(4));
	}

	@Test
	public void calculatesTheMaxOfNegativeResults() {
		AggregateSummary aggregateSummary = aggregateResultsFor(patient, new Result(-1), new Result(-4));
		assertThat(aggregateSummary.getMaximum(), is(-1));
	}

	
	
	@Test
	public void calculatesTheMinOfAllResults() {
		AggregateSummary aggregateSummary = aggregateResultsFor(patient, new Result(2), new Result(4));
		assertThat(aggregateSummary.getMinimum(), is(2));
	}

	protected AggregateSummary aggregateResultsFor(Patient patient, Result... results) {
		StubResultAggregator aggregator = new StubResultAggregator(patient, results);
		AggregateSummary aggregateSummary = aggregator.aggregateSummary(patient);
		return aggregateSummary;
	}

	@Test
	public void calculatesTheMeanOfTwoDifferentResults() {		
		AggregateSummary aggregateSummary = aggregateResultsFor(patient, new Result(1), new Result(3));
		assertThat(aggregateSummary.getMean(), is(new BigDecimal(2)));
	}
	
	class StubResultAggregator extends ResultAggregator {
		
		private List<Result> results;
		private Patient myPatient;

		public StubResultAggregator(Patient patient, Result... results) {
			this.myPatient = patient;
			this.results = Arrays.asList(results);
		}
		
		@Override
		protected List<Result> getResultsFor(Patient patient) {
			assertThat(patient, is(myPatient));
			return results;
		}
	}
}