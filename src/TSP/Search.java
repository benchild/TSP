package TSP;

public class Search {
	public int n;
	public int[] route;
	public double[][] x, dist;
	public boolean[] use;
	public Route best_route;
	
	public Search(double[][] x) {
		n = x.length;
		route = new int[n];
		this.x = x;
		use = new boolean[n];
	}
	
	public void evolution() {
		dist = new double[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++) 
				dist[i][j] = Math.sqrt((x[i][0] - x[j][0]) * (x[i][0] - x[j][0]) + (x[i][1] - x[j][1]) * (x[i][1] - x[j][1]));
		
		boolean[] pass = new boolean[n];
		int[] n_route = new int[n];
		double length = 0;
		pass[0] = true;
		for (int i = 0; i < n - 1; i++) {
			int k = 0;
			for (int j = 0; j < n; j++) 
				if (!pass[j] && (k == 0 || dist[n_route[i]][j] < dist[n_route[i]][k])) {
					k = j;
				}
			length += dist[n_route[i]][k];
			n_route[i + 1] = k;
			pass[k] = true;
		}
		length += dist[0][n_route[n - 1]];
		best_route = new Route(n_route, length);
		dfs(1, 0);
	}
	
	public void dfs(int pos, double length) {
		if (length + dist[0][route[pos - 1]] > best_route.length) {
			return;
		}
		if (pos == n) {
			for (int i = 1; i < n; i++)
				best_route.route[i] = route[i];
			best_route.length = length + dist[0][route[pos - 1]];
			return;
		}
		for (int i = 1; i < n; i++) 
			if (!use[i]) {
				use[i] = true;
				route[pos] = i;
				dfs(pos + 1, length + dist[route[pos - 1]][i]);
				use[i] = false;
			}
	}
}