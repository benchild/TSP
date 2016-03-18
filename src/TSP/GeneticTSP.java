package TSP;

import java.util.Random;
import java.util.Scanner;
import java.io.*;
import java.lang.Math;
import TSP.Route;

public class GeneticTSP {
	public class Individual{
		int[] route; // record each point by index
		double cost;
		Individual() {
			route = new int[N];
			for (int i = 0; i < N; ++i) {
				route[i] = i;
			}
			cost = 0;
		}
	}
	
	String data_folder = "../TestData/";
	int N; // number of places
	double[][] dist; // distance between each pair of point
	int population_size = 100;
	int max_generation = 100;
	double rs = 0.5; // selection rate
	double pc = 0.9; // crossover probability
	double pm = 0.4; //mutation probability 
	Individual[] group; // one generation of individuals 
	Individual[] father;
	Individual[] mother;
	Individual bestIDV; // individual with the least cost
	int best_gen = -1; // the generation where the best individual appears
	Route best_route;
	
	public void test() {
		// print dist
		/*for (int i = 0; i < N; ++i) {
			for (int j = 0; j < N; ++j) {
				System.out.print(dist[i][j]+" ");
			}
			System.out.println();
		}*/
		
		// print group
		/*for (int i = 0; i < group.length; ++i) {
			System.out.print("Route:");
			for (int j = 0; j < group[i].route.length; ++j) {
				System.out.print(" " + group[i].route[j]);
			}
			System.out.println();
			System.out.println("Cost: " + group[i].cost);
		}*/
		
		// print father and mother
		/*System.out.println("Father:");
		for (int i = 0; i < father.length; ++i) {
			System.out.print("Route:");
			for (int j = 0; j < father[i].route.length; ++j) {
				System.out.print(" " + father[i].route[j]);
			}
			System.out.println();
			System.out.println("Cost: " + father[i].cost);
		}
		System.out.println("Mother:");
		for (int i = 0; i < mother.length; ++i) {
			System.out.print("Route:");
			for (int j = 0; j < mother[i].route.length; ++j) {
				System.out.print(" " + mother[i].route[j]);
			}
			System.out.println();
			System.out.println("Cost: " + mother[i].cost);
		}*/
		
		// print best individual
		System.out.print("Best Individual Route:");
		for (int i = 0; i < bestIDV.route.length; ++i) {
			System.out.print(" " + bestIDV.route[i]);
		}
		System.out.println();
		System.out.println("Best Individual Cost: " + bestIDV.cost);
		System.out.println("Best Individual Generation: " + best_gen);
	}
	
	public GeneticTSP(double[][] pt) throws FileNotFoundException {
	    /*Scanner scanner = new Scanner(new FileInputStream(data_folder + "1.txt"));
		N = scanner.nextInt();
		scanner.nextLine();
		double[][] pt = new double[N][2];
		int cnt = 0;
		while (cnt < N) {
			pt[cnt][0] = scanner.nextDouble();
			pt[cnt][1] = scanner.nextDouble();
			++cnt;
			scanner.nextLine();
		}
		scanner.close();
		*/
		N = pt.length;
		dist = new double[N][N];
		for (int i = 0; i < N; ++i) {
			for (int j = 0; j < N; ++j) {
				dist[i][j] = Math.sqrt(Math.pow(pt[i][0]-pt[j][0], 2.0) + Math.pow(pt[i][1]-pt[j][1], 2.0));
			}
		}
	}
	
	private void shuffleRoute(int[] route)
	{
		int index, temp;
	    Random random = new Random();
	    for (int i = route.length - 1; i > 0; i--)
	    {
	        index = random.nextInt(i + 1);
	        temp = route[index];
	        route[index] = route[i];
	        route[i] = temp;
	    }
	}
	
	private double calcRouteCost(int[] route) {
		double cost = 0.0;
		for (int i = 0; i < route.length; ++i) {
			cost += dist[route[i]][route[(i+1) % route.length]];
		}
		return cost;
	}
	
	public void initialization() {
		group = new Individual[population_size];
		for (int i = 0; i < group.length; ++i) {
			group[i] = new Individual();
			shuffleRoute(group[i].route);
			group[i].cost = calcRouteCost(group[i].route);
		}
	}
	
	/* 
	 * Select parents using competition.
	 * Randomly choose two parents, pick the one with better fitness (lower cost).
	 * Choose father and mother in turn.
	 * One parent may be selected more than once (sampling with replacement).
	 */
	private void selection() {
		father = new Individual[population_size/2];
		mother = new Individual[population_size/2];
		Random random = new Random();
		for (int i = 0; i < population_size; ++i) {
			int rand1 = random.nextInt(population_size);
			int rand2 = random.nextInt(population_size);
			while (rand1 == rand2) {
				rand2 = random.nextInt(population_size);
			}
			int chosen;
			if (group[rand1].cost < group[rand2].cost) {
				chosen = rand1;
			}
			else {
				chosen = rand2;
			}
			if (i%2 == 0) {
				father[i/2] = group[chosen];
			}
			else {
				mother[i/2] = group[chosen];
			}
		}
	}
	
	public void crossover() {
		Random random = new Random();
		float f;
		for (int p = 0; p < population_size/2; ++p) {
			f = random.nextFloat();
			// do crossover
			if (f < pc) {
				int rand1 = random.nextInt(N);
				int rand2 = random.nextInt(N);
				int temp, i, j, k;
				Individual idv1 = new Individual();
				Individual idv2 = new Individual();
				while (rand1 == rand2) {
					rand2 = random.nextInt(N);
				}
				if (rand1 > rand2) {
					temp = rand1;
					rand1 = rand2;
					rand2 = temp;
				}
				
				for (i = 0, j = rand2; j < N; ++i, ++j) {
					idv2.route[i] = father[p].route[j];
				}
				
				int flag = i;
				
				for (k = 0, j = flag; j < N;)
		        {  
		            idv2.route[j] = mother[p].route[k++];  
		            for (i = 0; i < flag; i++) {  
		                if (idv2.route[i] == idv2.route[j]) {  
		                    break;  
		                }  
		            }  
		            if (i == flag) {  
		                j++;  
		            }  
		        }  
		  
		        flag = rand1;  
		        for (k = 0, j = 0; k < N;)  
		        {  
		        	idv1.route[j] = father[p].route[k++];  
		            for (i = 0; i < flag; i++) {  
		                if (mother[p].route[i] == idv1.route[j]) {  
		                    break;  
		                }  
		            }  
		            if (i == flag) {  
		                j++;  
		            }  
		        }  
		  
		        flag = N - rand1;
		        for (i = 0, j = flag; j < N; j++, i++) {  
		        	idv1.route[j] = mother[p].route[i];  
		        }  
		        
		        idv1.cost = calcRouteCost(idv1.route);
		        idv2.cost = calcRouteCost(idv2.route);
		        group[p*2] = idv1;
		        group[p*2+1] = idv2;
			}
			else {
				group[p*2] = father[p];
				group[p*2+1] = mother[p];
			}
		}
	}
	
	public void mutation() {
		Random random = new Random();
		float f;
		int rand1, rand2, temp;
		for (int i = 0; i < group.length; ++i) {
			f = random.nextFloat();
			// do mutation
			// Randomly select two places and reverse the route betwenn them
			if (f < pm) {
				rand1 = random.nextInt(N);
				rand2 = random.nextInt(N);
				while (rand1 == rand2) {
					rand2 = random.nextInt(N);
				}
				if (rand1 > rand2) {
					temp = rand1;
					rand1 = rand2;
					rand2 = temp;
				}
				while (rand1 < rand2) {
					temp = group[i].route[rand1];
					group[i].route[rand1] = group[i].route[rand2];
					group[i].route[rand2] = temp;
					++rand1;
					--rand2;
				}
				group[i].cost = calcRouteCost(group[i].route);
			}
		}
	}
	
	public void chooseBest(int cur_generation) {
		Individual localBestIDV = new Individual();
		localBestIDV = group[0];
		for (int i = 1; i < group.length; ++i) {
			if (group[i].cost < localBestIDV.cost) {
				localBestIDV = group[i];
			}
		}
		if (best_gen == -1 || bestIDV.cost > localBestIDV.cost) {
			bestIDV = localBestIDV;
			best_gen = cur_generation;
		}
	}
	
	public void swapRange(int[] arr, int start, int end) {
		int temp;
		while (start < end) {
			temp = arr[start];
			arr[start] = arr[end];
			arr[end] = temp;
			++start;
			--end;
		}
	}
	
	public void evolution() {
		initialization();
		for (int i = 0; i < max_generation; ++i) {
			selection();
			crossover();
			mutation();
			chooseBest(i);
		}
		int zero_pos = 0;
		for (int i = 0; i < bestIDV.route.length; ++i) {
			if (bestIDV.route[i] == 0) {
				zero_pos = i;
				break;
			}
		}
		swapRange(bestIDV.route, 0, zero_pos - 1);
		swapRange(bestIDV.route, zero_pos, bestIDV.route.length - 1);
		swapRange(bestIDV.route, 0, bestIDV.route.length - 1);
		best_route = new Route(bestIDV.route, bestIDV.cost);
	}
}