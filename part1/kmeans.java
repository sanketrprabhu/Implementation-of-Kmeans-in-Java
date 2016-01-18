



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Sanket
 */
import java.io.File;
import java.util.Collections;
import java.util.Random;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class kmeans {

	private static int NUM_CLUSTERS = 0; // Total clusters.
	private static final int TOTAL_DATA = 100; // Total data points.

	private static ArrayList < Data > dataSet = new ArrayList < Data > ();
	private static ArrayList < Centroid > centroids = new ArrayList < Centroid > ();

	private static class Data {
		private double mX = 0;
		private double mY = 0;
		private int id;
		private int mCluster = 0;

		public Data(int id, double x, double y) {
			this.ID(id);
			this.X(x);
			this.Y(y);
			return;
		}

		public void ID(int id) {
			this.id = id;
		}

		public void X(double x) {
			this.mX = x;
			return;
		}

		public double X() {
			return this.mX;
		}

		public void Y(double y) {
			this.mY = y;
			return;
		}

		public double Y() {
			return this.mY;
		}

		public void cluster(int clusterNumber) {
			this.mCluster = clusterNumber;
			return;
		}

		public int cluster() {
			return this.mCluster;
		}
	}
	private static class Centroid {
		private double mX = 0.0;
		private double mY = 0.0;
		private int id = 0;

		public Centroid(int newId, double newX, double newY) {
			this.id = newId;
			this.mX = newX;
			this.mY = newY;
			return;
		}


		public void X(double newX) {
			this.mX = newX;
			return;
		}

		public double X() {
			return this.mX;
		}

		public void Y(double newY) {
			this.mY = newY;
			return;
		}

		public double Y() {
			return this.mY;
		}
	}
	private static void Euclid(int k, ArrayList < Integer > id, ArrayList < Float > x, ArrayList < Float > y) {
		final double bigNumber = Math.pow(10, 10); // some big number that's sure to be larger than our data range.
		double minimum = bigNumber; // The minimum value to beat. 
		double distance = 0.0; // The current minimum value.
		int sampleNumber = 0;
		int cluster = 0;
		boolean isStillMoving = true;
		Data newData = null;


		//Initialize Centroid
		ArrayList < Integer > rand = new ArrayList < Integer > ();
		for (int i = 0; i < 100; i++) {
			rand.add(new Integer(i));
		}
		Collections.shuffle(rand);
		for (int i = 0; i < k; i++) {
			int d = rand.get(i);
			centroids.add(new Centroid(id.get(d), x.get(d), y.get(d)));
		}

		// Add in new data, one at a time, recalculating centroids with each new one. 
		while (dataSet.size() < TOTAL_DATA) {
			newData = new Data(id.get(sampleNumber), x.get(sampleNumber), y.get(sampleNumber));
			dataSet.add(newData);
			minimum = bigNumber;
			for (int i = 0; i < k; i++) {
				distance = dist(newData, centroids.get(i));
				if (distance < minimum) {
					minimum = distance;
					cluster = i;
				}
			}
			newData.cluster(cluster);

			// calculate new centroids.
			for (int i = 0; i < k; i++) {
				double totalX = 0;
				double totalY = 0;
				int totalInCluster = 0;
				for (int j = 0; j < dataSet.size(); j++) {
					if (dataSet.get(j).cluster() == i) {
						totalX += dataSet.get(j).X();
						totalY += dataSet.get(j).Y();
						totalInCluster++;
					}
				}
				if (totalInCluster > 0) {
					centroids.get(i).X(totalX / totalInCluster);
					centroids.get(i).Y(totalY / totalInCluster);
				}
			}
			sampleNumber++;
		}

		// Now, keep shifting centroids until equilibrium occurs.
		while (isStillMoving) {
			// calculate new centroids.
			for (int i = 0; i < k; i++) {
				double totalX = 0;
				double totalY = 0;
				int totalInCluster = 0;
				for (int j = 0; j < dataSet.size(); j++) {
					if (dataSet.get(j).cluster() == i) {
						totalX += dataSet.get(j).X();
						totalY += dataSet.get(j).Y();
						totalInCluster++;
					}
				}
				if (totalInCluster > 0) {
					centroids.get(i).X(totalX / totalInCluster);
					centroids.get(i).Y(totalY / totalInCluster);
				}
			}

			// Assign all data to the new centroids
			isStillMoving = false;

			for (int i = 0; i < dataSet.size(); i++) {
				Data tempData = dataSet.get(i);
				minimum = bigNumber;
				for (int j = 0; j < k; j++) {
					distance = dist(tempData, centroids.get(j));
					if (distance < minimum) {
						minimum = distance;
						cluster = j;
					}
				}
				tempData.cluster(cluster);
				if (tempData.cluster() != cluster) {
					tempData.cluster(cluster);
					isStillMoving = true;
				}
			}
		}
		return;
	}

	private static double dist(Data d, Centroid c) {
		return Math.sqrt(Math.pow((c.Y() - d.Y()), 2) + Math.pow((c.X() - d.X()), 2));
	}
	public static double SSE(ArrayList < Data > dataSet2, ArrayList < Centroid > centroids2, int k) {
		double sse = 0;
		for (int i = 0; i < dataSet.size(); i++) {
			Data tempData = dataSet.get(i);
			for (int j = 0; j < k; j++) {
				sse += Math.pow(dist(tempData, centroids.get(j)), 2);

			}
		}
		return (sse/1000);
	}
            
        public static void main(String[] args) {
		//Read Test data file
		ArrayList < Integer > indices = new ArrayList < > ();
		ArrayList < Float > x = new ArrayList < > ();
		ArrayList < Float > y = new ArrayList < > ();
		String res = "";
		NUM_CLUSTERS = Integer.parseInt(args[0]);
		try {
			File file = new File(args[2]); //Your file
			FileOutputStream fos = new FileOutputStream(file);
			PrintStream ps = new PrintStream(fos);
			System.setOut(ps);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			Scanner s = new Scanner(new File(args[1]));
			while (s.hasNextLine()) {
				String[] data = s.nextLine().split("\\s+");
				indices.add(Integer.parseInt(data[0]));
				x.add(Float.parseFloat(data[1]));
				y.add(Float.parseFloat(data[2]));
			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Euclid(NUM_CLUSTERS, indices, x, y);
		//Output required
		for (int i = 0; i < NUM_CLUSTERS; i++) {
			boolean isFirst = true;
			int cluster_id = i + 1;
			System.out.print(cluster_id + " ");
			for (int j = 0; j < TOTAL_DATA; j++) {

				if (dataSet.get(j).cluster() == i) {
					if (isFirst) {
						res = String.valueOf(dataSet.get(j).id);
						isFirst = false;
					} else res += "," + String.valueOf(dataSet.get(j).id);
				}

			} // j
			System.out.print(res);
			System.out.println();
		}

		double SSE = SSE(dataSet, centroids, NUM_CLUSTERS);
                System.out.println();
                System.out.println();
		System.out.println("SSE value= " + SSE);
		return;
	}
}