package TSP;

import java.util.Random;
import TSP.Route;

public class SA {
	public double[][] x;
	public double[][] dist;
	public int n;
	public int T0 = 30;
	public int k = 100;
	public int p = 100;
	public double epis = 1e-5;
	public double alpha = 0.97;
	public Random random;
	public Route best_route;
	
	public SA(double[][] x) {
		this.x = x;
		n = x.length;
	}
	
	public void evolution() {
		random = new Random();
		dist = new double[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++) 
				dist[i][j] = Math.sqrt((x[i][0] - x[j][0]) * (x[i][0] - x[j][0]) + (x[i][1] - x[j][1]) * (x[i][1] - x[j][1]));
		int[] route = initialRoute();
		double t = T0;
		int cur_p = 0;
		double length = calcLength(route);
		while (cur_p < p && Math.abs(t) > epis) {
			for (int i = 0; i < k; i++) {
				//operator 1
				int[] new_route = getNewRouteBySwap(route);
				double new_length = calcLength(new_route);
				if (new_length < length || Math.random() < Math.exp((length - new_length) / t)) {
					route = new_route;
				}
				
				//operator 2
				new_route = getNewRouteByInsert(route);
				new_length = calcLength(new_route);
				if (new_length < length || Math.random() < Math.exp((length - new_length) / t)) {
					route = new_route;
				}
			}
			t = t * alpha;
		}
		best_route = new Route(route, length);
	}
	
	public int[] initialRoute() {
		int[] route = new int[n];
		boolean[] use = new boolean[n];
		use[0] = true;
		for (int i = 0; i < n - 1; i++) {
			int k = 0;
			for (int j = 0; j < n; j++) 
				if (!use[j] && (k == 0 || dist[route[i]][j] < dist[route[i]][k])) {
					k = j;
				}
			route[i + 1] = k;
			use[k] = true;
		}
		return route;
	}
	
	public int[] getNewRouteBySwap(int[] route) {
		int x = random.nextInt(n - 1) + 1;
		int y = random.nextInt(n - 1) + 1;
		while (x == y) {
			y = random.nextInt(n - 1) + 1;
		}
		if (x > y) {
			x = x + y;
			y = x - y;
			x = x - y;
		}
		int[] new_route = new int[n];
		for (int i = 0; i < n; i++) 
			if (x <= i && i <= y) {
				new_route[i] = route[x + y - i];
			} else {
				new_route[i] = route[i];
			}
		return new_route;
	}
	
	public int[] getNewRouteByInsert(int[] route) {
		int x = random.nextInt(n - 1) + 1;
		int y = random.nextInt(n - 1) + 1;
		if (x > y) {
			x = x + y;
			y = x - y;
			x = x - y;
		}
		int z = random.nextInt(n - 1) + 1;
		while (y == z) {
			z = random.nextInt(n -  1) + 1;
		}
		if (y > z) {
			z = z + y;
			y = z - y;
			z = z - y;
		}
		if (x > y) {
			x = x + y;
			y = x - y;
			x = x - y;
		}
		int[] new_route = new int[n];
		for (int i = 0; i < x; i++) 
			new_route[i] = route[i];
		for (int i = x; i <= y; i++) 
			new_route[z - y + i] = route[i];
		for (int i = y + 1; i <= z; i++) 
			new_route[x + i - y - 1] = route[i];
		for (int i = z + 1; i < n; i++)
			new_route[i] = route[i];
		return new_route;
	}
	
	public double calcLength(int[] number) {
		double length = dist[number[0]][number[n - 1]];
		for (int i = 0; i < n - 1; i++) {
			length += dist[number[i]][number[i + 1]];
		}
		return length;
	}
	
}