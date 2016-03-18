package TSP;

import java.util.ArrayList;

import TSP.Route;

public class Christofides {
	public double[][] x, dist;
	public int n;
	public Route best_route;
	public ArrayList<Integer> euler_ans;
	public ArrayList<Integer> odd;
	public boolean[][] use;
	public class Node {
		public int num;
		public ArrayList<Integer> next;
		public Node(int i) {
			num = i;
			next = new ArrayList<Integer>();
		}
	}
	public Node[] node;
	
	public Christofides(double[][] x) {
		this.x = x;
		n = x.length;
	}
	
	public void evolution() {
		initialization();
		prime();
		pairMatching();
		//for (int i = 0; i < n; i++) 
		//	System.out.print(node[i].next.size() + " ");
		//System.out.println();
		eulerRoad(0);
		boolean[] use = new boolean[n];
		use[euler_ans.get(0)] = true;
		int now = 1;
		double length = 0;
		for (int i = 1; i < euler_ans.size(); i++) 
			if (!use[euler_ans.get(i)]) {
				best_route.route[now] = euler_ans.get(i);
				length += dist[best_route.route[now - 1]][best_route.route[now]];
				now++;
				use[euler_ans.get(i)] = true;
			}
		best_route.length = length + dist[best_route.route[0]][best_route.route[n - 1]];
	}
	
	public void initialization() {
		dist = new double[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++) 
				dist[i][j] = Math.sqrt((x[i][0] - x[j][0]) * (x[i][0] - x[j][0]) + (x[i][1] - x[j][1]) * (x[i][1] - x[j][1]));
		node = new Node[n];
		odd = new ArrayList<Integer>();
		best_route = new Route(n);
		euler_ans = new ArrayList<Integer>();
		use = new boolean[n][n];
	}

	public void prime() {
		double[] dis = new double[n];
		boolean[] use = new boolean[n];
		for (int i = 1; i < n; i++) {
			dis[i] = Integer.MAX_VALUE; 
		}
		for (int i = 0; i < n; i++) {
			node[i] = new Node(i);
		}
		int now = 0;
		use[0] = true;
		for (int i = 1; i < n; i++) {
			for (int j = 1; j < n; j++)
				dis[j] = Math.min(dis[j], dist[now][j]);
			double minDis = Integer.MAX_VALUE;
			int k = 0;
			for (int j = 1; j < n; j++)
				if (!use[j] && dis[j] < minDis) {
					k = j;
					minDis = dis[j];
				}
			node[now].next.add(k);
			node[k].next.add(now);
			now = k;
			use[k] = true;
		}
		for (int i = 0; i < n; i++) {
			if (node[i].next.size() % 2 == 1) {
				odd.add(i);
			}
		}
	}
	
	public void pairMatching() {
		boolean[] use = new boolean[n];
		for (int i = 0; i < odd.size() / 2; i++) {
			double gap = Integer.MAX_VALUE;
			int gap_p = 0, gap_q = 0;;
			for (int j = 0; j < odd.size(); j++)
				if (!use[j]) {
					double min1 = Integer.MAX_VALUE;
					double min2 = Integer.MAX_VALUE;
					int min_p = 0;
					for (int k = 0; k < odd.size(); k++) 
						if (!use[k] && j != k) {
							if (dist[odd.get(j)][odd.get(k)] < min1) {
								min1 = dist[odd.get(j)][odd.get(k)];
								min_p = k;
							}
							else if (dist[odd.get(j)][odd.get(k)] < min2) {
								min2 = dist[odd.get(j)][odd.get(k)];
							}
						}
					if (min2 - min1 < gap) {
						gap = min2 - min1;
						gap_p = j;
						gap_q = min_p;
					}
				}
			use[gap_p] = true;
			use[gap_q] = true;
			node[odd.get(gap_p)].next.add(odd.get(gap_q));
			node[odd.get(gap_q)].next.add(odd.get(gap_p));
		}
	}
	
	public void eulerRoad(int i) {
		for (int k : node[i].next) 
			if (!use[i][k]) {
				use[i][k] = true;
				use[k][i] = true;
				eulerRoad(k);
			}
		euler_ans.add(i);
	}
}