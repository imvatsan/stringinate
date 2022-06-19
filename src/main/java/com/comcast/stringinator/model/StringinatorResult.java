package com.comcast.stringinator.model;

import java.util.Map;

public class StringinatorResult {
	
	private final String input;
	private final Integer length;
	private final Map<Character, Long> charFrequency;
	
	public StringinatorResult(String input, Integer length, Map<Character, Long> charFrequency) {
		this.input = input;
		this.length = length;
		this.charFrequency = charFrequency;
	}
	
	public String getInput() {
		return input;
	}
	public Integer getLength() {
		return length;
	}

	public Map<Character, Long> getCharFrequency() {
		return charFrequency;
	}

}
