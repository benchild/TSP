package TSP;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import TSP.*;

public class TSP {
	public static double[][] x;
	public static int n;
	public static int test_time;
	
	public static void main(String[] args) throws IOException {
		//inputData();
		test();
	}
	
	public static void inputData() throws FileNotFoundException{
		InputStream in = new FileInputStream(new File("data/input.txt"));
		Scanner scanner = new Scanner(in);
		n = scanner.nextInt();
		x = new double[n][2];
		for (int i = 0; i < n; i++) {
			x[i][0] = scanner.nextDouble();
			x[i][1] = scanner.nextDouble();
		}
		scanner.close();
	}
	
	public static void test() throws FileNotFoundException {
		//n = 10, size = 100
		n = 15;
		int test_time = 10;
		int size = 100;
		long time_sa = 0, time_ga = 0, time_search = 0, time_chris = 0;
		DataGenerate data = new DataGenerate(n, size);
		for (int i = 0; i < test_time; i++) {
			System.out.println("test case: " + i);
			x = data.getRandomData();
			long time1 = System.currentTimeMillis();
			if (n <= 15) 
				testSearch();
			long time2 = System.currentTimeMillis();
			time_search += time2 - time1;
			testSA();
			long time3 = System.currentTimeMillis();
			time_sa += time3 - time2;
			testGA();
			long time4 = System.currentTimeMillis();
			time_ga += time4 - time3;
			testChristofides();
			long time5 = System.currentTimeMillis();
			time_chris += time5 - time4;
		}
		System.out.println();
		System.out.println("Time search: " + time_search);
		System.out.println("Time sa: " + time_sa);
		System.out.println("Time ga: " + time_ga);
		System.out.println("Time chris: " + time_chris);
		System.out.println("Length search: " + totalLength_search);
		System.out.println("Length sa: " + totalLength_sa);
		System.out.println("Length ga: " + totalLength_ga);
		System.out.println("Length chris: " + totalLength_chris);
	}
	
	public static double totalLength_sa = 0, totalLength_ga = 0, totalLength_search = 0, totalLength_chris = 0;
	
	public static void testSearch() throws FileNotFoundException {
		Search search = new Search(x);
		search.evolution();
		totalLength_search += search.best_route.length;
		print("Search", search.best_route);
	}
	
	public static void testSA() throws FileNotFoundException {
		SA sa = new SA(x);
		sa.evolution();
		totalLength_sa += sa.best_route.length;
		print("SA", sa.best_route);
	}
	
	public static void testGA() throws FileNotFoundException {
		GeneticTSP ga = new GeneticTSP(x);
		ga.evolution();
		totalLength_ga += ga.best_route.length;
		print("GA", ga.best_route);
	}
	
	public static void testChristofides() throws FileNotFoundException {
		Christofides chris = new Christofides(x);
		chris.evolution();
		totalLength_chris += chris.best_route.length;
		print("CHRIS", chris.best_route);
	}
	
	public static void print(String title, Route route) {
		System.out.println(title + " route: " + route);
		System.out.println(title + " Length: " + calcLength(route.route));
	}
	
	public static double calcLength(int[] number) {
		double length = distance(number[0], number[n - 1]);
		for (int i = 0; i < n - 1; i++) {
			length += distance(number[i], number[i + 1]);
		}
		return length;
	}
	
	public static double distance(int i, int j) {
		return Math.sqrt((x[i][0] - x[j][0]) * (x[i][0] - x[j][0]) + (x[i][1] - x[j][1]) * (x[i][1] - x[j][1]));
	}
	
}