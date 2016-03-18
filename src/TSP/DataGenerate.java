package TSP;

import java.util.Random;

public class DataGenerate {
	public int n;
	public int size;
	public Random random;
	public double[][] x;
	
	public DataGenerate(int n, int size) {
		this.n = n;
		this.size = size;
		random = new Random();
		x = new double[n][2];
	}
	
	public double[][] getRandomData() {
		for (int i = 0; i < n; i++)
			for (int j = 0; j < 2; j++) {
				x[i][j] = random.nextDouble() * size;
			}
		return x;
	}
}