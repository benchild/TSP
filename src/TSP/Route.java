package TSP;

public class Route {
	public int[] route;
	public double length;
	
	public Route(int n) { 
		route = new int[n];
	}
	
	public Route(int[] route, double length) {
		this.route = route;
		this.length = length;
	}
	
	public String toString() {
		String s = "";
		for (int i = 0; i < route.length; i++)
			s = s + route[i] + " ";
		return s;
	}
}