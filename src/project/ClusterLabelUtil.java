package project;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.MinMaxPriorityQueue;

import cc.mallet.util.Maths;

public class ClusterLabelUtil {
	// To control the size of potential label words
	private static int LABEL_CANDIDATE_SIZE = Integer.parseInt(PropertiesUtil.getProperty("LABEL_CANDIDATE_SIZE")); 
	
	/**
	 * Decide labels for each cluster
	 * @param clusters	clusters
	 * @param vectors	document vectors
	 * @param allDocs	the vector of all documents
	 * @param allWordList	a list of all words in all documents
	 * @return	List of labels for each cluster
	 */
	public static List<String> getAllLabels(List<Set<Integer>> clusters, DocumentVectorUtil docUtil){
			
			//double[][] vectors, double[] allDocs, List<String> allWordList){
		if(docUtil.vectors.length == 0){ return new ArrayList<String>(); }
		List<String> labels = new ArrayList<>();
		for(Set<Integer> cluster: clusters){
			double[] cVector = new double[docUtil.vectors[1].length];
			int size = cluster.size();
			for(int idx : cluster){
				for(int j=0; j<docUtil.allWordsList.size(); j++){
					cVector[j] += docUtil.vectors[idx][j]/size;
				}
			}
			
			labels.add(getLabel(cVector, docUtil));
		}
		return labels;
	}

	private static String getLabel(double[] cluster, DocumentVectorUtil docUtil){ //double[] allDocs, List<String> allWordList){
		//System.out.println("LABEL_CANDIDATE_SIZE="+LABEL_CANDIDATE_SIZE);
		
		MinMaxPriorityQueue<double[]> q = MinMaxPriorityQueue
				.orderedBy(new Comparator<double[]>(){
					public int compare(double[] a, double[] b){
						return b[1] > a[1]? 1 : -1;
					}
				})
                .maximumSize(LABEL_CANDIDATE_SIZE).create();
		
		double baseline = Maths.jensenShannonDivergence(cluster, docUtil.allDocs);
		
		// For each word, remove it from the vector and calculate 
		// the "jensenShannonDivergence" distance change
		// keep the words that lead to max distance change
		for(int i=0; i<cluster.length; i++){
			if(cluster[i] == 0){ continue; }
			double tmpC = cluster[i];
			//double tmpA = allDocs[i];
			cluster[i] = 0;
			//allDocs[i] = 0;
			double newScore = Maths.jensenShannonDivergence(cluster, docUtil.allDocs);
			//System.out.println("score newScore=" + newScore);
			double[] scoreChange = {i, newScore-baseline};
			q.add(scoreChange);
			cluster[i] = tmpC;
			//allDocs[i] = tmpA;
		}
		
		// Store the potential labels into a list
		List<String> potentialLabels = new ArrayList<>();
		while(!q.isEmpty()){
			potentialLabels.add(docUtil.allWordsList.get((int)q.remove()[0]));
		}
		
		System.out.println(potentialLabels);
		
		String result = WikiLabelUtil.compareLabelWithWiki(cluster, potentialLabels, docUtil);
		
		System.out.println("WIKI result=" + result);
		
		return result;
	}
}
