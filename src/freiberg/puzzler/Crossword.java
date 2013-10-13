package freiberg.puzzler;

import java.util.HashSet;
import java.util.Set;

public class Crossword {
	private int width;
	private int height;
	private Set<WordLocation> wordLocations;
	
	public Crossword(int width, int height) {
		this.width = width;
		this.height = height;
		this.wordLocations = new HashSet<WordLocation>();
	}
	
	public Crossword(int width, int height, Set<WordLocation> wordLocations) {
		this.width = width;
		this.height = height;
		this.wordLocations = wordLocations;
	}
	
	public int score() {
		int score = 0;
		for (WordLocation wloc : wordLocations) {
			score += wloc.getCity().getPopulation();
		}
		return score;
	}
	
	public int getNumberWords() {
		return wordLocations.size();
	}
	
	public boolean canPlaceWord(WordLocation wordLocation) {
		return isInBounds(wordLocation) && checkIntersection(wordLocation)
				&& isNewWord(wordLocation) && checkNeighbors(wordLocation);
	}
	
	public Crossword addWord(WordLocation wordLocation) {
		if (!canPlaceWord(wordLocation)) {
			throw new IllegalArgumentException("Illegal word location " + wordLocation);
		}
		Set<WordLocation> wordLocs = new HashSet<WordLocation>();
		for (WordLocation wordLoc : wordLocations) {
			wordLocs.add(wordLoc);
		}
		wordLocs.add(wordLocation);
		return new Crossword(width, height, wordLocs);
	}
	
	private boolean isInBounds(WordLocation wordLocation) {
		if (wordLocation.getOrientation() == Orientation.HORIZONTAL) {
			if (wordLocation.getPoint().getX() + wordLocation.getCity().getName().length() > this.width) {
				return false;
			}
			if (wordLocation.getPoint().getY() >= height) {
				return false;
			}
		} else if (wordLocation.getOrientation() == Orientation.VERTICAL) {
			if (wordLocation.getPoint().getY() + wordLocation.getCity().getName().length() > this.height) {
				return false;
			}
			if (wordLocation.getPoint().getX() >= width) {
				return false;
			}
		}
		return true;
	}
	
	private boolean isNewWord(WordLocation wordLocation) {
		for (WordLocation wordLoc : wordLocations) {
			if (wordLoc.getCity().getName().equalsIgnoreCase(wordLocation.getCity().getName())) {
				return false;
			}
		}
		return true;
	}
	
	private boolean isEmpty() {
		return wordLocations.isEmpty();
	}
	
	private boolean checkNeighbors(WordLocation wordLocation) {
		Set<Point> locsToCheck = locsToCheck(wordLocation);
		for (Point p : locsToCheck) {
			for (WordLocation wordLoc : wordLocations) {
				if (wordLoc.characterAtPoint(p) != null) {
					return false;
				}
			}
		}
		return true;
	}
	
	private Set<Point> locsToCheck(WordLocation wordLocation) {
		Set<Point> points = new HashSet<Point>();
		if (wordLocation.getOrientation() == Orientation.HORIZONTAL) {
			// left
			int xleft = wordLocation.getIthPoint(0).getX() - 1;
			points.add(new Point(xleft, wordLocation.getIthPoint(0).getY()));
			// right
			int xright = wordLocation.getIthPoint(wordLocation.getCity().getName().length() - 1).getX() + 1;
			points.add(new Point(xright, wordLocation.getIthPoint(wordLocation.getCity().getName().length() - 1).getY()));
			// top/bottom
			int yabove = wordLocation.getIthPoint(0).getY() - 1;
			int ybelow = wordLocation.getIthPoint(0).getY() + 1;
			for (int x = wordLocation.getIthPoint(0).getX(); x < wordLocation.getIthPoint(wordLocation.getCity().getName().length() - 1).getX(); x++) {
				points.add(new Point(x, yabove));
				points.add(new Point(x, ybelow));
			}
		} else {
			// top
			int ytop = wordLocation.getIthPoint(0).getY() - 1;
			points.add(new Point(wordLocation.getIthPoint(0).getX(), ytop));
			// bottom
			int ybottom = wordLocation.getIthPoint(wordLocation.getCity().getName().length() - 1).getY() + 1;
			points.add(new Point(wordLocation.getIthPoint(wordLocation.getCity().getName().length() - 1).getX(), ybottom));
			// left/right
			int xleft = wordLocation.getIthPoint(0).getX() - 1;
			int xright = wordLocation.getIthPoint(0).getX() + 1;
			for (int y = wordLocation.getIthPoint(0).getY(); y < wordLocation.getIthPoint(wordLocation.getCity().getName().length() - 1).getY(); y++) {
				points.add(new Point(xleft, y));
				points.add(new Point(xright, y));
			}
		}
		return points;
	}
	
	private boolean isOnBoard(Point p) {
		return (p.getX() >= 0 && p.getX() < width && p.getY() >= 0 && p.getY() < height);
	}
	
	private boolean checkIntersection(WordLocation wordLocation) {
		if (isEmpty()) {
			return true;
		}
		int intersections = 0;
		for (int loc = 0; loc < wordLocation.getCity().getName().length(); loc++) {
			String character = wordLocation.getIthChar(loc);
			Point pointAt = wordLocation.getIthPoint(loc);
			
			for (WordLocation alreadyOnBoard : wordLocations) {
				String charOnBoard = alreadyOnBoard.characterAtPoint(pointAt);
				if (charOnBoard != null) {
					if (charOnBoard.equalsIgnoreCase(character)) {
						intersections++;
					} else {
						return false;
					}
				}
			}
		}
		return intersections > 0;
	}
	
	public Crossword copy() {
		Set<WordLocation> wordLocs = new HashSet<WordLocation>();
		for (WordLocation wordLoc : wordLocations) {
			wordLocs.add(wordLoc);
		}
		return new Crossword(width, height, wordLocs);
	}
	
	public String prettyPrint() {
		String output = "";
		for (int y = 0; y < width; y++) {
			String row = "";
			for (int x = 0; x < height; x++) {
				Point point = new Point(x, y);
				boolean changed = false;
				for (WordLocation wordLoc : wordLocations) {
					String charAt = wordLoc.characterAtPoint(point);
					if (charAt != null) {
						row = row.substring(0, x).concat(charAt);
						changed = true;
					}
				}
				if (!changed) {
					row += "*";
				}
			}
			output += row + "\n";
		}
		return output;
	}

	@Override
	public String toString() {
		return "Crossword [width=" + width + ", height=" + height
				+ ", wordLocations=" + wordLocations + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + width;
		result = prime * result
				+ ((wordLocations == null) ? 0 : wordLocations.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Crossword other = (Crossword) obj;
		if (height != other.height)
			return false;
		if (width != other.width)
			return false;
		if (wordLocations == null) {
			if (other.wordLocations != null)
				return false;
		} else if (!wordLocations.equals(other.wordLocations))
			return false;
		return true;
	}
}
