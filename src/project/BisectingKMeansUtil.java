package project;

import java.util.Properties;
import uclalib.BisectingKMeans;
import edu.ucla.sspace.clustering.Assignments;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.vector.DoubleVector;

public class BisectingKMeansUtil {
	
	static BisectingKMeans clusterAlgo = new BisectingKMeans();
	private static final String REPEAT_PROPERTY_NAME = "REPEAT_PROPERTY_NAME";
	private static final String REPEAT_PROPERTY_VALUE = "REPEAT_PROPERTY_VALUE";
	private static final String SEED_PROPERTY_NAME = "SEED_PROPERTY_NAME";
	private static final String SEED_PROPERTY_VALUE = "SEED_PROPERTY_VALUE";
	
	/**
	 * To decide the best clusters number
	 * @param dataPoints	the document vectors
	 * @param props	properties for cluster algorithm to use
	 * @param minC	minimum number of centers
	 * @param maxC	maximum number of centers
	 * @return Assignments
	 */
	public static Assignments getBestClusterByNo(Matrix dataPoints, Properties props, int minC, int maxC){
		//props.setProperty("edu.ucla.sspace.clustering.DirectClustering.repeat", "30");
		//props.setProperty("edu.ucla.sspace.clustering.DirectClustering.seed", "edu.ucla.sspace.clustering.seeding.KMeansPlusPlusSeed");
		props.setProperty(PropertiesUtil.getProperty(REPEAT_PROPERTY_NAME), PropertiesUtil.getProperty(REPEAT_PROPERTY_VALUE));
		props.setProperty(PropertiesUtil.getProperty(SEED_PROPERTY_NAME), PropertiesUtil.getProperty(SEED_PROPERTY_VALUE));
		
		System.out.println(props.getProperty(REPEAT_PROPERTY_NAME));
		System.out.println(props.getProperty(SEED_PROPERTY_NAME));
		return jumpMethod(dataPoints, props, minC, maxC);
	}
	
	/**
	 * To decide the best cluster number, return the corresponding assignments
	 * @param dataPoints	the document vectors
	 * @param props	properties for cluster algorithm to use
	 * @param minC	minimum number of centers
	 * @param maxC	maximum number of centers
	 * @return	Assignments
	 */
	private static Assignments jumpMethod(Matrix dataPoints, Properties props, int minC, int maxC){
		Assignments[] assignments = new Assignments[maxC-minC+1];
		double[] avgDist = new double[maxC-minC+1];
		double[] D = new double[maxC-minC+1];
		double y = dataPoints.rows()/(double)2;
		
		for(int i=minC; i<=maxC; i++){
			assignments[i-minC] = getCluster(dataPoints, i, props);
			avgDist[i-minC] = getAvgDist(assignments[i-minC], dataPoints);
			//System.out.println("Dist " + i +" centers="+avgDist[i-minC]);
			D[i-minC] = Math.pow(avgDist[i-minC], -y);
		}
		
		int maxChangeIdx = 0;
		double maxChange = Integer.MIN_VALUE;
		for(int i=1; i<avgDist.length; i++){
			double thisChange = avgDist[i]-avgDist[i-1];
			if(thisChange > maxChange){
				maxChangeIdx = i;
				maxChange = thisChange;
			}
		}
		return assignments[maxChangeIdx];
	}
	
	private static Assignments getCluster(Matrix dataPoints, int numClusters, Properties props){
		Assignments result = clusterAlgo.cluster(dataPoints, numClusters, props);
		return result;
	}
	
	private static double getAvgDist(Assignments assignments, Matrix dataPoints){
		DoubleVector[] centers = assignments.getCentroids();
		double totalDist = 0;
		for(int i=0; i<dataPoints.rows(); i++){
			double minDistToC = Integer.MAX_VALUE;
			for(int j=0; j<centers.length; j++){
				minDistToC = Math.min(minDistToC, getDist(dataPoints.getRowVector(i).toArray(), centers[j].toArray()));
			}
			totalDist += minDistToC;
		}
		return totalDist/(double)dataPoints.rows();
	}
	
	/**
	 * Calculate Euclidean distance between two vectors
	 * @param vector
	 * @param center
	 * @return distance
	 */
	private static double getDist(double[] vector, double[] center){
		double dist = 0;
		for(int i=0; i<vector.length; i++){
			dist += Math.pow(center[i]-vector[i], 2);
		}
		return Math.pow(dist, 0.5);
	}

}
