package freiberg.puzzler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class FileParser {
	public static List<City> parseFile(String absolutePath) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(absolutePath));
			String line = null;
			List<City> cities = new ArrayList<City>();
			while ((line = reader.readLine()) != null) {
				cities.add(parseCity(line));
			}
			try {
				reader.close();
			} catch (Exception close) {
				
			}
			return cities;
		} catch (Exception e) {
			System.err.print("Could not read file " + absolutePath);
			throw new IllegalArgumentException("Illegal file " + absolutePath);
		}
	}
	
	private static City parseCity(String line) {
		String name = line.substring(0, line.indexOf(","));
		String population = line.substring(line.indexOf(",") + 2, line.length() - 1);
		population = population.replaceAll(",", "");
		int pop = 0;
		try {
			pop = Integer.parseInt(population);
		} catch (Exception e) {
			System.err.println("Could not parse city from " + line);
			throw new IllegalArgumentException("Could not parse city from " + line);
		}
		return new City(name, pop);
	}

}
