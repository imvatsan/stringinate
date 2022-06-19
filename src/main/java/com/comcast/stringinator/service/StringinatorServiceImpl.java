package com.comcast.stringinator.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
	
	private Map<String, Integer> seenStrings = loadFromFile("src/main/resources/db_file_frequency.txt");
	private Map<String, Integer> longStrings = loadFromFile("src/main/resources/db_file_longstring.txt");

	@Override
	public StringinatorResult stringinate(StringinatorInput input) {
		
		//Compute String Values and frequency in a Hash Map (seenStrings).
		seenStrings.compute(input.getInput(), (k, v) -> (v == null) ? Integer.valueOf(1) : v + 1);
		
		//Compute String Value and String length in a Hash Map (longStrings).
		longStrings.put(input.getInput(), Integer.valueOf(input.getInput().length()));
		
		//Compute frequency of characters in a given input String.
		Map<Character, Long> freqMap = getInputFrequency(input.getInput());
		
		writeToFile("src/main/resources/db_file_frequency.txt", seenStrings);
		
		writeToFile("src/main/resources/db_file_longstring.txt", longStrings);
		
		StringinatorResult result = new StringinatorResult(input.getInput(), Integer.valueOf(input.getInput().length()), freqMap);
		return result;
	}

	@Override
	public StatsResult stats() {
		List<String> most_popular = keysWithMaxValues(seenStrings);
		List<String> longest_input_received = keysWithMaxValues(longStrings);
		return new StatsResult(seenStrings, most_popular, longest_input_received);
	}
	
	
	/**
	 * Takes map as an input and returns a list of Strings that has the maximum value occurrence.
	 * @param stringsMap - input map
	 * @return List of Strings
	 */
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
	
	/**
	 * Takes String as an input and returns a map with Character value with the highest occurrence in the input String.
	 * @param input
	 * @return Map of Character and its occurrences
	 */
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
	
	/**
	 * Writes the HashMap to the external File.
	 * @param filePath - location of the file
	 * @param stringMaps - Map
	 */
	public void writeToFile(String filePath, Map<String, Integer> stringMaps){
		
		File file = new File(filePath);	  
		
		try (BufferedWriter bf = new BufferedWriter(new FileWriter(file))){	
			
            // iterate map entries
            for (Map.Entry<String, Integer> entry :
            	stringMaps.entrySet()) {
            	
                // Insert Key Value pairs defined by a colon
                bf.write(entry.getKey() + ":" + entry.getValue());
                bf.newLine();
            }
  
            bf.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
		
	}
	
	/**
	 * Loads data from the external file to the HashMap.
	 * @param filePath - location of the file.
	 * @return - HashMap
	 */
	public Map<String, Integer> loadFromFile(String filePath){
		
		File file = new File(filePath);
		Map<String, Integer> seenStringsFromFile = new HashMap<>();
		String newlines;
		
		if(file.exists()) {
			try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){	
				while ((newlines = reader.readLine()) != null) {
		            String[] stringValues = newlines.split(":", 2);
		            if (stringValues.length > 1) {
		                String key = stringValues[0];
		                Integer value = Integer.valueOf(stringValues[1]);
		                seenStringsFromFile.put(key, value);		                
		            } else {
		                System.out.println("No data found at line, ignoring: " + newlines);
		            }
		        }
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
		}
		
		return seenStringsFromFile;
		
	}

}
