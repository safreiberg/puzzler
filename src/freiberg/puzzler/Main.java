package freiberg.puzzler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
	private static final int NUM_TO_USE = 100;
	private static final int WIDTH = 7;
	private static final int HEIGHT = 7;
	
	private static List<City> getCitiesInPlay(List<City> cities) {
		List<City> citiesInPlay = new ArrayList<City>();
		for (int i = 0; i < NUM_TO_USE; i++) {
			citiesInPlay.add(cities.get(i));
		}
		return citiesInPlay;
	}
	
	public static void main(String[] args) {
		String filename = "/home/saf/puzzler/cities.csv";
		List<City> cities = FileParser.parseFile(filename);
		cities.remove(0);
		List<City> citiesInPlay = cities;
		
		List<City> rightSizeCities = new ArrayList<City>();
		for (City city : citiesInPlay) {
			if (city.getName().length() > 7) {
				continue;
			} else {
				rightSizeCities.add(city);
			}
		}
		
		final Queue<Crossword> crosswords = new LinkedList<Crossword>();
		final Set<Crossword> terminalCrosswords = new HashSet<Crossword>();
		final Set<Crossword> visited = new HashSet<Crossword>();
		
		final AtomicInteger latch = new AtomicInteger(1);
		
		Thread timer = new Thread(new Runnable() {
			@Override
			public void run() {
				long startTime = System.currentTimeMillis(); 
				while (latch.get() == 1) {
					long currentTime = System.currentTimeMillis();
					System.out.println("-----------------");
					System.out.println("Visited size: " + visited.size());
					System.out.println("Crossword size: " + crosswords.size());
					System.out.println("Terminal size: " + terminalCrosswords.size());
					System.out.println("Time elapsed: " + (currentTime - startTime)/1000 + " seconds");
					System.out.println("-----------------\n");
					try {
						Thread.sleep(15000);
					} catch (Exception e) {
						
					}
				}
			}
		});
		
		timer.start();
		
		
		// initialize all the 1-crosswords
		for (City city : rightSizeCities) {
			for (int x = 0; x < WIDTH; x++) {
				for (int y = 0; y < HEIGHT; y++) {
					Point point = new Point(x, y);
					for (Orientation orientation : Orientation.values()) {
						WordLocation wordLoc = new WordLocation(city, point, orientation);
						Crossword crossword = new Crossword(WIDTH, HEIGHT);
						if (crossword.canPlaceWord(wordLoc)) {
							crosswords.add(crossword.addWord(wordLoc));
						}
					}
				}
			}
		}
		while (crosswords.size() > 0) {
			Crossword crossword = crosswords.remove();
			if (terminalCrosswords.contains(crossword)) {
				continue;
			}
			if (visited.contains(crossword)) {
				continue;
			}
			// add if possible
			boolean changed = false;
			for (City city : rightSizeCities) {
				for (int x = 0; x < WIDTH; x++) {
					for (int y = 0; y < HEIGHT; y++) {
						Point point = new Point(x, y);
						for (Orientation orientation : Orientation.values()) {
							WordLocation wordLoc = new WordLocation(city, point, orientation);
							if (crossword.canPlaceWord(wordLoc)) {
								crosswords.add(crossword.addWord(wordLoc));
								changed = true;
							}
						}
					}
				}
			}
			if (!changed) {
				terminalCrosswords.add(crossword);
			}
			visited.add(crossword);
		}
		
		latch.decrementAndGet();
		
		// get max score
		int bestSeen = 0;
		Crossword bestCross = null;
		for (Crossword crossword : terminalCrosswords) {
			if (crossword.score() > bestSeen) {
				bestSeen = crossword.score();
				bestCross = crossword;
			}
		}
		
		System.out.println(bestSeen);
		System.out.println(bestCross.prettyPrint());
		
		// get max score
		bestSeen = 0;
		bestCross = null;
		for (Crossword crossword : visited) {
			if (crossword.score() > bestSeen) {
				bestSeen = crossword.score();
				bestCross = crossword;
			}
		}
		
		System.out.println(bestSeen);
		System.out.println(bestCross.prettyPrint());
	}
	
}
