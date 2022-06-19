package com.comcast.stringinator.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.comcast.stringinator.model.StatsResult;
import com.comcast.stringinator.model.StringinatorInput;
import com.comcast.stringinator.model.StringinatorResult;

@Service
public class StringinatorServiceImpl implements StringinatorService{
	
	private Map<String, Integer> seenStrings = new HashMap<>();
	
	private Map<String, Integer> longStrings = new HashMap<>();


	@Override
	public StringinatorResult stringinate(StringinatorInput input) {
		
		//Compute String Values and frequency in a Hash Map (seenStrings).
		seenStrings.compute(input.getInput(), (k, v) -> (v == null) ? Integer.valueOf(1) : v + 1);
		
		//Compute String Value and String length in a Hash Map (longStrings).
		longStrings.put(input.getInput(), Integer.valueOf(input.getInput().length()));
		
		//Compute frequency of characters in a given input String.
		Map<Character, Long> freqMap = getInputFrequency(input.getInput());
		
		StringinatorResult result = new StringinatorResult(input.getInput(), Integer.valueOf(input.getInput().length()), freqMap);
		return result;
	}

	@Override
	public StatsResult stats() {
		List<String> most_popular = keysWithMaxValues(seenStrings);
		List<String> longest_input_received = keysWithMaxValues(longStrings);
		return new StatsResult(seenStrings, most_popular, longest_input_received);
	}
	
	public List<String> keysWithMaxValues(Map<String, Integer> stringsMap) {
		
		//return empty list when Map is empty.
		if(stringsMap.isEmpty())
	        return Collections.emptyList();
		
		// Get the max value in the Map.
	    long longest = stringsMap.values().stream().max(Comparator.naturalOrder()).get();
	    
	    // Get all the keys corresponding to the max value, construct a List and return it.
	    return stringsMap.entrySet().stream()
	        .filter(e -> e.getValue() == longest)
	        .map(Map.Entry::getKey)
	        .collect(Collectors.toList());
	}
	
	public Map<Character, Long> getInputFrequency(String input){
		
		//Ignore White spaces and punctuation marks from the input and convert it to charArray Streams.
		Stream<Character> characters = input.replaceAll("\\p{Punct}"," ").replaceAll(" ", "").toLowerCase().chars().mapToObj(c -> (char) c);
		
		//charMap with Characters as Key and Occurrences as Values.
		Map<Character, Long> charMap = characters.collect(
	            Collectors.groupingBy(
	            Function.identity(),
	            Collectors.counting()
	            )
	        );
		
		// Get the max value in the Map.
		long frequentChar = charMap.values().stream().max(Comparator.naturalOrder()).get();
		  
		// Get all the key, values corresponding to the max value, construct a map and return it.
		return charMap.entrySet().stream()
			        .filter(e -> e.getValue() == frequentChar)
			        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

	}

}
