package com.comcast.stringinator.model;

import java.util.List;
import java.util.Map;

public class StatsResult {
	
	private final Map<String, Integer> inputs;
	public List<String> most_popular;
	public List<String> longest_input_received;
	
	public StatsResult(Map<String, Integer> inputs, List<String> most_popular, List<String> longest_input_received) {
		this.inputs = inputs;
		this.most_popular = most_popular;
		this.longest_input_received = longest_input_received;
	}

	public List<String> getMost_popular() {
		return most_popular;
	}

	public void setMost_popular(List<String> most_popular) {
		this.most_popular = most_popular;
	}

	public List<String> getLongest_input_received() {
		return longest_input_received;
	}

	public void setLongest_input_received(List<String> longest_input_received) {
		this.longest_input_received = longest_input_received;
	}

	public Map<String, Integer> getInputs() {
		return inputs;
	}
	
}
