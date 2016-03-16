package TSP;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import TSP.SA;

public class TSP {
	public static double[][] x;
	public static int n;
	public static void main(String[] args) throws IOException {
		InputStream in = new FileInputStream(new File("data/input.txt"));
		Scanner scanner = new Scanner(in);
		n = scanner.nextInt();
		x = new double[n][2];
		for (int i = 0; i < n; i++) {
			x[i][0] = scanner.nextDouble();
			x[i][1] = scanner.nextDouble();
		}
		
		SA sa = new SA(x);
		int[] ans_sa = sa.calcTSP();
		for (int i = 0; i < n; i++) {
			System.out.print(ans_sa[i] + " ");
		}
		System.out.println(calcLength(ans_sa));
		scanner.close();
	}
	public static double calcLength(int[] number) {
		double length = distance(number[0], number[n - 1]);
		for (int i = 0; i < n; i++) {
			length += distance(number[i], number[i + 1]);
		}
		return length;
	}
	public static double distance(int i, int j) {
		return Math.sqrt((x[i][0] - x[j][0]) * (x[i][0] - x[j][0]) + (x[i][1] - x[j][1]) * (x[i][1] - x[j][1]));
	}
}