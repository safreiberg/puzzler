package freiberg.puzzler;

public class WordLocation {
	private City city;
	private Point point;
	private Orientation orientation;
	
	public WordLocation(City city, Point point, Orientation orientation) {
		this.city = city;
		this.point = point;
		this.orientation = orientation;
	}
	
	public String getIthChar(int i) {
		return city.getName().substring(i, i + 1);
	}
	
	public Point getIthPoint(int i) {
		if (orientation == Orientation.HORIZONTAL) {
			return new Point(point.getX() + i, point.getY());
		} else if (orientation == Orientation.VERTICAL) {
			return new Point(point.getX(), point.getY() + i);
		}
		return null;
	}
	
	public String characterAtPoint(Point point) {
		for (int i = 0; i < city.getName().length(); i++) {
			if (point.equals(getIthPoint(i))) {
				return getIthChar(i);
			}
		}
		return null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result
				+ ((orientation == null) ? 0 : orientation.hashCode());
		result = prime * result + ((point == null) ? 0 : point.hashCode());
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
		WordLocation other = (WordLocation) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (orientation != other.orientation)
			return false;
		if (point == null) {
			if (other.point != null)
				return false;
		} else if (!point.equals(other.point))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WordLocation [city=" + city + ", point=" + point
				+ ", orientation=" + orientation + "]";
	}

	public City getCity() {
		return city;
	}

	public Point getPoint() {
		return point;
	}
	
	public Orientation getOrientation() {
		return orientation;
	}
	
}
